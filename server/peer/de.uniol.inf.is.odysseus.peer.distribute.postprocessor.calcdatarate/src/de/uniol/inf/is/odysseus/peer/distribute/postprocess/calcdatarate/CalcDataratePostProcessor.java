package de.uniol.inf.is.odysseus.peer.distribute.postprocess.calcdatarate;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.benchmark.logicaloperator.DatarateCalcAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.postprocess.AbstractOperatorInsertionPostProcessor;

/**
 * The {@link CalcDataratePostProcessor} inserts a {@link DatarateCalcAO} for each sink within a query.
 * @author Michael Brand
 */
public class CalcDataratePostProcessor extends AbstractOperatorInsertionPostProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(CalcDataratePostProcessor.class);
	
	@Override
	public String getName() {
		
		return "calcdatarate";
		
	}
	
	@Override
	protected Collection<ILogicalOperator> insertOperator(ILogicalOperator relativeSink) {
		
		CalcDataratePostProcessor.log.error("Can not insert a DatarateCalcAO before a real sink!");
		
		Collection<ILogicalOperator> operators = Lists.newArrayList(relativeSink);
		return operators;
		
	}
	
	@Override
	protected ILogicalOperator createOperator() {
		
		return new DatarateCalcAO();
		
	}

}