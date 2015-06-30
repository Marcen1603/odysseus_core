package de.uniol.inf.is.odysseus.query.transformation.python;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.registry.OperatorRegistry;
import de.uniol.inf.is.odysseus.query.transformation.python.filewrite.PythonFileWrite;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.AbstractTargetPlatform;

public class PythonTargetPlatform extends AbstractTargetPlatform{

	private String targetPlatformName = "Python";
	
	
	private static PythonFileWrite testWrite;
	
	@Override
	public String getTargetPlatformName() {
		return targetPlatformName;
	}


	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			TransformationParameter parameter) {
		// TODO Auto-generated method stub
		
		
		testWrite = new PythonFileWrite("TestFilePython.py",parameter);
		try {
			testWrite.createFile();
			
			
			walkThroughLogicalPlan(query,parameter);
			
			testWrite.closeFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//Test
	private  void walkThroughLogicalPlan(ILogicalOperator topAO, TransformationParameter parameter) throws IOException{
		
		if(topAO instanceof TopAO){	
		
		}else{
			System.out.println("Operator-Name: "+topAO.getName()+" "+ topAO.getClass().getSimpleName());
			IOperator opTrans = OperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), topAO.getClass().getSimpleName());
	
			
			if(opTrans != null ){
				System.out.println(opTrans.getCode(topAO));
				testWrite.writeBody(opTrans.getCode(topAO));
			}
		}
	
		for(LogicalSubscription  operator : topAO.getSubscribedToSource()){
			System.out.println("Operator-Name: "+operator.getTarget().getName()+" "+ operator.getTarget().getClass().getSimpleName());
			IOperator opTrans = OperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), operator.getTarget().getClass().getSimpleName());
			
			if(opTrans != null ){
				System.out.println(opTrans.getCode(operator.getTarget()));
				testWrite.writeBody(opTrans.getCode(operator.getTarget()));
			}
				
			for(LogicalSubscription  operator2 : operator.getTarget().getSubscribedToSource()){
				walkThroughLogicalPlan(operator2.getTarget(),parameter);
				
			}
		}
	}

}