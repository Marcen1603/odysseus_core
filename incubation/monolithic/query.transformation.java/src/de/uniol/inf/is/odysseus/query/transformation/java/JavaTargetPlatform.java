package de.uniol.inf.is.odysseus.query.transformation.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.util.FindSinksLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.analysis.JavaImportAnalyse;
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

	private static JavaFileWrite javaFileWrite;
	
	private Set<String> importList = new HashSet<String>();
	
	private StringBuilder body;
	private String osgiBinds;
	
	private List<ILogicalOperator> sourceOPs;
	private List<ILogicalOperator> sinkOPs;

	
	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}


	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			TransformationParameter parameter) {
		//clear transformation infos
		TransformationInformation.clear();
		
		//init sinksOps list
		sinkOPs = new ArrayList<ILogicalOperator>();
	
		//Start Odysseus index
		OdysseusIndex.search(parameter.getOdysseusPath());
	
		body = new StringBuilder();

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
		
		javaFileWrite = new JavaFileWrite("Main.java",parameter);
		
		walkTroughLogicalPlan(sourceOPs,parameter);
		
		//generate code for osgi binds
		osgiBinds = javaEmulateOSGIBindings.getCodeForOSGIBinds(parameter.getOdysseusPath());
		
		//add default java imports
		//addDefaultImport();
		
		//analyse imports for the body code
		//importList.addAll(new JavaImportAnalyse().analyseCodeForImport(parameter, body.toString()));

		importList.addAll(javaEmulateOSGIBindings.getNeededImports());
		
		//add imports that needed for the code
		importList.addAll(new JavaImportAnalyse().analyseCodeForImport(parameter, startDataStream()));
		
		try {
			
			//create java file
			javaFileWrite.createFile();
			
			javaFileWrite.writePackage();
			
			//write java imports
			javaFileWrite.writeImports(importList);
			
			//write java main
			javaFileWrite.writeClassTop();
			
			//write body of main
				javaFileWrite.writeBody(osgiBinds);
				javaFileWrite.writeBody(body.toString());
				javaFileWrite.writeBody(startDataStream());
			
			
			//close file
			javaFileWrite.writeBottom();
			
			ExecuteShellComand.compileJavaProgram(parameter.getTempDirectory());	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * TODO how to get the needed imports for java? classLoader ?
	 */
	private void addDefaultImport() {
		importList.add("java.util.ArrayList");
		importList.add("java.util.List");
		importList.add("java.io.IOException");
		importList.add("java.util.concurrent.TimeUnit");
	}
	
	
	private String generateSubscription(ILogicalOperator operator) {
		
		StringBuilder code = new StringBuilder();
	
		String operatorVariable = TransformationInformation.getInstance().getVariable(operator);
		
		Collection<LogicalSubscription> subscriptionSourceList = operator.getSubscribedToSource();
			
		   if(!subscriptionSourceList.isEmpty()){
			   for(LogicalSubscription sub : subscriptionSourceList){
					  String targetOp =  TransformationInformation.getInstance().getVariable(sub.getTarget());
					  if(!targetOp.equals("")){
						  code.append("\n");
							code.append("\n");
							code.append(operatorVariable+"PO.subscribeToSource("+targetOp+"PO, "+sub.getSinkInPort()+", "+sub.getSourceOutPort()+", "+targetOp+"PO.getOutputSchema());");
							code.append("\n");
					  }	
				   }
		   }
		

		code.append("\n");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
	}
	
	
	
	
	/*
	 * Test
	 * 
	 ArrayList<IPhysicalOperator> testList = new ArrayList<IPhysicalOperator>();
	 testList.add(soccergame37PO);
	 IOperatorOwner operatorOwner = new PhysicalQuery(testList);
	 

	   testAccess.open(operatorOwner); 
	     while(true){
        	testAccess.transferNext();
        }
	 */
	private String startDataStream(){
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("\n");
		code.append("ArrayList<IPhysicalOperator> physicalOperatorList = new ArrayList<IPhysicalOperator>();");
		code.append("\n");
		code.append("physicalOperatorList.add("+TransformationInformation.getInstance().getVariable(sourceOPs.get(0))+"PO);");
		code.append("\n");
		code.append("IOperatorOwner operatorOwner = new PhysicalQuery(physicalOperatorList);");
		code.append("\n");
		code.append("\n");
	
		//add owner to op
		for (Entry<ILogicalOperator, String> entry : TransformationInformation.getInstance().getOperatorList().entrySet())
		{
			code.append(entry.getValue()+"PO.addOwner(operatorOwner);");
			code.append("\n");
		}
	
		//Open on sink ops
		for(ILogicalOperator sinkOp : sinkOPs){
				code.append(TransformationInformation.getInstance().getVariable(sinkOp)+"PO.open(operatorOwner);");
				code.append("\n");
		}
		
		code.append("\n");
		code.append("while("+TransformationInformation.getInstance().getVariable(sourceOPs.get(0))+"PO.hasNext()){");
		code.append("\n");
		code.append(TransformationInformation.getInstance().getVariable(sourceOPs.get(0))+"PO.transferNext();");
		code.append("\n");
		code.append("}");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
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
				body.append(operatorCode);
				
				//add import for *PO
				importList.addAll(opTrans.getNeededImports());
	
				//subcode imports
				importList.addAll(opCodeFragment.getImports());
				
			
				//generate subscription
				body.append(generateSubscription(operator));	
			}
		}
		
	
		for(LogicalSubscription s:operator.getSubscriptions()){
			generateCode(s.getTarget(),parameter);
		}
		
	}

}
