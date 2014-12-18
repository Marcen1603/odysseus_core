package de.uniol.inf.is.odysseus.wrapper.dds;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.domain.DomainParticipantFactoryQos;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.dynamicdata.DynamicDataReader;
import com.rti.dds.dynamicdata.DynamicDataSeq;
import com.rti.dds.dynamicdata.DynamicDataTypeSupport;
import com.rti.dds.infrastructure.ConditionSeq;
import com.rti.dds.infrastructure.Duration_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.WaitSet;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;
import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.DDSDynamicDataDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.TypeCodeMapper;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLTranslator;

public class DDSTransportHandler extends AbstractPushTransportHandler {

	static final String QOS_FILE = "qosfile";
	static final String IDL_FILE = "idlfile";
	static final String TOPIC_TYPE = "topictype";
	static final String TOPIC = "topic";
	static final String QOS_LIB = "qoslibrary";
	static final String QOS_PROFILE = "qosprofile";
	
	Thread reader;

	private DomainParticipant participant;
	private String topicType;
	private String topicName;
	private String qosLibrary;
	private String qosProfile;

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		try {
			return new DDSTransportHandler(protocolHandler, options);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public DDSTransportHandler() {
	}

	public DDSTransportHandler(IProtocolHandler<?> protocolHandler,
			OptionMap options) throws FileNotFoundException {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) throws FileNotFoundException {

		options.checkRequired(QOS_FILE, IDL_FILE, TOPIC, TOPIC_TYPE, QOS_LIB, QOS_PROFILE);

		// ToDo allow multiple topics?
		topicType = options.get(TOPIC_TYPE);
		topicName = options.get(TOPIC);
		
//		qosLibrary = "ice_library";
//		qosProfile = "waveform_data";
		qosLibrary = options.get(QOS_LIB);
		qosProfile = options.get(QOS_PROFILE);

		
		String qosFile = options.get(QOS_FILE);
		String idlFileName = options.get(IDL_FILE);

		IDLTranslator translator = new IDLTranslator(idlFileName);
		translator.processIDLFile();

		DomainParticipantFactory domFactory = DomainParticipantFactory
				.get_instance();
		DomainParticipantFactoryQos factoryQos = new DomainParticipantFactoryQos();

		factoryQos.profile.url_profile.add(qosFile);

		domFactory.set_qos(factoryQos);

		int domainId = 0;



		// Here we use 'default' Quality of Service settings where QoS settings
		// are configured via the USER_QOS_PROFILES.xml
		// in the current working directory

		participant = DomainParticipantFactory.get_instance()
				.create_participant(domainId,
						DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null,
						StatusKind.STATUS_MASK_NONE);
		
		// Register Type and topic
		
		TypeCode topicTypeCode = TypeCodeMapper.getTypeCode(topicType);
		if (topicTypeCode == null){
			throw new IllegalArgumentException("Typecode "+topicType+" for topic "+topicName+" not defined.");
		}
		
		DynamicDataTypeSupport dynamicTypeSupport = new DynamicDataTypeSupport(
				topicTypeCode, DynamicDataTypeSupport.TYPE_PROPERTY_DEFAULT);

		dynamicTypeSupport.register_type(participant, topicName);
	}

	@Override
	public String getName() {
		return "DDS";
	}

	@Override
	public void processInOpen() throws IOException {

		reader = new Thread() {

			@Override
			public void run() {
				Topic thisTopic = participant.create_topic(topicName,
						topicType, DomainParticipant.TOPIC_QOS_DEFAULT, null,
						StatusKind.STATUS_MASK_NONE);

				// Create a reader endpoint for samplearray data
				DynamicDataReader reader = (DynamicDataReader) participant
						.create_datareader_with_profile(thisTopic,
								qosLibrary,
								qosProfile, null,
								StatusKind.STATUS_MASK_NONE);

				// A waitset allows us to wait for various status changes in
				// various
				// entities
				WaitSet ws = new WaitSet();

				// Here we configure the status condition to trigger when new
				// data
				// becomes available to the reader
				reader.get_statuscondition().set_enabled_statuses(
						StatusKind.DATA_AVAILABLE_STATUS);

				// And register that status condition with the waitset so we can
				// monitor
				// its triggering
				ws.attach_condition(reader.get_statuscondition());

				// will contain triggered conditions
				ConditionSeq cond_seq = new ConditionSeq();

				// we'll wait as long as necessary for data to become available
				Duration_t timeout = new Duration_t(
						Duration_t.DURATION_INFINITE_SEC,
						Duration_t.DURATION_INFINITE_NSEC);

				// // Will contain the data samples we read from the reader
				DynamicDataSeq data_seq = new DynamicDataSeq();

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
								reader.read(
										data_seq,
										info_seq,
										ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
										SampleStateKind.NOT_READ_SAMPLE_STATE,
										ViewStateKind.ANY_VIEW_STATE,
										InstanceStateKind.ALIVE_INSTANCE_STATE);

								// Iterator over the samples
								for (int i = 0; i < info_seq.size(); i++) {
									SampleInfo si = (SampleInfo) info_seq
											.get(i);
									DynamicData data = (DynamicData) data_seq
											.get(i);
									// If the updated sample status contains
									// fresh data
									// that we can evaluate
									if (si.valid_data) {

										DDSDynamicDataDataReader complexTypeReader = (DDSDynamicDataDataReader) TypeCodeMapper
												.getDataReader(topicType);
										Tuple<IMetaAttribute> result = complexTypeReader
												.getValue(data);

										System.out.println(result);
										fireProcess(result);
									}

								}
							} catch (RETCODE_NO_DATA noData) {
								// No Data was available to the read call
							} finally {
								// the objects provided by "read" are owned by
								// the
								// reader and we must return them
								// so the reader can control their lifecycle
								reader.return_loan(data_seq, info_seq);
							}
						}
					}
				}
			}
		};
		reader.setName("DDS Thread");
		reader.setDaemon(true);
		reader.start();
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

}
