package org.kevink.dubbok.common.dto;

import lombok.Data;
import org.kevink.dubbok.common.enums.RpcResponseCode;

import java.io.Serializable;

@Data
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 8417220888319671155L;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    public static RpcResponse success(Object data) {
        RpcResponse response = new RpcResponse();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        response.setMessage(RpcResponseCode.SUCCESS.getMessage());
        if (data != null) {
            response.setData(data);
        }
        return response;
    }

    public static RpcResponse fail(RpcResponseCode rpcResponseCode) {
        RpcResponse response = new RpcResponse();
        response.setCode(rpcResponseCode.getCode());
        response.setMessage(rpcResponseCode.getMessage());
        return response;
    }

}
