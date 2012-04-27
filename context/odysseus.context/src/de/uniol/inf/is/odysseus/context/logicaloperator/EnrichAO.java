package de.uniol.inf.is.odysseus.context.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "ENRICH", minInputPorts = 1, maxInputPorts = 1)
public class EnrichAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -6701002329614782111L;
	private String storeName;

	public EnrichAO(EnrichAO op) {
		super(op);
		this.storeName = op.storeName;
	}

	public EnrichAO() {
		super();
	}

	@Parameter(name = "storeName", type = StringParameter.class)
	public void setStoreName(String storeName) {
		this.storeName = storeName;
		calcOutputSchema();
	}

	private void calcOutputSchema() {
		if (getInputSchema(0) != null) {
			// Attributes of the source
			List<SDFAttribute> outattribs = new ArrayList<SDFAttribute>(getInputSchema(0).getAttributes());
			// Attributes of the context store
			SDFSchema s = ContextStoreManager.getStore(storeName).getSchema();
			outattribs.addAll(s.getAttributes());
			setOutputSchema(new SDFSchema(getInputSchema(0).getURI() + "_ENRICH", outattribs));
		}

	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		if (getOutputSchema() == null) {
			calcOutputSchema();
		}
		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new EnrichAO(this);
	}

	public String getStoreName() {
		return this.storeName;
	}

}
