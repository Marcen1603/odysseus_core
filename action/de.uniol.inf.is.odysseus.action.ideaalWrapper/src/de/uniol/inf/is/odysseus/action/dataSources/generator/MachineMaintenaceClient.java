package de.uniol.inf.is.odysseus.action.dataSources.generator;

import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.action.dataSources.generator.TupleGenerator.GeneratorType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MachineMaintenaceClient extends ISourceClient {
	private GeneratorConfig generatorConfig;
	
	private TupleGenerator tupleGenerator;

	public MachineMaintenaceClient(GeneratorConfig generatorConfig, GeneratorType type) throws GeneratorException{
		this.generatorConfig = generatorConfig;
		
		this.tupleGenerator = new TupleGenerator(generatorConfig, type);
	}	
	
	@Override
	public void cleanUp() {
		//not necessary
	}

	@Override
	public SDFAttributeList getSchema() {
		return this.tupleGenerator.getSchema();
	}

	@Override
	public boolean processData() {
		
		return true;
	}

}
