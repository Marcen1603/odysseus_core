package de.uniol.inf.is.odysseus.codegenerator.dalvik;

import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.dalvik.filewriter.DalvikFileWrite;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.model.enums.UpdateMessageEventType;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.AbstractTargetPlatform;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * targetplatform for dalvik codegeneration. This class
 * handel the codegeneration for dalvik.
 * 
 * Attention this codegeneration process use the
 * jre codegenerator rules! This is defined over osgi  
 * components definitions. Look at the OSGI-INF folder!
 * 
 * @author MarcPreuschaft
 *
 */
public class DalvikTargetPlatform extends AbstractTargetPlatform{
	
	public DalvikTargetPlatform() {
		super("Dalvik");
	}

	@Override
	public void convertQueryToStandaloneSystem(
			ILogicalOperator query,
			QueryAnalyseInformation queryAnalyseInformation,
			TransformationParameter parameter,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException {
		
		//add userfeedback
		sendMessageEvent(10, "Start the transformation",UpdateMessageEventType.INFO);
		
		//clear transformation infos
		DefaultCodegeneratorStatus.clear();
		
		DefaultCodegeneratorStatus.getInstance().setOperatorList(queryAnalyseInformation.getOperatorList());
	
		bodyCode = new StringBuilder();
		sdfSchemaCode  = new StringBuilder();
		
		//start query transformation
		transformQuery(queryAnalyseInformation,parameter, transformationConfiguration);
		
		CodeFragmentInfo osgiBind = CreateJreDefaultCode.getCodeForOSGIBinds(parameter.getOdysseusDirectory(), queryAnalyseInformation);
		frameworkImportList.addAll(osgiBind.getFrameworkImports());
		importList.addAll(osgiBind.getImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateJreDefaultCode.getCodeForStartStreams(queryAnalyseInformation, parameter.getScheduler());
		frameworkImportList.addAll(startStreams.getFrameworkImports());
		importList.addAll(startStreams.getImports());
		
		//create a new instanz of the dalvikFileWrite
		DalvikFileWrite dalvikFileWrite = new DalvikFileWrite("MainActivityFragment.java",parameter,frameworkImportList,importList,osgiBind.getCode(),bodyCode.toString(),startStreams.getCode(), queryAnalyseInformation.getOperatorConfigurationList(), CSchedulerRegistry.getScheduler("dalvik", parameter.getScheduler()));
		
		try {
			//create all android project files 
			dalvikFileWrite.createProject();
			
			sendMessageEvent(100, generateSummary(parameter,projectInfo(parameter)),UpdateMessageEventType.INFO);
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
	
	/**
	 * generate a info message string
	 * this function is called when the transformation is finish
	 * 
	 * @param parameter
	 * @return
	 */
	private String projectInfo(TransformationParameter parameter){
		StringBuilder projectInfo = new StringBuilder();
		projectInfo.append("\n\n------------------Info------------------\n\n");
		projectInfo.append("Please open the generated project with Android-Studio!\n");
		return projectInfo.toString();
		
	}

}
