package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;

public class CostModelService {
	private static final Logger LOG = LoggerFactory.getLogger(CostModelService.class);
	private static ICostModel<IPhysicalOperator> costModel;
	

	public void bindCostModel(ICostModel<IPhysicalOperator> cm ) {
		costModel = cm;
		LOG.debug("Bound CostModel: " + cm);
	}
	
	public void unbindCostModel( ICostModel<IPhysicalOperator> cm ) {
		if(cm == costModel) {
			costModel = null;
			LOG.debug("Unbound CostModel: " + cm);
		}
	}
	
	public static ICostModel<IPhysicalOperator> getCostModel() {
		return costModel;
	}
	
	public static boolean isBound() {
		return costModel != null;
	}
	
}