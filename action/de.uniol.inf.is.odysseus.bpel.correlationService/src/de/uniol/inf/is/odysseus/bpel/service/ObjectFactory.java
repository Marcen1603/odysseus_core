
package de.uniol.inf.is.odysseus.bpel.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.bpel.service package. 
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

    private final static QName _Correlation_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/bpelService/", "correlation");
    private final static QName _In_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/bpelService/", "in");
    private final static QName _Maintained_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/bpelService/", "maintained");
    private final static QName _Tool_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/bpelService/", "tool");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.bpel.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/bpelService/", name = "correlation")
    public JAXBElement<String> createCorrelation(String value) {
        return new JAXBElement<String>(_Correlation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/bpelService/", name = "in")
    public JAXBElement<String> createIn(String value) {
        return new JAXBElement<String>(_In_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/bpelService/", name = "maintained")
    public JAXBElement<Integer> createMaintained(Integer value) {
        return new JAXBElement<Integer>(_Maintained_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/bpelService/", name = "tool")
    public JAXBElement<Integer> createTool(Integer value) {
        return new JAXBElement<Integer>(_Tool_QNAME, Integer.class, null, value);
    }

}
