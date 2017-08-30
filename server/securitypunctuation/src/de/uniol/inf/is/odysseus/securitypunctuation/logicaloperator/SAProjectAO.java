package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SAPROJECT", doc = "Project Operator that processes Security Punctuations", category = {
		LogicalOperatorCategory.BASE })
public class SAProjectAO extends ProjectAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3826972870633591949L;

	
	public SAProjectAO() {
		super();
	
	}

	public SAProjectAO(SAProjectAO saProjectAO) {
		super(saProjectAO);
		
	}
	
	public SAProjectAO(ProjectAO projectAO) {
		super(projectAO);
		
	}

	@Override
	public SAProjectAO clone() {
		return new SAProjectAO(this);
		
	}

	public List<String> getRestrictedAttributes() {

		List<String> restrictedAttributes = new ArrayList<>();
		SDFSchema output = this.getOutputSchema();
		for (SDFAttribute in : this.getInputSchema()) {
			if (output.findAttribute(in.getURI()) == null) {
				restrictedAttributes.add(in.getAttributeName());
			}
		}
	
		return restrictedAttributes;


	}

}
