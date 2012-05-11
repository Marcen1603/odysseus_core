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
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.IAttributeResolver;

@LogicalOperator(name = "ENRICH", minInputPorts = 1, maxInputPorts = 1)
public class EnrichAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -6701002329614782111L;
	private String storeName;
	private List<String> attributes;
	private boolean outer = false;

	public EnrichAO(EnrichAO op) {
		super(op);
		this.storeName = op.storeName;
		this.attributes = op.attributes;
		this.outer = op.outer;
	}

	public EnrichAO() {
		super();
	}

	@Parameter(type = StringParameter.class, name = "ATTRIBUTES", isList = true, optional = true)
	public void setAttributes(List<String> readingSchema) {
		this.attributes = readingSchema;
	}

	@Parameter(name = "STORE", type = StringParameter.class)
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	@Parameter(name = "OUTER", type = StringParameter.class, optional = true)
	public void setOuterEnrich(String outer) {
		this.outer = Boolean.parseBoolean(outer);
	}

	private void calcOutputSchema() {
		if (getInputSchema(0) != null) {
			// Attributes of the source
			List<SDFAttribute> outattribs = new ArrayList<SDFAttribute>(getInputSchema(0).getAttributes());
			// Attributes of the context store
			for (String attributeName : this.attributes) {
				// SDFSchema s =
				// ContextStoreManager.getStore(storeName).getSchema();
				SDFSchema storeSchema = ContextStoreManager.getStore(storeName).getSchema();
				IAttributeResolver ar = new DirectAttributeResolver(storeSchema);
				SDFAttribute attribute = ar.getAttribute(attributeName);
				if (attribute == null) {
					throw new IllegalArgumentException("Attribute \"" + attributeName + "\" does not exist in store \"" + storeName + "\"");
				}
				outattribs.add(attribute);
			}
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

	@Override
	public void initialize() {
		super.initialize();
		if (this.attributes == null) {
			this.attributes = new ArrayList<String>();
			for (SDFAttribute a : ContextStoreManager.getStore(storeName).getSchema()) {
				this.attributes.add(a.getAttributeName());
			}
		}
		calcOutputSchema();
	}

	public List<String> getAttributes() {
		return this.attributes;
	}

	public boolean isOuter() {
		return this.outer;
	}

}
