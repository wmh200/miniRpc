package hust.wang.netty.rpc.message;

import lombok.Data;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午3:15
 **/
@Data
public class RpcResponseMessage extends Message{
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
