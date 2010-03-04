package de.uniol.inf.is.odysseus.action.dataSources.generator;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.action.dataSources.generator.TupleGenerator.GeneratorType;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MachineMaintenaceClient extends ISourceClient {
	private GeneratorConfig generatorConfig;
	
	private TupleGenerator tupleGenerator;

	public MachineMaintenaceClient(GeneratorConfig generatorConfig, GeneratorType type) throws GeneratorException{
		this.generatorConfig = generatorConfig;
		
		this.tupleGenerator = new TupleGenerator(generatorConfig, type);
		
		super.logger = LoggerFactory.getLogger(MachineMaintenaceClient.class);
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
		RelationalTuple<IMetaAttribute> tuple = this.tupleGenerator.generateTuple();
		super.sendTupleToClients(tuple);
		return true;
	}

}
