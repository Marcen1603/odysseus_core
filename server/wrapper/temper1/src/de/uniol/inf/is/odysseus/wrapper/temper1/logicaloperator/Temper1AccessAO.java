package de.uniol.inf.is.odysseus.wrapper.temper1.logicaloperator;


import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;


@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "Temper1ACCESS", doc="Returns the value of a temperature sensor of the type TEMPer1.", category={LogicalOperatorCategory.SOURCE})
public class Temper1AccessAO extends AbstractAccessAO {
	
	private static final long serialVersionUID = 1L;
	
	public Temper1AccessAO() {
		super();
		setWrapper(Constants.GENERIC_PULL);
		
		List<SDFAttribute> schema = new LinkedList<>();
		schema.add(new SDFAttribute(null, "temperature", SDFDatatype.DOUBLE, null));
		schema.add(new SDFAttribute(null, "timestamp", SDFDatatype.TIMESTAMP, null));
		setAttributes(schema);
	}
	
	public Temper1AccessAO(Temper1AccessAO temper1AccessAO) {
		super(temper1AccessAO);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new Temper1AccessAO(this);
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "protocol", optional = false, possibleValues="getProtocolValues", doc = "The name of the protocol handler to use, e.g. Csv or SizeByteBuffer.")
	public void setProtocolHandler(String protocolHandler) {
		super.setProtocolHandler(protocolHandler);
	}
	
	public List<String> getProtocolValues(){
		return ProtocolHandlerRegistry.getHandlerNames();
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "DataHandler", optional = false, possibleValues="getDataHandlerValues", doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		super.setDataHandler(dataHandler);
	}
}
