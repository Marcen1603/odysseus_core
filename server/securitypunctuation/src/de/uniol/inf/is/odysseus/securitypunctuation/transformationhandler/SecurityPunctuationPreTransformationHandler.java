package de.uniol.inf.is.odysseus.securitypunctuation.transformationhandler;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.AbstractPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SPAnalyzerAO;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SecurityShieldAO;

public class SecurityPunctuationPreTransformationHandler extends AbstractPreTransformationHandler {
	public final static String NAME = "SecurityPunctuationPreTransformationHandler";


	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {
		ILogicalOperator logicalOp = query.getLogicalPlan();

		List<ILogicalOperator> sources = AbstractDataDictionary.findSources(logicalOp);
	
		
		for (ILogicalOperator s : sources) {
			SecurityShieldAO SecurityShieldToInsert=new SecurityShieldAO();
			RestructHelper.insertOperatorBefore2(SecurityShieldToInsert, s);
			SPAnalyzerAO toInsert = new SPAnalyzerAO();
			RestructHelper.insertOperatorBefore2(toInsert, s);
			
		}

	}
/*
	static public void findSelects(ILogicalOperator topOperator) {
		ILogicalOperator logicalOp = new SelectAO();
		GenericGraphWalker<ILogicalOperator> walker;
		FindSourcesLogicalVisitor<ILogicalOperator> findSources = new FindSourcesLogicalVisitor<>();
		walker = new GenericGraphWalker<ILogicalOperator>();
		walker.prefixWalk(logicalOp, findSources);
		
		List<ILogicalOperator> sources = findSources.getResult();
		for (ILogicalOperator s : sources) {
			SPAnalyzerAO toInsert = new SPAnalyzerAO();
			RestructHelper.insertOperatorBefore2(toInsert, s);

		}
*/
	}

