package com.mofang.framework.web.server.handler;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.reactor.ActionDispatcher;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter
{
	private PostDataParserType parserType;
	private ActionResolve actionResolve;
	
	public HttpServerHandler(PostDataParserType parserType, ActionResolve actionResolve)
	{
		this.parserType = parserType;
		this.actionResolve = actionResolve;
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if (msg instanceof HttpRequest)
        {
        	DefaultFullHttpRequest request = (DefaultFullHttpRequest)msg;
            if (is100ContinueExpected(request))
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            
            ///处理请求
            HttpRequestContext context = new HttpRequestContext(request, parserType);
			context.setRemoteAddress(ctx.channel().remoteAddress());
			ActionDispatcher dispatcher = ActionDispatcher.getInstance();
			dispatcher.setActionResolve(actionResolve);
			String rspdata = dispatcher.execute(context);
			if(null == rspdata)
				rspdata = "";

            ///返回应答
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(rspdata.getBytes()));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            sendHttpResponse(ctx, request, response);
        }
    }
	
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res)
    {
		if (res.getStatus().code() != 200)
        {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }

        boolean keepAlive = isKeepAlive(req);
        System.out.println("keep-alive : " + keepAlive);
        if (!keepAlive) 
        {
            ctx.write(res).addListener(ChannelFutureListener.CLOSE);
        } 
        else
        {
        	res.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.write(res);
        }
    }
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
	{
        ctx.flush();
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}