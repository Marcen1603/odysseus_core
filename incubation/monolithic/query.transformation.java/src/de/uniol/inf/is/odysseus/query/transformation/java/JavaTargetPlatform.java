package de.uniol.inf.is.odysseus.query.transformation.java;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.filewriter.JavaFileWrite;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OperatorToVariable;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.JavaEmulateOSGIBindings;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.registry.OperatorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.AbstractTargetPlatform;

public class JavaTargetPlatform extends AbstractTargetPlatform{

	private String targetPlatformName = "Java";

	private static JavaFileWrite testWrite;
	
	private Set<String> importList = new HashSet<String>();
	
	private StringBuilder body;
	
	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}


	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			TransformationParameter parameter) {
		// TODO Auto-generated method stub
		body = new StringBuilder();
		
		JavaEmulateOSGIBindings javaEmulateOSGIBindings = new JavaEmulateOSGIBindings();
		testWrite = new JavaFileWrite("TestFile.java",parameter);
		
		addDefaultImport();
		
		walkThroughLogicalPlan(query,parameter);
		
		
		try {
			
			//create java file
			testWrite.createFile();
			
			//write java imports
			testWrite.writeImports(importList);
			
			//write java main
			testWrite.writeClassTop();
			
			//write body of main
				testWrite.writeBody(javaEmulateOSGIBindings.getCodeForDataHandlerRegistry());
				testWrite.writeBody(body.toString());
				testWrite.writeBody(generateSubscription(query));
				testWrite.writeBody(startDataStream());
			
			
			//close file
			testWrite.writeBottom();
			

			
			//ExecuteShellComand.compileJavaProgram(parameter.getTempDirectory());	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	


	/*
	 * TODO how to get the needed imports? 
	 */
	private void addDefaultImport() {
		
		importList.add("de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute");
		importList.add("de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype");
		importList.add("de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema");
		importList.add("de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory");
		importList.add("de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner");
		importList.add("de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery");
		
		importList.add("de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry");
		importList.add("de.uniol.inf.is.odysseus.core.datahandler.IDataHandler");
		importList.add("de.uniol.inf.is.odysseus.core.datahandler.IntegerHandler");
		importList.add("de.uniol.inf.is.odysseus.core.datahandler.StringHandler");
		importList.add("de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler");
		
	}
	
	
	/*
	 * Test
	 	testAccess.subscribeSink(testProject, 0, 0, testAccess.getOutputSchema());
	 	testAccess.connectSink(testProject, 0, 0, testAccess.getOutputSchema());
	 	TODO Weg von Quelle zur Senke 
	 */

	private String generateSubscription(ILogicalOperator query) {
		
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("\n");
		code.append("testAccess.subscribeSink(testProject, 0, 0, testAccess.getOutputSchema());");
		code.append("\n");
		code.append("testAccess.connectSink(testProject, 0, 0, testAccess.getOutputSchema());"); 
		code.append("\n");
		code.append("\n");
		code.append("\n");
		return code.toString();
	}
	
	
	/*
	 * Test
	 * 
	 IOperatorOwner operatorOwner = new LogicalQuery();
	   testAccess.open(operatorOwner); 
	     while(true){
        	testAccess.transferNext();
        }
	 */
	private String startDataStream(){
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("\n");
		code.append("IOperatorOwner operatorOwner = new LogicalQuery();");
		code.append("\n");
		code.append("testAccess.open(operatorOwner);");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("while(true){");
		code.append("\n");
		code.append("testAccess.transferNext();");
		code.append("\n");
		code.append("}");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
	}


	//Test
	private  void walkThroughLogicalPlan(ILogicalOperator topAO, TransformationParameter parameter){
		
		if(topAO instanceof TopAO){	
		
		}else{
			System.out.println("Operator-Name: "+topAO.getName()+" "+ topAO.getClass().getSimpleName());
			IOperator opTrans = OperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), topAO.getClass().getSimpleName());

			
			if(opTrans != null ){
				OperatorToVariable.addOperator(topAO);
				System.out.println(opTrans.getCode(topAO));
				//testWrite.writeBody(opTrans.getCode(topAO));
				
				body.append(opTrans.getCode(topAO));
				
				importList.addAll(opTrans.getNeededImports());
			}
		}
	
		for(LogicalSubscription  operator : topAO.getSubscribedToSource()){
			System.out.println("Operator-Name: "+operator.getTarget().getName()+" "+ operator.getTarget().getClass().getSimpleName());
			IOperator opTrans = OperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), operator.getTarget().getClass().getSimpleName());
			
			if(opTrans != null ){
				OperatorToVariable.addOperator(operator.getTarget());
				System.out.println(opTrans.getCode(operator.getTarget()));
				//testWrite.writeBody(opTrans.getCode(operator.getTarget()));
				
				body.append(opTrans.getCode(operator.getTarget()));
			}
				
			for(LogicalSubscription  operator2 : operator.getTarget().getSubscribedToSource()){
				walkThroughLogicalPlan(operator2.getTarget(),parameter);
				
			}
		}
	}

}
