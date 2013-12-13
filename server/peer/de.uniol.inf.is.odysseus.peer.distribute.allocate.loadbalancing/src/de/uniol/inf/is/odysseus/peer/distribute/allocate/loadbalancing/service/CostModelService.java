package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;

public class CostModelService {

	private static final Logger LOG = LoggerFactory.getLogger(CostModelService.class);

	private static OperatorCostModel<?> costModel;

	public void bindCostModel(ICostModel<?> model) {
		if (model instanceof OperatorCostModel) {
			costModel = (OperatorCostModel<?>) model;
			LOG.debug("CostModel bound {}", costModel);
		}
	}

	public void unbindCostModel(ICostModel<?> model) {
		if (costModel == model) {
			costModel = null;
			LOG.debug("CostModel unbound {}", costModel);
		}
	}

	@SuppressWarnings("rawtypes")
	public static OperatorCostModel get() {
		return costModel;
	}
}
