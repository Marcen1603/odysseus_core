package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLSplit", doc = "Splits XML documents smaller parts.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Project+operator", category = {LogicalOperatorCategory.BASE })
public class XMLSplitAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 1L;
	private List<SDFAttribute> attributes;
	
	public XMLSplitAO() {
		super();
	}
	
	public XMLSplitAO(XMLSplitAO ao) {
		super(ao);
		this.attributes = ao.getAttributes();
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "expressions", optional = false, isList = true, doc = "A list XPath expressions that describe how to split the document.")
	public void setOutputSchemaWithList(List<SDFAttribute> outputSchema) {
		attributes = outputSchema;
	}
	
	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new XMLSplitAO(this);
	}
	
}
