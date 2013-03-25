package de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.standardfuzzyruleengine;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionMigrationFuzzyRuleEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.planmigration.PlanMigrationCost;

public class StandardPlanAdaptionMigrationFuzzyRuleEngine implements
		IPlanAdaptionMigrationFuzzyRuleEngine {

	private static final Logger LOG = LoggerFactory.getLogger(StandardPlanAdaptionMigrationFuzzyRuleEngine.class);
	
	// TODO: werte finden/raten/schätzen
	@SuppressWarnings({ "rawtypes", "unchecked" })
	ICost<ILogicalOperator> minCostDifference = new OperatorCost(new HashMap<ILogicalOperator, OperatorEstimation>(), 0.0,0.0);
	ICost<PlanMigration> maxMigrationCost = new PlanMigrationCost(null, null, 1000.0, 1000.0, 10001);
	
	@Override
	public boolean evaluate(ICost<ILogicalOperator> costDifference,
			ICost<PlanMigration> migrationCosts) {
		if(this.minCostDifference == null || this.maxMigrationCost == null) {
			LOG.debug("Comparative costs are null");
			return false;
		}
//		int compared = costDifference.compareTo(this.minCostDifference);
//		compared += this.maxMigrationCost.compareTo(migrationCosts);
//		return compared > 0;
		// FIXME: das ist nur zu testzwecken
		return true;
	}

}
