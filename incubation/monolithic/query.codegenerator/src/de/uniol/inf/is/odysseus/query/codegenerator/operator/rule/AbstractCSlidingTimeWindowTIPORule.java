package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCSlidingTimeWindowTIPORule<T extends AbstractWindowAO> extends AbstractRule<AbstractWindowAO> {

	public AbstractCSlidingTimeWindowTIPORule(String name) {
		super(name);
	}


	@Override
	public boolean isExecutable(AbstractWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof AbstractWindowAO) {

			AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
			switch (operator.getWindowType()) {
			case TIME:
				if (operator.getWindowSlide() == null
						&& operator.getWindowAdvance() == null) {
					return true;
				}
				return false;
			default:
				return false;
			}

		}
		return false;
	}

	//TODO fix it
	public void analyseOperator(AbstractWindowAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		/*
		AccessAO accessAO = (AccessAO) logicalOperator;

		transformationInformation.addDataHandler(accessAO.getDataHandler());
		transformationInformation.addProtocolHandler(accessAO
				.getProtocolHandler());
		transformationInformation.addTransportHandler(accessAO
				.getTransportHandler());
*/
	}

}
