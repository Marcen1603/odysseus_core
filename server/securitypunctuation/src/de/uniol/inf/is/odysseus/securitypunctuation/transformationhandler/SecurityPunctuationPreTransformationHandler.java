package de.uniol.inf.is.odysseus.securitypunctuation.transformationhandler;

import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SAJoinAO;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SAProjectAO;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SASelectAO;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SPAnalyzerAO;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SecurityShieldAO;

/* needed for processing of security punctuations
 * this PreTransformationhandler replaces the operators Select, Project and Join 
 * with their security aware counterparts and adds an SPAnalyzer to the beginning of the queryplan
 *
 */

public class SecurityPunctuationPreTransformationHandler extends AbstractPreTransformationHandler {
	public final static String NAME = "SPPreTransformationHandler";
	private static final Logger LOG = LoggerFactory.getLogger(SecurityPunctuationPreTransformationHandler.class);
	String tupleRangeAttribute=null;

	@Override
	public String getName() {
		return NAME;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {
		ILogicalOperator logicalOp = query.getLogicalPlan();

		Set<Class<? extends ILogicalOperator>> operatorClasses = new HashSet<>();

		// add the Operatorclasses that are supposed to be replaced with their
		// security aware counterpart
		if (!handlerParameters.isEmpty()) {
			for (Pair p : handlerParameters) {
				if (p.getE1().equals("TupleRangeAttribute")) {
					tupleRangeAttribute =(String) p.getE2();
					LOG.info((String)p.getE2());
					break;
				}
			}
		}

		operatorClasses.add(SelectAO.class);
		operatorClasses.add(ProjectAO.class);
		operatorClasses.add(JoinAO.class);

		CollectOperatorLogicalGraphVisitor visitor = new CollectOperatorLogicalGraphVisitor(operatorClasses, false);
		GenericGraphWalker copyWalker = new GenericGraphWalker();
		copyWalker.prefixWalk(logicalOp, visitor);
		Set logicalPlan = visitor.getResult();
		replaceOperator(logicalPlan);

		/*
		 * finds the sources and places a SPAnalyzer behind it
		 */
		List<ILogicalOperator> sources = AbstractDataDictionary.findSources(logicalOp);

		for (ILogicalOperator s : sources) {
			SPAnalyzerAO toInsert = new SPAnalyzerAO();
			RestructHelper.insertOperatorBefore2(toInsert, s);

		}

		// places a Security Shield Operator at the top of the logical query
		// plan
		if (logicalOp instanceof TopAO) {
			CopyOnWriteArrayList<LogicalSubscription> sourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>(
					logicalOp.getSubscribedToSource());
			for (LogicalSubscription logicalSubscription : sourceSubscriptions) {
				// unsibscribe the topAO from all source subscriptions
				ILogicalOperator sourceOperator = logicalSubscription.getTarget();
				logicalOp.unsubscribeFromSource(logicalSubscription);

				// create SecurityShieldAO operator and connect to all existing
				// sources
				SecurityShieldAO securityShieldAO = new SecurityShieldAO(tupleRangeAttribute);
				sourceOperator.subscribeSink(securityShieldAO, 0, logicalSubscription.getSourceOutPort(),
						logicalSubscription.getSchema());

				// reconnect existing topAO
				securityShieldAO.subscribeSink(logicalOp, logicalSubscription.getSinkInPort(), 0,
						logicalSubscription.getSchema());
			}
		}

	}

	/*
	 * replaces the operators with their security aware implementations
	 */
	@SuppressWarnings("rawtypes")
	private void replaceOperator(Set logicalPlan) {
		for (Object ao : logicalPlan) {
			if (ao instanceof SelectAO) {
				RestructHelper.replace((SelectAO) ao, new SASelectAO(((SelectAO) ao).getPredicate()));
			} else if (ao instanceof ProjectAO) {
				RestructHelper.replace((ProjectAO) ao, new SAProjectAO((ProjectAO) ao));
			} else if (ao instanceof JoinAO) {
				RestructHelper.replace((JoinAO) ao, new SAJoinAO(tupleRangeAttribute));
			}
		}
	}

}
