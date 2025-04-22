package sbs.getcry.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sbs.getcry.common.R;
/**
 * @Author : saeko
 * @Date: 2025/4/14 20:36
 * @Description: 全局异常处理器, 处理非认证异常的其他异常并统一返回错误信息.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public R handleValidException(Exception e) {
        String errorMsg = e.getLocalizedMessage();
        return R.fail(errorMsg);
    }
}
