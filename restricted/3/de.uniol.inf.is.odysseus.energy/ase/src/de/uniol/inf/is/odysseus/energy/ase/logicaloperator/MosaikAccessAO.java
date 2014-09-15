package de.uniol.inf.is.odysseus.energy.ase.logicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "MOSAIK", doc="operator to connect to mosaik.", category={LogicalOperatorCategory.SOURCE})
public class MosaikAccessAO extends AbstractAccessAO {
	private static final long serialVersionUID = -6949561307060489773L;

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
	
	@Override
	public AbstractLogicalOperator clone() {
		return new MosaikAccessAO(this);
	}
	
	private void init() {
		this.setWrapper("GenericPush");
		this.setTransportHandler("ZeroMQ");
		this.setProtocolHandler("JSON");
		this.setDataHandler("KeyValueObject");
		Map<String, String> options = new HashMap<String, String>();
		options.put("host","127.0.0.1");
		options.put("readport", "5558");
		options.put("writeport", "5559");
		options.put("delayofmsg", "0");
		options.put("threads", "1");
		options.put("ByteOrder", "LittleEndian");
		options.put("mosaik", "true");
		this.setOptionMap(options);
	}

}
