package hust.wang.netty.rpc.service;

import hust.wang.netty.rpc.annotation.RpcServer;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午6:49
 **/
@RpcServer
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String name) {
        return "你好, " + name;
    }
}
