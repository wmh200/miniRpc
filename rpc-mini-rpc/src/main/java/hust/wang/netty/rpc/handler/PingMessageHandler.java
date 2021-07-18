package hust.wang.netty.rpc.handler;

import hust.wang.netty.rpc.message.PingMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测处理器
 * @Author wangmh
 * @Date 2021/6/25 下午4:03
 **/
@Slf4j
public class PingMessageHandler extends SimpleChannelInboundHandler<PingMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
        log.debug("接收到心跳信号{}",msg.getMessageType());
    }
}
