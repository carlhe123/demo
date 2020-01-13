package com.carl.demo.properties;

import com.carl.demo.bean.HdfsFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.hadoop")
public class HdfsProperties implements InitializingBean {

    private static final Long DEFAULT_BUFFER = 4096L;
    private String fsUri;
    private String username;
    private String buffer;
    private HdfsFile hdfsFile;

    public String getFsUri() {
        return fsUri;
    }

    public void setFsUri(String fsUri) {
        this.fsUri = fsUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getBufferSize() {
        Long bufferSize = DEFAULT_BUFFER;
        if (this.buffer != null) {
            String strBuffer = this.buffer.toUpperCase();
            int unit = 1;
            int idx;
            if ((idx = strBuffer.indexOf("M")) > 0) {
                unit = 1048576;
            } else if ((idx = strBuffer.indexOf("K")) > 0) {
                unit = 1024;
            } else if ((idx = strBuffer.indexOf("G")) > 0) {
                unit = 1073741824;
            }

            try {
                String strSize = idx > 0 ? strBuffer.substring(0, idx) : strBuffer;
                long size = Long.parseLong(strSize);
                bufferSize = size * (long)unit;
            } catch (NumberFormatException var8) {
            }
        }

        return bufferSize;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public HdfsFile getHdfsFile() {
        return hdfsFile;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.hdfsFile = new HdfsFile(fsUri,username,getBufferSize());
    }
}
