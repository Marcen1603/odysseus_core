package de.uniol.inf.is.odysseus.keyvalue.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateAndRenameSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="KeyValueToTuple", doc="Translates a key-value/json object to a tuple", category={LogicalOperatorCategory.TRANSFORM})
public class KeyValueToTupleAO extends UnaryLogicalOp{

	private static final long serialVersionUID = 4804826171047928513L;
	
	private boolean keepInputObject = false;
	private String type = "";
	private List<RenameAttribute> attributes;

	public KeyValueToTupleAO() {
	}
	
	public KeyValueToTupleAO(KeyValueToTupleAO keyValueToTuple) {
		super(keyValueToTuple);
		this.keepInputObject = keyValueToTuple.keepInputObject;
		this.attributes = keyValueToTuple.attributes;
	}

	@Parameter(name="Schema", type=CreateAndRenameSDFAttributeParameter.class, optional=false, isList=true)
	public void setAttributes(List<RenameAttribute> attributes){
		this.attributes = attributes;
	}
	
	public List<RenameAttribute> getAttributes() {
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
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		for(RenameAttribute att: attributes) {
			SDFAttribute sdfAtt = att.getAttribute();
			String name;
			if(!att.getNewName().equals("")) {
				name = att.getNewName();
			} else {
				name = att.getAttribute().getQualName();
			}
			attributeList.add(new SDFAttribute(sdfAtt.getSourceName(), name, sdfAtt.getDatatype(), sdfAtt.getUnit(), sdfAtt.getDtConstraints()));
		} 
		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema(type, (Class<? extends IStreamObject<?>>) Tuple.class, attributeList,getInputSchema());
		return schema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new KeyValueToTupleAO(this);
	}

}
