package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;

public abstract class AbstractAggregateTIPORule extends AbstractRule{
	
	public AbstractAggregateTIPORule(String name) {
		super(name);
	}


	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
	
		if(logicalOperator instanceof AggregateAO){
			
			return true;
		}
	
		return false;
	}


	@Override
	public Class<?> getConditionClass() {
		return AggregateTIPO.class;
	}


	public void analyseOperator(ILogicalOperator logicalOperator,QueryAnalyseInformation transformationInformation){
		

		
	}
	
	@Override
	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		
	
		
	
		
	}

}
