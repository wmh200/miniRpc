package hust.wang.netty.rpc.manage;

/**
 * @Author wangmh
 * @Date 2021/6/25 下午5:07
 **/


import hust.wang.netty.rpc.annotation.RpcServer;
import hust.wang.netty.rpc.annotation.RpcServerScan;
import hust.wang.netty.rpc.factory.ServiceFactory;
import hust.wang.netty.rpc.handler.HeartBeatServerHandler;
import hust.wang.netty.rpc.handler.PingMessageHandler;
import hust.wang.netty.rpc.handler.RpcRequestMessageHandler;
import hust.wang.netty.rpc.protocol.MessageCodecSharable;
import hust.wang.netty.rpc.protocol.ProtocolFrameDecoder;
import hust.wang.netty.rpc.register.NacosServerRegistry;
import hust.wang.netty.rpc.register.ServerRegistry;
import hust.wang.netty.rpc.util.PackageScanUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Rpc服务端管理器
 *
 * @author chenlei
 */
public class RpcServiceManager {
    protected String host;
    protected int port;
    protected ServerRegistry serverRegistry;
    protected ServiceFactory serviceFactory;
    NioEventLoopGroup worker = new NioEventLoopGroup();
    NioEventLoopGroup boss = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();

    public RpcServiceManager(String host, int port) {
        this.host = host;
        this.port = port;
        serverRegistry = new NacosServerRegistry();
        serviceFactory = new ServiceFactory();

        // 完成注册 扫描启动类上层的包 遍历所有target下的项目路径 找到加了注解的类注册到nacos里
        autoRegistry();
    }
    /**
     * 开启服务
     */
    public void start() {
        //日志
        LoggingHandler LOGGING = new LoggingHandler(LogLevel.DEBUG);
        //消息节码器
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        //RPC请求处理器
        RpcRequestMessageHandler RPC_HANDLER = new RpcRequestMessageHandler();
        //心跳处理器
        HeartBeatServerHandler HEATBEAT_SERVER = new HeartBeatServerHandler();
        //心跳请求的处理器
        PingMessageHandler PINGMESSAGE = new PingMessageHandler();
        try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new ProtocolFrameDecoder());//定长解码器
                            pipeline.addLast(MESSAGE_CODEC);
                            pipeline.addLast(LOGGING);
                            pipeline.addLast(RPC_HANDLER);
                        }
                    });
            //绑定端口
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("启动服务出错");
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    /**
     * 自动扫描@RpcServer注解  注册服务
     */
    public void autoRegistry() {
        String mainClassPath = PackageScanUtils.getStackTrace();
        Class<?> mainClass;
        try {
            mainClass = Class.forName(mainClassPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("启动类为找到");
        }
        if (mainClass.isAnnotationPresent(RpcServer.class)) {
            throw new RuntimeException("启动类缺少@RpcServer 注解");
        }
        String annotationValue = mainClass.getAnnotation(RpcServerScan.class).value();
        //如果注解路径的值是空，则等于main父路径包下 获取父路径
        if ("".equals(annotationValue)) {
            annotationValue = mainClassPath.substring(0, mainClassPath.lastIndexOf("."));
        }
        //获取所有类的set集合
        Set<Class<?>> set = PackageScanUtils.getClasses(annotationValue);
        System.out.println(set.size());
        for (Class<?> c : set) {
            //只有有@RpcServer注解的才注册
            if (c.isAnnotationPresent(RpcServer.class)) {
                String ServerNameValue = c.getAnnotation(RpcServer.class).name();
                Object object;
                try {
                    object = c.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                    System.err.println("创建对象" + c + "发生错误");
                    continue;
                }
                //注解的值如果为空，使用类名
                if ("".equals(ServerNameValue)) {
                    addServer(object,c.getCanonicalName());
                } else {
                    addServer(object, ServerNameValue);
                }
            }
        }
    }

    /**
     * 添加对象到工厂和注册到注册中心
     *
     * @param server
     * @param serverName
     * @param <T>
     */
    public <T> void addServer(T server, String serverName) {
        serviceFactory.addServiceProvider(server, serverName);
        serverRegistry.register(serverName, new InetSocketAddress(host, port));
    }

}
