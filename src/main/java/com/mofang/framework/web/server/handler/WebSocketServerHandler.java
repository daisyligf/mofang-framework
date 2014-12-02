package com.mofang.framework.web.server.handler;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.reactor.ActionDispatcher;
import com.mofang.framework.web.server.reactor.context.WebSocketRequestContext;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>
{
	private WebSocketServerHandshaker handshaker;
    private String requestUrl;
    private PostDataParserType parserType;
    private Class<?> closeHandlerClass;
    private ActionResolve actionResolve;
	
	public WebSocketServerHandler(PostDataParserType parserType, ActionResolve actionResolve)
	{
		this.parserType = parserType;
		this.actionResolve = actionResolve;
	}
    
	public void setWebSocketCloseHandlerClass(Class<?> handlerClass)
	{
		this.closeHandlerClass = handlerClass;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof FullHttpRequest)
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        else if (msg instanceof WebSocketFrame)
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception
    {
        if (!request.getDecoderResult().isSuccess())
        {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        if ("/favicon.ico".equals(request.getUri()))
        {
            FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
            sendHttpResponse(ctx, request, res);
            return;
        }
        
        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(request), null, false);
        handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null)
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        else
            handshaker.handshake(ctx.channel(), request);
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception
    {
        if (frame instanceof CloseWebSocketFrame)
        {
        	WebSocketCloseHandler closeHandler = (WebSocketCloseHandler)closeHandlerClass.newInstance();
        	closeHandler.handle(ctx);
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame)
        {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame))
        {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        
      ///装饰WebSocketRequestContext
    	WebSocketRequestContext context = new WebSocketRequestContext(frame, parserType);
		context.setRemoteAddress(ctx.channel().remoteAddress());
		context.setRequestUrl(requestUrl);
		context.setChannelContext(ctx);

        ///处理请求
		ActionDispatcher dispatcher = ActionDispatcher.getInstance();
		dispatcher.setActionResolve(actionResolve);
		String rspdata = dispatcher.execute(context);
		if(null == rspdata)
			rspdata = "";
		
		///返回应答
		ctx.channel().write(new TextWebSocketFrame(rspdata));
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
    	Channel channel=ctx.channel();
    	if (evt instanceof IdleStateEvent)
    	{	
            IdleStateEvent e= (IdleStateEvent) evt;
            if (e.state()== IdleState.ALL_IDLE)
            {
            	WebSocketCloseHandler closeHandler = (WebSocketCloseHandler)closeHandlerClass.newInstance();
            	closeHandler.handle(ctx);
                channel.close(); 
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }

    private static String getWebSocketLocation(FullHttpRequest req)
    {
        return "ws://" + req.headers().get(HOST);
    }
}