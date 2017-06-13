package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.util.ArrayList;
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

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "Construct", doc = "Constructs a new DOM filled with resolved XPath-Expressions", category = {
		LogicalOperatorCategory.TRANSFORM })
public class ConstructAO extends UnaryLogicalOp
{
	private static final long serialVersionUID = -4970136188613426865L;
	Logger LOG = LoggerFactory.getLogger(ConstructAO.class);
	private List<String> newExpressions;
	private List<String> expressions;

	public ConstructAO() {
	}

	public ConstructAO(ConstructAO constructAO) {
		super(constructAO);
		this.newExpressions = new ArrayList<>(constructAO.newExpressions);
		this.expressions = new ArrayList<>(constructAO.expressions);
	}
	
	public @Override ConstructAO clone()
	{
		return new ConstructAO(this);
	}
	
	@Parameter(type = StringParameter.class, name = "expressions", optional = false, isList = true, doc = "A list of XPAthExpressions.")
	public void setExpressions(List<String> outputSchema)
	{
		expressions = outputSchema;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "newExpressions", optional = false, isList = true, doc = "A list of XPAthExpressions.")
	public void setNewExpressions(List<String> outputSchema)
	{
		newExpressions = outputSchema;
	}

	public List<String> getExpressions()
	{
		return expressions;
	}

	public List<String> getNewExpressions()
	{
		return newExpressions;
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
