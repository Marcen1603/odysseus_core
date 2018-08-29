package de.uniol.inf.is.odysseus.wrapper.dds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.domain.DomainParticipantFactoryQos;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.dynamicdata.DynamicDataReader;
import com.rti.dds.dynamicdata.DynamicDataSeq;
import com.rti.dds.dynamicdata.DynamicDataTypeSupport;
import com.rti.dds.infrastructure.BadKind;
import com.rti.dds.infrastructure.Bounds;
import com.rti.dds.infrastructure.ConditionSeq;
import com.rti.dds.infrastructure.Duration_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.WaitSet;
import com.rti.dds.publication.PublisherQos;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.SubscriberQos;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;
import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.DDSDynamicDataDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.TypeCodeMapper;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLTranslator;

public class DDSTransportHandler extends AbstractPushTransportHandler implements UncaughtExceptionHandler {

	public static final String NAME = "DDS";

	static final InfoService INFO = InfoServiceFactory.getInfoService(DDSTransportHandler.class);

	public static final String QOS_FILE = "qosfile";
	public static final String IDL_FILE = "idlfile";
	public static final String TOPIC_TYPE = "topictype";
	public static final String TOPIC = "topic";
	public static final String DOMAIN = "domain";
	public static final String QOS_LIB = "qoslibrary";
	public static final String QOS_PROFILE = "qosprofile";

	Thread reader;

	private DomainParticipant participant;
	private String topicTypeName;
	private String topicName;
	private String qosLibrary;
	private String qosProfile;

	private Topic topic;

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		try {
			return new DDSTransportHandler(protocolHandler, options);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public DDSTransportHandler() {
	}

	public DDSTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) throws FileNotFoundException {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) throws FileNotFoundException {

		//Logger.get_instance().set_verbosity(LogVerbosity.NDDS_CONFIG_LOG_VERBOSITY_STATUS_ALL);

		options.checkRequired(QOS_FILE, IDL_FILE, TOPIC, TOPIC_TYPE, QOS_LIB, QOS_PROFILE);

		// ToDo allow multiple topics?
		topicTypeName = options.get(TOPIC_TYPE);
		topicName = options.get(TOPIC);

		// qosLibrary = "ice_library";
		// qosProfile = "waveform_data";
		qosLibrary = options.get(QOS_LIB);
		qosProfile = options.get(QOS_PROFILE);

		String qosFile = options.get(QOS_FILE);
		String idlFileName = options.get(IDL_FILE);

		IDLTranslator translator = new IDLTranslator(idlFileName);
		translator.processIDLFile();

		System.out.println(TypeCodeMapper.getNametocode());

		DomainParticipantFactory domFactory = DomainParticipantFactory.get_instance();
		DomainParticipantFactoryQos factoryQos = new DomainParticipantFactoryQos();

		try {
			URL url;
			try{
				url = new URL(qosFile);
			}catch(MalformedURLException ex){
				url = new URL("file:////"+qosFile);
			}
			loadQosLibrary(factoryQos, url);
		} catch (IOException e) {
			e.printStackTrace();
			INFO.error("Error reading config file "+qosFile, e);
		}
//		factoryQos.profile.url_profile.add(qosFile);

		factoryQos.resource_limits.max_objects_per_thread = 8192;
		domFactory.set_qos(factoryQos);

		int domainId = options.getInt(DOMAIN, 0);

		// Here we use 'default' Quality of Service settings where QoS settings
		// are configured via the USER_QOS_PROFILES.xml
		// in the current working directory

		participant = DomainParticipantFactory.get_instance().create_participant(domainId,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);

		participant.enable();

        SubscriberQos subscriberQos = new SubscriberQos();
        participant.get_default_subscriber_qos(subscriberQos);

        subscriberQos.partition.name.clear();
        subscriberQos.partition.name.add("*");
        participant.set_default_subscriber_qos(subscriberQos);
        PublisherQos publisherQos = new PublisherQos();
        participant.get_default_publisher_qos(publisherQos);

        publisherQos.partition.name.clear();
        participant.set_default_publisher_qos(publisherQos);


		// Register Type and topic
        TypeCode topicTypeCode = TypeCodeMapper.getTypeCode(topicTypeName);
		if (topicTypeCode == null) {
			throw new IllegalArgumentException("Typecode " + topicTypeName + " for topic " + topicName + " not defined.");
		}

		topicTypeCode.print_IDL(0);
		// set schema from type code
		try {
			SDFSchema schema = TypeCodeMapper.typeCodeToSchema(topicTypeCode);
			setSchema(schema);
		} catch (BadKind | Bounds e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		DynamicDataTypeSupport dynamicTypeSupport = new DynamicDataTypeSupport(topicTypeCode,
				DynamicDataTypeSupport.TYPE_PROPERTY_DEFAULT);

		dynamicTypeSupport.register_type(participant, topicTypeName);


		topic = participant.create_topic(topicName, topicTypeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);

	}

    public static void loadQosLibrary(DomainParticipantFactoryQos qos, URL url) throws IOException {

        if (url != null) {

            InputStream is = url.openStream();
            java.util.Scanner scanner = new java.util.Scanner(is);
            try {
                qos.profile.url_profile.clear();
                qos.profile.string_profile.clear();
                qos.profile.string_profile.add(scanner.useDelimiter("\\A").next());
            } finally {
                scanner.close();
                try {
                    is.close();
                } catch (IOException e) {
                    INFO.error("", e);
                }
            }
        }
        else{
            INFO.warning("Could not locate config file "+url);
    }
    }

	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public void processInOpen() throws IOException {

	}

	@Override
	public void processInStart() {
		reader = new Thread() {

			@Override
			public void run() {
				try {

					// Create a reader endpoint
					DynamicDataReader reader = (DynamicDataReader) participant.create_datareader_with_profile(topic,
							qosLibrary, qosProfile, null , StatusKind.STATUS_MASK_NONE);

					// A waitset allows us to wait for various status changes in
					// various
					// entities
					WaitSet ws = new WaitSet();

					// Here we configure the status condition to trigger when
					// new
					// data
					// becomes available to the reader
					reader.get_statuscondition().set_enabled_statuses(StatusKind.DATA_AVAILABLE_STATUS);

					// And register that status condition with the waitset so we
					// can
					// monitor
					// its triggering
					ws.attach_condition(reader.get_statuscondition());

					// will contain triggered conditions
					ConditionSeq cond_seq = new ConditionSeq();

					// we'll wait as long as necessary for data to become
					// available
					Duration_t timeout = new Duration_t(Duration_t.DURATION_INFINITE_SEC,
							Duration_t.DURATION_INFINITE_NSEC);

//					// // Will contain the data samples we read from the reader
					DynamicDataSeq data_seq = new DynamicDataSeq();

			        // Will contain the data samples we read from the reader
//			        ice.SampleArraySeq data_seq = new ice.SampleArraySeq();

					// Will contain the SampleInfo information about those data
					SampleInfoSeq info_seq = new SampleInfoSeq();

					// This loop will repeat until the process is terminated
					while (!isInterrupted()) {
						// Wait for a condition to be triggered
						ws.wait(cond_seq, timeout);
						// Check that our status condition was indeed triggered
						if (cond_seq.contains(reader.get_statuscondition())) {
							// read the actual status changes
							int status_changes = reader.get_status_changes();

							// Ensure that DATA_AVAILABLE is one of the statuses
							// that
							// changed in the DataReader.
							// Since this is the only enabled status (see above)
							// this is
							// here mainly for completeness
							if (0 != (status_changes & StatusKind.DATA_AVAILABLE_STATUS)) {
								try {
									// Read samples from the reader
									reader.read(data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
											SampleStateKind.NOT_READ_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
											InstanceStateKind.ALIVE_INSTANCE_STATE);

									// Iterator over the samples
									for (int i = 0; i < info_seq.size(); i++) {
										SampleInfo si = (SampleInfo) info_seq.get(i);
										DynamicData data = (DynamicData) data_seq.get(i);
										// If the updated sample status contains
										// fresh data
										// that we can evaluate
										if (si.valid_data) {

											DDSDynamicDataDataReader complexTypeReader = (DDSDynamicDataDataReader) TypeCodeMapper
													.getDataReader(topicTypeName);
											Tuple<IMetaAttribute> result = complexTypeReader.getValue(data);

											fireProcess(result);
										}

									}
								} catch (RETCODE_NO_DATA noData) {
									// No Data was available to the read call
								} finally {
									// the objects provided by "read" are owned
									// by
									// the
									// reader and we must return them
									// so the reader can control their lifecycle
									reader.return_loan(data_seq, info_seq);
								}
							}
						}
					}
				} catch (Exception e) {
					INFO.error("Error reading from DDS Source", e);
				}
			}
		};
		reader.setName("DDS Thread");
		reader.setDaemon(true);
		reader.start();
		reader.setUncaughtExceptionHandler(this);
	}


	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processInClose() throws IOException {
		reader.interrupt();
	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(t+" ");
		e.printStackTrace();
	}

}
