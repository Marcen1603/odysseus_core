package de.uniol.inf.is.odysseus.query.codegenerator.python;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.query.codegenerator.python.filewrite.PythonFileWrite;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.AbstractTargetPlatform;

public class PythonTargetPlatform extends AbstractTargetPlatform{

	
	private static PythonFileWrite testWrite;

	public PythonTargetPlatform() {
		super("Python");
	}
	
	
	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			QueryAnalyseInformation queryAnalyseInformation,
			TransformationParameter parameter,
			BlockingQueue<ProgressBarUpdate> queue,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException {
	

		
		testWrite = new PythonFileWrite("TestFilePython.py",parameter);
		try {
			testWrite.createFile();
			
			
			transformQuery(queryAnalyseInformation,parameter, transformationConfiguration);
			
			testWrite.closeFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



	@Override
	public void generateOperatorCodeOperatorReady(ILogicalOperator operator,
			TransformationParameter parameter,
			TransformationConfiguration transformationConfiguration,
			QueryAnalyseInformation queryAnalseInformation,
			ICOperatorRule<ILogicalOperator> opsTrans) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void generateOperatorSubscription(ILogicalOperator operator,
			QueryAnalyseInformation queryAnalseInformation) {
		// TODO Auto-generated method stub
		
	}





}
