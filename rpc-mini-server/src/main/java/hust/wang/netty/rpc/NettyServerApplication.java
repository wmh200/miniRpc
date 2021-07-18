package hust.wang.netty.rpc;

import hust.wang.netty.rpc.annotation.RpcServerScan;
import hust.wang.netty.rpc.manage.RpcServiceManager;

/**
 * @Author wangmh
 * @Date 2021/7/18 下午5:15
 **/
@RpcServerScan
public class NettyServerApplication {
    public static void main(String[] args) {
        //创建服务管理器  启动服务
        new RpcServiceManager("127.0.0.1",8080).start();
    }
}
