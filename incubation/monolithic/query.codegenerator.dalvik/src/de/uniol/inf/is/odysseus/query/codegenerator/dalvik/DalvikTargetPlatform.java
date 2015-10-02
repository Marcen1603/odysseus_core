package de.uniol.inf.is.odysseus.query.codegenerator.dalvik;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.dalvik.filewriter.DalvikFileWrite;
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

public class DalvikTargetPlatform extends AbstractTargetPlatform{


	
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
		updateProgressBar(10, "Start the transformation",UpdateMessageStatusType.INFO);
		
		//clear transformation infos
		DefaultCodegeneratorStatus.clear();
		
		DefaultCodegeneratorStatus.getInstance().setOperatorList(queryAnalyseInformation.getOperatorList());
	
		bodyCode = new StringBuilder();
		sdfSchemaCode  = new StringBuilder();
		
		transformQuery(queryAnalyseInformation,parameter, transformationConfiguration);
		

		CodeFragmentInfo osgiBind = CreateJreDefaultCode.getCodeForOSGIBinds(parameter.getOdysseusPath(), queryAnalyseInformation);
		importList.addAll(osgiBind.getImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateJreDefaultCode.getCodeForStartStreams(queryAnalyseInformation, parameter.getExecutor());
		
		importList.addAll(startStreams.getImports());
		
		DalvikFileWrite dalvikFileWrite = new DalvikFileWrite("MainActivityFragment.java",parameter,importList,osgiBind.getCode(),bodyCode.toString(),startStreams.getCode(), queryAnalyseInformation.getOperatorConfigurationList(), CSchedulerRegistry.getExecutor("Java", parameter.getExecutor()));
		
		try {
			dalvikFileWrite.createProject();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public ICRCPOptionComposite getOptionsRCP(Composite parent, int style) {
		// TODO Auto-generated method stub
		return null;
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

}
