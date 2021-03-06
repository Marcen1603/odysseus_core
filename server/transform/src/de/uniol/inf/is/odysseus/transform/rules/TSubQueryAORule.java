package de.uniol.inf.is.odysseus.transform.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SubQueryAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.OutputConnectorPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SubQueryPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSubQueryAORule extends AbstractTransformationRule<SubQueryAO> {

	@Override
	public int getPriority() {
		// Must be more than access ao rule
		return super.getPriority() + 10;
	}

	@Override
	public void execute(SubQueryAO operator, TransformationConfiguration config) throws RuleException {
		IServerExecutor executor = config.getOption(IServerExecutor.class.getName());
		if (executor == null) {
			throw new TransformationException(
					"Cannot create SubQueryPO. Executor not set in Transformation Configuration!");
		}
		
		IPhysicalQuery pquery;
		
		if (operator.getQueryID() != null) {
			pquery = executor.getPhysicalQueryByString(operator.getQueryID()+ "", getCaller());
		}else if (operator.getQueryName() != null) {
			pquery = executor.getPhysicalQueryByString(operator.getQueryName(), getCaller());
		}else {
			pquery = compileQuery(operator, config, executor);
		}

		if (pquery == null) {
			throw new TransformationException("Could not create query for SubqueryAO!");
		}

		List<IPhysicalOperator> roots = pquery.getRoots();
		for (IPhysicalOperator root : roots) {
			if (!root.isSource()) {
				executor.removeQuery(pquery.getID(), getCaller());
				throw new TransformationException("SubqueryPO cannot have sinks at top!");
			}
		}

		SubQueryPO<IStreamObject<?>> po;
		try {
			po = new SubQueryPO<>(pquery, executor, getCaller());
		} catch (Exception e) {
			executor.removeQuery(pquery.getID(), getCaller());
			throw new TransformationException(e);
		}

		if (operator.getAttributes() == null) {

			// Output schema of subquery can be calculated from participating roots
			for (IPhysicalOperator root : roots) {
				if (root instanceof OutputConnectorPO<?>) {
					OutputConnectorPO<?> outConn = (OutputConnectorPO<?>) root;
					operator.setOutputSchema(outConn.getPort(), outConn.getOutputSchema());
				}
			}
			LogicalPlan.recalcOutputSchemas(operator, false);

		}

		defaultExecute(operator, po, config, true, true);

	}

	private IPhysicalQuery compileQuery(SubQueryAO operator, TransformationConfiguration config,
			IServerExecutor executor) {
		IPhysicalQuery pquery;
		final String queryText = getQueryText(operator);
		Context context = config.getContext().copy();
		OptionMap options = operator.getOptionsMap();
		for (String key: options.getOptions().keySet()) {
			context.putOrReplace(key, options.getValue(key));
		}
	
		Collection<Integer> q = executor.addQuery(queryText, operator.getQueryParser(), getCaller(),
				context);
		if (q.size() != 1) {
			for (Integer queryId : q) {
				executor.removeQuery(queryId, getCaller());
			}
			throw new TransformationException("SubQueryPO can only handle a single query!");
		}

		pquery = executor.getPhysicalQueryByString(q.iterator().next() + "", getCaller());
		pquery.getLogicalQuery().setName(new Resource(getCaller().getUser().getName(),operator.getName()));
		return pquery;
	}

	private String getQueryText(SubQueryAO operator) {
		final String queryText;
		if (operator.getQueryFileName() != null) {
			StringBuilder querTextBuilder = new StringBuilder();
			if (operator.getQueryFileName().contains("://")) {
				URL url;
				try {
					url = new URL(operator.getQueryFileName());
				} catch (MalformedURLException e1) {
					throw new TransformationException("Error reading from " + operator.getQueryFileName(), e1);
				}
				try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));) {
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						querTextBuilder.append(inputLine).append(System.lineSeparator());
					}
				} catch (IOException e) {
					throw new TransformationException("Error reading from " + operator.getQueryFileName(), e);
				}
				queryText = querTextBuilder.toString();
			} else {
				File file = new File(operator.getQueryFileName());
				try (BufferedReader in = new BufferedReader(new FileReader(file));) {
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						querTextBuilder.append(inputLine).append(System.lineSeparator());
					}
				} catch (IOException e) {
					throw new TransformationException("Error reading from " + operator.getQueryFileName(), e);
				}
				queryText = querTextBuilder.toString();
			}
		} else {
			queryText = operator.getQueryText();
		}
		return queryText;
	}

	@Override
	public boolean isExecutable(SubQueryAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
