import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class ClientHeartBeatHandler extends ChannelHandlerAdapter {

    private ScheduledExecutorService scheduled= Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> heartBeat;

    private InetAddress address;

    private static final String SUCCESS_KEY ="auth_success_key";

 

    @Override

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        address = InetAddress.getLocalHost();
        String name = "dln";
        String pass = "1234";
        String auth = name + "," + pass;
        ctx.writeAndFlush(auth);
    }

 

    @Override

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

 

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof String) {
                String data = (String) msg;
//                if(SUCCESS_KEY.equals(data)) {
//                    heartBeat =scheduled.scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 5,TimeUnit.SECONDS);
//                    System.out.println(msg);
//                } else {
//                    System.out.println(msg);
//                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

 

    private class HeartBeatTask implements Runnable{

        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

 

        @Override

        public void run() {
            try {
                RequestInfo requestInfo = new RequestInfo();
                System.out.println("=====================");
                requestInfo.setIp(address.getHostAddress());
                ctx.writeAndFlush(requestInfo);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

}