package com.kkhill;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoapServer {

    public static void main(String[] args) {
//        org.eclipse.californium.core.CoapServer server = new org.eclipse.californium.core.CoapServer();//主机为localhost 端口为默认端口5683
//        server.add(new CoapResource("hello"){//创建一个资源为hello 请求格式为 主机：端口\hello
//
//            @Override
//            public void handleGET(CoapExchange exchange) { //重写处理GET请求的方法
//                exchange.respond(CoAP.ResponseCode.CONTENT, "Hello CoAP!");
//            }
//
//        });
//        server.add(new CoapResource("time"){ //创建一个资源为time 请求格式为 主机：端口\time
//
//            @Override
//            public void handleGET(CoapExchange exchange) {
//                Date date = new Date();
//                exchange.respond(CoAP.ResponseCode.CONTENT,
//                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
//            }
//
//        });
//        server.start();

        try {

            URI uri = new URI("coap://127.0.0.1:6100/state");
            CoapClient client = new CoapClient();
            Request request = new Request(CoAP.Code.GET);
            // must use the empty token to avoid decoding error !!!
            request.setToken(new Token(new byte[]{}));
            request.setURI(uri);
            CoapResponse response = client.advanced(request);
            System.out.println(response.getResponseText());

            URI uri2 = new URI("coap://127.0.0.1:6100/brightness");
            Request request2 = new Request(CoAP.Code.GET);
            // must use the empty token to avoid decoding error !!!
            request2.setToken(new Token(new byte[]{}));
            request2.setURI(uri2);
            CoapResponse response2 = client.advanced(request2);
            System.out.println(response2.getResponseText());

//            URI uri = new URI("coap://127.0.0.1:6100/state");
//            CoapClient client = new CoapClient(uri);
//            CoapResponse response = client.get();
//            System.out.println(response.getResponseText());
        } catch (URISyntaxException | ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }
}
