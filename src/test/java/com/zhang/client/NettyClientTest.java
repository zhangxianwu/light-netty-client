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

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author xianwu.zhang
 */
public class NettyClientTest {

    @Test
    public void testGet() throws Exception {
        final String url = "http://www.baidu.com:80";

        Map<String, Integer> maxPerRoute = new HashMap<String, Integer>();
        maxPerRoute.put("www.baidu.com:80", 100);

        final NettyHttpClient client = new NettyHttpClient.ConfigBuilder()
            .maxIdleTimeInMilliSecondes(200 * 1000).maxPerRoute(maxPerRoute)
            .connectTimeOutInMilliSecondes(30 * 1000).build();

        final NettyHttpRequest request = new NettyHttpRequest();
        request.header(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=GBK").uri(url);

        NettyHttpResponseFuture responseFuture = client.doGet(request);
        NettyHttpResponse response = (NettyHttpResponse) responseFuture.get();
        client.close();

        print(response);
    }

    //@Test
    public void testPost() throws Exception {
        final String postUrl = "http://www.xxx.com:8080/testPost";
        final String postContent = "";// json format

        Map<String, Integer> maxPerRoute = new HashMap<String, Integer>();
        maxPerRoute.put("www.xxx.com:80", 100);

        final NettyHttpClient client = new NettyHttpClient.ConfigBuilder()
            .maxIdleTimeInMilliSecondes(200 * 1000).maxPerRoute(maxPerRoute)
            .connectTimeOutInMilliSecondes(30 * 1000).build();

        final NettyHttpRequest request = new NettyHttpRequest();
        request.header(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=GBK").uri(postUrl)
            .content(postContent, null);

        NettyHttpResponseFuture responseFuture = client.doPost(request);
        NettyHttpResponse response = (NettyHttpResponse) responseFuture.get();
        client.close();
        print(response);
    }

    private void print(NettyHttpResponse response) {
        System.out.println("STATUS: " + response.getStatus());
        System.out.println("VERSION: " + response.getVersion());
        System.out.println();

        if (!response.getHeaders().isEmpty()) {
            for (String name : response.getHeaders().names()) {
                for (String value : response.getHeaders().getAll(name)) {
                    System.out.println("HEADER: " + name + " = " + value);
                }
            }
        }
        System.out.println("CHUNKED CONTENT :");
        for (ByteBuf buf : response.getContents()) {
            System.out.print(buf.toString(CharsetUtil.UTF_8));
        }
    }
}
