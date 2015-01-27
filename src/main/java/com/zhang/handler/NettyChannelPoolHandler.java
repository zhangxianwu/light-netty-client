/*
 * Copyright 2014 The LightNettyClient Project
 *
 * The Light netty client Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.zhang.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.zhang.pool.NettyChannelPool;
import com.zhang.util.NettyHttpResponseFutureUtil;

/**
 * @author xianwu.zhang
 */
public class NettyChannelPoolHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger logger = Logger.getLogger(NettyChannelPoolHandler.class.getName());

    private NettyChannelPool    channelPool;

    /**
     * @param channelPool
     */
    public NettyChannelPoolHandler(NettyChannelPool channelPool) {
        super();
        this.channelPool = channelPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse headers = (HttpResponse) msg;
            NettyHttpResponseFutureUtil.setPendingResponse(ctx.channel(), headers);
        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            NettyHttpResponseFutureUtil.setPendingContent(ctx.channel(), httpContent);
            if (httpContent instanceof LastHttpContent) {
                boolean connectionClose = NettyHttpResponseFutureUtil
                    .headerContainConnectionClose(ctx.channel());

                NettyHttpResponseFutureUtil.done(ctx.channel());
                //the maxKeepAliveRequests config will cause server close the channel, and return 'Connection: close' in headers                
                if (!connectionClose) {
                    channelPool.returnChannel(ctx.channel());
                }
            }
        }
    }

    /**
     * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            logger.log(Level.WARNING, "remove idle channel: " + ctx.channel());
            ctx.channel().close();
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    /**
     * @param channelPool
     *            the channelPool to set
     */
    public void setChannelPool(NettyChannelPool channelPool) {
        this.channelPool = channelPool;
    }
}
