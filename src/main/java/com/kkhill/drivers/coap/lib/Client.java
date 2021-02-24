package com.kkhill.drivers.coap.lib;

import com.kkhill.core.Catcher;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private String ip;
    private String port;
    private String prefix;


    private CoapClient client;

    public Client(String ip, String port) {
        this.ip = ip;
        this.port = port;
        this.prefix = String.format("coap://%s:%s", ip, port);
        this.client = new CoapClient();
        this.client.setTimeout((long) 3);
    }

    public String send(String resource, String payload, String type) {
        try {
            URI uri = new URI(String.format("%s/%s", this.prefix, resource));
            Request request = null;
            if("put".equals(type) || "PUT".equals(type)) {
                request = new Request(CoAP.Code.PUT);
            } else if("get".equals(type) || "GET".equals(type)) {
                request = new Request(CoAP.Code.GET);
            } else {
                return null;
            }
            request.setToken(new Token(new byte[]{}));
            if(payload!=null) request.setPayload(payload);
            request.setURI(uri);
            CoapResponse response = client.advanced(request);
            return response == null ? null : response.getResponseText();
        } catch (URISyntaxException | ConnectorException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", "6100");
        String res = client.send("state", "turn_off", "PUT");
        System.out.println(res);
    }
}
