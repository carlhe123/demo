package com.carl.demo.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class BusinessException extends RuntimeException {

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

    public BusinessException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }

    public BusinessException(Throwable cause){
        super(cause);
    }

    public String getStackMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        this.printStackTrace(ps);
        String message = baos.toString();

        try {
            baos.close();
        } catch (IOException var5) {
        }

        return message;
    }
}
