package hust.wang.netty.rpc.proxy;

/**
 * 客户端创建代理类
 * @Author wangmh
 * @Date 2021/6/25 下午6:50
 **/

import hust.wang.netty.rpc.manage.RpcClientManager;
import hust.wang.netty.rpc.message.RpcRequestMessage;
import hust.wang.netty.rpc.protocol.SequenceIdGenerator;
import io.netty.util.concurrent.DefaultPromise;
import java.lang.reflect.Proxy;
public class ClientProxy {

    private final RpcClientManager RPC_CLIENT;

    public ClientProxy(RpcClientManager client){
        this.RPC_CLIENT=client;
    }

    //JDK动态代理创建代理类
    public  <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = serviceClass.getInterfaces();
        //创建代理对象
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            // 1. 将方法调用转换为 消息对象
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            // 2. 准备一个空 Promise 对象，来接收结果 存入集合            指定 promise 对象异步接收结果线程
            DefaultPromise<Object> promise = new DefaultPromise<Object>(RpcClientManager.group.next());
            RpcClientManager.PROMISES.put(sequenceId, promise);
            // 3. 将消息对象发送出去
            RPC_CLIENT.sendRpcRequest(msg);
            // 4. 等待 promise 结果
            promise.await();
            if(promise.isSuccess()) {
                // 调用正常
                return promise.getNow();
            } else {
                // 调用失败
                throw new RuntimeException(promise.cause());
            }
        });
        return (T) o;
    }

}
