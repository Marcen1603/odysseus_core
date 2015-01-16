package de.uniol.inf.is.odysseus.keyvalue.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="KeyValueToTuple", doc="Translates a key-value/json object to a tuple", category={LogicalOperatorCategory.TRANSFORM})
public class KeyValueToTupleAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 4804826171047928513L;
	
	private boolean keepInputObject = false;
	private String type = "";
	private List<SDFAttribute> attributes;

	public KeyValueToTupleAO() {
	}
	
	public KeyValueToTupleAO(KeyValueToTupleAO keyValueToTuple) {
		super(keyValueToTuple);
		this.keepInputObject = keyValueToTuple.keepInputObject;
		this.attributes = keyValueToTuple.attributes;
	}

	@Parameter(name="Schema", type=CreateSDFAttributeParameter.class, optional=false, isList=true)
	public void setAttributes(List<SDFAttribute> attributes){
		this.attributes = attributes;
	}
	
	public List<SDFAttribute> getAttributes() {
		return this.attributes;
	}
	
	@Parameter(name = "type", type=StringParameter.class, optional= false )
	public void setType(String type){
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	@Parameter(name = "keepInput", type=BooleanParameter.class )
	public void setKeepInputObject(boolean keepInputObject) {
		this.keepInputObject = keepInputObject;
	}
	
	public boolean isKeepInputObject() {
		return keepInputObject;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema schema = new SDFSchema(type, Tuple.class, attributes);
		return schema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new KeyValueToTupleAO(this);
	}

}
