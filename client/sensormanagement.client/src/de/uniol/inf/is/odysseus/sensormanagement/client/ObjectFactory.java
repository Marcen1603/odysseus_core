
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.uniol.inf.is.odysseus.sensormanagement.client package. 
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
@SuppressWarnings(value = { "all" })
public class ObjectFactory {

    private final static QName _AddSensorResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "addSensorResponse");
    private final static QName _StopLiveView_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "stopLiveView");
    private final static QName _AddSensor_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "addSensor");
    private final static QName _StartLiveViewResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "startLiveViewResponse");
    private final static QName _GetSensorByIdResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorByIdResponse");
    private final static QName _GetSensorType_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorType");
    private final static QName _StopLoggingResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "stopLoggingResponse");
    private final static QName _StopAllLoggingResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "stopAllLoggingResponse");
    private final static QName _GetSensorIds_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorIds");
    private final static QName _RemoveSensorResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "removeSensorResponse");
    private final static QName _RemoveSensor_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "removeSensor");
    private final static QName _GetSensorIdsResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorIdsResponse");
    private final static QName _InitServiceResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "initServiceResponse");
    private final static QName _GetSensorById_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorById");
    private final static QName _StartLoggingResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "startLoggingResponse");
    private final static QName _StopAllLogging_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "stopAllLogging");
    private final static QName _GetSensorTypes_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorTypes");
    private final static QName _ModifySensorResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "modifySensorResponse");
    private final static QName _StartLiveView_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "startLiveView");
    private final static QName _StopLogging_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "stopLogging");
    private final static QName _ModifySensor_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "modifySensor");
    private final static QName _GetSensorTypeResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorTypeResponse");
    private final static QName _GetSensorTypesResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "getSensorTypesResponse");
    private final static QName _InvalidUserDataException_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "InvalidUserDataException");
    private final static QName _InitService_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "initService");
    private final static QName _StopLiveViewResponse_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "stopLiveViewResponse");
    private final static QName _StartLogging_QNAME = new QName("http://server.sensormanagement.odysseus.is.inf.uniol.de/", "startLogging");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.uniol.inf.is.odysseus.sensormanagement.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StopLiveView }
     * 
     */
    public StopLiveView createStopLiveView() {
        return new StopLiveView();
    }

    /**
     * Create an instance of {@link AddSensorResponse }
     * 
     */
    public AddSensorResponse createAddSensorResponse() {
        return new AddSensorResponse();
    }

    /**
     * Create an instance of {@link StopAllLoggingResponse }
     * 
     */
    public StopAllLoggingResponse createStopAllLoggingResponse() {
        return new StopAllLoggingResponse();
    }

    /**
     * Create an instance of {@link AddSensor }
     * 
     */
    public AddSensor createAddSensor() {
        return new AddSensor();
    }

    /**
     * Create an instance of {@link StartLiveViewResponse }
     * 
     */
    public StartLiveViewResponse createStartLiveViewResponse() {
        return new StartLiveViewResponse();
    }

    /**
     * Create an instance of {@link GetSensorByIdResponse }
     * 
     */
    public GetSensorByIdResponse createGetSensorByIdResponse() {
        return new GetSensorByIdResponse();
    }

    /**
     * Create an instance of {@link GetSensorType }
     * 
     */
    public GetSensorType createGetSensorType() {
        return new GetSensorType();
    }

    /**
     * Create an instance of {@link StopLoggingResponse }
     * 
     */
    public StopLoggingResponse createStopLoggingResponse() {
        return new StopLoggingResponse();
    }

    /**
     * Create an instance of {@link RemoveSensor }
     * 
     */
    public RemoveSensor createRemoveSensor() {
        return new RemoveSensor();
    }

    /**
     * Create an instance of {@link GetSensorIdsResponse }
     * 
     */
    public GetSensorIdsResponse createGetSensorIdsResponse() {
        return new GetSensorIdsResponse();
    }

    /**
     * Create an instance of {@link GetSensorIds }
     * 
     */
    public GetSensorIds createGetSensorIds() {
        return new GetSensorIds();
    }

    /**
     * Create an instance of {@link RemoveSensorResponse }
     * 
     */
    public RemoveSensorResponse createRemoveSensorResponse() {
        return new RemoveSensorResponse();
    }

    /**
     * Create an instance of {@link StopAllLogging }
     * 
     */
    public StopAllLogging createStopAllLogging() {
        return new StopAllLogging();
    }

    /**
     * Create an instance of {@link InitServiceResponse }
     * 
     */
    public InitServiceResponse createInitServiceResponse() {
        return new InitServiceResponse();
    }

    /**
     * Create an instance of {@link GetSensorById }
     * 
     */
    public GetSensorById createGetSensorById() {
        return new GetSensorById();
    }

    /**
     * Create an instance of {@link StartLoggingResponse }
     * 
     */
    public StartLoggingResponse createStartLoggingResponse() {
        return new StartLoggingResponse();
    }

    /**
     * Create an instance of {@link GetSensorTypes }
     * 
     */
    public GetSensorTypes createGetSensorTypes() {
        return new GetSensorTypes();
    }

    /**
     * Create an instance of {@link ModifySensorResponse }
     * 
     */
    public ModifySensorResponse createModifySensorResponse() {
        return new ModifySensorResponse();
    }

    /**
     * Create an instance of {@link StartLiveView }
     * 
     */
    public StartLiveView createStartLiveView() {
        return new StartLiveView();
    }

    /**
     * Create an instance of {@link ModifySensor }
     * 
     */
    public ModifySensor createModifySensor() {
        return new ModifySensor();
    }

    /**
     * Create an instance of {@link GetSensorTypeResponse }
     * 
     */
    public GetSensorTypeResponse createGetSensorTypeResponse() {
        return new GetSensorTypeResponse();
    }

    /**
     * Create an instance of {@link StopLogging }
     * 
     */
    public StopLogging createStopLogging() {
        return new StopLogging();
    }

    /**
     * Create an instance of {@link GetSensorTypesResponse }
     * 
     */
    public GetSensorTypesResponse createGetSensorTypesResponse() {
        return new GetSensorTypesResponse();
    }

    /**
     * Create an instance of {@link InvalidUserDataException }
     * 
     */
    public InvalidUserDataException createInvalidUserDataException() {
        return new InvalidUserDataException();
    }

    /**
     * Create an instance of {@link InitService }
     * 
     */
    public InitService createInitService() {
        return new InitService();
    }

    /**
     * Create an instance of {@link StopLiveViewResponse }
     * 
     */
    public StopLiveViewResponse createStopLiveViewResponse() {
        return new StopLiveViewResponse();
    }

    /**
     * Create an instance of {@link StartLogging }
     * 
     */
    public StartLogging createStartLogging() {
        return new StartLogging();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSensorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "addSensorResponse")
    public JAXBElement<AddSensorResponse> createAddSensorResponse(AddSensorResponse value) {
        return new JAXBElement<AddSensorResponse>(_AddSensorResponse_QNAME, AddSensorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopLiveView }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "stopLiveView")
    public JAXBElement<StopLiveView> createStopLiveView(StopLiveView value) {
        return new JAXBElement<StopLiveView>(_StopLiveView_QNAME, StopLiveView.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSensor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "addSensor")
    public JAXBElement<AddSensor> createAddSensor(AddSensor value) {
        return new JAXBElement<AddSensor>(_AddSensor_QNAME, AddSensor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartLiveViewResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "startLiveViewResponse")
    public JAXBElement<StartLiveViewResponse> createStartLiveViewResponse(StartLiveViewResponse value) {
        return new JAXBElement<StartLiveViewResponse>(_StartLiveViewResponse_QNAME, StartLiveViewResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorByIdResponse")
    public JAXBElement<GetSensorByIdResponse> createGetSensorByIdResponse(GetSensorByIdResponse value) {
        return new JAXBElement<GetSensorByIdResponse>(_GetSensorByIdResponse_QNAME, GetSensorByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorType")
    public JAXBElement<GetSensorType> createGetSensorType(GetSensorType value) {
        return new JAXBElement<GetSensorType>(_GetSensorType_QNAME, GetSensorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopLoggingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "stopLoggingResponse")
    public JAXBElement<StopLoggingResponse> createStopLoggingResponse(StopLoggingResponse value) {
        return new JAXBElement<StopLoggingResponse>(_StopLoggingResponse_QNAME, StopLoggingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopAllLoggingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "stopAllLoggingResponse")
    public JAXBElement<StopAllLoggingResponse> createStopAllLoggingResponse(StopAllLoggingResponse value) {
        return new JAXBElement<StopAllLoggingResponse>(_StopAllLoggingResponse_QNAME, StopAllLoggingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorIds }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorIds")
    public JAXBElement<GetSensorIds> createGetSensorIds(GetSensorIds value) {
        return new JAXBElement<GetSensorIds>(_GetSensorIds_QNAME, GetSensorIds.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSensorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "removeSensorResponse")
    public JAXBElement<RemoveSensorResponse> createRemoveSensorResponse(RemoveSensorResponse value) {
        return new JAXBElement<RemoveSensorResponse>(_RemoveSensorResponse_QNAME, RemoveSensorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveSensor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "removeSensor")
    public JAXBElement<RemoveSensor> createRemoveSensor(RemoveSensor value) {
        return new JAXBElement<RemoveSensor>(_RemoveSensor_QNAME, RemoveSensor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorIdsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorIdsResponse")
    public JAXBElement<GetSensorIdsResponse> createGetSensorIdsResponse(GetSensorIdsResponse value) {
        return new JAXBElement<GetSensorIdsResponse>(_GetSensorIdsResponse_QNAME, GetSensorIdsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "initServiceResponse")
    public JAXBElement<InitServiceResponse> createInitServiceResponse(InitServiceResponse value) {
        return new JAXBElement<InitServiceResponse>(_InitServiceResponse_QNAME, InitServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorById")
    public JAXBElement<GetSensorById> createGetSensorById(GetSensorById value) {
        return new JAXBElement<GetSensorById>(_GetSensorById_QNAME, GetSensorById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartLoggingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "startLoggingResponse")
    public JAXBElement<StartLoggingResponse> createStartLoggingResponse(StartLoggingResponse value) {
        return new JAXBElement<StartLoggingResponse>(_StartLoggingResponse_QNAME, StartLoggingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopAllLogging }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "stopAllLogging")
    public JAXBElement<StopAllLogging> createStopAllLogging(StopAllLogging value) {
        return new JAXBElement<StopAllLogging>(_StopAllLogging_QNAME, StopAllLogging.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorTypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorTypes")
    public JAXBElement<GetSensorTypes> createGetSensorTypes(GetSensorTypes value) {
        return new JAXBElement<GetSensorTypes>(_GetSensorTypes_QNAME, GetSensorTypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModifySensorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "modifySensorResponse")
    public JAXBElement<ModifySensorResponse> createModifySensorResponse(ModifySensorResponse value) {
        return new JAXBElement<ModifySensorResponse>(_ModifySensorResponse_QNAME, ModifySensorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartLiveView }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "startLiveView")
    public JAXBElement<StartLiveView> createStartLiveView(StartLiveView value) {
        return new JAXBElement<StartLiveView>(_StartLiveView_QNAME, StartLiveView.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopLogging }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "stopLogging")
    public JAXBElement<StopLogging> createStopLogging(StopLogging value) {
        return new JAXBElement<StopLogging>(_StopLogging_QNAME, StopLogging.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModifySensor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "modifySensor")
    public JAXBElement<ModifySensor> createModifySensor(ModifySensor value) {
        return new JAXBElement<ModifySensor>(_ModifySensor_QNAME, ModifySensor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorTypeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorTypeResponse")
    public JAXBElement<GetSensorTypeResponse> createGetSensorTypeResponse(GetSensorTypeResponse value) {
        return new JAXBElement<GetSensorTypeResponse>(_GetSensorTypeResponse_QNAME, GetSensorTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSensorTypesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "getSensorTypesResponse")
    public JAXBElement<GetSensorTypesResponse> createGetSensorTypesResponse(GetSensorTypesResponse value) {
        return new JAXBElement<GetSensorTypesResponse>(_GetSensorTypesResponse_QNAME, GetSensorTypesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidUserDataException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "InvalidUserDataException")
    public JAXBElement<InvalidUserDataException> createInvalidUserDataException(InvalidUserDataException value) {
        return new JAXBElement<InvalidUserDataException>(_InvalidUserDataException_QNAME, InvalidUserDataException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "initService")
    public JAXBElement<InitService> createInitService(InitService value) {
        return new JAXBElement<InitService>(_InitService_QNAME, InitService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopLiveViewResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "stopLiveViewResponse")
    public JAXBElement<StopLiveViewResponse> createStopLiveViewResponse(StopLiveViewResponse value) {
        return new JAXBElement<StopLiveViewResponse>(_StopLiveViewResponse_QNAME, StopLiveViewResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartLogging }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.sensormanagement.odysseus.is.inf.uniol.de/", name = "startLogging")
    public JAXBElement<StartLogging> createStartLogging(StartLogging value) {
        return new JAXBElement<StartLogging>(_StartLogging_QNAME, StartLogging.class, null, value);
    }

}
