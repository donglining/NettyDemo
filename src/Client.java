import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                   .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                          sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                           sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                           sc.pipeline().addLast(new ClientHeartBeatHandler());
                        }

                    });
            ChannelFuture future =bootstrap.connect(new InetSocketAddress("127.0.0.1", 8765)).sync();
            System.out.println(" ‰»Î1£∫");
            Scanner sc = new Scanner(System.in);
            System.out.println(" ‰»Î2£∫");
            while(true){
            	String s = sc.nextLine();
            	System.out.println(" ‰»Î£∫"+s);
            	RequestInfo r = new RequestInfo();
            	r.setIp(s);
            	future.channel().writeAndFlush(r);
            }
//            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}