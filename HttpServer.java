package com.pointwise.httpserver;

import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.http.*;
import com.twitter.finagle.http.HttpMuxer;
import com.twitter.util.Future;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;
import java.net.InetSocketAddress;

public class HttpServer {
    public static int Calculate(String query) throws NumberFormatException {
        int sum = 0;
        System.out.println(query + " query");
        for (String ints : query.split(",")) {
            System.out.println(ints);
            try {
                sum = sum + Integer.parseInt(ints.toString());
            } catch (NumberFormatException e) {
                System.out.println(e);
                return -1;
            }
        }
        return sum;
    }

    public static int RemoteMethod(HttpRequest request) throws StringIndexOutOfBoundsException {
        try {
            String xx = request.getUri();
            xx = xx.substring(2);
            return Calculate(xx);
        } catch (StringIndexOutOfBoundsException s) {
            System.out.println(s);
            return -1;
        }


    }


        public static void main(String[] args) {

            Service<HttpRequest, HttpResponse> service = new Service<HttpRequest, HttpResponse>() {

                public Future<HttpResponse> apply(HttpRequest request) {

                    String result = Integer.toString(RemoteMethod(request));
                    HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                    if (result.equals("-1")) {
                        res.setContent(ChannelBuffers.copiedBuffer("Enter Correct URL, Correct Format is http://localhost:10000/?int1,int2,int3,..", CharsetUtil.UTF_8));
                       // res.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/bold; charset=UTF-8");
                        
                    } else {
                        res.setContent(ChannelBuffers.copiedBuffer("Your Sum is: " + result, CharsetUtil.UTF_8));
                        //res.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/bold; charset=UTF-8");
                    }
                    return Future.<HttpResponse>value(res);
                }
            };

            ServerBuilder.safeBuild(service, ServerBuilder.get().codec(Http.get()).name("HttpServer").bindTo(new InetSocketAddress("localhost", 8000)));
        }
    }
