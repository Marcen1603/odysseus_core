package de.uniol.inf.is.odysseus.recommendation.logicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name="UNNEST_RECOMMENDATION_CANDIDATES", maxInputPorts=1, minInputPorts=1, doc="UnnestRecommendationCandidates unnested the incoming RecommendationCandidates and send on the last item a TuplePunctuation.", category = { LogicalOperatorCategory.MINING })
public class UnnestRecommendationCandidatesAO extends UnaryLogicalOp{

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(UnnestRecommendationCandidatesAO.class);
	private static final long serialVersionUID = 1L;
	private SDFAttribute attribute;

	public UnnestRecommendationCandidatesAO(){
		super();
	}

	public UnnestRecommendationCandidatesAO(UnnestRecommendationCandidatesAO op){
		super(op);
		this.attribute = op.getAttribute();
	}

	@Parameter(name = "ATTRIBUTE", type = ResolvedSDFAttributeParameter.class)
	public void setAttribute(final SDFAttribute attribute) {
		this.attribute = attribute;
	}

	public SDFAttribute getAttribute() {
		return attribute;
	}

	public int getAttributePosition(){
		return this.getInputSchema().indexOf(this.attribute);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new UnnestRecommendationCandidatesAO(this);
	}
}
