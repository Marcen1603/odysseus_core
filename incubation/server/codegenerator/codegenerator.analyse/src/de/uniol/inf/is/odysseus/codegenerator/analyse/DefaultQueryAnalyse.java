package de.uniol.inf.is.odysseus.codegenerator.analyse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.ICAnalyse;
import de.uniol.inf.is.odysseus.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.codegenerator.model.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.model.enums.UpdateMessageEventType;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.registry.COperatorRuleRegistry;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.ITargetPlatform;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.registry.TargetPlatformRegistry;
import de.uniol.inf.is.odysseus.codegenerator.utils.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.codegenerator.utils.SessionHelper;
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
import de.uniol.inf.is.odysseus.transform.rules.TDeleteRenameAORule;

/**
 * Default class for query analyse
 * 
 * @author MarcPreuschaft
 *
 */
public class DefaultQueryAnalyse implements ICAnalyse{
	
	private  Logger LOG = LoggerFactory.getLogger(DefaultQueryAnalyse.class);
	
	private  QueryAnalyseInformation transformationInformation;
	
	//helping variables for detecting remove renameAO
	private  boolean renameRemoved = false;
	private  boolean newRound = false;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startQueryTransformation(TransformationParameter parameter){
		LOG.debug("Start query analyse!"+ parameter.getParameterForDebug());
		
		//send new message to the message bus
		CodegeneratorMessageBus.sendUpdate(new CodegeneratorMessageEvent(-1, "Start query analyse",UpdateMessageEventType.INFO));
		
		//create a new instanz of QueryAnalyseInformation
		transformationInformation = new QueryAnalyseInformation();
	
		//get query for the analyse (transformation)
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
		
		//start query analyse
		analyseLogicalPlan(savedPlan, parameter, transformationConfiguration);
		
		//get the targetPlatfrom 
		ITargetPlatform targetPlatform = TargetPlatformRegistry.getTargetPlatform(parameter.getTargetPlatformName());
		
		try {
			//start the transformation of the query
			targetPlatform.convertQueryToStandaloneSystem(savedPlan,transformationInformation, parameter, transformationConfiguration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		

	}
	
	/**
	 * prepare logical plan, start to remove all 
	 * renameAO that are not needed
	 *  
	 * @param sourceOps
	 */
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
	
	/**
	 * remove renameAO from a query
	 * 
	 * @param operator
	 */
	private void removeAO(ILogicalOperator operator){
		
		for(LogicalSubscription op:operator.getSubscriptions()){
			removeAO(op.getTarget());
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
		
		//get all metaDataTypes from transformatonConfiguration
		for(String metaDataType : transformationConfiguration.getDefaultMetaTypeSet()){
			transformationInformation.addMetaData(metaDataType);	
		}
		
		//find all sinks in the query plan
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
					   //TODO wird nicht mehr ben�tigt? Workaround vom RenameAO
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
	
	/**
	 * analyse a logical operator
	 * get DataHandler form SDFSchema
	 * get protocolHandler, transportHandler, mep-funktion
	 * 
	 * @param operator
	 * @param parameter
	 * @param transformationConfiguration
	 * @throws InterruptedException
	 */
	private void analyseOperator(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		ICOperatorRule<ILogicalOperator> opTrans = COperatorRuleRegistry.getOperatorRules(parameter.getTargetPlatformName(), operator, transformationConfiguration);
		
		if(opTrans != null ){
		
			if(!transformationInformation.isOperatorAdded(operator)){
			
				//reg the operator to generate a uniq operatorVariable
				transformationInformation.addOperator(operator);
				
				//analyse the operator 
				opTrans.analyseOperator(operator,transformationInformation);
				//get all dataHandler form the SDFSchema
				opTrans.addDataHandlerFromSDFSchema(operator, transformationInformation);
				//check if the operator can have a external operatorconfiguration
				opTrans.addOperatorConfiguration(operator, transformationInformation);
			}
		}
		
		for(LogicalSubscription op:operator.getSubscriptions()){
			analyseOperator(op.getTarget(),parameter, transformationConfiguration);
		}
	}
}
