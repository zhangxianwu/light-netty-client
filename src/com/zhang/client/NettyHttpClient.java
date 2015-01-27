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
package com.zhang.client;

import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.zhang.handler.AdditionalChannelInitializer;
import com.zhang.pool.NettyChannelPool;
import com.zhang.util.NettyHttpRequestUtil;

/**
 * @author xianwu.zhang
 */
public class NettyHttpClient {

    private NettyChannelPool channelPool;

    private ConfigBuilder    configBuilder;

    private NettyHttpClient(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        this.channelPool = new NettyChannelPool(configBuilder.getMaxPerRoute(), configBuilder
            .getConnectTimeOutInMilliSecondes(), configBuilder.getMaxIdleTimeInMilliSecondes(),
            configBuilder.getForbidForceConnect(), configBuilder.getAdditionalChannelInitializer(),
            configBuilder.getOptions(), configBuilder.getGroup());
    }

    public NettyHttpResponseFuture doPost(NettyHttpRequest request) throws Exception {

        HttpRequest httpRequest = NettyHttpRequestUtil.create(request, HttpMethod.POST);
        InetSocketAddress route = new InetSocketAddress(request.getUri().getHost(), request
            .getUri().getPort());

        return channelPool.sendRequest(route, httpRequest);
    }

    public NettyHttpResponseFuture doGet(NettyHttpRequest request) throws Exception {
        HttpRequest httpRequest = NettyHttpRequestUtil.create(request, HttpMethod.GET);
        InetSocketAddress route = new InetSocketAddress(request.getUri().getHost(), request
            .getUri().getPort());
        return channelPool.sendRequest(route, httpRequest);
    }

    public void close() throws InterruptedException {
        channelPool.close();
    }

    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    public void setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
    }

    public static final class ConfigBuilder {
        @SuppressWarnings("unchecked")
        private Map<ChannelOption, Object>   options            = new HashMap<ChannelOption, Object>();

        // max idle time for a channel before close
        private int                          maxIdleTimeInMilliSecondes;

        // max time wait for a channel return from pool
        private int                          connectTimeOutInMilliSecondes;

        /**
         * value is false indicates that when there is not any channel in pool and no new
         * channel allowed to be create based on maxPerRoute, a new channel will be forced
         * to create.Otherwise, a <code>TimeoutException</code> will be thrown
         * value is false.
         */
        private boolean                      forbidForceConnect = false;

        private AdditionalChannelInitializer additionalChannelInitializer;

        // max number of channels allow to be created per route
        private Map<String, Integer>         maxPerRoute;

        private EventLoopGroup               customGroup;

        public ConfigBuilder() {
        }

        public NettyHttpClient build() {
            return new NettyHttpClient(this);
        }

        public ConfigBuilder maxPerRoute(Map<String, Integer> maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
            return this;
        }

        public ConfigBuilder connectTimeOutInMilliSecondes(int connectTimeOutInMilliSecondes) {
            this.connectTimeOutInMilliSecondes = connectTimeOutInMilliSecondes;
            return this;
        }

        @SuppressWarnings("unchecked")
        public ConfigBuilder option(ChannelOption key, Object value) {
            options.put(key, value);
            return this;
        }

        public ConfigBuilder maxIdleTimeInMilliSecondes(int maxIdleTimeInMilliSecondes) {
            this.maxIdleTimeInMilliSecondes = maxIdleTimeInMilliSecondes;
            return this;
        }

        public ConfigBuilder additionalChannelInitializer(
                                                          AdditionalChannelInitializer additionalChannelInitializer) {
            this.additionalChannelInitializer = additionalChannelInitializer;
            return this;
        }

        public ConfigBuilder customGroup(EventLoopGroup customGroup) {
            this.customGroup = customGroup;
            return this;
        }

        public ConfigBuilder forbidForceConnect(boolean forbidForceConnect) {
            this.forbidForceConnect = forbidForceConnect;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Map<ChannelOption, Object> getOptions() {
            return options;
        }

        public int getMaxIdleTimeInMilliSecondes() {
            return maxIdleTimeInMilliSecondes;
        }

        public AdditionalChannelInitializer getAdditionalChannelInitializer() {
            return additionalChannelInitializer;
        }

        public Map<String, Integer> getMaxPerRoute() {
            return maxPerRoute;
        }

        public int getConnectTimeOutInMilliSecondes() {
            return connectTimeOutInMilliSecondes;
        }

        public EventLoopGroup getGroup() {
            return this.customGroup;
        }

        public boolean getForbidForceConnect() {
            return this.forbidForceConnect;
        }
    }
}
