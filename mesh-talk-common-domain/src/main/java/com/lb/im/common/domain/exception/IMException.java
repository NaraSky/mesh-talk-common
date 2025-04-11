package com.lb.im.common.domain.exception;

public class IMException extends RuntimeException{

    private Integer code;

    public IMException(String message) {
        super(message);
    }

    public IMException(Integer code, String message){
        super(message);
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
