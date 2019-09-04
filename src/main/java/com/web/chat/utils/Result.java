package com.web.chat.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 返回值
 *
 * @param <T>
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    private Integer code;
    private String msg;
    private T data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result<?> result = (Result<?>) o;
        return Objects.equals(code, result.code) &&
                Objects.equals(msg, result.msg) &&
                Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg, data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 返回失败
     *
     * @param code
     * @param msg
     * @return
     */
    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result error() {
        return error(-1, "error");
    }

    /**
     * 返回成功
     *
     * @param o
     * @return
     */
    public static Result success(Object o) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("success");
        result.setData(o);
        return result;
    }

    public static Result success() {
        return success(null);
    }
}
