package de.uniol.inf.is.odysseus.query.transformation.python;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.transformation.python.filewrite.PythonFileWrite;
import de.uniol.inf.is.odysseus.query.transformation.target.platform.AbstractTargetPlatform;

public class PythonTargetPlatform extends AbstractTargetPlatform{

	
	private static PythonFileWrite testWrite;

	public PythonTargetPlatform() {
		super("Python");
	}
	
	
	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			QueryAnalyseInformation transformationInforamtion,
			TransformationParameter parameter,
			BlockingQueue<ProgressBarUpdate> queue,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException {
	

		
		
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
		
	
	}



}
