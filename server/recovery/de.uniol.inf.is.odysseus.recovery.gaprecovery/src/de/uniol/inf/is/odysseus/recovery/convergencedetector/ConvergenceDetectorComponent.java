package de.uniol.inf.is.odysseus.recovery.convergencedetector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorCollector;
import de.uniol.inf.is.odysseus.recovery.convergencedetector.logicaloperator.ConvergenceDetectorAO;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Component that inserts {@link ConvergenceDetectorAO}s after each time or
 * element window after recovery in order to adjust the {@link ITrust}.
 *
 * @author Michael Brand
 *
 */
public class ConvergenceDetectorComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ConvergenceDetectorComponent.class);

	@Override
	public void initialize(Properties config) {
		// Nothing to do.
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting convergence detectors...");
		for (ILogicalQuery query : queries) {
			insertConvergenceDetectors(query);
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
	 * Inserts a {@link ConvergenceDetectorAO} after each window operator.
	 */
	private static void insertConvergenceDetectors(ILogicalQuery query) {
		/*
		 * First idea was to insert them only, if stateful operators are used.
		 * But not all operators, which may result in a convergence phase
		 * implement IStatefulAO/PO. Even if all stateful operators would, what
		 * about non-deterministic, stateless operators?
		 */

		/*
		 * Second idea was to check, if convergence detectors are already
		 * inserted (would be for a crash after a crash). This is not the case,
		 * because this method is called after the backup of the query, so the
		 * modification is not logged.
		 */

		List<ILogicalOperator> operators = OperatorCollector.collect(query.getLogicalPlan());
		new LogicalGraphWalker(operators).walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {
				SDFSchema schema = operator.getInputSchema(0);
				if(operator instanceof AbstractWindowAO && schema.hasMetatype(ITrust.class)) {
					if (operator instanceof TimeWindowAO) {
						TimeWindowAO tw = (TimeWindowAO) operator;
						// Advance may be null
						TimeValueItem advance = tw.getWindowAdvance();
						if (advance == null) {
							advance = new TimeValueItem(1, tw.getBaseTimeUnit());
						}
						ConvergenceDetectorAO convergenceDetector = new ConvergenceDetectorAO(tw.getWindowSize(), advance,
								tw.getBaseTimeUnit());
						insertConvergenceDetector(tw, convergenceDetector);
					} else if (operator instanceof ElementWindowAO) {
						ElementWindowAO ew = (ElementWindowAO) operator;
						// Advance may be null
						Long advance = ew.getWindowAdvanceE();
						if (advance == null) {
							advance = new Long(1);
						}
						ConvergenceDetectorAO convergenceDetector = new ConvergenceDetectorAO(
								ew.getWindowSizeE().longValue(), advance.longValue());
						insertConvergenceDetector(ew, convergenceDetector);
					}
				}
			}
		});
	}

	/**
	 * Inserts a given {@link ConvergenceDetectorAO} after a given window
	 * operator.
	 */
	static void insertConvergenceDetector(AbstractWindowAO windowAO, ConvergenceDetectorAO convergenceDetector) {
		Collection<LogicalSubscription> subs = new ArrayList<>(windowAO.getSubscriptions());
		windowAO.unsubscribeFromAllSinks();
		convergenceDetector.subscribeToSource(windowAO, 0, 0, windowAO.getInputSchema());
		for (LogicalSubscription sub : subs) {
			convergenceDetector.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(),
					sub.getSchema());
		}
	}

}