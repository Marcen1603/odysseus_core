/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.action.dataSources.generator;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.dataSources.ISourceClient;
import de.uniol.inf.is.odysseus.action.dataSources.generator.TupleGenerator.GeneratorType;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class MachineMaintenaceClient extends ISourceClient {
	private long frequency;
	private double acceleration;
	
	private TupleGenerator tupleGenerator;

	public MachineMaintenaceClient(GeneratorConfig generatorConfig, GeneratorType type) 
			throws GeneratorException{
		ScenarioDatamodel.initiDataModel(generatorConfig);
		
		this.frequency = generatorConfig.getFrequencyOfUpdates();
		this.acceleration = generatorConfig.getAccelerationFactor();
		
		this.tupleGenerator = new TupleGenerator(generatorConfig, type);
		
		super.logger = LoggerFactory.getLogger(MachineMaintenaceClient.class);
	}	
	
	@Override
	public void cleanUp() {
		//reset model for new clients
		ScenarioDatamodel.reset();
	}

	@Override
	public SDFSchema getSchema() {
		return this.tupleGenerator.getSchema();
	}

	@Override
	public boolean processData() {
		Tuple<IMetaAttribute> tuple;
		try {
			tuple = this.tupleGenerator.generateTuple();
		} catch (GeneratorException e) {
			super.logger.info("TupleGenerator <"+ this.tupleGenerator.getGenTyp().name() +">stopped ...");
			super.logger.info(e.getMessage());
			
			return false;
		}
		
		if (tuple != null){
			super.sendTupleToClients(tuple);
		}
		
		try {
			//wait for next update
			Thread.sleep(this.frequency);
			
			//accelerate
			this.frequency *= (1 / this.acceleration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

}
