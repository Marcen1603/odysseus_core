package de.uniol.inf.is.odysseus.context.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
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
	List<String> contextStoreNames = new ArrayList<String>();
	private SDFSchema outputSchema = null;

	public EnrichAO(EnrichAO op) {
		super(op);
		contextStoreNames = new ArrayList<String>(op.contextStoreNames);		
	}

	public EnrichAO() {
		super();
	}

	@Parameter(name = "storeNames", type = StringParameter.class, isList = true)
	public void setContextSources(List<String> sources) {
		this.contextStoreNames = new ArrayList<String>(sources);
		calcOutputSchema();
	}

	private void calcOutputSchema() {
		if (getInputSchema(0) != null) {
			// Attributes of the source
			List<SDFAttribute> outattribs = new ArrayList<SDFAttribute>(
					getInputSchema(0).getAttributes());
			// Attributes of the context store
			for (String store : contextStoreNames) {
				// TODO: Wie trennt man den Namen des Kontext-Stores von einem
				// ggf. vorhanden Attributnamen im Store?
				// erstmal mit "."
				int pointPos = store.indexOf(".");
				String storeName = store;
				String attributeName = null;
				if (pointPos > 0) {
					storeName = store.substring(0, pointPos);
					attributeName = store.substring(pointPos + 1);
				}
				try {
					SDFSchema s = ContextStoreManager.getInstance().getStoreSchema(
							storeName);
					if (attributeName != null){
						SDFAttribute attribute = s.findAttribute(attributeName);
						if (attribute != null){
							outattribs.add(attribute);
						}
					}

				} catch (ContextManagementException e) {
					e.printStackTrace();
					// TODO: Error Handling if store not available
				}
			}
			outputSchema = new SDFSchema(
					getInputSchema(0).getURI() + "_ENRICH", outattribs);
		}

	}

	@Override
	public SDFSchema getOutputSchema() {
		if (outputSchema == null) {
			calcOutputSchema();
		}
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new EnrichAO(this);
	}

}
