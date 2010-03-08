package de.uniol.inf.is.odysseus.action.dataSources.generator;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.benchmark.BenchmarkData;
import de.uniol.inf.is.odysseus.action.benchmark.IActuatorBenchmark;
import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.action.dataSources.generator.TupleGenerator.GeneratorType;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MachineMaintenaceClient extends ISourceClient {
	private long frequency;
	private double acceleration;
	
	private TupleGenerator tupleGenerator;

	public MachineMaintenaceClient(GeneratorConfig generatorConfig, GeneratorType type, boolean benchmark) 
			throws GeneratorException{
		this.frequency = generatorConfig.getFrequencyOfUpdates();
		this.acceleration = generatorConfig.getAccelerationFactor();
		
		this.tupleGenerator = new TupleGenerator(generatorConfig, type, benchmark);
		
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
		RelationalTuple<IMetaAttribute> tuple;
		try {
			tuple = this.tupleGenerator.generateTuple();
		} catch (GeneratorException e) {
			super.logger.info("TupleGenerator <"+ this.tupleGenerator.getGenTyp().name() +">stopped ...");
			super.logger.info(e.getMessage());
			
			return false;
		}
		
		if (tuple != null){
			super.sendTupleToClients(tuple);
			if (this.tupleGenerator.isBenchmark()){
				((BenchmarkData)tuple.getMetadata()).addOutputTime(IActuatorBenchmark.Operation.DATAEXTRACTION.name());
			}
		}
		
		try {
			//wait for next update
			sleep(this.frequency);
			
			//accelerate
			this.frequency *= (1 / this.acceleration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

}
