package org.kevink.dubbok.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RpcErrorMessage {

    SERVICE_NOT_FOUND("Service not found"),
    SERVICE_NOT_IMPLEMENTED("Service not implemented"),
    SERVICE_INVOCATION_FAIL("Service invocation failed");

    /**
     * 错误信息
     */
    private final String message;

}
