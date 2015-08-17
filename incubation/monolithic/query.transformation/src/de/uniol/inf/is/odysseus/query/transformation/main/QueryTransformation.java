package de.uniol.inf.is.odysseus.query.transformation.main;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.FindSinksLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.transformation.modell.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.IOperatorRule;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.registry.OperatorRuleRegistry;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.ITargetPlatform;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.query.transformation.utils.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.query.transformation.utils.QueryTransformationHelper;

public class QueryTransformation {
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformation.class);

	private static TransformationInformation transformationInformation;
	
	@SuppressWarnings("unchecked")
	public static void startQueryTransformation(TransformationParameter parameter,BlockingQueue<ProgressBarUpdate> queue ){
		
		LOG.debug("Start query transformation!"+ parameter.getParameterForDebug());
		
		ILogicalQuery queryTopAo = ExecutorServiceBinding.getExecutor().getLogicalQueryById(parameter.getQueryId(), QueryTransformationHelper.getActiveSession());
		
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(queryTopAo);
		@SuppressWarnings("rawtypes")
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(queryTopAo.getLogicalPlan(), copyVisitor);
		
		ILogicalOperator savedPlan = copyVisitor.getResult();
		
		TransformationConfiguration transformationConfiguration =	ExecutorServiceBinding.getExecutor().getBuildConfigForQuery(queryTopAo).getTransformationConfiguration();
		
		analyseQuery(savedPlan, parameter, transformationConfiguration);
		
		ITargetPlatform targetPlatform = TargetPlatformRegistry.getTargetPlatform(parameter.getProgramLanguage());
		
		try {
			targetPlatform.convertQueryToStandaloneSystem(savedPlan,transformationInformation, parameter, queue, transformationConfiguration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void analyseQuery(ILogicalOperator query,TransformationParameter parameter, TransformationConfiguration transformationConfiguration){
		
		transformationInformation = new TransformationInformation();
		
		FindSinksLogicalVisitor<ILogicalOperator> findSinksVisitor = new FindSinksLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(query, findSinksVisitor);
	
		
		for(ILogicalOperator topAO : findSinksVisitor.getResult()){
			for(LogicalSubscription sourceOPSub : topAO.getSubscribedToSource()){
				transformationInformation.addSinkOp(sourceOPSub.getTarget());
			}
		}
		
		
		FindSourcesLogicalVisitor<ILogicalOperator> findSourcesVisitor = new FindSourcesLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walkerSources = new GenericGraphWalker();
		walkerSources.prefixWalk(query, findSourcesVisitor);
		
		transformationInformation.addSourceOp(findSourcesVisitor.getResult());
		
		try {
			walkTroughLogicalPlan(transformationInformation.getSourceOpList(),parameter, transformationConfiguration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}
	
	private static void walkTroughLogicalPlan(List<ILogicalOperator> operatorSources,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		for(ILogicalOperator sourceOperator: operatorSources){
				analyseOperator(sourceOperator,parameter, transformationConfiguration);
		}
		
	}
	
	private static void analyseOperator(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());
		
		IOperatorRule opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		
		if(opTrans != null ){
		
			if(!transformationInformation.isOperatorAdded(operator)){
			
				//reg the operator to generate a uniq operatorVariable
				transformationInformation.addOperator(operator);
				
				opTrans.analyseOperator(operator,transformationInformation);
				opTrans.addDataHandlerFromSDFSchema(operator, transformationInformation);

			}
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			analyseOperator(s.getTarget(),parameter, transformationConfiguration);
		}
		
	}

}
