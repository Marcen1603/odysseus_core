package de.uniol.inf.is.odysseus.query.codegenerator.analyse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.FindSinksLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.codegenerator.ICAnalyse;
import de.uniol.inf.is.odysseus.query.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.registry.OperatorRuleRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.ITargetPlatform;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.SessionHelper;
import de.uniol.inf.is.odysseus.transform.rules.TDeleteRenameAORule;
public class DefaultQueryAnalyse implements ICAnalyse{
	
	private  Logger LOG = LoggerFactory.getLogger(DefaultQueryAnalyse.class);

	private  QueryAnalyseInformation transformationInformation;
	private  boolean renameRemoved = false;
	private  boolean newRound = false;

	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startQueryTransformation(TransformationParameter parameter){
		

		LOG.debug("Start query transformation!"+ parameter.getParameterForDebug());
		
		CodegeneratorMessageBus.sendUpdate(new CodegeneratorMessageEvent(-1, "Start query analyse",UpdateMessageStatusType.INFO));
		
		
		transformationInformation = new QueryAnalyseInformation();
		
	
		//get query
		ILogicalQuery queryTopAo = ExecutorServiceBinding.getExecutor().getLogicalQueryById(parameter.getQueryId(), SessionHelper.getActiveSession());
		
		//get transformationConfiguration for the query
		TransformationConfiguration transformationConfiguration =	ExecutorServiceBinding.getExecutor().getBuildConfigForQuery(queryTopAo).getTransformationConfiguration();
		
		//copy logicalGraph
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(queryTopAo);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(queryTopAo.getLogicalPlan(), copyVisitor);
		
		ILogicalOperator savedPlan = copyVisitor.getResult();
	
		//find sources in logicalPlan
		FindSourcesLogicalVisitor<ILogicalOperator> findSourcesVisitor = new FindSourcesLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walkerSources = new GenericGraphWalker();
		walkerSources.prefixWalk(savedPlan, findSourcesVisitor);
		
		transformationInformation.addSourceOp(findSourcesVisitor.getResult());
		
		//remove all RenameAO 
		do{
			prepareLogicalPlan(transformationInformation.getSourceOpList());
		}while(newRound);
		
		analyseLogicalPlan(savedPlan, parameter, transformationConfiguration);
		
		
		ITargetPlatform targetPlatform = TargetPlatformRegistry.getTargetPlatform(parameter.getProgramLanguage());
		

		try {
			targetPlatform.convertQueryToStandaloneSystem(savedPlan,transformationInformation, parameter, transformationConfiguration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		

	}
	

	private void prepareLogicalPlan(List<ILogicalOperator> sourceOps){
		renameRemoved = false;
		
		for(ILogicalOperator operator : sourceOps){
			removeAO(operator);
		}
		
		if(renameRemoved){
			newRound = true;
		}else{
			newRound= false;
		}
	}
	
	private void removeAO(ILogicalOperator operator){
		
		for(LogicalSubscription s:operator.getSubscriptions()){
			removeAO(s.getTarget());
		}
		
		if(operator instanceof RenameAO){
			RenameAO renameAO = (RenameAO)operator;
			
			TDeleteRenameAORule tdelteRenameAORule =  new TDeleteRenameAORule();
	
			if(tdelteRenameAORule.isExecutable(renameAO, null)){
				RestructHelper.removeOperator(renameAO, true);
				renameRemoved = true;
			}else{
				LOG.debug("Operator: "+renameAO.getName() +"could not removed!");
			}
		
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void analyseLogicalPlan(ILogicalOperator query,TransformationParameter parameter, TransformationConfiguration transformationConfiguration){
		
		
		for(String metaDataType : transformationConfiguration.getDefaultMetaTypeSet()){
			transformationInformation.addMetaData(metaDataType);	
		}
		
		FindSinksLogicalVisitor<ILogicalOperator> findSinksVisitor = new FindSinksLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(query, findSinksVisitor);
	
		
		for(ILogicalOperator topAO : findSinksVisitor.getResult()){
			if(topAO instanceof TopAO){
				
			
					for(LogicalSubscription sourceOPSub : topAO.getSubscribedToSource()){
						
						transformationInformation.addSinkOp(sourceOPSub.getTarget());
					}
			
				
			}else{
				//TODO refactoring...
				if(topAO instanceof RenameAO){
					
					if(topAO.getSubscriptions().isEmpty()){
						transformationInformation.addSinkOp(topAO);
					}else{
					   //TODO wird nicht mehr benötigt? Workaround vom RenameAO
						for(LogicalSubscription sourceOPSub : topAO.getSubscribedToSource()){
							transformationInformation.addSinkOp(sourceOPSub.getTarget());
						}
					}
					
				}else{
					transformationInformation.addSinkOp(topAO);
				}
			}
		}
		
		try {
			
			for(ILogicalOperator sourceOperator: transformationInformation.getSourceOpList()){
				analyseOperator(sourceOperator,parameter, transformationConfiguration);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	
		
	}
	
	private void analyseOperator(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		ICOperatorRule<ILogicalOperator> opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		
		if(opTrans != null ){
		
			if(!transformationInformation.isOperatorAdded(operator)){
			
				//reg the operator to generate a uniq operatorVariable
				transformationInformation.addOperator(operator);
				
				opTrans.analyseOperator(operator,transformationInformation);
				opTrans.addDataHandlerFromSDFSchema(operator, transformationInformation);
				opTrans.addOperatorConfiguration(operator, transformationInformation);
			}
		}
		

		for(LogicalSubscription s:operator.getSubscriptions()){
			analyseOperator(s.getTarget(),parameter, transformationConfiguration);
		}

		
		
	}
	


}
