package hust.wang.netty.rpc.factory;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午4:08
 **/

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供服务的工厂  提供具体的调用对象 相当于模拟spring
 *
 * @author chenlei
 */
@Slf4j
public class ServiceFactory{
    /**
     * 保存所有有注解@RpcService的集合
     */
    public static final Map<String, Object> serviceFactory = new ConcurrentHashMap<>();


    /**
     * 添加已注解的类进入工厂
     * @param service 具体的service
     * @param serviceName  service的名字
     * @param <T>
     */
    public <T> void addServiceProvider(T service, String serviceName) {
        if (serviceFactory.containsKey(serviceName)) {
            return;
        }
        serviceFactory.put(serviceName, service);
        log.debug("服务类{}添加进工厂",serviceName);
    }

    /**
     * 远程调用接口从该方法获取
     * @param serviceName 服务名字
     * @return
     */
    public Object getServiceProvider(String serviceName) {
        Object service = serviceFactory.get(serviceName);
        if (service == null) {
            throw new RuntimeException("未发现该服务");
        }
        return service;
    }
}