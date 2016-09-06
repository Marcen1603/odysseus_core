package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;

/**
 * abstract rule for the AggregateAO
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractAggregateTIPORule<T extends AggregateAO> extends AbstractCIntervalRule<T> {

	public AbstractAggregateTIPORule(String name) {
		super(name);
	}

	
	@Override
	public void analyseOperator(AggregateAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		resovleAggregateFunctionBuilder(logicalOperator, transformationInformation);
	}
	
	/**
	 * extract all AggreateFunction from a AggreateAO operator
	 * 
	 * @param logicalOperator
	 * @param transformationInformation
	 */
	private void resovleAggregateFunctionBuilder(AggregateAO logicalOperator,QueryAnalyseInformation transformationInformation){
		SDFSchema inputSchema = logicalOperator.getInputSchema();
        final Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = logicalOperator.getAggregations();

        for (final SDFSchema attrList : aggregations.keySet()) {
            if (SDFSchema.subset(attrList, inputSchema)) {
                final Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(attrList);
                for (final Entry<AggregateFunction, SDFAttribute> e : funcs.entrySet()) {
                    final FESortedClonablePair<SDFSchema, AggregateFunction> p = new FESortedClonablePair<SDFSchema, AggregateFunction>(attrList, e.getKey());
              
                
                     IAggregateFunctionBuilder builder = AggregateFunctionBuilderRegistry.getBuilder(inputSchema.getType(), p.getE2().getName());

                     transformationInformation.addAggregateFunctionBuilder(builder);
                     
                }
            }
        }
		
		
	}
	
}
