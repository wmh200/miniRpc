package hust.wang.netty.rpc;

import hust.wang.netty.rpc.manage.RpcClientManager;
import hust.wang.netty.rpc.proxy.ClientProxy;
import hust.wang.netty.rpc.service.HelloService;
import hust.wang.netty.rpc.service.NbServiceImpl;

/**
 * @Author wangmh
 * @Date 2021/7/18 下午7:34
 **/

public class NettyClientApplication {
    public static void main(String[] args) {
        RpcClientManager clientManager = new RpcClientManager();
        //创建代理对象
        HelloService service = new ClientProxy(clientManager).getProxyService(NbServiceImpl.class);
        System.out.println(service.sayHello("杰少"));
    }
}
