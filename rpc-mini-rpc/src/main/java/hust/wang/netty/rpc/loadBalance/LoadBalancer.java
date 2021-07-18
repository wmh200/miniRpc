package hust.wang.netty.rpc.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡算法
 * @Author wangmh
 * @Date 2021/6/25 下午4:37
 **/
public interface LoadBalancer {
    Instance getInstance(List<Instance> list);
}
