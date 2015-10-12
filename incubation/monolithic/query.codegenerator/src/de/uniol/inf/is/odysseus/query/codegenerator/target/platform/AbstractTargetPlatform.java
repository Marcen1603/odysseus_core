package de.uniol.inf.is.odysseus.query.codegenerator.target.platform;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.message.bus.CodegeneratorMessageBus;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.registry.OperatorRuleRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;

public abstract class AbstractTargetPlatform implements ITargetPlatform{
	
	private static Logger LOG = LoggerFactory.getLogger(AbstractTargetPlatform.class);
	
	
	protected Set<String> importList = new HashSet<String>();
	protected StringBuilder bodyCode;
	protected StringBuilder sdfSchemaCode;
	
	public abstract void generateOperatorCodeOperatorReady(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation,ICOperatorRule<ILogicalOperator> opTrans);
	public abstract void generateOperatorSubscription(ILogicalOperator operator,QueryAnalyseInformation queryAnalseInformation );
	
	private BlockingQueue<ProgressBarUpdate> progressBarQueue;
	private String targetPlatformName = "";
	
	public AbstractTargetPlatform(String targetPlatformName) {
		this.setTargetPlatformName(targetPlatformName);
	}
	
	@Override
	public BlockingQueue<ProgressBarUpdate> getProgressBarQueue() {
		return progressBarQueue;
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
	public void updateProgressBar(int value, String text, UpdateMessageStatusType statusType){
		CodegeneratorMessageBus.sendUpdate(new ProgressBarUpdate(value, text,statusType));
		LOG.info(statusType+": "+text);
		
	}
	
	@Override
	public String getSpecialOptionInfos(){
		return "";
	}
	
	
	protected void transformQuery(QueryAnalyseInformation queryAnalyseInformation,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		List<ILogicalOperator> operatorSources = queryAnalyseInformation.getSourceOpList();
		
		for(ILogicalOperator sourceOperator: operatorSources){
				preCheckOperator(sourceOperator,parameter, transformationConfiguration,queryAnalyseInformation);
		}
		
		//sdfschema nach oben schieben
		StringBuilder tempBodyCode = new StringBuilder();
		tempBodyCode = bodyCode;
		
		bodyCode = sdfSchemaCode;
		bodyCode.append(tempBodyCode);
		
	}

	
	protected void preCheckOperator(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());

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
	
	protected void generateOperatorCode(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		
		ICOperatorRule<ILogicalOperator> opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		if(opTrans != null ){
		
			if(!DefaultCodegeneratorStatus.getInstance().isOperatorCodeReady(operator)){
				generateOperatorCodeOperatorReady(operator,parameter,transformationConfiguration,queryAnalseInformation, opTrans);
			}
			
			
			 	generateOperatorSubscription(operator, queryAnalseInformation);

		}else{
			updateProgressBar(-1, "No rule available for "+operator.getName()+" is a "+ operator.getClass().getSimpleName()  ,UpdateMessageStatusType.WARNING);
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			preCheckOperator(s.getTarget(),parameter, transformationConfiguration,queryAnalseInformation);
		}
	}
	
}
