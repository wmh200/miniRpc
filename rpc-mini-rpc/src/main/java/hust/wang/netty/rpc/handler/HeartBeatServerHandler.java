package hust.wang.netty.rpc.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测处理器
 * @Author wangmh
 * @Date 2021/6/25 下午4:01
 **/
@Slf4j
public class HeartBeatServerHandler extends ChannelDuplexHandler {
    /**
     * 处理特殊事件  IdleStateEvent
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //读空闲事件
            if (event.state() == IdleState.READER_IDLE) {
                log.debug("长时间没有收到消息了，断开连接");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx,evt);
    }
}
