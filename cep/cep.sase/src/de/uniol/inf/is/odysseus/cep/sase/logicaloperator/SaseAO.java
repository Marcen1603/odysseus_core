package de.uniol.inf.is.odysseus.cep.sase.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="SASE", minInputPorts=1, maxInputPorts=Integer.MAX_VALUE, doc="This operator can parse a query in SASE+ syntax.")
public class SaseAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 15531070881194678L;
	String query;
	boolean oneMatchPerInstance;
	int heartbeatrate;
	private List<SDFAttribute> attributes;
	private String typeName;
		
	public SaseAO(SaseAO op) {
		super(op);
		this.query = op.query;
		this.oneMatchPerInstance = op.oneMatchPerInstance;
		this.heartbeatrate = op.heartbeatrate;
		this.typeName = op.typeName;
		this.attributes = new ArrayList<SDFAttribute>(op.attributes);
	}

	public SaseAO() {
		super();
	}

	public String getQuery() {
		return query;
	}

	@Parameter(name="Query", type=StringParameter.class, optional=false)
	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isOneMatchPerInstance() {
		return oneMatchPerInstance;
	}

	@Parameter(name="OneMatchPerInstance", type=BooleanParameter.class)
	public void setOneMatchPerInstance(boolean oneMatchPerInstance) {
		this.oneMatchPerInstance = oneMatchPerInstance;
	}

	public int getHeartbeatrate() {
		return heartbeatrate;
	}

	@Parameter(name="Heartbeatrate", type=IntegerParameter.class)
	public void setHeartbeatrate(int heartbeatrate) {
		this.heartbeatrate = heartbeatrate;
	}
	
	@Parameter(name="SCHEMA", type=CreateSDFAttributeParameter.class, optional=false, isList=true)
	public void setAttributes(List<SDFAttribute> attributes){
		this.attributes = attributes;
	}
	
	@Parameter(name="Type", type=StringParameter.class, optional=false)
	public void setTypeName(String typeName){
		this.typeName=typeName;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema schema = new SDFSchema(typeName, Tuple.class, attributes);
		return schema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new SaseAO(this);
	}

}
