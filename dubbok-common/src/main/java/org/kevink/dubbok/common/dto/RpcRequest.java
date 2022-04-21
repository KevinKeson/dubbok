package org.kevink.dubbok.common.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -1629298832116310500L;

    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 参数列表
     */
    private Object[] parameters;

}
