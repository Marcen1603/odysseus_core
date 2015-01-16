package de.uniol.inf.is.odysseus.wrapper.mosaik.logicaloperator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "MOSAIK", doc="operator to connect to mosaik.", category={LogicalOperatorCategory.SOURCE})
public class MosaikAccessAO extends AbstractAccessAO {
	private static final long serialVersionUID = -6949561307060489773L;
	
	private String version;

	public MosaikAccessAO() {
		super();
		this.init();
	}

	public MosaikAccessAO(MosaikAccessAO other){
		super(other);
	}
	
	public MosaikAccessAO(Resource resource, String wrapper, String transport,
			String protocol, String datahandler, Map<String, String> options) {
		super();
		this.init();
	}
	
	@Parameter(type = StringParameter.class, name = "VERSION", optional = true, isList = false, doc = "use zeromq or simapi")
	public void setVersion(String version) {
		this.version = version;
		this.init();
	}

	@GetParameter(name = "VERSION")
	public String getVersion() {
		return this.version;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new MosaikAccessAO(this);
	}
	
	private void init() {
		Map<String, String> options = new HashMap<String, String>();
		if(this.version != null && this.version.equalsIgnoreCase("simapi")) {
			this.setWrapper("GenericPush");
			this.setTransportHandler("TCPServer");
			this.setProtocolHandler("Mosaik");
			this.setDataHandler("KeyValueObject");
			
			options.put("port", "5554");
		} else {
			this.setWrapper("GenericPush");
			this.setTransportHandler("ZeroMQ");
			this.setProtocolHandler("JSON");
			this.setDataHandler("KeyValueObject");
			
			options.put("host","127.0.0.1");
			options.put("readport", "5558");
			options.put("writeport", "5559");
			options.put("delayofmsg", "0");
			options.put("threads", "1");
			options.put("subscriptionfilter", "");
			options.put("basetimeunit", "seconds");
		}
		options.put("byteorder", "LittleEndian");		
		this.setOptionMap(options);
		
		List<SDFAttribute> schemaAttributes = new ArrayList<>();
		schemaAttributes.add(new SDFAttribute("", "timestamp", SDFDatatype.START_TIMESTAMP, null));
		this.setOutputSchema(new SDFSchema("", KeyValueObject.class , schemaAttributes));
	}

}
