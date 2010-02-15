
package de.uniol.inf.is.odysseus.bpel.correlationservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.bpel.correlationservice package. 
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

    private final static QName _Correlation_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/correlationService/", "correlation");
    private final static QName _In_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/correlationService/", "in");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.bpel.correlationservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/correlationService/", name = "correlation")
    public JAXBElement<String> createCorrelation(String value) {
        return new JAXBElement<String>(_Correlation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/correlationService/", name = "in")
    public JAXBElement<String> createIn(String value) {
        return new JAXBElement<String>(_In_QNAME, String.class, null, value);
    }

}
