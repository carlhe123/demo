package com.carl.demo.bean;

import com.carl.demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IOUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HdfsFile {
    private Configuration conf;
    private String userName;
    private String hdfsUri;
    private Long bufferSize;

    public HdfsFile(String hdfsUri, String userName, Long bufferSize) {
        this.userName = userName;
        this.hdfsUri = hdfsUri;
        this.bufferSize = bufferSize;
        this.conf = new Configuration();
        this.conf.set("fs.defaultFS", hdfsUri);
        this.conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        this.conf.set("fs.file.impl", LocalFileSystem.class.getName());
    }

    public void mkdir(String hdfsDir) throws BusinessException {
        if (StringUtils.isEmpty(hdfsDir)) {
            throw new BusinessException("HdfsFile==Filename can't be null");
        } else {
            FileSystem hdfs = null;

            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                boolean b = hdfs.mkdirs(new Path(hdfsDir));
                if (!b) {
                    throw new BusinessException("HdfsFile==建立目录失败");
                }
            } catch (Exception ex) {
                log.error("hdfsFile创建目录失败：{} {}",ex.getMessage(),ex.getStackTrace());
                throw new BusinessException("HdfsFile==建立目录失败" + ex.getMessage());
            } finally {
                this.closeFile(hdfs);
            }

        }
    }

    public void delete(String hdfsDir, boolean recursive) throws BusinessException {
        if (!StringUtils.isEmpty(hdfsDir)) {
            FileSystem hdfs = null;

            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                boolean b = hdfs.delete(new Path(hdfsDir), recursive);
                if (!b) {
                    throw new BusinessException("HdfsFile==删除文件时失败");
                }
            } catch (Exception ex) {
                log.error("hdfsFile删除文件失败：{} {}",ex.getMessage(),ex.getStackTrace());
                throw new BusinessException("HdfsFile==删除文件失败"+ ex.getMessage());
            } finally {
                this.closeFile(hdfs);
            }

        }
    }

    public boolean exists(String hdfsDir) throws BusinessException {
        if (StringUtils.isEmpty(hdfsDir)) {
            return false;
        } else {
            FileSystem hdfs = null;

            boolean b;
            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                b = hdfs.exists(new Path(hdfsDir));
            } catch (Exception ex) {
                log.error("hdfsFile判定文件是否存在时失败：{} {}",ex.getMessage(),ex.getStackTrace());
                throw new BusinessException("HdfsFile==判定文件是否存在时失败" + ex.getMessage());
            } finally {
                this.closeFile(hdfs);
            }

            return b;
        }
    }

    public FileStatus getFileStatus(String hdfsDir) throws BusinessException {
        if (StringUtils.isEmpty(hdfsDir)) {
            return null;
        } else {
            FileSystem hdfs = null;

            FileStatus status;
            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                status = hdfs.getFileStatus(new Path(hdfsDir));
            } catch (Exception ex) {
                throw new BusinessException("HdfsFile==获取文件状态时失败" + ex.getMessage());
            } finally {
                this.closeFile(hdfs);
            }

            return status;
        }
    }

    public List<String> list(String hdfsDir) throws BusinessException {
        List<String> names = new ArrayList();
        if (StringUtils.isEmpty(hdfsDir)) {
            return names;
        } else {
            FileSystem hdfs = null;

            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                RemoteIterator iter = hdfs.listFiles(new Path(hdfsDir), true);

                while(iter.hasNext()) {
                    LocatedFileStatus stat = (LocatedFileStatus)iter.next();
                    if (stat.isFile()) {
                        names.add(stat.getPath().toString());
                    } else if (stat.isDirectory()) {
                        names.add(stat.getPath().toString());
                    } else if (stat.isSymlink()) {
                        names.add(stat.getPath().toString());
                    }
                }
            } catch (Exception ex) {

                throw new BusinessException("HdfsFile==列表文件时失败" + ex.getMessage());
            } finally {
                this.closeFile(hdfs);
            }

            return names;
        }
    }

    public void upload(String hdfsDir, byte[] content) throws BusinessException {
        if (StringUtils.isEmpty(hdfsDir)) {
            throw new BusinessException("HdfsFile==file name can't be empty");
        } else {
            FileSystem hdfs = null;
            FSDataOutputStream fsDos = null;

            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                fsDos = hdfs.create(new Path(hdfsDir));
                fsDos.write(content);
            } catch (Exception ex) {
                throw new BusinessException(ex.getMessage());
            } finally {
                if (fsDos != null) {
                    try {
                        fsDos.close();
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }

                this.closeFile(hdfs);
            }

        }
    }

    public void append(String hdfsDir, byte[] content) throws BusinessException {
        if (StringUtils.isEmpty(hdfsDir)) {
            throw new BusinessException("HdfsFile==file name can't be empty");
        } else {
            FileSystem hdfs = null;

            try {
                this.conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
                this.conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                Path path = new Path(hdfsDir);
                if (hdfs.exists(path)) {
                    InputStream is = new ByteArrayInputStream(content);
                    OutputStream os = hdfs.append(path);
                    IOUtils.copyBytes(is, os, this.bufferSize, true);
                    os.close();
                    is.close();
                } else {
                    this.upload(hdfsDir, content);
                }
            } catch (Exception ex) {
                throw new BusinessException(ex.getMessage());
            } finally {
                this.closeFile(hdfs);
            }

        }
    }

    public byte[] download(String hdfsDir) throws BusinessException {
        if (StringUtils.isEmpty(hdfsDir)) {
            throw new BusinessException("HdfsFile==file name can't be empty");
        } else {
            FileSystem hdfs = null;
            FSDataInputStream fsDis = null;

            byte[] buffer;
            try {
                hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                Path hdfsPath = new Path(hdfsDir);
                if (!hdfs.exists(hdfsPath)) {
                    throw new BusinessException("HdfsFile==file isn't exist");
                }

                FileStatus stat = hdfs.getFileStatus(hdfsPath);
                buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
                fsDis = hdfs.open(hdfsPath);
                fsDis.readFully(0L, buffer);
            } catch (Exception ex) {
                throw new BusinessException("HdfsFile==下载文件时失败" + ex.getMessage());
            } finally {
                if (fsDis != null) {
                    try {
                        fsDis.close();
                    } catch (IOException var13) {
                        var13.printStackTrace();
                    }
                }

                this.closeFile(hdfs);
            }

            return buffer;
        }
    }

    public void copy(String sourceDir, String destDir) throws BusinessException {
        if (!StringUtils.isEmpty(sourceDir) && !StringUtils.isEmpty(destDir)) {
            if (!this.exists(sourceDir)) {
                throw new BusinessException("HdfsFile==源文件不存在");
            } else {
                FileSystem hdfs = null;

                try {
                    hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
                    FSDataInputStream sourceDis = hdfs.open(new Path(sourceDir));
                    FSDataOutputStream destDis = hdfs.create(new Path(destDir));
                    IOUtils.copyBytes(sourceDis, destDis, this.bufferSize, true);
                } catch (Exception ex) {
                    throw new BusinessException("HdfsFile==复制文件时失败" + ex.getMessage());
                } finally {
                    this.closeFile(hdfs);
                }

            }
        } else {
            throw new BusinessException("HdfsFile==文件名不能为空");
        }
    }

    public void rename(String oldDir, String newDir) throws Exception {
        if (StringUtils.isEmpty(oldDir) || !oldDir.equals(newDir)) {
            this.copy(oldDir, newDir);
            if (this.exists(oldDir)) {
                this.delete(oldDir, true);
            }

        }
    }

    public void copyFromLocal(String sourceDir, String destDir) throws BusinessException {
        Path sourcePath = new Path(sourceDir);
        Path destPath = new Path(destDir);
        FileSystem hdfs = null;

        try {
            hdfs = FileSystem.get(new URI(this.hdfsUri), this.conf, this.userName);
            hdfs.copyFromLocalFile(sourcePath, destPath);
        } catch (Exception ex) {
            throw new BusinessException("HdfsFile==复制文件时失败" + ex.getMessage());
        } finally {
            this.closeFile(hdfs);
        }

    }

    private void closeFile(FileSystem hdfs) {
        if (hdfs != null) {
            try {
                hdfs.close();
            } catch (IOException var3) {
            }
        }

    }
}
