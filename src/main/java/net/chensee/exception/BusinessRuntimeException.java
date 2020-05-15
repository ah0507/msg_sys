package net.chensee.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessRuntimeException extends RuntimeException {

    /**
     * 结果码
     */
    private Integer code;

    private String param;

    private String msg;

    public BusinessRuntimeException(Integer code) {
        this.code = code;
    }

    public BusinessRuntimeException(Integer code, String param) {
        this.code = code;
        this.param = param;
    }

    public BusinessRuntimeException(String msg) {
        this.msg = msg;
    }
}
