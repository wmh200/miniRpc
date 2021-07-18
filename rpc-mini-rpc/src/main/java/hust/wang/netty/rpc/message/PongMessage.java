package hust.wang.netty.rpc.message;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午3:20
 **/
public class PongMessage extends Message{
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
