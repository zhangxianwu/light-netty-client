# light-netty-client
Light-netty-client is a light framework for rapid development of asynchronous clients based on netty 4.x.

The sample code for sending get request:

    String getUrl = "http://www.baidu.com:80";
    NettyHttpRequest request = new NettyHttpRequest();
    request.header(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=GBK").uri(getUrl);

    NettyHttpClient client = new NettyHttpClient.ConfigBuilder()
        .maxIdleTimeInMilliSecondes(200 * 1000)
        .connectTimeOutInMilliSecondes(30 * 1000).build();

    NettyHttpResponseFuture responseFuture = client.doGet(request);
    NettyHttpResponse result = (NettyHttpResponse) responseFuture.get();
    client.close();
The sample code for sending post request:

    String postUrl = "http://www.xxx.com:8080/testPost";
    String postContent = "";//json format
    NettyHttpRequest request = new NettyHttpRequest();
    request.header(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=GBK").uri(postUrl).content(
        postContent, null);

    NettyHttpClient client = new NettyHttpClient.ConfigBuilder()
        .maxIdleTimeInMilliSecondes(200 * 1000)
        .connectTimeOutInMilliSecondes(30 * 1000)
        .build();

    NettyHttpResponseFuture responseFuture = client.doPost(request);
    NettyHttpResponse result = (NettyHttpResponse) responseFuture.get();
    client.close();
You can config the maximum number of channels to allow in the channel pool like this:

    Map<String, Integer> maxPerRoute = new HashMap<String, Integer>();
    maxPerRoute.put("www.baidu.com:80", 100);

    NettyHttpClient client = new NettyHttpClient.ConfigBuilder()
        .maxIdleTimeInMilliSecondes(200 * 1000)
        .connectTimeOutInMilliSecondes(30 * 1000)
        .maxPerRoute(maxPerRoute)
        .build();
If you not config this, the default value is 200.


For more information,please refer to the following blog
http://xw-z1985.iteye.com/blog/2180873
