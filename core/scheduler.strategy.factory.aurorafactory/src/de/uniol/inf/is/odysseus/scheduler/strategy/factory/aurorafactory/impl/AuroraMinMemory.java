package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

public class AuroraMinMemory extends AbstractExecListScheduling {


//	private float calculateMemoryReductionRate( op) {
//		
//		AbstractPlanOperator operator = (AbstractPlanOperator)op;
//		
//		// Berechnung durchführen.
//		// Die Speicherverbrauch-Reduktionsrate berechnet sich durch die Division
//		// des Produktes der Größe der Eingebeelemente und 1 - der Selektivität
//		// und den Kosten eines Operators.
//		
//		int tsize = operator.getInputElementSize();
//		
//		float selektivity = selectivityMonitor.getParameterValue(operator);
//		
//		float cost = costMonitor.getParameterValue(operator);
//		
//		float reductionRate = (tsize * (1 - selektivity)) / cost;
//		
//		// Ergebnis speichern
//		this.memoryRRs.put(op, reductionRate);
//		
//		// Ergebnis liefern
//		return reductionRate;
//		
//	}
	
	
	public AuroraMinMemory(IPartialPlan plan) {
		super(plan);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan operators) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void applyChangedPlan() {
		calculateExecutionList(getPlan());
	}

}
