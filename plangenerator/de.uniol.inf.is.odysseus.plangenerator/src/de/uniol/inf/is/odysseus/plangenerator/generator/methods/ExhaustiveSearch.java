/**
 * 
 */
package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.plangenerator.util.PlanGeneratorHelper;

/**
 * This plan generation method performes an exhaustive search over all possible
 * semantiv equivalent plans of the given plan.
 * 
 * @author Merlin Wasmann
 * 
 */
public class ExhaustiveSearch extends AbstractPlanGenerationMethod {

	private static final Logger LOG = LoggerFactory
			.getLogger(ExhaustiveSearch.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.odysseus.plangenerator.generator.methods.IPlanGenerationMethod
	 * #
	 * generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator
	 * , de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.
	 * configuration.PlanGenerationConfiguration)
	 */
	@Override
	public List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner) {
		List<ILogicalOperator> plans = new ArrayList<ILogicalOperator>();

		initialize(plan);

		// walk through the different join plans.
		this.nWayJoinList = new ArrayList<Collection<ILogicalOperator>>();
		for (int i = 2; i <= getMaxJoinCount(); i++) {
			LOG.debug("Plans for i = " + i);
			createJoinPlans(i, this.nWayJoinList);
		}

		// the last collection in this list contains the max-way join plans.
		Collection<ILogicalOperator> joinPlans = this.nWayJoinList
				.get(this.nWayJoinList.size() - 1);

		LOG.debug("Print resulting Join plans.");
		for (ILogicalOperator joinPlan : joinPlans) {
			ILogicalOperator filledPlan = fillPlanWithRemainingOperators(joinPlan);
			if (PlanGeneratorHelper.hasValidWindowPositions(filledPlan)) {
				plans.add(filledPlan);
			} else {
				LOG.error("Plan: " + filledPlan.getClass().getSimpleName() + " (" + filledPlan.hashCode() + ") was not valid");
			}
			// plans.add(joinPlan);
		}

		// set the owners
		for (ILogicalOperator p : plans) {
			PlanGeneratorHelper.setNewOwnerForPlan(p, owner);
			PlanGeneratorHelper.printPlan("With owner", p);
		}

		// if(!getPredicateHelper().allPredicatesSatisfied()) {
		// System.err.println("[ExhaustiveSearch] Not all predicates have been satisfied.");
		// }

		return plans;
	}

}
