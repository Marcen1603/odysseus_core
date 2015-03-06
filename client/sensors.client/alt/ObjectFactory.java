
package de.uniol.inf.is.odysseus.sensors.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.sensors package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IsSensorRegistered_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "isSensorRegistered");
    private final static QName _IsSensorRegisteredResponse_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "isSensorRegisteredResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.sensors
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IsSensorRegistered }
     * 
     */
    public IsSensorRegistered createIsSensorRegistered() {
        return new IsSensorRegistered();
    }

    /**
     * Create an instance of {@link IsSensorRegisteredResponse }
     * 
     */
    public IsSensorRegisteredResponse createIsSensorRegisteredResponse() {
        return new IsSensorRegisteredResponse();
    }

    /**
     * Create an instance of {@link StringResponse }
     * 
     */
    public StringResponse createStringResponse() {
        return new StringResponse();
    }

    /**
     * Create an instance of {@link SensorAttribute }
     * 
     */
    public SensorAttribute createSensorAttribute() {
        return new SensorAttribute();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link SensorSchema }
     * 
     */
    public SensorSchema createSensorSchema() {
        return new SensorSchema();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsSensorRegistered }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "isSensorRegistered")
    public JAXBElement<IsSensorRegistered> createIsSensorRegistered(IsSensorRegistered value) {
        return new JAXBElement<IsSensorRegistered>(_IsSensorRegistered_QNAME, IsSensorRegistered.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsSensorRegisteredResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "isSensorRegisteredResponse")
    public JAXBElement<IsSensorRegisteredResponse> createIsSensorRegisteredResponse(IsSensorRegisteredResponse value) {
        return new JAXBElement<IsSensorRegisteredResponse>(_IsSensorRegisteredResponse_QNAME, IsSensorRegisteredResponse.class, null, value);
    }

}
