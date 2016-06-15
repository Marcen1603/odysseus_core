package de.uniol.inf.is.odysseus.wrapper.dds.logicaloperator;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractReceiveAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.wrapper.dds.DDSTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "DDSSource", category={LogicalOperatorCategory.SOURCE}, doc = "Allows to read input from a csv based file")
public class DDSSource extends AbstractReceiveAO {

	private static final long serialVersionUID = -3200078517889579479L;
	boolean textDelimiterSet = false;

	public DDSSource() {
		super();
		setTransportHandler(DDSTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
	}

	public DDSSource(DDSSource csvFileSource) {
		super(csvFileSource);
		textDelimiterSet = csvFileSource.textDelimiterSet;
	}


	@Parameter(type = FileNameParameter.class, name = DDSTransportHandler.QOS_FILE, optional = false, doc = "The file that contains the qos information")
	public void setFilename(String filename) {
		addOption(DDSTransportHandler.QOS_FILE,filename);
	}
	
	public String getFilename() {
		return getOption(DDSTransportHandler.QOS_FILE);
	}

	@Parameter(type = FileNameParameter.class, name = DDSTransportHandler.IDL_FILE, optional = false, doc = "The IDL file that describes the data  model.")
	public void setIDLFile(String filename) {
		addOption(DDSTransportHandler.IDL_FILE,filename);
	}
	
	public String getIDLFile() {
		return getOption(DDSTransportHandler.IDL_FILE);
	}

	@Parameter(type = StringParameter.class, name = DDSTransportHandler.TOPIC, optional = false, doc = "The DDS topic to connect to.")
	public void setTopic(String topic){
		addOption(DDSTransportHandler.TOPIC, topic);
	}
	
	public String getTopic(){
		return getOption(DDSTransportHandler.TOPIC);
	}

	@Parameter(type = StringParameter.class, name = DDSTransportHandler.TOPIC_TYPE, optional = false, doc = "The DDS topic type to connect to. Must match, else there will be no connection!")
	public void setTopicType(String topicType){
		addOption(DDSTransportHandler.TOPIC_TYPE, topicType);
	}
	
	public String getTopicType(){
		return getOption(DDSTransportHandler.TOPIC_TYPE);
	}
	
	@Parameter(type = StringParameter.class, name = DDSTransportHandler.QOS_LIB, optional = false, doc = "The name of the qos library for this topic.")
	public void setQOSLibrary(String qosLib){
		addOption(DDSTransportHandler.QOS_LIB, qosLib);
	}
	
	public String getQOSLibrary(){
		return getOption(DDSTransportHandler.QOS_LIB);
	}

	@Parameter(type = StringParameter.class, name = DDSTransportHandler.QOS_PROFILE, optional = false, doc = "The QOS_PROFILE of the qos library for this topic.")
	public void setQOSProfile(String qosLib){
		addOption(DDSTransportHandler.QOS_PROFILE, qosLib);
	}
	
	public String getQOSProfile(){
		return getOption(DDSTransportHandler.QOS_PROFILE);
	}

	@Parameter(type = LongParameter.class, name = DDSTransportHandler.DOMAIN, optional = false, doc = "The DDS domain.")
	public void setDomain(Long qosLib){
		addOption(DDSTransportHandler.DOMAIN, qosLib+"");
	}
	
	public Long getDomain(){
		return Long.parseLong(getOption(DDSTransportHandler.DOMAIN));
	}

	@Override
	public AbstractReceiveAO clone() {
		return new DDSSource(this);
	}
		

}
