package de.uniol.inf.is.odysseus.query.codegenerator.jre;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.registry.CExecutorRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.IOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.registry.OperatorRuleRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.AbstractTargetPlatform;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.ExecuteShellComand;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;

public class JreTargetPlatform extends AbstractTargetPlatform{
	
	private Set<String> importList = new HashSet<String>();
	private StringBuilder bodyCode;
	private StringBuilder sdfSchemaCode;
	
	public JreTargetPlatform(){
		super("Jre");
	}
	
	@Override
	public void convertQueryToStandaloneSystem(
			ILogicalOperator query,
			QueryAnalyseInformation queryAnalyseInformation,
			TransformationParameter parameter,
			BlockingQueue<ProgressBarUpdate> progressBarQueue,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException  {
		//clear transformation infos
		JreCodegeneratorStatus.clear();
		
		JreCodegeneratorStatus.getInstance().setOperatorList(queryAnalyseInformation.getOperatorList());
		
		this.setProgressBarQueue(progressBarQueue);
		
		//add userfeedback
		updateProgressBar(10, "Start the transformation", UpdateMessageStatusType.INFO);
		
		bodyCode = new StringBuilder();
		sdfSchemaCode  = new StringBuilder();
		
		//Start Odysseus index
		updateProgressBar(15, "Index the Odysseus codepath",UpdateMessageStatusType.INFO);
		OdysseusIndex.getInstance().search(parameter.getOdysseusPath());
	
		transformQuery(queryAnalyseInformation,parameter, transformationConfiguration);
		
		//generate code for osgi binds
		updateProgressBar(70, "Generate OSGI emulation code",UpdateMessageStatusType.INFO);
		
		CodeFragmentInfo osgiBind = CreateJreDefaultCode.getCodeForOSGIBinds(parameter.getOdysseusPath(), queryAnalyseInformation);
		importList.addAll(osgiBind.getImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateJreDefaultCode.getCodeForStartStreams(queryAnalyseInformation, parameter.getExecutor());
		
		importList.addAll(startStreams.getImports());
	
		updateProgressBar(75, "Create Java files",UpdateMessageStatusType.INFO);
		JavaFileWrite javaFileWrite = new JavaFileWrite("Main.java",parameter,importList,osgiBind.getCode(),bodyCode.toString(),startStreams.getCode(), queryAnalyseInformation.getOperatorConfigurationList(), CExecutorRegistry.getExecutor(parameter.getProgramLanguage(), parameter.getExecutor()));
		
		try {
			updateProgressBar(80, "Create Java project",UpdateMessageStatusType.INFO);
			javaFileWrite.createProject();
	
			updateProgressBar(85, "Compile the Java project",UpdateMessageStatusType.INFO);
			ExecuteShellComand.compileJavaProgram(parameter.getTempDirectory());	
			
			updateProgressBar(100, "Transformation finish",UpdateMessageStatusType.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private void transformQuery(QueryAnalyseInformation queryAnalyseInformation,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
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
	
	private void preCheckOperator(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());

		if(operator.getSubscribedToSource().size() >= 2){
			if(JreCodegeneratorStatus.getInstance().isOperatorReadyforCodegeneration(operator)){
				generateOperatorCode(operator,parameter, transformationConfiguration,queryAnalseInformation);
			}
		}else{
			generateOperatorCode(operator,parameter, transformationConfiguration,queryAnalseInformation);
		}
			
	}
	

	private void generateOperatorCode(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		
		IOperatorRule<ILogicalOperator> opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		if(opTrans != null ){
		
			if(!JreCodegeneratorStatus.getInstance().isOperatorCodeReady(operator)){
				
				this.getProgressBarQueue().put(new ProgressBarUpdate(20, operator.getName()+" is a "+ operator.getClass().getSimpleName() +" --> "+opTrans.getName(),UpdateMessageStatusType.INFO));
				
				//add ready
				JreCodegeneratorStatus.getInstance().addOperatorToCodeReady(operator);
		
				//generate the default code e.g. SDFSchema
				CodeFragmentInfo initOp = CreateJreDefaultCode.getCodeForInitOperator(operator);
				sdfSchemaCode.append(initOp.getCode());
				
				//String operatorCode = initOp.getCode();
				
				//add imports for default code
				importList.addAll(initOp.getImports());
				
				//generate operator code
				CodeFragmentInfo opCodeFragment = opTrans.getCode(operator);
				
				String operatorCode = opCodeFragment.getCode();
			
				//add operator code to java body
				bodyCode.append(operatorCode);
				
				//add import for *PO
				//importList.addAll(opTrans.getNeededImports());
	
				//subcode imports
				importList.addAll(opCodeFragment.getImports());
				
			}
			
			//generate subscription
			CodeFragmentInfo  subscription = CreateJreDefaultCode.getCodeForSubscription(operator, queryAnalseInformation);
			if(subscription!= null){
				bodyCode.append(subscription.getCode());	
				importList.addAll(subscription.getImports());
			}
		}else{
			updateProgressBar(-1, "No rule available for "+operator.getName()+" is a "+ operator.getClass().getSimpleName()  ,UpdateMessageStatusType.WARNING);
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			preCheckOperator(s.getTarget(),parameter, transformationConfiguration,queryAnalseInformation);
		}
	}



}
