

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.domain.DomainParticipantFactoryQos;
import com.rti.dds.dynamicdata.DynamicData;
import com.rti.dds.dynamicdata.DynamicDataReader;
import com.rti.dds.dynamicdata.DynamicDataSeq;
import com.rti.dds.dynamicdata.DynamicDataTypeSupport;
import com.rti.dds.infrastructure.ConditionSeq;
import com.rti.dds.infrastructure.Duration_t;
import com.rti.dds.infrastructure.FloatSeq;
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
import com.rti.dds.typecode.ExtensibilityKind;
import com.rti.dds.typecode.StructMember;
import com.rti.dds.typecode.TCKind;
import com.rti.dds.typecode.TypeCode;
import com.rti.dds.typecode.TypeCodeFactory;

public class HelloICE {
	public static void main(String[] args) {
		
		DomainParticipantFactory domFactory = DomainParticipantFactory.get_instance();
		DomainParticipantFactoryQos factoryQos = new DomainParticipantFactoryQos();
		factoryQos.profile.url_profile.add("umbenannt.xml");
		
		domFactory.set_qos(factoryQos);
		
		
		int domainId = 0;

		// domainId is the one command line argument
		if (args.length > 0) {
			domainId = Integer.parseInt(args[0]);
		}

		// Extract to vars;
		String ice_sample_array_type_support_get_type_name = "ice::SampleArray";
		String ice_numeric_type_support_get_type_name = "ice::Numeric";// ice.NumericTypeSupport.get_type_name();
		String ice_sample_array_topic_value = "SampleArray"; // ice.SampleArrayTopic.VALUE;
		String ice_numeric_topic_value = "Numeric";// ice.NumericTopic.VALUE;

		// QoS
		String QosProfiles_ice_library = "ice_library";
		String QosProfiles_default_profile = "default_profile";
		String QosProfiles_state = "state";
		String QosProfiles_device_identity = "device_identity";
		String QosProfiles_numeric_data = "numeric_data";
		String QosProfiles_waveform_data = "waveform_data";
		String QosProfiles_heartbeat = "heartbeat";
		String QosProfiles_timesync = "timesync";

		// Here we use 'default' Quality of Service settings where QoS settings
		// are configured via the USER_QOS_PROFILES.xml
		// in the current working directory

		// A domain participant is the main access point into the DDS domain.
		// Endpoints are created within the domain participant
		DomainParticipant participant = DomainParticipantFactory.get_instance()
				.create_participant(domainId,
						DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null,
						StatusKind.STATUS_MASK_NONE);
		
		
		//TypeCodeFactory factory = TypeCodeFactory.get_instance();

		// Create Types dynamically:

		// -----------------------------------
		// Basetypes
		// -----------------------------------
		TypeCode uniqueDeviceIdentifierTypeCode = TypeCodeFactory.TheTypeCodeFactory
				.create_alias_tc("ice::UniqueDeviceIdentifier", new TypeCode(
						TCKind.TK_STRING, 64), false);
		TypeCode metricIdentifierTypeCode = TypeCodeFactory.TheTypeCodeFactory
				.create_alias_tc("ice::MetricIdentifier", new TypeCode(
						TCKind.TK_STRING, 64), false);
		TypeCode instanceIdentifierTypeCode = TypeCodeFactory.TheTypeCodeFactory
				.create_alias_tc("ice::InstanceIdentifier", TypeCode.TC_LONG,
						false);
		TypeCode unitIdentifierTypeCode = TypeCodeFactory.TheTypeCodeFactory
				.create_alias_tc("ice::UnitIdentifier", new TypeCode(
						TCKind.TK_STRING, 64), false);
		TypeCode valuesTypeCode = TypeCodeFactory.TheTypeCodeFactory
				.create_alias_tc("ice::Values", new TypeCode(1024,
						TypeCode.TC_FLOAT), false);
		uniqueDeviceIdentifierTypeCode.print_IDL(2);
		
	

		int pos = 0;
		StructMember sm[] = new StructMember[2];

		sm[pos] = new StructMember("sec", false, (short) -1, false,
				(TypeCode) TypeCode.TC_LONG, 0, false);
		pos++;
		sm[pos] = new StructMember("nanosec", false, (short) -1, false,
				(TypeCode) TypeCode.TC_LONG, 1, false);
		pos++;

		TypeCode time_tTypeCode = TypeCodeFactory.TheTypeCodeFactory
				.create_struct_tc("ice::Time_t",
						ExtensibilityKind.EXTENSIBLE_EXTENSIBILITY, sm);

		// ----------------------------------------------------------
		// SampleArray
		// ----------------------------------------------------------
		
		StructMember sample_array_type_member[] = new StructMember[7];
		String[] sampleArrayNames = {"unique_device_identifier", "metric_id", "instance_id", "unit_id", "frequency", "values","device_time"};
		pos = 0;
		sample_array_type_member[pos] = new StructMember(
				sampleArrayNames[pos], false, (short) -1, true,
				uniqueDeviceIdentifierTypeCode, 0, false);
		pos++;
		sample_array_type_member[pos] = new StructMember(sampleArrayNames[pos], false,
				(short) -1, true, metricIdentifierTypeCode, 1, false);
		pos++;
		sample_array_type_member[pos] = new StructMember(sampleArrayNames[pos], false,
				(short) -1, true, instanceIdentifierTypeCode, 2, false);
		pos++;
		sample_array_type_member[pos] = new StructMember(sampleArrayNames[pos], false,
				(short) -1, true, unitIdentifierTypeCode, 3, false);
		pos++;
		sample_array_type_member[pos] = new StructMember(sampleArrayNames[pos], false,
				(short) -1, true, TypeCode.TC_LONG, 4, false);
		pos++;
		sample_array_type_member[pos] = new StructMember(sampleArrayNames[pos], false,
				(short) -1, false, valuesTypeCode, 5, false);
		pos++;
		sample_array_type_member[pos] = new StructMember(sampleArrayNames[pos], false,
				(short) -1, false, time_tTypeCode, 6, false);
		pos++;

		TypeCode sample_array_type = TypeCodeFactory.TheTypeCodeFactory
				.create_struct_tc(ice_sample_array_type_support_get_type_name,
						ExtensibilityKind.EXTENSIBLE_EXTENSIBILITY,
						sample_array_type_member);

		DynamicDataTypeSupport sampleArraytypeSupport = new DynamicDataTypeSupport(
				sample_array_type, DynamicDataTypeSupport.TYPE_PROPERTY_DEFAULT);

		sampleArraytypeSupport.register_type(participant,
				ice_sample_array_type_support_get_type_name);

		sample_array_type.print_IDL(2);
		
		// ----------------------------------------------------------
		// Numeric Type
		// ----------------------------------------------------------
		pos = 0;
		sm = new StructMember[6];

		sm[pos] = new StructMember("unique_device_identifier", false, (short) -1,
				true, uniqueDeviceIdentifierTypeCode, 0, false);
		pos++;
		sm[pos] = new StructMember("metric_id", false, (short) -1, true,
				metricIdentifierTypeCode, 1, false);
		pos++;
		sm[pos] = new StructMember("instance_id", false, (short) -1, true,
				instanceIdentifierTypeCode, 2, false);
		pos++;
		sm[pos] = new StructMember("unit_id", false, (short) -1, true,
				unitIdentifierTypeCode, 3, false);
		pos++;
		sm[pos] = new StructMember("value", false, (short) -1, false,
				TypeCode.TC_FLOAT, 4, false);
		pos++;
		sm[pos] = new StructMember("device_time", false, (short) -1, false,
				valuesTypeCode, 5, false);
		pos++;

		TypeCode numeric_type = TypeCodeFactory.TheTypeCodeFactory
				.create_struct_tc("ice::Numeric",
						ExtensibilityKind.EXTENSIBLE_EXTENSIBILITY, sm);

		DynamicDataTypeSupport numericValueTypeSupport = new DynamicDataTypeSupport(
				numeric_type, DynamicDataTypeSupport.TYPE_PROPERTY_DEFAULT);

		numericValueTypeSupport.register_type(participant,
				ice_numeric_type_support_get_type_name);

		// --------------------------------------------------------------------
		// TOPICS
		// --------------------------------------------------------------------
		
		Topic sampleArrayTopic = participant.create_topic(
				ice_sample_array_topic_value,
				ice_sample_array_type_support_get_type_name,
				DomainParticipant.TOPIC_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);

		// A second topic if for Numeric data
		Topic numericTopic = participant.create_topic(ice_numeric_topic_value,
				ice_numeric_type_support_get_type_name,
				DomainParticipant.TOPIC_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);

		// Create a reader endpoint for samplearray data
		DynamicDataReader saReader = (DynamicDataReader) participant.create_datareader_with_profile(
				sampleArrayTopic, QosProfiles_ice_library,
				QosProfiles_waveform_data, null, StatusKind.STATUS_MASK_NONE);

		DynamicDataReader nReader = (DynamicDataReader) participant.create_datareader_with_profile(
				numericTopic, QosProfiles_ice_library,
				QosProfiles_numeric_data, null, StatusKind.STATUS_MASK_NONE);

		// A waitset allows us to wait for various status changes in various
		// entities
		WaitSet ws = new WaitSet();

		// Here we configure the status condition to trigger when new data
		// becomes available to the reader
		saReader.get_statuscondition().set_enabled_statuses(
				StatusKind.DATA_AVAILABLE_STATUS);

		nReader.get_statuscondition().set_enabled_statuses(
				StatusKind.DATA_AVAILABLE_STATUS);

		// And register that status condition with the waitset so we can monitor
		// its triggering
		ws.attach_condition(saReader.get_statuscondition());

		ws.attach_condition(nReader.get_statuscondition());

		// will contain triggered conditions
		ConditionSeq cond_seq = new ConditionSeq();

		// we'll wait as long as necessary for data to become available
		Duration_t timeout = new Duration_t(Duration_t.DURATION_INFINITE_SEC,
				Duration_t.DURATION_INFINITE_NSEC);

//		// Will contain the data samples we read from the reader
//		ice.SampleArraySeq sa_data_seq = new ice.SampleArraySeq();
		DynamicDataSeq sa_data_seq = new DynamicDataSeq();

		//ice.NumericSeq n_data_seq = new ice.NumericSeq();
		DynamicDataSeq n_data_seq = new DynamicDataSeq();
		
		// Will contain the SampleInfo information about those data
		SampleInfoSeq info_seq = new SampleInfoSeq();

		// This loop will repeat until the process is terminated
		for (;;) {
			// Wait for a condition to be triggered
			ws.wait(cond_seq, timeout);
			// Check that our status condition was indeed triggered
			if (cond_seq.contains(saReader.get_statuscondition())) {
				// read the actual status changes
				int status_changes = saReader.get_status_changes();

				// Ensure that DATA_AVAILABLE is one of the statuses that
				// changed in the DataReader.
				// Since this is the only enabled status (see above) this is
				// here mainly for completeness
				if (0 != (status_changes & StatusKind.DATA_AVAILABLE_STATUS)) {
					try {
						// Read samples from the reader
						saReader.read(sa_data_seq, info_seq,
								ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
								SampleStateKind.NOT_READ_SAMPLE_STATE,
								ViewStateKind.ANY_VIEW_STATE,
								InstanceStateKind.ALIVE_INSTANCE_STATE);

						// Iterator over the samples
						for (int i = 0; i < info_seq.size(); i++) {
							SampleInfo si = (SampleInfo) info_seq.get(i);
							DynamicData data = (DynamicData) sa_data_seq
									.get(i);
							// If the updated sample status contains fresh data
							// that we can evaluate
							if (si.valid_data) {
								System.out.println(data.get_string(sampleArrayNames[0], 0));
								System.out.println(data.get_string(sampleArrayNames[1], 0));						
								System.out.println(data.get_long(sampleArrayNames[2], 0));						
								System.out.println(data.get_string(sampleArrayNames[3], 0));						
								System.out.println(data.get_long(sampleArrayNames[4], 0));						
								FloatSeq fls = new FloatSeq();
								data.get_float_seq(fls, sampleArrayNames[5], 0);
								System.out.println(fls);			
								DynamicData dd = new DynamicData();
								data.get_complex_member(dd, sampleArrayNames[6], 0);
								System.out.println("Time "+dd.get_long("sec",0)+":"+dd.get_long("nanosec",0));
								System.out.println();
							}

						}
					} catch (RETCODE_NO_DATA noData) {
						// No Data was available to the read call
					} finally {
						// the objects provided by "read" are owned by the
						// reader and we must return them
						// so the reader can control their lifecycle
						saReader.return_loan(sa_data_seq, info_seq);
					}
				}
			}
			if (cond_seq.contains(nReader.get_statuscondition())) {
				// read the actual status changes
				int status_changes = nReader.get_status_changes();

				// Ensure that DATA_AVAILABLE is one of the statuses that
				// changed in the DataReader.
				// Since this is the only enabled status (see above) this is
				// here mainly for completeness
				if (0 != (status_changes & StatusKind.DATA_AVAILABLE_STATUS)) {
					try {
						// Read samples from the reader
						nReader.read(n_data_seq, info_seq,
								ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
								SampleStateKind.NOT_READ_SAMPLE_STATE,
								ViewStateKind.ANY_VIEW_STATE,
								InstanceStateKind.ALIVE_INSTANCE_STATE);

						// Iterator over the samples
						for (int i = 0; i < info_seq.size(); i++) {
							SampleInfo si = (SampleInfo) info_seq.get(i);
							DynamicData data = (DynamicData) n_data_seq.get(i);
							// If the updated sample status contains fresh data
							// that we can evaluate
							if (si.valid_data) {
//								if (data.metric_id
//										.equals(rosetta.MDC_PULS_OXIM_SAT_O2.VALUE)) {
//									// This is an O2 saturation from pulse
//									// oximetry
//									// System.out.println("SpO2="+data.value);
//								} else if (data.metric_id
//										.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)) {
//									// This is a pulse rate from pulse oximetry
//									// System.out.println("Pulse Rate="+data.value);
//								}
								System.out.println(data);
							}

						}
					} catch (RETCODE_NO_DATA noData) {
						// No Data was available to the read call
					} finally {
						// the objects provided by "read" are owned by the
						// reader and we must return them
						// so the reader can control their lifecycle
						nReader.return_loan(n_data_seq, info_seq);
					}
				}
			}
		}
	}
}
