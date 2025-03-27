package sbs.getcry.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @Author : Puppet
 * @Date: 2025/3/27 17:39
 * @Description: 统一返回结果 R 工具类
 */
@Data
public class R<T> {
    private int code;
    private String msg;
    private T data;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static R ok() {
        return ok("success");
    }

    public static R ok(String msg) {
        return ok(msg, null);
    }

    public static <T> R ok(String msg, T data) {
        return new R(HttpStatus.OK.value(), msg, data);
    }

    public static R fail() {
        return fail("error");
    }

    public static R fail(String msg) {
        return fail(msg, null);
    }

    public static <T> R fail(String msg, T data) {
        return new R(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    /**
     * 支持自定义状态码
     *
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R of(int code, String msg, T data) {
        return new R(code, msg, data);
    }

}
