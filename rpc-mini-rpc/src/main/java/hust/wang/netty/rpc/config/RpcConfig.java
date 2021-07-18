package hust.wang.netty.rpc.config;

import hust.wang.netty.rpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午3:38
 **/
public abstract class RpcConfig {
    static Properties properties;
    static {
        try( InputStream in = RpcConfig.class.getResourceAsStream("/application.properties")){
            properties = new Properties();
            properties.load(in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static int getServerPort(){
        String value = properties.getProperty("server.port");
        if(value == null) {
            return 8080;
        } else {
            return Integer.parseInt(value);
        }
    }
    public static Serializer.Algorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if(value == null) {
            return Serializer.Algorithm.Java;
        } else {
            return Serializer.Algorithm.valueOf(value);
        }
    }
}
