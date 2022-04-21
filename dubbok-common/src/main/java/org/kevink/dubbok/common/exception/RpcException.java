package org.kevink.dubbok.common.exception;

import org.kevink.dubbok.common.enums.RpcErrorMessage;

public class RpcException extends Exception {

    private static final long serialVersionUID = 9065376956842918365L;

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessage rpcErrorMessage) {
        super(rpcErrorMessage.getMessage());
    }

    public RpcException(RpcErrorMessage rpcErrorMessage, String detail) {
        super(rpcErrorMessage.getMessage() + ": " + detail);
    }

}
