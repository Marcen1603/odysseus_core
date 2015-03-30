package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Iterator;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.PredicateSelectivityHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;

@SuppressWarnings("rawtypes")
public class JoinTIPOEstimator extends StandardPhysicalOperatorEstimator<JoinTIPO> {

	@Override
	protected Class<? extends JoinTIPO> getOperatorClass() {
		return JoinTIPO.class;
	}

	@Override
	public double getSelectivity() {
		IPredicate joinPredicate = getOperator().getJoinPredicate();
		if( joinPredicate instanceof TruePredicate ) {
			return 1.0;
		}
		
		PredicateSelectivityHelper helper = new PredicateSelectivityHelper(joinPredicate, getHistogramMap());
		Optional<Double> optSelectivity = helper.getSelectivity();
		if (optSelectivity.isPresent()) {
			double trueProp = optSelectivity.get();

			Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
			DetailCost firstOperator = operatorIterator.next();
			DetailCost secondOperator = operatorIterator.next();

			double combinations = firstOperator.getDatarate() * (secondOperator.getDatarate() * secondOperator.getWindowSize()) + secondOperator.getDatarate() * (firstOperator.getDatarate() * firstOperator.getWindowSize());

			double outputRate = combinations * trueProp;
			double inputRate = firstOperator.getDatarate() * firstOperator.getWindowSize() * secondOperator.getDatarate() * secondOperator.getWindowSize();

			return outputRate / inputRate;
		}
		return super.getSelectivity();
	}

	@Override
	public double getDatarate() {
		Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
		DetailCost firstOperator = operatorIterator.next();
		DetailCost secondOperator = operatorIterator.next();

		double combinations = firstOperator.getDatarate() * (secondOperator.getDatarate() * secondOperator.getWindowSize()) + secondOperator.getDatarate() * (firstOperator.getDatarate() * firstOperator.getWindowSize());
		return combinations * getSelectivity();
	}
	
	@Override
	public double getWindowSize() {
		Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
		DetailCost firstOperator = operatorIterator.next();
		DetailCost secondOperator = operatorIterator.next();

		return (firstOperator.getWindowSize() * secondOperator.getWindowSize()) / (firstOperator.getWindowSize() + secondOperator.getWindowSize());
	}
	
	@Override
	public double getMemory() {
		Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
		DetailCost firstOperator = operatorIterator.next();
		DetailCost secondOperator = operatorIterator.next();
		
		return ( firstOperator.getDatarate() * firstOperator.getWindowSize() + secondOperator.getDatarate() * secondOperator.getWindowSize() ) * EstimatorHelper.sizeInBytes(getOperator().getOutputSchema());
	}
	
	@Override
	public double getCpu() {
		Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
		DetailCost firstOperator = operatorIterator.next();
		DetailCost secondOperator = operatorIterator.next();
		
		return  ( firstOperator.getDatarate() * firstOperator.getWindowSize() + secondOperator.getDatarate() * secondOperator.getWindowSize() ) * ( DEFAULT_CPU_COST * 10.0 );
	}
}
