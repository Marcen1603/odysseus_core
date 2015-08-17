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
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.JavaEmulateOSGIBindings;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
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
		
	
		//Start Odysseus index
		updateProgressBar(15, "Index the Odysseus codepath");
		OdysseusIndex.getInstance().search(parameter.getOdysseusPath());
	
		bodyCode = new StringBuilder();

		JavaEmulateOSGIBindings javaEmulateOSGIBindings = new JavaEmulateOSGIBindings();
		
		walkTroughLogicalPlan(transformationInforamtion.getSourceOpList(),parameter, transformationConfiguration);
		
		//generate code for osgi binds
		updateProgressBar(70, "Generate OSGI emulation code");
		osgiBindCode = javaEmulateOSGIBindings.getCodeForOSGIBinds(parameter.getOdysseusPath(), transformationInforamtion);
		
		importList.addAll(javaEmulateOSGIBindings.getNeededImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateDefaultCode.codeForStartStreams(transformationInforamtion.getSinkOpList(), transformationInforamtion.getSourceOpList(), parameter.getExecutor());
		
		//generate executor code and files
		ExecutorRegistry.getExecutor("Java", parameter.getExecutor()).createNeededFiles(parameter);
		
	
		importList.addAll(startStreams.getImports());
	
		updateProgressBar(75, "Create Java files");
		JavaFileWrite javaFileWrite = new JavaFileWrite("Main.java",parameter,importList,osgiBindCode,bodyCode.toString(),startStreams.getCode());
		
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
	

	private void walkTroughLogicalPlan(List<ILogicalOperator> operatorSources,TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		
		for(ILogicalOperator sourceOperator: operatorSources){
				generateCode(sourceOperator,parameter, transformationConfiguration);
		}
		
	}
	
	private void generateCode(ILogicalOperator operator,  TransformationParameter parameter, TransformationConfiguration transformationConfiguration) throws InterruptedException{
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());

	
		IOperatorRule opTrans = OperatorRuleRegistry.getOperatorRules(parameter.getProgramLanguage(), operator, transformationConfiguration);
		if(opTrans != null ){
		
			if(!OperatorTransformationInformation.getInstance().isOperatorAdded(operator)){
				
				this.getProgressBarQueue().put(new ProgressBarUpdate(20, operator.getName()+" is a "+ operator.getClass().getSimpleName() +" --> "+opTrans.getName()));
				
				//reg the operator to generate a uniq operatorVariable
				OperatorTransformationInformation.getInstance().addOperator(operator);

				//generate the default code e.g. SDFSchema
				CodeFragmentInfo initOp = CreateDefaultCode.initOperator(operator);
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
				
			
				//generate subscription
				CodeFragmentInfo  subscription = CreateDefaultCode.generateSubscription(operator);
				bodyCode.append(subscription.getCode());	
				importList.addAll(subscription.getImports());
			}
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			generateCode(s.getTarget(),parameter, transformationConfiguration);
		}
		
	}








}
