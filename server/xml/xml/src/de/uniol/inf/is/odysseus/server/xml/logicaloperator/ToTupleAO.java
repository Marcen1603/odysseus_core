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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateAndRenameSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLToTuple", doc = "Translates XML objects to a tuple", category = {LogicalOperatorCategory.TRANSFORM})
public class ToTupleAO extends UnaryLogicalOp
{
	private static final long serialVersionUID = -5737495514565877599L;

	Logger LOG = LoggerFactory.getLogger(ToTupleAO.class);

	private List<RenameAttribute> attributes;
	private List<String> expressions = new ArrayList<>();

	public ToTupleAO()
	{
	}

	public ToTupleAO(ToTupleAO ao)
	{
		super(ao);
		this.attributes = ao.attributes;
		this.expressions = new ArrayList<>(ao.expressions);
	}

	@Parameter(name = "Schema", type = CreateAndRenameSDFAttributeParameter.class, optional = false, isList = true)
	public void setAttributes(List<RenameAttribute> attributes)
	{
		this.attributes = attributes;
	}

	@Parameter(type = StringParameter.class, name = "expressions", optional = false, isList = true, doc = "A list of XPAthExpressions.")
	public void setExpressions(List<String> expList)
	{
		expressions = expList;
	}

	public List<String> getExpressions()
	{
		return expressions;
	}

	public List<RenameAttribute> getAttributes()
	{
		return this.attributes;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos)
	{
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		for (RenameAttribute att : attributes)
		{
			SDFAttribute sdfAtt = att.getAttribute();
			String name;
			if (!att.getNewName().equals(""))
			{
				name = att.getNewName();
			} else
			{
				name = att.getAttribute().getQualName();
			}
			name = SDFAttribute.replaceSpecialChars(name);
			attributeList.add(new SDFAttribute(sdfAtt.getSourceName(), name, sdfAtt.getDatatype(), sdfAtt.getUnit(), sdfAtt.getDtConstraints()));
		}
		final List<SDFMetaSchema> metaSchema;
		metaSchema = getInputSchema().getMetaschema();
		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, getInputSchema());
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
		setOutputSchema(outputSchema);
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
