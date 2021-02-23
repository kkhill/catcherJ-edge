package com.kkhill.drivers.demolight1.lib;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    private String ip;
    private String port;
    private boolean state;
    private int brightness = 70;
    private int temperature;

    private CoapClient client;

    public Client(String ip, String port) {
        this.ip = ip;
        this.port = port;
        this.client = new CoapClient();
    }

    public boolean open() {
        try {
            URI uri = new URI(String.format("coap://%s:%s/state", ip, port));
            Request request = new Request(CoAP.Code.PUT);
            request.setToken(new Token(new byte[]{}));
            request.setPayload("turn_on");
            request.setURI(uri);
            client.advanced(request);
        } catch (URISyntaxException | ConnectorException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean close() {
        try {
            URI uri = new URI(String.format("coap://%s:%s/state", ip, port));
            Request request = new Request(CoAP.Code.PUT);
            request.setToken(new Token(new byte[]{}));
            request.setPayload("turn_off");
            request.setURI(uri);
            client.advanced(request);
        } catch (URISyntaxException | ConnectorException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean state() {
        try {
            URI uri = new URI(String.format("coap://%s:%s/state", ip, port));
            Request request = new Request(CoAP.Code.GET);
            request.setToken(new Token(new byte[]{}));
            request.setURI(uri);
            CoapResponse response = client.advanced(request);
            return response.getResponseText().equals("on");
        } catch (URISyntaxException | ConnectorException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean setBrightness(int brightness) {
        this.brightness = brightness;
        return true;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean setTemperature(int temperature) {
        this.temperature = temperature;
        return true;
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", "6100");
        boolean res = client.state();
        System.out.println(res);
        client.open();
        res = client.state();
        System.out.println(res);
        client.close();
        res = client.state();
        System.out.println(res);
    }
}
