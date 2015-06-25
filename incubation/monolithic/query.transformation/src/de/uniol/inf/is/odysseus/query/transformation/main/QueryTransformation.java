package de.uniol.inf.is.odysseus.query.transformation.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.ITargetPlatform;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.query.transformation.utils.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.query.transformation.utils.QueryTransformationHelper;

public class QueryTransformation {
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformation.class);
	
	public static void startQueryTransformation(TransformationParameter parameter){
		
		LOG.debug("Start query transformation!"+ parameter.getParameterForDebug());
		
		ILogicalQuery queryTopAo = ExecutorServiceBinding.getExecutor().getLogicalQueryById(parameter.getQueryId(), QueryTransformationHelper.getActiveSession());

		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(queryTopAo);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(queryTopAo.getLogicalPlan(), copyVisitor);
		
		ILogicalOperator savedPlan = copyVisitor.getResult();
		
	
		ITargetPlatform targetPlatform = TargetPlatformRegistry.getTargetPlatform(parameter.getProgramLanguage());
		
		targetPlatform.convertQueryToStandaloneSystem(savedPlan, parameter);
	}

}
