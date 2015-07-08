package de.uniol.inf.is.odysseus.query.transformation.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.util.FindSinksLogicalVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.analysis.JavaImportAnalyse;
import de.uniol.inf.is.odysseus.query.transformation.java.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorToVariable;
import de.uniol.inf.is.odysseus.query.transformation.java.shell.commands.ExecuteShellComand;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.JavaEmulateOSGIBindings;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.registry.OperatorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.AbstractTargetPlatform;

public class JavaTargetPlatform extends AbstractTargetPlatform{

	private String targetPlatformName = "Java";

	private static JavaFileWrite testWrite;
	
	private Set<String> importList = new HashSet<String>();
	
	private StringBuilder body;
	private String osgiBinds;
	
	private ILogicalOperator rootOP;
	private ILogicalOperator sinkOP;
	
	private List<ILogicalOperator> operatorList ;
	
	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}


	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			TransformationParameter parameter) {
	
		//Start Odysseus index
		OdysseusIndex.search(parameter.getOdysseusPath());
		
		operatorList = new ArrayList<ILogicalOperator>();
		body = new StringBuilder();

		FindSinksLogicalVisitor<ILogicalOperator> findSinksVisitor = new FindSinksLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(query, findSinksVisitor);
		
		List<ILogicalOperator> sinkList = findSinksVisitor.getResult();
		
		
		for(ILogicalOperator op : sinkList){
			op.getName();
		}
		
		JavaEmulateOSGIBindings javaEmulateOSGIBindings = new JavaEmulateOSGIBindings();
		
		testWrite = new JavaFileWrite("Main.java",parameter);
		
		addDefaultImport();
		
		walkTroughLogicalPlanNeu(query,parameter);
		
		//analyse imports for the body code
		importList.addAll(new JavaImportAnalyse().analyseCodeForImport(parameter, body.toString()));
		
		osgiBinds = javaEmulateOSGIBindings.getCodeForOSGIBinds(parameter.getOdysseusPath());
		
		importList.addAll(javaEmulateOSGIBindings.getNeededImports());
		
		//add imports that needed for the code
		importList.addAll(new JavaImportAnalyse().analyseCodeForImport(parameter, startDataStream()));
		
		try {
			
			//create java file
			testWrite.createFile();
			
			testWrite.writePackage();
			
			//write java imports
			testWrite.writeImports(importList);
			
			//write java main
			testWrite.writeClassTop();
			
			//write body of main
				testWrite.writeBody(osgiBinds);
				testWrite.writeBody(body.toString());
				testWrite.writeBody(generateSubscription(operatorList));
				testWrite.writeBody(startDataStream());
			
			
			//close file
			testWrite.writeBottom();
			
			ExecuteShellComand.compileJavaProgram(parameter.getTempDirectory());	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	


	/*
	 * TODO how to get the needed imports? 
	 */
	private void addDefaultImport() {
		
		importList.add("java.util.ArrayList");
		importList.add("java.util.List");
		importList.add("java.io.IOException");
	
	}
	
	
	/*
	 * Test
	 	testAccess.subscribeSink(testProject, 0, 0, testAccess.getOutputSchema());
	 	testAccess.connectSink(testProject, 0, 0, testAccess.getOutputSchema());
	 	TODO Weg von Quelle zur Senke 
	 */

	private String generateSubscription(List<ILogicalOperator> operatorList) {
		
		StringBuilder code = new StringBuilder();
		
		for(ILogicalOperator op : operatorList){
			
		  String operatorVariable = OperatorToVariable.getVariable(op);
		
		   Collection<LogicalSubscription> subscriptionSourceList = op.getSubscribedToSource();
			
		   if(!subscriptionSourceList.isEmpty()){
			   for(LogicalSubscription sub : subscriptionSourceList){
					  String targetOp =  OperatorToVariable.getVariable(sub.getTarget());
					  if(!targetOp.equals("")){
						  code.append("\n");
							code.append("\n");
							code.append(operatorVariable+"PO.subscribeToSource("+targetOp+"PO, 0, 0, "+targetOp+"PO.getOutputSchema());");
							code.append("\n");
					  }	
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
		code.append("physicalOperatorList.add("+OperatorToVariable.getVariable(rootOP)+"PO);");
		code.append("\n");
		code.append("IOperatorOwner operatorOwner = new PhysicalQuery(physicalOperatorList);");
		code.append("\n");
		//Open on sink op
		code.append(OperatorToVariable.getVariable(sinkOP)+"PO.open(operatorOwner);");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("while("+OperatorToVariable.getVariable(rootOP)+"PO.hasNext()){");
		code.append("\n");
		code.append(OperatorToVariable.getVariable(rootOP)+"PO.transferNext();");
		code.append("\n");
		code.append("}");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
	}

	
	private void walkTroughLogicalPlanNeu(ILogicalOperator operator, TransformationParameter parameter){
		System.out.println("Operator-Name: "+operator.getName()+" "+ operator.getClass().getSimpleName());
		
		if(operator.getSubscribedToSource().isEmpty()){
			this.rootOP = operator;
		}
		
		if(operator.isSinkOperator()){
			this.sinkOP = operator;
		}
		
		IOperator opTrans = OperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), operator.getClass().getSimpleName());
		if(opTrans != null ){
			System.out.println(opTrans.getCode(operator));
			
			OperatorToVariable.addOperator(operator);
			String tempCode = opTrans.getCode(operator);
		
			body.append(tempCode);
			
			//add import for *PO
			importList.addAll(opTrans.getNeededImports());
			
		
			//add imports that needed for the code
			//importList.addAll(new JavaImportAnalysis().analyseCodeForImport(parameter, tempCode));
		
			operatorList.add(operator);
		}
		
		for(LogicalSubscription s:operator.getSubscribedToSource()){
			walkTroughLogicalPlanNeu(s.getTarget(),parameter);
		}
	}


}
