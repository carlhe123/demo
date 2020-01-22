package com.carl.demo.util;


import java.io.*;
import java.util.List;

import com.carl.demo.config.HdfsConfig;
import com.carl.demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class HdfsClient {

    @Resource
    HdfsConfig config;

    /**
     * 在HDFS创建文件夹
     * @param path
     * @return
     * @throws Exception
     */
    public boolean mkdir(String path) {
        log.info("需要创建的目录路径：{}",path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (existFile(path)) {
            return true;
        }
        try {
            config.getHdfsFile().mkdir(path);
        }catch (Exception ex){
            return false;
        }
        log.info("目录创建成功");
        return true;
    }

    /**
     * 判断HDFS文件是否存在
     * @param path
     * @return
     * @throws Exception
     */
    public boolean existFile(String path) {
        log.info("判断是否存在文件:{}",path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        boolean isExists;
        try {
            isExists = config.getHdfsFile().exists(path);
        }catch (Exception ex){
            return false;
        }
        return isExists;
    }

    /**
     * 读取HDFS目录信息
     * @param path 目录路径
     * @return <code>null</code> 路径为空/不存在对应的目录 <br>
     *     <code>list</code> 对应路径下的文件名称列表
     * @throws Exception
     */
    public List<String> list(String path) throws BusinessException {
        log.info("根据路径查询列表：{}",path);
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        return config.getHdfsFile().list(path);
    }

    /**
     * HDFS重命名文件
     * @param oldDir
     * @param newDir
     * @return
     * @throws Exception
     */
    public boolean renameDir(String oldDir, String newDir) throws BusinessException {
        log.info("旧文件名：{},新文件名：{}",oldDir,newDir);
        if (StringUtils.isEmpty(oldDir) || StringUtils.isEmpty(newDir)) {
            return false;
        }
        try {
            config.getHdfsFile().rename(oldDir, newDir);
        }catch (Exception ex){
            throw new BusinessException("重命名目录文件异常："+ex.getMessage());
        }
        log.info("重命名文件成功");
        return true;
    }

    /**
     * 删除HDFS文件
     * @param path
     * @return
     * @throws Exception
     */
    public boolean deleteFile(String path,boolean recursive) throws BusinessException {
        log.info("删除文件路径：{}",path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (!existFile(path)) {
            return false;
        }
        config.getHdfsFile().delete(path,recursive);
        log.info("文件删除成功");
        return true;
    }

    /**
     * 上传HDFS文件
     * @param uploadPath 上传路径
     * @param content 文件字节码
     * @throws Exception
     */
    public void uploadFile(String uploadPath,byte[] content) throws BusinessException {
        log.info("上传路径：{},上传文件大小：{}",uploadPath,content.length);
        if (StringUtils.isEmpty(uploadPath)) {
            return;
        }
        config.getHdfsFile().upload(uploadPath,content);
        log.info("文件上传成功");
    }

    /**
     * 上传HDFS文件
     * @param uploadPath 上传路径
     * @throws Exception
     */
    public void uploadFile(String uploadPath,File file) throws BusinessException {
        log.info("上传路径：{},文件大小：{}",uploadPath,file.length());
        if (StringUtils.isEmpty(uploadPath)) {
            return;
        }
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            //将文件输入流 写入字节输出流
            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            //将文件字节数组上传到指定的路径
            config.getHdfsFile().upload(uploadPath,outputStream.toByteArray());
        } catch (FileNotFoundException e) {
            log.error("file not found {} {}",e.getMessage(),e.getStackTrace());
            throw new BusinessException("file not found");
        } catch (IOException e) {
            log.error("read file exception {} {}",e.getMessage(),e.getStackTrace());
            throw new BusinessException("read file exception");
        }finally {
            try { inputStream.close();} catch (IOException e) { }
            try { outputStream.close(); } catch (IOException e) { }
        }
        log.info("文件上传成功");
    }

    /**
     * 下载HDFS文件
     * @param downloadPath
     * @throws Exception
     */
    public byte[] downloadFile(String downloadPath) throws BusinessException {
        log.info("下载文件路径：{}",downloadPath);
        if (StringUtils.isEmpty(downloadPath)) {
            throw new BusinessException("下载路径为空");
        }
        byte[] result = config.getHdfsFile().download(downloadPath);
        log.info("文件下载成功");
        return result;
    }
}
