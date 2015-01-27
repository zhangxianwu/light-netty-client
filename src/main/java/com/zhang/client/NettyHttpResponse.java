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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author xianwu.zhang
 */
public class NettyHttpResponse {
    private volatile boolean            success = false;
    private volatile HttpResponseStatus status;
    private volatile HttpVersion        version;
    private volatile HttpHeaders        headers;
    private volatile List<ByteBuf>      contents;
    private volatile Throwable          cause;

    public NettyHttpResponse() {
        super();
    }

    public String getResponseBody() {
        return getResponseBody(Charset.forName("GBK"));
    }

    public String getResponseBody(Charset charset) {
        if (null == contents || 0 == contents.size()) {
            return null;
        }
        StringBuilder responseBody = new StringBuilder();
        for (ByteBuf content : contents) {
            responseBody.append(content.toString(charset));
        }

        return responseBody.toString();
    }

    public void addContent(ByteBuf byteBuf) {
        if (null == contents) {
            contents = new ArrayList<ByteBuf>();
        }
        contents.add(byteBuf);
    }

    /**
     * @return the version
     */
    public HttpVersion getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    /**
     * @return the contents
     */
    public List<ByteBuf> getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(List<ByteBuf> contents) {
        this.contents = contents;
    }

    /**
     * @return the headers
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    /**
     * @return the status
     */
    public HttpResponseStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    /**
     * Getter method for property <tt>success</tt>.
     * 
     * @return property value of success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Setter method for property <tt>success</tt>.
     * 
     * @param success value to be assigned to property success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Getter method for property <tt>cause</tt>.
     * 
     * @return property value of cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Setter method for property <tt>cause</tt>.
     * 
     * @param cause value to be assigned to property cause
     */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

}
