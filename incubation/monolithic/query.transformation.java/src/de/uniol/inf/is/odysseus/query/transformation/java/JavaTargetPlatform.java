package de.uniol.inf.is.odysseus.query.transformation.java;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.executor.registry.ExecutorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.java.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorTransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.shell.commands.ExecuteShellComand;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.EmulateJavaOSGIBindings;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.transformation.modell.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.IOperatorRule;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.registry.OperatorRuleRegistry;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.AbstractTargetPlatform;

public class JavaTargetPlatform extends AbstractTargetPlatform{
	
	private Set<String> importList = new HashSet<String>();
	private StringBuilder bodyCode;
	private String osgiBindCode;
	
	public JavaTargetPlatform(){
		super("Java");
	}
	
	@Override
	public void convertQueryToStandaloneSystem(
			ILogicalOperator query,
			de.uniol.inf.is.odysseus.query.transformation.modell.TransformationInformation transformationInforamtion,
			TransformationParameter parameter,
			BlockingQueue<ProgressBarUpdate> progressBarQueue,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException  {
		this.setProgressBarQueue(progressBarQueue);
		
		//add userfeedback
		updateProgressBar(10, "Start the transformation");
		
		//clear transformation infos
		OperatorTransformationInformation.clear();
		
		OperatorTransformationInformation.getInstance().setOperatorList(transformationInforamtion.getOperatorList());
		//Start Odysseus index
		updateProgressBar(15, "Index the Odysseus codepath");
		OdysseusIndex.getInstance().search(parameter.getOdysseusPath());
	
		bodyCode = new StringBuilder();

		EmulateJavaOSGIBindings javaEmulateOSGIBindings = new EmulateJavaOSGIBindings();
		
		walkTroughLogicalPlan(transformationInforamtion,parameter, transformationConfiguration);
		
		//generate code for osgi binds
		updateProgressBar(70, "Generate OSGI emulation code");
		osgiBindCode = javaEmulateOSGIBindings.getCodeForOSGIBinds(parameter.getOdysseusPath(), transformationInforamtion);
		
		importList.addAll(javaEmulateOSGIBindings.getNeededImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateJavaDefaultCode.codeForStartStreams(transformationInforamtion.getSinkOpList(), transformationInforamtion.getSourceOpList(), parameter.getExecutor());
		
		importList.addAll(startStreams.getImports());
	
		updateProgressBar(75, "Create Java files");
		JavaFileWrite javaFileWrite = new JavaFileWrite("Main.java",parameter,importList,osgiBindCode,bodyCode.toString(),startStreams.getCode(), transformationInforamtion.getOperatorConfigurationList(), ExecutorRegistry.getExecutor("Java", parameter.getExecutor()));
		
		try {
			updateProgressBar(80, "Create Java project");
			javaFileWrite.createProject();
	
			updateProgressBar(85, "Compile the Java project");
			ExecuteShellComand.compileJavaProgram(parameter.getTempDirectory());	
			
			updateProgressBar(100, "Transformation finish");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	

	private void walkTroughLogicalPlan(TransformationInformation transformationInforamtion,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		List<ILogicalOperator> operatorSources = transformationInforamtion.getSourceOpList();
		
		
		for(ILogicalOperator sourceOperator: operatorSources){
				generateCode(sourceOperator,parameter, transformationConfiguration,transformationInforamtion);
		}
		
	}
	
	private void generateCode(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration,TransformationInformation transformationInforamtion) throws InterruptedException{
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());

	
		IOperatorRule opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		if(opTrans != null ){
		
			if(!OperatorTransformationInformation.getInstance().isOperatorCodeReady(operator)){
				
				this.getProgressBarQueue().put(new ProgressBarUpdate(20, operator.getName()+" is a "+ operator.getClass().getSimpleName() +" --> "+opTrans.getName()));
				
				//add ready
				OperatorTransformationInformation.getInstance().addOperatorToCodeReady(operator);
			

				//generate the default code e.g. SDFSchema
				CodeFragmentInfo initOp = CreateJavaDefaultCode.initOperator(operator);
				String operatorCode = initOp.getCode();
				
				//add imports for default code
				importList.addAll(initOp.getImports());
				
				//generate operator code
				CodeFragmentInfo opCodeFragment = opTrans.getCode(operator);
				
				operatorCode += opCodeFragment.getCode();
			
				//add operator code to java body
				bodyCode.append(operatorCode);
				
				//add import for *PO
				//importList.addAll(opTrans.getNeededImports());
	
				//subcode imports
				importList.addAll(opCodeFragment.getImports());
				
			
		
			}
			
			//generate subscription
			CodeFragmentInfo  subscription = CreateJavaDefaultCode.generateSubscription(operator, transformationInforamtion);
			if(subscription!= null){
				bodyCode.append(subscription.getCode());	
				importList.addAll(subscription.getImports());
			}
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			generateCode(s.getTarget(),parameter, transformationConfiguration,transformationInforamtion);
		}
		
	}








}
