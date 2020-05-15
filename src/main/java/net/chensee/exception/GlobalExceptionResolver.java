package net.chensee.exception;

import lombok.extern.slf4j.Slf4j;
import net.chensee.exception.entity.CustomExceptionEntity;
import net.chensee.exception.entity.SysExceptionEntity;
import net.chensee.exception.util.ParamConvertUtil;
import net.chensee.msg.po.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * 全局Controller层异常处理类 ExceptionHandler拦截异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

    @Autowired
    private SysExceptionEntity sysExceptionEntity;

    @Autowired
    private CustomExceptionEntity customExceptionEntity;

    /**
     * 处理所有系统不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ObjectResponse handleException(Exception e) {
        log.error("", e);
        String msg = this.getMsg(e);
        return ObjectResponse.fail(msg);
    }

    /**
     * 处理所有自定义异常
     *
     * @param e 业务异常
     * @return json结果
     */
    @ExceptionHandler(BusinessRuntimeException.class)
    @ResponseBody
    public ObjectResponse handleOpdRuntimeException(BusinessRuntimeException e) {
        log.error("", e);
        Map<String, String> exception = customExceptionEntity.getException();
        String eMsg = e.getMsg();
        if (eMsg.isEmpty()) {
            Integer code = e.getCode();
            String param = e.getParam();
            String msg = exception.get(code.toString());
            if (param != null) {
                msg = ParamConvertUtil.parseParams1(msg, param);
            }
            return ObjectResponse.fail(code, msg);
        }else{
            return ObjectResponse.fail(e.getMsg());
        }
    }

    /**
     * get请求参数校验抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ObjectResponse handleBingException(MissingServletRequestParameterException e) {
        log.error("", e);
        String parameterName = e.getParameterName();
        String msg = this.getMsg(e);
        msg = ParamConvertUtil.parseParams1(msg,parameterName);
        return ObjectResponse.fail(msg);
    }

    private String getMsg(Exception e) {
        Map<String, String> sysExceptionMap = sysExceptionEntity.getCustomException();
        String sysExceptionMsg = null;
        for (Map.Entry<String, String> entry : sysExceptionMap.entrySet()) {
            String sysExceptionName = entry.getKey();
            try {
                Class<?> aClass = Class.forName(sysExceptionName);
                if (e.getClass().isAssignableFrom(aClass)) {
                    sysExceptionMsg = entry.getValue();
                    break;
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return sysExceptionMsg != null ? sysExceptionMsg : sysExceptionEntity.getOtherException();
    }

    /**
     * post请求参数校验抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ObjectResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("", e);
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ObjectResponse.fail(defaultMessage);
    }

    /**
     * 请求方法中校验抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ObjectResponse constraintViolationExceptionHandler(ConstraintViolationException e){
        log.error("", e);
        //获取异常中第一个错误信息
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return ObjectResponse.fail(message);
    }
}
