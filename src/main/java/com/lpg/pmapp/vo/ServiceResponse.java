package com.lpg.pmapp.vo;

/**
 *
 * @author olatundeonigbinde
 */
public class ServiceResponse {
    private int code;
    private String message;
    public static final int SUCCESS_CODE = 0;
    public static final int FAILED_CODE = 1;
    public static final String SUCCESS_MESSAGE = "Operation successful";
    public static final String FAILED_MESSAGE = "Operation failed";

    public ServiceResponse(int code) {
        this.code = code;
    }

    public ServiceResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
