package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.transformation.utils.Utils;

public abstract class AbstractCSelectAORule extends AbstractRule{

	public AbstractCSelectAORule(String name, String targetPlatform) {
		super(name,targetPlatform );
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if(logicalOperator instanceof SelectAO){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Class<?> getConditionClass() {
		return SelectAO.class;
	}


	public void analyseOperator(
			ILogicalOperator logicalOperator,
		QueryAnalyseInformation transformationInformation) {
		
		SelectAO selectAO = (SelectAO) logicalOperator;
		IPredicate<?> predicate = selectAO.getPredicate();
		
	
		String predicateValue = predicate.toString();
		IExpression<?> mepExpression  = MEP.getInstance().parse(predicateValue);

		
		Map<String,IExpression<?>> mepFunctions = Utils.getAllMEPFunctions(mepExpression);

		
		transformationInformation.addMEPFunction(mepFunctions);
	}
	


	
	   
}
