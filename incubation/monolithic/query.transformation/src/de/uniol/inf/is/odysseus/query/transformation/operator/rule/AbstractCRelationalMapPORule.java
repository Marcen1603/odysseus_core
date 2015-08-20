package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;

public abstract class AbstractCRelationalMapPORule extends AbstractRule{
	
	
	public AbstractCRelationalMapPORule(String name, String targetPlatform) {
		super(name,targetPlatform );
	}
	
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if(logicalOperator instanceof MapAO){
			if (((MapAO)logicalOperator).getInputSchema().getType() == Tuple.class) {
				return true;
			}
		}
		return false;
	}

	
	@Override
	public Class<?> getConditionClass() {
		return MapAO.class;
	}
	
	@Override
	public void analyseOperator(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		
		
		MapAO mapAO = (MapAO) logicalOperator;
		
		for (NamedExpression e : mapAO.getExpressions()) {
			transformationInformation.addMEPFunction(e.expression.getMEPExpression());
		}
		
		
	
		
	}
}
