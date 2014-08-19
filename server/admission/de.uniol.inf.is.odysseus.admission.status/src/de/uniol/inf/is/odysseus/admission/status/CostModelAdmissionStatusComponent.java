package de.uniol.inf.is.odysseus.admission.status;

import java.util.Collection;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class CostModelAdmissionStatusComponent implements IAdmissionStatusComponent {

	public double getEstimatedCpuCostLogical( Collection<ILogicalOperator> operators ) {
		return AdmissionStatusPlugIn.getLogicalCostModel().estimateCost(operators).getCpuSum();
	}
	
	public double getEstimatedMemCostLogical( Collection<ILogicalOperator> operators ) {
		return AdmissionStatusPlugIn.getLogicalCostModel().estimateCost(operators).getMemorySum();
	}
	
	public double getEstimatedNetCostLogical( Collection<ILogicalOperator> operators ) {
		return AdmissionStatusPlugIn.getLogicalCostModel().estimateCost(operators).getNetworkSum();
	}
	
	public double getEstimatedCpuCostPhysical( Collection<IPhysicalOperator> operators ) {
		return AdmissionStatusPlugIn.getPhysicalCostModel().estimateCost(operators).getCpuSum();
	}
	
	public double getEstimatedMemCostPhysical( Collection<IPhysicalOperator> operators ) {
		return AdmissionStatusPlugIn.getPhysicalCostModel().estimateCost(operators).getMemorySum();
	}
	
	public double getEstimatedNetCostPhysical( Collection<IPhysicalOperator> operators ) {
		return AdmissionStatusPlugIn.getPhysicalCostModel().estimateCost(operators).getNetworkSum();
	}
}
