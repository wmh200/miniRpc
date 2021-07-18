package hust.wang.netty.rpc.handler;

import hust.wang.netty.rpc.factory.ServiceFactory;
import hust.wang.netty.rpc.message.RpcRequestMessage;
import hust.wang.netty.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午4:05
 **/
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) throws Exception {
        RpcResponseMessage responseMessage=new RpcResponseMessage();
        //设置请求的序号
        responseMessage.setSequenceId(message.getSequenceId());
        Object result;
        try {
            //通过名称从工厂获取本地注解了@RpcServer的实例
            Object service = ServiceFactory.serviceFactory.get(message.getInterfaceName());
            //根据方法名，参数 获取方法 反射获取方法
            Method method = service.getClass().getMethod(message.getMethodName(),message.getParameterTypes());
            //通过具体参数信息 来调用方法
            result = method.invoke(service, message.getParameterValue());
            //设置返回值
            responseMessage.setReturnValue(result);
        } catch ( NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            responseMessage.setExceptionValue(new Exception("远程调用出错:"+e.getMessage()));
        }finally {
            ctx.writeAndFlush(responseMessage);
            ReferenceCountUtil.release(message);
        }
    }
}
