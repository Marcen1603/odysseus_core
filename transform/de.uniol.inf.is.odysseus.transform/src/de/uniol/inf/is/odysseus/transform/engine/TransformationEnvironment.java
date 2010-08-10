package de.uniol.inf.is.odysseus.transform.engine;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;

/**
 * Handles the local state: current loaded configuration for each transformation instance etc.
 * 
 * 
 * @author Dennis Geesen
 *
 */
public class TransformationEnvironment{
	
	private TransformationConfiguration transformationConfiguration = null;
	private WorkingMemory workingMemory;
	
	
	public TransformationEnvironment(TransformationConfiguration transformConfig){
		this.transformationConfiguration = transformConfig;
		this.workingMemory = new WorkingMemory(this);
	}
		
	public TransformationConfiguration getTransformationConfig(){
		return this.transformationConfiguration;
	}

	public void processTransformation() {
		this.workingMemory.process();		
	}

	public WorkingMemory getWorkingMemory() {
		return this.workingMemory;
	}

	public TransformationConfiguration getTransformationConfiguration() {
		return this.transformationConfiguration;
	}
	
}
