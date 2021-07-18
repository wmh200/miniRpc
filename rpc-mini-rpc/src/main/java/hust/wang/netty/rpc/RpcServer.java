package hust.wang.netty.rpc;

/**
 * 服务端测试
 * @Author wangmh
 * @Date 2021/6/25 下午6:48
 **/

import com.alibaba.nacos.api.exception.NacosException;
import hust.wang.netty.rpc.annotation.RpcServerScan;
import hust.wang.netty.rpc.manage.RpcServiceManager;

@RpcServerScan
public class RpcServer {
    public static void main(String[] args) throws InterruptedException, NacosException {
        //创建服务管理器  启动服务
        new RpcServiceManager("127.0.0.1",8080).start();

    }
}
