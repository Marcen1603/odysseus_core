package de.uniol.inf.is.odysseus.query.codegenerator.dalvik;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.query.codegenerator.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.dalvik.filewriter.DalvikFileWrite;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.registry.ExecutorRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.IOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.registry.OperatorRuleRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.AbstractTargetPlatform;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

public class DalvikTargetPlatform extends AbstractTargetPlatform{
	
	private Set<String> importList = new HashSet<String>();
	private StringBuilder bodyCode;
	private StringBuilder sdfSchemaCode;
	
	
	public DalvikTargetPlatform() {
		super("Dalvik");
	}

	@Override
	public void convertQueryToStandaloneSystem(
			ILogicalOperator query,
			QueryAnalyseInformation queryAnalyseInformation,
			TransformationParameter parameter,
			BlockingQueue<ProgressBarUpdate> progressBarQueue,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException {
		
		
		//add userfeedback
		updateProgressBar(10, "Start the transformation");
		
		//clear transformation infos
		JavaTransformationInformation.clear();
		
		JavaTransformationInformation.getInstance().setOperatorList(queryAnalyseInformation.getOperatorList());
	
		bodyCode = new StringBuilder();
		sdfSchemaCode  = new StringBuilder();
		
		walkTroughLogicalPlan(queryAnalyseInformation,parameter, transformationConfiguration);
		

		CodeFragmentInfo osgiBind = CreateJavaDefaultCode.getCodeForOSGIBinds(parameter.getOdysseusPath(), queryAnalyseInformation);
		importList.addAll(osgiBind.getImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateJavaDefaultCode.codeForStartStreams(queryAnalyseInformation, parameter.getExecutor());
		
		importList.addAll(startStreams.getImports());
		
		DalvikFileWrite dalvikFileWrite = new DalvikFileWrite("MainActivityFragment.java",parameter,importList,osgiBind.getCode(),bodyCode.toString(),startStreams.getCode(), queryAnalyseInformation.getOperatorConfigurationList(), ExecutorRegistry.getExecutor("Java", parameter.getExecutor()));
		
		try {
			dalvikFileWrite.createProject();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void walkTroughLogicalPlan(QueryAnalyseInformation queryAnalyseInformation,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		List<ILogicalOperator> operatorSources = queryAnalyseInformation.getSourceOpList();
		
		for(ILogicalOperator sourceOperator: operatorSources){
				generateCode(sourceOperator,parameter, transformationConfiguration,queryAnalyseInformation);
		}
		
		
		//sdfschema nach oben schieben
		StringBuilder tempBodyCode = new StringBuilder();
		tempBodyCode = bodyCode;
		
		bodyCode = sdfSchemaCode;
		bodyCode.append(tempBodyCode);

	}
	

	private void generateCode(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,QueryAnalyseInformation queryAnalseInformation) throws InterruptedException{
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());

	
		IOperatorRule opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		if(opTrans != null ){
		
			if(!JavaTransformationInformation.getInstance().isOperatorCodeReady(operator)){
				
				//this.getProgressBarQueue().put(new ProgressBarUpdate(20, operator.getName()+" is a "+ operator.getClass().getSimpleName() +" --> "+opTrans.getName()));
				
				//add ready
				JavaTransformationInformation.getInstance().addOperatorToCodeReady(operator);
		
				//generate the default code e.g. SDFSchema
				CodeFragmentInfo initOp = CreateJavaDefaultCode.initOperator(operator);
				sdfSchemaCode.append(initOp.getCode());
				
				//String operatorCode = initOp.getCode();
				
				//add imports for default code
				importList.addAll(initOp.getImports());
				
				//generate operator code
				CodeFragmentInfo opCodeFragment = opTrans.getCode(operator);
				
				String operatorCode = opCodeFragment.getCode();
			
				//add operator code to java body
				bodyCode.append(operatorCode);
				
				//subcode imports
				importList.addAll(opCodeFragment.getImports());
				
			}
			
			//generate subscription
			CodeFragmentInfo  subscription = CreateJavaDefaultCode.generateSubscription(operator, queryAnalseInformation);
			if(subscription!= null){
				bodyCode.append(subscription.getCode());	
				importList.addAll(subscription.getImports());
			}
			
			
		}
		
		for(LogicalSubscription s:operator.getSubscriptions()){
			generateCode(s.getTarget(),parameter, transformationConfiguration,queryAnalseInformation);
		}

		
	}

}
