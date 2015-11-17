package de.uniol.inf.is.odysseus.codegenerator.target.platform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.codegenerator.model.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.model.enums.UpdateMessageEventType;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.registry.COperatorRuleRegistry;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractTargetPlatform implements ITargetPlatform{
	
	//targetPlatform name is set by osgi
	private String targetPlatformName = "";
	
	protected Set<String> frameworkImportList = new HashSet<String>();
	protected Set<String> importList = new HashSet<String>();
	
	
	protected StringBuilder bodyCode;
	protected StringBuilder sdfSchemaCode;
	
	/**
	 * hook methode to generate operator code
	 * 
	 * @param operator
	 * @param parameter
	 * @param transformationConfiguration
	 * @param queryAnalseInformation
	 * @param opTrans
	 */
	public abstract void generateOperatorCodeOperatorReady(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation,ICOperatorRule<ILogicalOperator> opTrans);
	
	/**
	 * hook methode to generate operator subscription code
	 * 
	 * @param operator
	 * @param queryAnalseInformation
	 */
	public abstract void generateOperatorSubscription(ILogicalOperator operator,QueryAnalyseInformation queryAnalseInformation );

	
	public AbstractTargetPlatform(String targetPlatformName) {
		this.setTargetPlatformName(targetPlatformName);
	}
	
	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}

	@Override
	public void setTargetPlatformName(String targetPlatformName) {
		this.targetPlatformName = targetPlatformName;
	}
	
	@Override
	public void sendMessageEvent(int value, String text, UpdateMessageEventType statusType){
		CodegeneratorMessageBus.sendUpdate(new CodegeneratorMessageEvent(value, text,statusType));
	}
	
	@Override
	public String getSpecialOptionInfos(){
		return "";
	}
	
	/**
	 * start the transformation from source to sink
	 * 
	 * @param queryAnalyseInformation
	 * @param parameter
	 * @param transformationConfiguration
	 * @throws InterruptedException
	 */
	protected void transformQuery(QueryAnalyseInformation queryAnalyseInformation,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		CodegeneratorMessageBus.warningErrorDetected = false;

		List<ILogicalOperator> operatorSources = queryAnalyseInformation.getSourceOpList();
		
		for(ILogicalOperator sourceOperator: operatorSources){
				preCheckOperator(sourceOperator,parameter, transformationConfiguration,queryAnalyseInformation);
		}
		
		//push the sdfSchema at the top
		StringBuilder tempBodyCode = new StringBuilder();
		tempBodyCode = bodyCode;
		
		bodyCode = sdfSchemaCode;
		bodyCode.append(tempBodyCode);
		
	}

	/**
	 * pre check the logical operator. If all conditions are met then
	 * call the generateOperatorCode method.
	 * 
	 * @param operator
	 * @param parameter
	 * @param transformationConfiguration
	 * @param queryAnalseInformation
	 * @throws InterruptedException
	 */
	protected void preCheckOperator(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		
		if(operator.getSubscribedToSource().size() >= 2){
			if(DefaultCodegeneratorStatus.getInstance().isOperatorReadyforCodegeneration(operator)){
				try {
					generateOperatorCode(operator,parameter, transformationConfiguration,queryAnalseInformation);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else{
			try {
				generateOperatorCode(operator,parameter, transformationConfiguration,queryAnalseInformation);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
	}
	
	/**
	 * resolve the operatorRule to transform the logical operator
	 * 
	 * @param operator
	 * @param parameter
	 * @param transformationConfiguration
	 * @param queryAnalseInformation
	 * @throws InterruptedException
	 */
	protected void generateOperatorCode(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		
		ICOperatorRule<ILogicalOperator> opTrans = COperatorRuleRegistry.getOperatorRules(parameter.getTargetPlatformName(), operator, transformationConfiguration);
		if(opTrans != null ){
		
			if(!DefaultCodegeneratorStatus.getInstance().isOperatorCodeReady(operator)){
				generateOperatorCodeOperatorReady(operator,parameter,transformationConfiguration,queryAnalseInformation, opTrans);
			}
			
			 generateOperatorSubscription(operator, queryAnalseInformation);

		}else{
			sendMessageEvent(-1, "No rule available for "+operator.getName()+" is a "+ operator.getClass().getSimpleName()  ,UpdateMessageEventType.WARNING);
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			preCheckOperator(s.getTarget(),parameter, transformationConfiguration,queryAnalseInformation);
		}
	}
	

	/**
	 * create a summary for the user after transformation is finished
	 * 
	 * @param parameter
	 * @param optional
	 * @return
	 */
	protected String generateSummary(TransformationParameter parameter, String optional){
		StringBuilder summary = new StringBuilder();
		summary.append("Transformation finish \n\n");
		
		if(CodegeneratorMessageBus.warningErrorDetected()){
			summary.append("------------------WARNING or ERROR DETECTED------------------\n\n");
			summary.append("please check the log!\n\n");
		}
		
		summary.append("------------------Summary------------------\n\n");
		summary.append("Target directory: "+parameter.getTargetDirectory()+"\n");
		summary.append("Targetplatform: "+parameter.getTargetPlatformName()+"\n");
		summary.append(optional.toString());
		return summary.toString();
	}
	
}
