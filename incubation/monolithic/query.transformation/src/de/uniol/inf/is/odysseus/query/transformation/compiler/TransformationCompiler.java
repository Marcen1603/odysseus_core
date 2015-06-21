package de.uniol.inf.is.odysseus.query.transformation.compiler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.query.transformation.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.query.transformation.QueryTransformationHelper;
import de.uniol.inf.is.odysseus.query.transformation.filewriter.FileWrite;
import de.uniol.inf.is.odysseus.query.transformation.operator.java.ITransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.registry.QueryTransformationOperatorRegistry;

public class TransformationCompiler {

private static FileWrite testWrite;
	private static Logger LOG = LoggerFactory.getLogger(TransformationCompiler.class);
	
	public static void transformQueryToStandaloneSystem(TransformationCompilerParameter parameter) throws IOException{

	LOG.debug("Start query transformation!"+ parameter.getParameterForDebug());
		
	ILogicalQuery queryTopAo = ExecutorServiceBinding.getExecutor().getLogicalQueryById(parameter.getQueryId(), QueryTransformationHelper.getActiveSession());

	
	CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(queryTopAo);
	GenericGraphWalker walker = new GenericGraphWalker();
	walker.prefixWalk(queryTopAo.getLogicalPlan(), copyVisitor);
	
	ILogicalOperator savedPlan = copyVisitor.getResult();
	
	
	
	testWrite = new FileWrite("TestFile.txt",parameter);
	testWrite.createFile();
	
	
	testWrite.writeClassTop();
	
	walkThroughLogicalPlan(savedPlan,parameter);
	
	testWrite.writeBottom();
	}

	
	//Test
	private static void walkThroughLogicalPlan(ILogicalOperator topAO, TransformationCompilerParameter parameter) throws IOException{
		
		if(topAO instanceof TopAO){	
		
		}else{
			System.out.println("Operator-Name: "+topAO.getName()+" "+ topAO.getClass().getSimpleName());
			ITransformationOperator opTrans = QueryTransformationOperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), topAO.getClass().getSimpleName());
	
			
			if(opTrans != null ){
				System.out.println(opTrans.getCode());
				testWrite.writeBody(opTrans.getCode());
			}
		}
	
		for(LogicalSubscription  operator : topAO.getSubscribedToSource()){
			System.out.println("Operator-Name: "+operator.getTarget().getName()+" "+ operator.getTarget().getClass().getSimpleName());
			ITransformationOperator opTrans = QueryTransformationOperatorRegistry.getOperatorTransformation(parameter.getProgramLanguage(), operator.getTarget().getClass().getSimpleName());
			
			if(opTrans != null ){
				System.out.println(opTrans.getCode());
				testWrite.writeBody(opTrans.getCode());
			}
				
			for(LogicalSubscription  operator2 : operator.getTarget().getSubscribedToSource()){
				walkThroughLogicalPlan(operator2.getTarget(),parameter);
				
			}
		}
	}
}
