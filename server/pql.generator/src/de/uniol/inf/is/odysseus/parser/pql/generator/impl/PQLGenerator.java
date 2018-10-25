package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGeneratorPostProcessor;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGeneratorPreProcessor;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLStatementGenerator;

public class PQLGenerator implements IPQLGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(PQLGenerator.class);

	private static final String NAME_SEPARATOR = "_";
	private static int operatorCounter = 0;

	private final List<IPQLGeneratorPostProcessor> postProcessors = Lists.newArrayList();
	private final List<IPQLGeneratorPreProcessor> preProcessors = Lists.newArrayList();

	// called by OSGi-DS
	public void bindPostProcessor(IPQLGeneratorPostProcessor processor) {
		postProcessors.add(processor);

		LOG.debug("PQLGenerator post processor bound {}", processor);
	}

	// called by OSGi-DS
	public void unbindPostProcessor(IPQLGeneratorPostProcessor processor) {
		if (postProcessors.contains(processor)) {
			postProcessors.remove(processor);
			LOG.debug("PQLGenerator post processor unbound {}", processor);
		}
	}

	// called by OSGi-DS
	public void bindPreProcessor(IPQLGeneratorPreProcessor processor) {
		preProcessors.add(processor);

		LOG.debug("PQLGenerator pre processor bound {}", processor);
	}

	// called by OSGi-DS
	public void unbindPreProcessor(IPQLGeneratorPreProcessor processor) {
		if (preProcessors.contains(processor)) {
			preProcessors.remove(processor);
			LOG.debug("PQLGenerator pre processor unbound {}", processor);
		}
	}

	@Override
	public String generatePQLStatement(ILogicalPlan plan) {
		return generatePQLStatement(plan.getRoot());
	}
	
	@Override
	public String generatePQLStatement(ILogicalOperator startOperator) {
		Objects.requireNonNull(startOperator, "Operator for generating pql-statement must not be null!");

		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperators(startOperator, operators);

		ParameterInfoHelper.prepareParameterInfos(operators);
		preprocess(startOperator);

		Map<ILogicalOperator, String> names = generateOperatorNames(operators);
		List<ILogicalOperator> sourceOperators = determineSourceOperators(operators);

		List<String> pqlStatements = generateStatements(sourceOperators, names);
		String statement = concatStatements(pqlStatements);
		return postprocess(statement);
	}

	private String postprocess(String statement) {
		for (IPQLGeneratorPostProcessor processor : postProcessors) {
			statement = processor.postProcess(statement);
		}
		return statement;
	}

	private void preprocess(ILogicalOperator startOperatorCopy) {
		for (IPQLGeneratorPreProcessor processor : preProcessors) {
			processor.preprocess(startOperatorCopy);
		}
	}

	private static List<String> generateStatements(List<ILogicalOperator> sourceOperators, Map<ILogicalOperator, String> names) {
		List<String> statements = Lists.newArrayList();

		List<ILogicalOperator> visitedOperators = Lists.newArrayList();
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList();
		operatorsToVisit.addAll(sourceOperators);

		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator operator = operatorsToVisit.remove(0);

			if (!(operator instanceof TopAO)) {
				IPQLStatementGenerator<ILogicalOperator> generator = PQLStatementGeneratorManager.getInstance().getPQLStatementGenerator(operator);
				String statement = generator.generateStatement(operator, names);
				if (statement == null) {
					LOG.error("Returned PQL-Statement for {} is null!", operator);
				} else if (!statement.isEmpty()) {
					statements.add(statement);
				}
			}

			visitedOperators.add(operator);
			for (LogicalSubscription subscription : operator.getSubscriptions()) {
				ILogicalOperator target = subscription.getSink();
				if (!visitedOperators.contains(target) && areAllSourceSubscriptionsVisited(target, visitedOperators)) {
					operatorsToVisit.add(target);
				}
			}
		}

		return statements;
	}

	private static boolean areAllSourceSubscriptionsVisited(ILogicalOperator operator, List<ILogicalOperator> visitedOperators) {
		for (LogicalSubscription subscription : operator.getSubscribedToSource()) {
			ILogicalOperator target = subscription.getSource();
			if (!visitedOperators.contains(target)) {
				return false;
			}
		}
		return true;
	}

	private static List<ILogicalOperator> determineSourceOperators(List<ILogicalOperator> operators) {
		List<ILogicalOperator> sources = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {
			if (operator.getSubscribedToSource().size() == 0) {
				sources.add(operator);
			}
		}

		return sources;
	}

	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {
			if (!(currentOperator instanceof TopAO)) {
				list.add(currentOperator);
			}

			for (LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperators(subscription.getSink(), list);
			}

			for (LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperators(subscription.getSource(), list);
			}
		}
	}

	private static Map<ILogicalOperator, String> generateOperatorNames(List<ILogicalOperator> operators) {
		Map<ILogicalOperator, String> names = Maps.newHashMap();

		for (ILogicalOperator operator : operators) {
			String opName = determineName(operator);
			String statementName = generateStatementName(names.values(), opName);

			names.put(operator, statementName);
		}

		return names;
	}

	private static String generateStatementName(Collection<String> usedNames, String opName) {
		String statementName = null;
		do {
			statementName = opName + NAME_SEPARATOR + (operatorCounter++);
		} while (usedNames.contains(statementName));
		return statementName;
	}

	private static String determineName(ILogicalOperator operator) {
		return operator.getClass().getSimpleName();
	}

	private static String concatStatements(List<String> pqlStatements) {
		StringBuilder sb = new StringBuilder();

		for (String pqlStatement : pqlStatements) {
			sb.append(pqlStatement).append("\n");
		}

		return sb.toString();
	}
}
