package hust.wang.netty.rpc.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午4:44
 **/
public class RandomRule implements LoadBalancer{
    private final Random random=new Random();

    /**
     * 随机获取实例
     * @param list
     * @return
     */
    @Override
    public Instance getInstance(List<Instance> list) {
        return list.get(random.nextInt(list.size()));
    }
}
