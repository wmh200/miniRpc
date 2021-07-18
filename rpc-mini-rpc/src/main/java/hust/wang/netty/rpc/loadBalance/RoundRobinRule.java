package hust.wang.netty.rpc.loadBalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午4:45
 **/
public class RoundRobinRule implements LoadBalancer{
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    /**
     * 防止Integer越界 超过Integer最大值
     *
     * @return
     */
    private final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        return next;
    }


    /**
     * 轮询获取实例
     * @param list
     * @return
     */
    @Override
    public Instance getInstance(List<Instance> list) {
        int index = getAndIncrement() % list.size();
        return list.get(index);
    }
}
