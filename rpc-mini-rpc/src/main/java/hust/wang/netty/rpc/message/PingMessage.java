package hust.wang.netty.rpc.message;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午3:17
 **/
public class PingMessage extends Message{
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
