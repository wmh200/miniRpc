package hust.wang.netty.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于包扫描用来启动类上
 * 默认是扫描启动类上一级包中所有的类
 * @Author wangmh
 * @Date 2021/6/25 下午5:17
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcServerScan {
    public String value() default "";
}
