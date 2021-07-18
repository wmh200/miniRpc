package hust.wang.netty.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午4:35
 **/
public interface ServerDiscovery {
    /**
     * 根据服务名找到InetSocketAddress
     */
    InetSocketAddress getService(String serviceName) throws NacosException;
}
