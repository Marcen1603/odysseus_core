package de.uniol.inf.is.odysseus.rcp.profile.views;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;

public final class OperatorStatistic {

	private final String name;
	private final String type;
	
	private final double cpuCost;
	private final double selectivity;
	private final double dataRate;
	
	private final int hashID;
	
	private final long elementsStoredCount1;
	private final long elementsStoredCount2;

	private final IPhysicalOperator operator;
	
	public OperatorStatistic(IPhysicalOperator operator, OperatorCost<IPhysicalOperator> cost, OperatorEstimation<IPhysicalOperator> est) {
		Preconditions.checkNotNull(operator, "Operator for statistics must not be null!");
		
		this.name = operator.getName();
		this.type = operator.getClass().getSimpleName();
		
		this.cpuCost = cost.getCpuCost();
		
		this.selectivity = est.getSelectivity();
		this.dataRate = est.getDataStream().getDataRate();
		
		this.hashID = operator.hashCode();
		
		if (operator instanceof AbstractSource){
			this.elementsStoredCount1 = ((AbstractSource<?>) operator).getElementsStored1();
			this.elementsStoredCount2 = ((AbstractSource<?>) operator).getElementsStored2();			
		} else {
			this.elementsStoredCount1 = -1;
			this.elementsStoredCount2 = -1;
		}
		
		this.operator = operator;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public double getCpuCost() {
		return cpuCost;
	}

	public double getSelectivity() {
		return selectivity;
	}
	
	public double getDataRate() {
		return dataRate;
	}
	
	public int getHashID() {
		return hashID;
	}
	
	public long getElementsStoredCount1() {
		return elementsStoredCount1;
	}
	
	public boolean hasElementsStoredCount1() {
		return elementsStoredCount1 >= 0;
	}

	public long getElementsStoredCount2() {
		return elementsStoredCount2;
	}
	
	public boolean hasElementsStoredCount2() {
		return elementsStoredCount2 >= 0;
	}

	public IPhysicalOperator getOperator() {
		return operator;
	}
}
