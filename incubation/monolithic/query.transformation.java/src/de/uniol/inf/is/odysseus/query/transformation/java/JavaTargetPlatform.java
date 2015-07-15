package de.uniol.inf.is.odysseus.query.transformation.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.util.FindSinksLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.shell.commands.ExecuteShellComand;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.JavaEmulateOSGIBindings;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.registry.OperatorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.AbstractTargetPlatform;

public class JavaTargetPlatform extends AbstractTargetPlatform{

	private String targetPlatformName = "Java";

	private Set<String> importList = new HashSet<String>();
	
	private StringBuilder bodyCode;
	private String osgiBindCode;
	
	private List<ILogicalOperator> sourceOPs;
	private List<ILogicalOperator> sinkOPs;

	
	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			TransformationParameter parameter) {
		//clear transformation infos
		TransformationInformation.clear();
		
		//init sinksOps list
		sinkOPs = new ArrayList<ILogicalOperator>();
	
		//Start Odysseus index
		OdysseusIndex.search(parameter.getOdysseusPath());
	
		bodyCode = new StringBuilder();

		FindSinksLogicalVisitor<ILogicalOperator> findSinksVisitor = new FindSinksLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(query, findSinksVisitor);
	
		
		for(ILogicalOperator topAO : findSinksVisitor.getResult()){
			for(LogicalSubscription sourceOPSub : topAO.getSubscribedToSource()){
				sinkOPs.add(sourceOPSub.getTarget());
			}
		}
		
		FindSourcesLogicalVisitor<ILogicalOperator> findSourcesVisitor = new FindSourcesLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walkerSources = new GenericGraphWalker();
		walkerSources.prefixWalk(query, findSourcesVisitor);
		
		sourceOPs = findSourcesVisitor.getResult();
	
		JavaEmulateOSGIBindings javaEmulateOSGIBindings = new JavaEmulateOSGIBindings();
		
		walkTroughLogicalPlan(sourceOPs,parameter);
		
		//generate code for osgi binds
		osgiBindCode = javaEmulateOSGIBindings.getCodeForOSGIBinds(parameter.getOdysseusPath());
		
		importList.addAll(javaEmulateOSGIBindings.getNeededImports());
		
		//generate start code
		CodeFragmentInfo startStreams = CreateDefaultCode.codeForStartStreams(sinkOPs, sourceOPs);
		
		importList.addAll(startStreams.getImports());
	
		JavaFileWrite javaFileWrite = new JavaFileWrite("Main.java",parameter,importList,osgiBindCode,bodyCode.toString(),startStreams.getCode());
		
		try {
			javaFileWrite.createProject();
	
			ExecuteShellComand.compileJavaProgram(parameter.getTempDirectory());	
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	private void walkTroughLogicalPlan(List<ILogicalOperator> operatorSources,TransformationParameter parameter){
		
		for(ILogicalOperator sourceOperator: operatorSources){
				generateCode(sourceOperator,parameter);
		}
		
	}
	
	private void generateCode(ILogicalOperator operator,  TransformationParameter parameter){
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());
		
	
		IOperator opTrans = OperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), operator.getClass().getSimpleName());
		if(opTrans != null ){
		
			if(!TransformationInformation.getInstance().isOperatorAdded(operator)){
				
			
				//reg the operator to generate a uniq operatorVariable
				TransformationInformation.getInstance().addOperator(operator);

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
				importList.addAll(opTrans.getNeededImports());
	
				//subcode imports
				importList.addAll(opCodeFragment.getImports());
				
			
				//generate subscription
				CodeFragmentInfo  subscription = CreateDefaultCode.generateSubscription(operator);
				bodyCode.append(subscription.getCode());	
				importList.addAll(subscription.getImports());
			}
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			generateCode(s.getTarget(),parameter);
		}
		
	}

}
