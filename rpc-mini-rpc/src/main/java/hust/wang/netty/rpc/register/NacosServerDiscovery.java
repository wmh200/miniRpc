package hust.wang.netty.rpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import hust.wang.netty.rpc.loadBalance.LoadBalancer;
import hust.wang.netty.rpc.loadBalance.RoundRobinRule;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午4:34
 **/
public class NacosServerDiscovery implements ServerDiscovery{

    private final LoadBalancer loadBalancer;


    public NacosServerDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer == null ? new RoundRobinRule() : loadBalancer;
    }


    /**
     * 根据服务名找到服务地址
     *
     * @param serviceName
     * @return
     */
    @Override
    public InetSocketAddress getService(String serviceName) throws NacosException {
        List<Instance> instanceList = NacosUtils.getAllInstance(serviceName);
        System.out.println(serviceName);
        if (instanceList.size() == 0) {
            throw new RuntimeException("找不到对应服务");
        }
        Instance instance = loadBalancer.getInstance(instanceList);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }

}
