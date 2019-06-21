package com.baimeng.eassyjoke.bean;

import java.io.Serializable;

/**
 * web端向pda端响应时的对象（需要经过gson处理后返回给请求段）
 *
 * @param <T>
 * @author liwenhao
 *         2016-12-19 上午11:29:17
 */
public class AResponse<T> implements Serializable {

    private static final long serialVersionUID = -7816945325851639128L;

    public boolean success;

    public String message;

    public T data;

    public long total = 0;

    public AResponse() {
        this(true, "", null);
    }

    public AResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public AResponse(boolean success, long total, T data, String message) {
        this.success = success;
        this.total = total;
        this.data = data;
        this.message = message;
    }

    @Override
    public String toString() {
        return "AResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}
