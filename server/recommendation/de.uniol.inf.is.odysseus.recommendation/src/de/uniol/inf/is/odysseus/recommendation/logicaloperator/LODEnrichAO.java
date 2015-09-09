package de.uniol.inf.is.odysseus.recommendation.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractEnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Christopher Schwarz
 */
//@LogicalOperator(name = "LOD_ENRICH", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.ENRICH }, doc = "Enriches a data stream with linked open data.")
public class LODEnrichAO extends AbstractEnrichAO {
	private static final long serialVersionUID = -3556250359299636522L;

	/**
	 * Default constructor.
	 */
	public LODEnrichAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public LODEnrichAO(LODEnrichAO logicalOperator) {
		super(logicalOperator);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public LODEnrichAO clone() {
		return new LODEnrichAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
		return getOutputSchema();
	}
}
