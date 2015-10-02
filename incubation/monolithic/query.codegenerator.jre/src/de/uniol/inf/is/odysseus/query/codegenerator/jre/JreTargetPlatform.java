package de.uniol.inf.is.odysseus.query.codegenerator.jre;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.rcp.RCPJreOptions;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ICRCPOptionComposite;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.scheduler.registry.CSchedulerRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.AbstractTargetPlatform;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.ExecuteShellComand;

public class JreTargetPlatform extends AbstractTargetPlatform{
	
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
		DefaultCodegeneratorStatus.clear();
		
		DefaultCodegeneratorStatus.getInstance().setOperatorList(queryAnalyseInformation.getOperatorList());
		
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
		JavaFileWrite javaFileWrite = new JavaFileWrite("Main.java",parameter,importList,osgiBind.getCode(),bodyCode.toString(),startStreams.getCode(), queryAnalyseInformation.getOperatorConfigurationList(), CSchedulerRegistry.getExecutor(parameter.getProgramLanguage(), parameter.getExecutor()));
		
		try {
			updateProgressBar(80, "Create Java project",UpdateMessageStatusType.INFO);
			javaFileWrite.createProject();


			if(isAutobuild(parameter)){
				updateProgressBar(85, "Compile the Java project",UpdateMessageStatusType.INFO);
				ExecuteShellComand.executeAntScript(parameter.getTargetDirectory());	
			}
		
			
			updateProgressBar(100, "Transformation finish",UpdateMessageStatusType.INFO);
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
		
		updateProgressBar(20, operator.getName()+" is a "+ operator.getClass().getSimpleName() +" --> "+opTrans.getName(),UpdateMessageStatusType.INFO);
		
		//add ready
		DefaultCodegeneratorStatus.getInstance().addOperatorToCodeReady(operator);

		//generate the default code e.g. SDFSchema
		CodeFragmentInfo initOp = CreateJreDefaultCode.getCodeForInitOperator(operator);
		sdfSchemaCode.append(initOp.getCode());
		
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
	
	public void generateOperatorSubscription(ILogicalOperator operator,QueryAnalyseInformation queryAnalseInformation){
		//generate subscription
		CodeFragmentInfo  subscription = CreateJreDefaultCode.getCodeForSubscription(operator, queryAnalseInformation);
		if(subscription!= null){
			bodyCode.append(subscription.getCode());	
			importList.addAll(subscription.getImports());
		}
	}
	
	@Override
	public ICRCPOptionComposite getOptionsRCP(Composite parent, int style) {
		return new RCPJreOptions(parent, style);
	}

	
	private boolean isAutobuild(TransformationParameter parameter){
		Map<String, String> options = parameter.getOptions();
		if(options != null){
			if(options.containsKey("autobuild")){
				if(options.get("autobuild").equals("true")){
					return true;
				}else{
					return false;
				}
			}
			
		}
		return false;
	}




}
