import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;



public class ServerHeartBeatHandler extends ChannelHandlerAdapter {
    private static Map<String, String>AUTH_IP_MAP = new HashMap<>();
    private static final String SUCCESS_KEY ="auth_success_key";

    static {
        AUTH_IP_MAP.put("dln","1234");
    }

    private boolean auth(ChannelHandlerContext ctx, Object msg) {
        String[] rets = ((String)msg).split(",");
        String auth = AUTH_IP_MAP.get(rets[0]);
        System.out.println("校验");
        if(auth != null &&auth.equals(rets[1])) {
            ctx.writeAndFlush(SUCCESS_KEY);
            return true;
        } else {
            ctx.writeAndFlush("authfailure!").addListener(ChannelFutureListener.CLOSE);
            return false;

        }

    }

    

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof String) {
            auth(ctx, msg);

        } else if(msg instanceof RequestInfo) {

            RequestInfo info = (RequestInfo)msg;

            System.out.println("服务器：" +info.getIp());

           System.out.println("-----------------------------------------------");

            ctx.writeAndFlush("inforeceived!");

        } else {
            ctx.writeAndFlush("connectfailure").addListener(ChannelFutureListener.CLOSE);
        }

    }

}