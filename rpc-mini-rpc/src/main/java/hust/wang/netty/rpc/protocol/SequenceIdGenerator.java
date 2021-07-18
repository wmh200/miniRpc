package hust.wang.netty.rpc.protocol;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午6:55
 **/

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于生成id
 */
public abstract class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
