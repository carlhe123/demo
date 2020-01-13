package com.carl.demo.util;


import java.util.List;

import com.carl.demo.exception.FrameException;
import com.carl.demo.properties.HdfsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@EnableConfigurationProperties(HdfsProperties.class)
public class HdfsClient {

    @Resource
    HdfsProperties properties;

    /**
     * 在HDFS创建文件夹
     * @param path
     * @return
     * @throws Exception
     */
    public boolean mkdir(String path) {
        log.info("需要创建的目录路径：" + path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (existFile(path)) {
            return true;
        }
        try {
            properties.getHdfsFile().mkdir(path);
        }catch (FrameException ex){
            log.info(ex.getStackMessage());
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
        log.info("判断是否存在文件:" + path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        boolean isExists;
        try {
            isExists = properties.getHdfsFile().exists(path);
        }catch (Exception ex){
            log.info(ex.getMessage());
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
    public List<String> list(String path) throws FrameException{
        log.info("根据路径查询列表："+ path);
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        return properties.getHdfsFile().list(path);
    }

    /**
     * HDFS重命名文件
     * @param oldDir
     * @param newDir
     * @return
     * @throws Exception
     */
    public boolean renameDir(String oldDir, String newDir) throws FrameException {
        log.info("就文件名：" + oldDir + ",新文件名：" + newDir);
        if (StringUtils.isEmpty(oldDir) || StringUtils.isEmpty(newDir)) {
            return false;
        }
        try {
            properties.getHdfsFile().rename(oldDir, newDir);
        }catch (Exception ex){
            throw new FrameException("重命名目录文件异常："+ex.getMessage());
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
    public boolean deleteFile(String path,boolean recursive) throws FrameException {
        log.info("删除文件路径："+path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (!existFile(path)) {
            return false;
        }
        properties.getHdfsFile().delete(path,recursive);
        log.info("文件删除成功");
        return true;
    }

    /**
     * 上传HDFS文件
     * @param uploadPath 上传路径
     * @param content 文件字节码
     * @throws Exception
     */
    public void uploadFile(String uploadPath,byte[] content) throws FrameException {
        log.info("上传路径："+uploadPath + ",上传文件大小："+content.length);
        if (StringUtils.isEmpty(uploadPath)) {
            return;
        }
        properties.getHdfsFile().upload(uploadPath,content);
        log.info("文件上传成功");
    }

    /**
     * 下载HDFS文件
     * @param downloadPath
     * @throws Exception
     */
    public byte[] downloadFile(String downloadPath) throws FrameException {
        log.info("下载文件路径：" + downloadPath);
        if (StringUtils.isEmpty(downloadPath)) {
            throw new FrameException("下载路径为空");
        }
        byte[] result = properties.getHdfsFile().download(downloadPath);
        log.info("文件下载成功");
        return result;
    }
}
