package de.uniol.inf.is.odysseus.codegenerator.jre;

import java.io.IOException;
import java.util.HashSet;

import de.uniol.inf.is.odysseus.codegenerator.jre.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.codegenerator.jre.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.codegenerator.jre.model.JreTargetplatformOption;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.modell.enums.UpdateMessageEventType;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.AbstractTargetPlatform;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.codegenerator.utils.ExecuteShellComand;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public class JreTargetPlatform extends AbstractTargetPlatform{
	
	public JreTargetPlatform(){
		super("Jre");
	}
	
	@Override
	public void convertQueryToStandaloneSystem(
			ILogicalOperator query,
			QueryAnalyseInformation queryAnalyseInformation,
			TransformationParameter parameter,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException  {
		
		//clear defaultCodegeneratorStatus 
		DefaultCodegeneratorStatus.clear();
		DefaultCodegeneratorStatus.getInstance().setOperatorList(queryAnalyseInformation.getOperatorList());
		
		//init variables
		bodyCode = new StringBuilder();
		sdfSchemaCode  = new StringBuilder();
		frameworkImportList = new HashSet<String>();
		importList = new HashSet<String>();
		
		//get special transformation options
		JreTargetplatformOption jreTargetplatformOption = new JreTargetplatformOption();
		jreTargetplatformOption.parse(parameter);
		
		//add userfeedback
		sendMessageEvent(10, "Start the transformation", UpdateMessageEventType.INFO);
		
		//Start Odysseus index
		sendMessageEvent(15, "Index the Odysseus codepath",UpdateMessageEventType.INFO);
		OdysseusIndex.getInstance().search(parameter.getOdysseusDirectory());
	
		//start query transformation
		transformQuery(queryAnalyseInformation,parameter, transformationConfiguration);
		
		//generate code for osgi binds
		sendMessageEvent(70, "Generate OSGI emulation code",UpdateMessageEventType.INFO);
		
		CodeFragmentInfo osgiBind = CreateJreDefaultCode.getCodeForOSGIBinds(parameter.getOdysseusDirectory(), queryAnalyseInformation);
		frameworkImportList.addAll(osgiBind.getFrameworkImports());
		importList.addAll(osgiBind.getImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateJreDefaultCode.getCodeForStartStreams(queryAnalyseInformation, parameter.getExecutor());
		frameworkImportList.addAll(startStreams.getFrameworkImports());
		importList.addAll(startStreams.getImports());
		
	
		sendMessageEvent(75, "Create Java files",UpdateMessageEventType.INFO);
		JavaFileWrite javaFileWrite = new JavaFileWrite("Main.java",parameter,frameworkImportList,importList,osgiBind.getCode(),bodyCode.toString(),startStreams.getCode(), queryAnalyseInformation.getOperatorConfigurationList(), CSchedulerRegistry.getScheduler(parameter.getProgramLanguage(), parameter.getExecutor()));
		
		try {
			sendMessageEvent(80, "Create Java project",UpdateMessageEventType.INFO);
			javaFileWrite.createProject();


			if(jreTargetplatformOption.isAutobuild()){
				sendMessageEvent(85, "Compile the Java project",UpdateMessageEventType.INFO);
				ExecuteShellComand.executeAntScript(parameter.getTargetDirectory());	
			}
		
			sendMessageEvent(100, generateSummary(parameter,compiledProgramm(parameter)),UpdateMessageEventType.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void generateOperatorCodeOperatorReady(ILogicalOperator operator,
			TransformationParameter parameter,
			TransformationConfiguration transformationConfiguration,
			QueryAnalyseInformation queryAnalseInformation,
			ICOperatorRule<ILogicalOperator> opTrans) {
		
		sendMessageEvent(20, operator.getName()+" is a "+ operator.getClass().getSimpleName() +" --> "+opTrans.getName(),UpdateMessageEventType.INFO);
		
		//add ready
		DefaultCodegeneratorStatus.getInstance().addOperatorToCodeReady(operator);

		//generate the default code e.g. SDFSchema
		CodeFragmentInfo initOp = CreateJreDefaultCode.getCodeForInitOperator(operator);
		sdfSchemaCode.append(initOp.getCode());
		
		//add imports for default code
		frameworkImportList.addAll(initOp.getFrameworkImports());
		importList.addAll(initOp.getImports());
		
		//generate operator code
		CodeFragmentInfo opCodeFragment = opTrans.getCode(operator);
		
		String operatorCode = opCodeFragment.getCode();
	
		//add operator code to java body
		bodyCode.append(operatorCode);

		//subcode imports
		frameworkImportList.addAll(opCodeFragment.getFrameworkImports());
		importList.addAll(opCodeFragment.getImports());
		
	}
	
	public void generateOperatorSubscription(ILogicalOperator operator,QueryAnalyseInformation queryAnalseInformation){
		//generate subscription
		CodeFragmentInfo  subscription = CreateJreDefaultCode.getCodeForSubscription(operator, queryAnalseInformation);
		if(subscription!= null){
			bodyCode.append(subscription.getCode());	
			frameworkImportList.addAll(subscription.getFrameworkImports());
			importList.addAll(subscription.getImports());
		}
	}
	
	
	@Override
	public String getSpecialOptionInfos(){
		return "autobuild=true/false";
	}

	
	private String compiledProgramm(TransformationParameter parameter){
		StringBuilder compiledProgramm = new StringBuilder();
		compiledProgramm.append("Compiled programm: "+parameter.getTargetDirectory()+"\\target");
		return compiledProgramm.toString();
	}

}
