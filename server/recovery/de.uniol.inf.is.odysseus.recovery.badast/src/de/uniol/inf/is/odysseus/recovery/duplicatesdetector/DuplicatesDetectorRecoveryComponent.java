package de.uniol.inf.is.odysseus.recovery.duplicatesdetector;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorCollector;
import de.uniol.inf.is.odysseus.recovery.duplicatesdetector.logicaloperator.DupliatesDetectorAO;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Component that inserts {@link DupliatesDetectorAO}s before each sink after recovery
 * in order to update the {@link ITrust}.
 * 
 * @author Michael Brand
 *
 */
public class DuplicatesDetectorRecoveryComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DuplicatesDetectorRecoveryComponent.class);

	/**
	 * The key for the trust-of-duplicates property.
	 */
	private static final String DUPLICATES_TRUST_KEY = "duplicatesTrust";

	/**
	 * The default value for the trust of duplicates. May be overridden by
	 * {@link #newInstance(Properties)} with a properties key
	 * {@link #DUPLICATES_TRUST_KEY}.
	 */
	private static final double DEFAULT_DUPLICATES_TRUST = 0.5;

	/**
	 * The trust to use for duplicates.
	 */
	private double duplicatesTrust;
	
	@Override
	public void initialize(Properties config) {
		this.duplicatesTrust = (double) config.getOrDefault(DUPLICATES_TRUST_KEY, DEFAULT_DUPLICATES_TRUST);
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting duplicates detectors...");
		for (ILogicalQuery query : queries) {
			insertDuplicatesDetectors(query);
		}
		LOG.info("... done");
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		// Nothing to do.
		return queries;
	}

	/**
	 * Inserts a {@link DupliatesDetectorAO} before each sink.
	 */
	private void insertDuplicatesDetectors(ILogicalQuery query) {
		List<ILogicalOperator> operators = OperatorCollector.collect(query.getLogicalPlan());
		new LogicalGraphWalker(operators).walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {
				if (isSinkOperator(operator)) {
					DupliatesDetectorAO updater = new DupliatesDetectorAO(DuplicatesDetectorRecoveryComponent.this.duplicatesTrust);
					updater.addOwner(query);
					Collection<LogicalSubscription> subs = Lists.newArrayList(operator.getSubscribedToSource());
					operator.unsubscribeFromAllSources();
					SDFSchema schema = subs.iterator().next().getSchema();
					for (LogicalSubscription sub : subs) {
						updater.subscribeToSource(sub.getSource(), sub.getSinkInPort(), sub.getSourceOutPort(),
								sub.getSchema());
					}
					updater.subscribeSink(operator, 0, 0, schema);
				}
			}
		});
	}

	/**
	 * In this context a sink operator is either a typical sink operator (see
	 * {@link ILogicalOperator#isSourceOperator()} and
	 * {@link ILogicalOperator#isSinkOperator()}) or an operator without
	 * outgoing subscriptions.
	 */
	private static boolean isSinkOperator(ILogicalOperator operator) {
		if (operator instanceof TopAO) {
			return false;
		} else if (operator.isSinkOperator() && !operator.isSourceOperator()) {
			return true;
		} else if (operator.getSubscriptions().isEmpty()) {
			return true;
		}
		for (LogicalSubscription sub : operator.getSubscriptions()) {
			if (!(sub.getSink() instanceof TopAO)) {
				return false;
			}
		}
		return true;
	}

}