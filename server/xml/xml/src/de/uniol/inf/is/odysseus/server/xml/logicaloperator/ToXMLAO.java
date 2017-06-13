package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ToXML", doc = "Constructs an XML object from a tuple", category = {LogicalOperatorCategory.TRANSFORM})
public class ToXMLAO extends UnaryLogicalOp
{
	private static final long serialVersionUID = -7878339457198812181L;
	Logger LOG = LoggerFactory.getLogger(ToXMLAO.class);
	private List<String> expressions = new ArrayList<>();
	private List<SDFAttribute> attributes = new ArrayList<>();

	public ToXMLAO()
	{
		super();
	}

	public ToXMLAO(ToXMLAO ao)
	{
		super(ao);
		this.attributes = new ArrayList<>(ao.attributes);
		this.expressions = new ArrayList<>(ao.expressions);
	}

	public @Override ToXMLAO clone()
	{
		return new ToXMLAO(this);
	}

		/*
	@Override(non - Javadoc)
	protected SDFSchema getOutputSchemaIntern(int pos)
	{
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		attributeList.add(new SDFAttribute("XML Source", "XML", new SDFDatatype("STRING")));
		final List<SDFMetaSchema> metaSchema;
		metaSchema = getInputSchema().getMetaschema();

		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("ToXML", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, getInputSchema());
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
		return outputSchema;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		Collection<SDFAttribute> emptyAttributes = new ArrayList<>();
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(getInputSchema(pos).getURI(),
				(Class<? extends IStreamObject<?>>) XMLStreamObject.class, emptyAttributes, getInputSchema());
		setOutputSchema(newOutputSchema);
		return newOutputSchema;
	}
	
	public IMetaAttribute getLocalMetaAttribute()
	{
		return null;
	}

	public boolean readMetaData()
	{
		return false;
	}

	@Parameter(type = StringParameter.class, name = "expressions", optional = false, isList = true, doc = "A list of XPAthExpressions.")
	public void setExpressions(List<String> expList)
	{
		expressions = expList;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "attributes", optional = false, isList = true, doc = "A list of Tupleattributes.")
	public void setAttributes(List<SDFAttribute> outputSchema)
	{
		attributes = outputSchema;
	}

	public List<String> getExpressions()
	{
		return expressions;
	}

	public List<SDFAttribute> getAttributes()
	{
		return attributes;
	}
}
