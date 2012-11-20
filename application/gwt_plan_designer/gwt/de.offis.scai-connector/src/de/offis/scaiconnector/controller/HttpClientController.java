package de.offis.scaiconnector.controller;


import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Maintains a HTTP connection to an url and can send request (PUT).
 *
 * @author Alexander Funk
 * 
 */
public class HttpClientController {

    public static enum RequestType {

//        GET {
//
//            public HttpEntityEnclosingRequestBase getRequestObj(String url, String request, String contentType) throws Exception {
//
//                HttpGet obj = new HttpGet(url);
//                obj.setEntity(new StringEntity(request));
//                obj.setHeader("Content-type", contentType);
//                return obj;
//            }
//        }, POST {
//
//            public HttpEntityEnclosingRequestBase getRequestObj(String url, String request, String contentType) throws Exception {
//
//                HttpPut obj = new HttpPut(url);
//                obj.setEntity(new StringEntity(request));
//                obj.setHeader("Content-type", contentType);
//                return obj;
//            }
//        },
        PUT {

            public HttpEntityEnclosingRequestBase getRequestObj(String url, String request, String contentType) throws Exception {

                HttpPut obj = new HttpPut(url);
                obj.setEntity(new StringEntity(request));
                obj.setHeader("Content-type", contentType);
                return obj;
            }
        };

        abstract public HttpEntityEnclosingRequestBase getRequestObj(String url, String request, String contentType) throws Exception;
    }
    
    private String url;

    public HttpClientController(String url) {
        this.url = url;
    }

    public String sendRequest(String request, RequestType type) throws Exception {
        String result = null;
        try {
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            result = client.execute(type.getRequestObj(url, request, "application/xml"), responseHandler);

            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            throw new Exception("Could not connect to URL: " + url);
        }

        return result;
    }

    public String sendPutRequest(String request) throws Exception {
        return sendRequest(request, RequestType.PUT);
    }
}
