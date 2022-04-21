package org.kevink.dubbok.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RpcResponseCode {

    FAIL(20, "Method call failed"),
    SUCCESS(10, "Method call succeeded"),
    CLASS_NOT_FOUND(21, "Class not found"),
    METHOD_NOT_FOUND(22, "Method not found");

    /**
     * 响应编码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;

}
