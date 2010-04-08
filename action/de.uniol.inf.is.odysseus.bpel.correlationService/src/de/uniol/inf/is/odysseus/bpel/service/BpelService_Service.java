
/*
 * 
 */

package de.uniol.inf.is.odysseus.bpel.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.5
 * Thu Apr 08 13:13:21 CEST 2010
 * Generated source version: 2.2.5
 * 
 */


@WebServiceClient(name = "bpelService", 
                  wsdlLocation = "file:bpelService.wsdl",
                  targetNamespace = "http://de.uni.ol.inf.is.odysseus/bpelService/") 
public class BpelService_Service extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://de.uni.ol.inf.is.odysseus/bpelService/", "bpelService");
    public final static QName BpelServiceSOAP = new QName("http://de.uni.ol.inf.is.odysseus/bpelService/", "bpelServiceSOAP");
    static {
        URL url = null;
        try {
            url = new URL("file:bpelService.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:bpelService.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public BpelService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BpelService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BpelService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns BpelService
     */
    @WebEndpoint(name = "bpelServiceSOAP")
    public BpelService getBpelServiceSOAP() {
        return super.getPort(BpelServiceSOAP, BpelService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BpelService
     */
    @WebEndpoint(name = "bpelServiceSOAP")
    public BpelService getBpelServiceSOAP(WebServiceFeature... features) {
        return super.getPort(BpelServiceSOAP, BpelService.class, features);
    }

}
