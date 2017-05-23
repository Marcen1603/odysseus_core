package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SAPROJECT", doc = "-", category = {
		LogicalOperatorCategory.BASE })
public class SAProjectAO extends ProjectAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3826972870633591949L;
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	

	
	
	public SAProjectAO() {
		super();
		LOG.info("SAProjectAO aufgerufen");
	}

	public SAProjectAO(SAProjectAO saProjectAO) {
		super(saProjectAO);
		LOG.info("SAProjectAO aufgerufen");
	}
	
	public SAProjectAO(ProjectAO projectAO) {
		super(projectAO);
		
	}

	@Override
	public SAProjectAO clone() {
	LOG.info("SAProjectAO aufgerufen");
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
		for(String str:restrictedAttributes){
			LOG.info(str);
		}
		return restrictedAttributes;

		/*
		 * List<String> outputAttributes = new ArrayList<>(); SDFSchema output =
		 * this.getOutputSchema(); for (SDFAttribute out : output) {
		 * outputAttributes.add(out.getAttributeName()); } return
		 * outputAttributes;
		 */

	}

}
