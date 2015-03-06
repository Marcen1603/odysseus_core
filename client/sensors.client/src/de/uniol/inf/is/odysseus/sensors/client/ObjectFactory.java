
package de.uniol.inf.is.odysseus.sensors.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.sensors.client package. 
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

    private final static QName _AddSensor_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "addSensor");
    private final static QName _RemoveSensor_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "removeSensor");
    private final static QName _AddSensorResponse_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "addSensorResponse");
    private final static QName _InvalidUserDataException_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "InvalidUserDataException");
    private final static QName _RemoveSensorResponse_QNAME = new QName("http://sensors.odysseus.is.inf.uniol.de/", "removeSensorResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.sensors.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RemoveSensor }
     * 
     */
    public RemoveSensor createRemoveSensor() {
        return new RemoveSensor();
    }

    /**
     * Create an instance of {@link InvalidUserDataException }
     * 
     */
    public InvalidUserDataException createInvalidUserDataException() {
        return new InvalidUserDataException();
    }

    /**
     * Create an instance of {@link RemoveSensorResponse }
     * 
     */
    public RemoveSensorResponse createRemoveSensorResponse() {
        return new RemoveSensorResponse();
    }

    /**
     * Create an instance of {@link AddSensorResponse }
     * 
     */
    public AddSensorResponse createAddSensorResponse() {
        return new AddSensorResponse();
    }

    /**
     * Create an instance of {@link AddSensor }
     * 
     */
    public AddSensor createAddSensor() {
        return new AddSensor();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSensor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "addSensor")
    public JAXBElement<AddSensor> createAddSensor(AddSensor value) {
        return new JAXBElement<AddSensor>(_AddSensor_QNAME, AddSensor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSensor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "removeSensor")
    public JAXBElement<RemoveSensor> createRemoveSensor(RemoveSensor value) {
        return new JAXBElement<RemoveSensor>(_RemoveSensor_QNAME, RemoveSensor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSensorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "addSensorResponse")
    public JAXBElement<AddSensorResponse> createAddSensorResponse(AddSensorResponse value) {
        return new JAXBElement<AddSensorResponse>(_AddSensorResponse_QNAME, AddSensorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidUserDataException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "InvalidUserDataException")
    public JAXBElement<InvalidUserDataException> createInvalidUserDataException(InvalidUserDataException value) {
        return new JAXBElement<InvalidUserDataException>(_InvalidUserDataException_QNAME, InvalidUserDataException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSensorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sensors.odysseus.is.inf.uniol.de/", name = "removeSensorResponse")
    public JAXBElement<RemoveSensorResponse> createRemoveSensorResponse(RemoveSensorResponse value) {
        return new JAXBElement<RemoveSensorResponse>(_RemoveSensorResponse_QNAME, RemoveSensorResponse.class, null, value);
    }

}
