
package de.uniol.inf.is.odysseus.webservice;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.webservice package. 
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

    private final static QName _ActuatorReference_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "actuatorReference");
    private final static QName _Fault_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "fault");
    private final static QName _QueryID_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "queryID");
    private final static QName _Actuator_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "actuator");
    private final static QName _Query_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "query");
    private final static QName _Source_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "source");
    private final static QName _ActuatorSchema_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "actuatorSchema");
    private final static QName _Schema_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "schema");
    private final static QName _Id_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "id");
    private final static QName _ActuatorToDelete_QNAME = new QName("http://de.uni.ol.inf.is.odysseus/OdysseusWS/", "actuatorToDelete");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ActuatorInformation }
     * 
     */
    public ActuatorInformation createActuatorInformation() {
        return new ActuatorInformation();
    }

    /**
     * Create an instance of {@link ActuatorReducedInformation }
     * 
     */
    public ActuatorReducedInformation createActuatorReducedInformation() {
        return new ActuatorReducedInformation();
    }

    /**
     * Create an instance of {@link SchemaArray }
     * 
     */
    public SchemaArray createSchemaArray() {
        return new SchemaArray();
    }

    /**
     * Create an instance of {@link SourceSchema }
     * 
     */
    public SourceSchema createSourceSchema() {
        return new SourceSchema();
    }

    /**
     * Create an instance of {@link Method }
     * 
     */
    public Method createMethod() {
        return new Method();
    }

    /**
     * Create an instance of {@link QueryType }
     * 
     */
    public QueryType createQueryType() {
        return new QueryType();
    }

    /**
     * Create an instance of {@link Fault }
     * 
     */
    public Fault createFault() {
        return new Fault();
    }

    /**
     * Create an instance of {@link SourceDescription }
     * 
     */
    public SourceDescription createSourceDescription() {
        return new SourceDescription();
    }

    /**
     * Create an instance of {@link ParameterType }
     * 
     */
    public ParameterType createParameterType() {
        return new ParameterType();
    }

    /**
     * Create an instance of {@link ActuatorSchemaArray }
     * 
     */
    public ActuatorSchemaArray createActuatorSchemaArray() {
        return new ActuatorSchemaArray();
    }

    /**
     * Create an instance of {@link SchemaElementType }
     * 
     */
    public SchemaElementType createSchemaElementType() {
        return new SchemaElementType();
    }

    /**
     * Create an instance of {@link Channel }
     * 
     */
    public Channel createChannel() {
        return new Channel();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActuatorReducedInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "actuatorReference")
    public JAXBElement<ActuatorReducedInformation> createActuatorReference(ActuatorReducedInformation value) {
        return new JAXBElement<ActuatorReducedInformation>(_ActuatorReference_QNAME, ActuatorReducedInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "fault")
    public JAXBElement<Fault> createFault(Fault value) {
        return new JAXBElement<Fault>(_Fault_QNAME, Fault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "queryID")
    public JAXBElement<BigInteger> createQueryID(BigInteger value) {
        return new JAXBElement<BigInteger>(_QueryID_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActuatorInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "actuator")
    public JAXBElement<ActuatorInformation> createActuator(ActuatorInformation value) {
        return new JAXBElement<ActuatorInformation>(_Actuator_QNAME, ActuatorInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "query")
    public JAXBElement<QueryType> createQuery(QueryType value) {
        return new JAXBElement<QueryType>(_Query_QNAME, QueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SourceDescription }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "source")
    public JAXBElement<SourceDescription> createSource(SourceDescription value) {
        return new JAXBElement<SourceDescription>(_Source_QNAME, SourceDescription.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActuatorSchemaArray }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "actuatorSchema")
    public JAXBElement<ActuatorSchemaArray> createActuatorSchema(ActuatorSchemaArray value) {
        return new JAXBElement<ActuatorSchemaArray>(_ActuatorSchema_QNAME, ActuatorSchemaArray.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SchemaArray }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "schema")
    public JAXBElement<SchemaArray> createSchema(SchemaArray value) {
        return new JAXBElement<SchemaArray>(_Schema_QNAME, SchemaArray.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "id")
    public JAXBElement<String> createId(String value) {
        return new JAXBElement<String>(_Id_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActuatorReducedInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/", name = "actuatorToDelete")
    public JAXBElement<ActuatorReducedInformation> createActuatorToDelete(ActuatorReducedInformation value) {
        return new JAXBElement<ActuatorReducedInformation>(_ActuatorToDelete_QNAME, ActuatorReducedInformation.class, null, value);
    }

}
