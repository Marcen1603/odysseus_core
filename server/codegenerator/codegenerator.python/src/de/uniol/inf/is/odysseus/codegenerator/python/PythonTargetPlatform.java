package de.uniol.inf.is.odysseus.codegenerator.python;

import java.io.IOException;

import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.codegenerator.model.TransformationParameter;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.ICOperatorRule;
import de.uniol.inf.is.odysseus.codegenerator.python.filewrite.PythonFileWrite;
import de.uniol.inf.is.odysseus.codegenerator.target.platform.AbstractTargetPlatform;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * this is only a dummy class to demonstrate a 
 * python targetPlatform
 * 
 * this codegenerator process create only one
 * python file!
 * 
 * @author MarcPreuschaft
 *
 */
public class PythonTargetPlatform extends AbstractTargetPlatform{

	public PythonTargetPlatform() {
		super("Python");
	}
	
	
	@Override
	public void convertQueryToStandaloneSystem(ILogicalOperator query,
			QueryAnalyseInformation queryAnalyseInformation,
			TransformationParameter parameter,
			TransformationConfiguration transformationConfiguration)
			throws InterruptedException {
	

		
		PythonFileWrite pythonFileWrite = new PythonFileWrite("TestFilePython.py",parameter);
		try {
			transformQuery(queryAnalyseInformation,parameter, transformationConfiguration);
			
			
			pythonFileWrite.createFile();
			pythonFileWrite.closeFile();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


	/**
	 * impl here the python code generation prozess
	 */
	@Override
	public void generateOperatorCodeOperatorReady(ILogicalOperator operator,
			TransformationParameter parameter,
			TransformationConfiguration transformationConfiguration,
			QueryAnalyseInformation queryAnalseInformation,
			ICOperatorRule<ILogicalOperator> opsTrans) {
			
		
	}


	@Override
	public void generateOperatorSubscription(ILogicalOperator operator,
			QueryAnalyseInformation queryAnalseInformation) {

		
	}





}
