package de.uniol.inf.is.odysseus.action.dataSources.generator;

import java.util.Map;

import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MachineMaintenaceClient extends ISourceClient {
	private Map<String, Number> properties;
	private GeneratorConfig generatorConfig;

	public MachineMaintenaceClient(GeneratorConfig generatorConfig){
		this.generatorConfig = generatorConfig;
	}	
	
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public SDFAttributeList getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processData() {
		// TODO Auto-generated method stub
		return false;
	}

}
