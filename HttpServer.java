package com.pointwise.httpserver;

import com.opower.finagle.resteasy.server.ResteasyServiceBuilder;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.Server;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.http.Http;
import com.twitter.finagle.http.HttpMuxer;
import com.twitter.util.Future;
import com.twitter.util.FutureTransformer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * @author Helmut Steiner
 */
public class HttpServer extends Service<HttpRequest, HttpResponse> implements Runnable {
    
    
    private Future<String> getContentAsync(HttpRequest request) {
        // asynchronously gets content, possibly by submitting
        // a function to a FuturePool
        return Future.value("content");
    }
    
    public <U,V> CompletableFuture<V> thenCombineAsync(
        CompletableFuture<? extends U> other, 
        BiFunction<? super T,? super U,? extends V> fn, 
        Executor executor) {
            
          
            
        }

    public Future<HttpResponse> apply(HttpRequest request) {

        Future<String> contentFuture = getContentAsync(request);
        return contentFuture.transformedBy(new FutureTransformer<String, HttpResponse>() {
            @Override
            public HttpResponse map(String content) {
                HttpResponse httpResponse =
                        new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                HttpResponse httpResponseBadRequest =
                        new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                HttpResponse httpResponseBadRequest =
                        new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.ACCEPTED);
                HttpResponse httpResponsBadRequest =
                        new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TIMEOUT);
                httpResponse.setContent(ChannelBuffers.wrappedBuffer(content.getBytes()));
                return httpResponse;
            }

            @Override
            public HttpResponse handle(Throwable throwable) {
                HttpResponse httpResponse =
                        new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE);
                httpResponse.setContent(ChannelBuffers.wrappedBuffer(throwable.toString().getBytes()));
                return httpResponse;
            }
        });
    }

    public static void main(String[] args) {
        /*
        * set path to /foo/bar
        */
        HttpMuxer muxService = new HttpMuxer().withHandler("/foo/bar", new HttpServer());
        HttpMuxer muxIsoServiceBus = new HttpMuxer().withHandler("isoBus/getUser", new HttpServer());
        HttpMuxer muxIsoBusRunner = new HttpMuxer().withHandler("isoBus/getDevice", new HttpServer());
            
        Service service = ResteasyServiceBuilder.get()
                .withEndpoint(new RoutingService(String id) {

            @Override
            public String getInfoMessage() {
              return "Server is up and running";
            }
        } )
          
      
                .build();
        
        ServerBuilder builder = ServerBuilder.get()
                .name("HttpServer")
                .codec(Http.get())
                .bindTo(new InetSocketAddress("localhost", 8080));

        public void run() {
            Server server = ServerBuilder.safeBuild(service, builder);
        }
        System.out.println("Waiting for requests...");
    }
}
