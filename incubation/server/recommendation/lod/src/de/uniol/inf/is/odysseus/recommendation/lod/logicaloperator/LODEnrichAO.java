package de.uniol.inf.is.odysseus.recommendation.lod.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Christopher Schwarz
 */
@LogicalOperator(name = "LOD_ENRICH", minInputPorts = 1, maxInputPorts = 1, category = {LogicalOperatorCategory.ENRICH}, doc = "Enriches a stream with linked open data.")
public class LODEnrichAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -5480581520336543686L;

	/**
	 * Default constructor.
	 */
	public LODEnrichAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public LODEnrichAO(LODEnrichAO operator) {
		super(operator);
	}
	
	@Override
	public LODEnrichAO clone() {
		return new LODEnrichAO(this);
	}
}
