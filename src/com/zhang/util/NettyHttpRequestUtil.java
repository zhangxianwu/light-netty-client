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
package com.zhang.util;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Map.Entry;

import com.zhang.client.NettyHttpRequest;

/**
 * @author xianwu.zhang
 */
public class NettyHttpRequestUtil {

    public static HttpRequest create(NettyHttpRequest request, HttpMethod httpMethod) {
        HttpRequest httpRequest = null;
        if (HttpMethod.POST == httpMethod) {
            httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, httpMethod, request
                .getUri().getRawPath(), request.getContent().retain());

            httpRequest.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                request.getContent().readableBytes());
        } else {
            httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, httpMethod, request
                .getUri().getRawPath());
        }
        for (Entry<String, Object> entry : request.getHeaders().entrySet()) {
            httpRequest.headers().set(entry.getKey(), entry.getValue());
        }
        httpRequest.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        httpRequest.headers().set(HttpHeaders.Names.HOST, request.getUri().getHost());

        return httpRequest;
    }
}
