package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLStreamObjectToTuple", doc = "Translates an XMLStreamobject to a tuple", category = {
		LogicalOperatorCategory.TRANSFORM })
public class ToTupleAO extends UnaryLogicalOp
{
	private static final long serialVersionUID = -2295654008085314986L;

	Logger LOG = LoggerFactory.getLogger(ToTupleAO.class);

	public ToTupleAO() {
	}

	public ToTupleAO(ToTupleAO toTuple) {
		super(toTuple);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos)
	{
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		attributeList.add(new SDFAttribute("XML Source", "XML", new SDFDatatype("STRING")));
		final List<SDFMetaSchema> metaSchema;
		metaSchema = getInputSchema().getMetaschema();
		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("XMLToTuple", (Class<? extends IStreamObject<?>>) Tuple.class,
				attributeList, getInputSchema());
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone()
	{
		return new ToTupleAO(this); 
	}

	public IMetaAttribute getLocalMetaAttribute()
	{
		return null;
	}

	public boolean readMetaData()
	{
		return false;
	}

}
