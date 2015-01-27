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
import io.netty.buffer.Unpooled;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xianwu.zhang
 */
public class NettyHttpRequest {

    private URI                 uri;

    private Map<String, Object> headers;

    private ByteBuf             content;
    
    private static final Charset DEFAUT_CHARSET = Charset.forName("GBK");

    public NettyHttpRequest uri(String uri) {
        this.uri = URI.create(uri);
        return this;
    }

    public NettyHttpRequest uri(URI uri) {
        if (null == uri) {
            throw new NullPointerException("uri");
        }
        this.uri = uri;
        return this;
    }

    public NettyHttpRequest header(String key, Object value) {
        if (null == this.headers) {
            this.headers = new HashMap<String, Object>();
        }
        headers.put(key, value);
        return this;
    }

    public NettyHttpRequest headers(Map<String, Object> headers) {
        if (null == headers) {
            throw new NullPointerException("headers");
        }

        if (null == this.headers) {
            this.headers = new HashMap<String, Object>();
        }

        this.headers.putAll(headers);
        return this;
    }

    public NettyHttpRequest content(ByteBuf content) {
        if (null == content) {
            throw new NullPointerException("content");
        }

        this.content = content;
        return this;
    }

    public NettyHttpRequest content(byte[] content) {
        if (null == content) {
            throw new NullPointerException("content");
        }
        this.content = Unpooled.copiedBuffer(content);
        return this;
    }

    public NettyHttpRequest content(String content, Charset charset) {
        if (null == content) {
            throw new NullPointerException("content");
        }
        charset = null == charset ? DEFAUT_CHARSET : charset;
        this.content = Unpooled.copiedBuffer(content, charset);
        return this;
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public ByteBuf getContent() {
        return content;
    }
}
