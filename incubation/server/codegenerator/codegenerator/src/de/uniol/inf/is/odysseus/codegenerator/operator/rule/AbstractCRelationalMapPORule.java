package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCRelationalMapPORule<T extends MapAO> extends AbstractCOperatorRule<MapAO> {

	public AbstractCRelationalMapPORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(MapAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
			if (logicalOperator.getInputSchema().getType() == Tuple.class) {
				return true;
			}else{
				return false;
			}
	
		
	}


	@Override
	public void analyseOperator(MapAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		//read all expression from the logicalOperator
		for (NamedExpression e : logicalOperator.getExpressions()) {
			IExpression<?> temp = e.expression.getMEPExpression();
			
			//add the detected expression
			transformationInformation.addMEPFunction(getAllMEPFunctions(temp));
		}
	}
}
