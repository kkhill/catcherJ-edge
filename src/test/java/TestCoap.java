import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.Test;
import java.net.URI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCoap {

    @Test
    public void testCoap() {
        try {

            URI uri = new URI("coap://127.0.0.1:6100/state");
            CoapClient client = new CoapClient();
            Request request = new Request(CoAP.Code.GET);
            request.setToken(new Token(new byte[]{}));
            request.setURI(uri);
            CoapResponse response = client.advanced(request);
            System.out.println(response.getResponseText());

//            URI uri = new URI("coap://127.0.0.1:6100/state");
//            CoapClient client = new CoapClient(uri);
//            CoapResponse response = client.get();
//            System.out.println(response.getResponseText());
        } catch (URISyntaxException | ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

}


