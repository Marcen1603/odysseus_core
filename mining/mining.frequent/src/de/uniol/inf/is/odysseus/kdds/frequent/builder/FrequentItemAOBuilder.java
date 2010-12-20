package de.uniol.inf.is.odysseus.kdds.frequent.builder;

import de.uniol.inf.is.odysseus.kdds.frequent.logical.FrequentItemAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class FrequentItemAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -9177473830261170455L;
	private DirectParameter<String> strategyParam = new DirectParameter<String>("TYPE", REQUIREMENT.OPTIONAL);
	private DirectParameter<Double> sizeParam = new DirectParameter<Double>("SIZE", REQUIREMENT.MANDATORY);
	private ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>("ATTRIBUTES", REQUIREMENT.MANDATORY, new ResolvedSDFAttributeParameter("project attribute",
			REQUIREMENT.MANDATORY));

	public FrequentItemAOBuilder() {
		super(1, 1);
		setParameters(strategyParam, sizeParam, attributes);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		FrequentItemAO.Strategy strategy = FrequentItemAO.Strategy.Simple;
		String type = strategyParam.getValue().toUpperCase();
		double size = sizeParam.getValue().doubleValue();
		if (type.equals("SIMPLE")) {
			strategy = FrequentItemAO.Strategy.Simple;
		}
		if (type.equals("LOSSY") || type.equals("LOSSYCOUNTING")) {
			strategy = FrequentItemAO.Strategy.LossyCounting;
		}
		if (type.equals("SPACE") || type.equals("SPACESAVING")) {
			strategy = FrequentItemAO.Strategy.SpaceSaving;
		}

		FrequentItemAO frequent = new FrequentItemAO(size, strategy, attributes.getValue());
		return frequent;
	}

}
