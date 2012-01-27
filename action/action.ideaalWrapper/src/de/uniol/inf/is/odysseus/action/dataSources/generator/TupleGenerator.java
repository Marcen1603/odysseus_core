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

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class TupleGenerator {
	private SDFAttributeList schema;
	private GeneratorType genTyp;
	private GeneratorConfig config;
	
	private ScenarioDatamodel datamodel;
	
		
	private static int factoryNo = 0;
	private static int machineNo = 0;
	
	public enum GeneratorType{Factory, Machine, Install_Pure, Install_DB, Usage};

	public TupleGenerator(GeneratorConfig config, GeneratorType type) throws GeneratorException{
		this.schema = new SDFAttributeList(type.name());
		this.genTyp = type;
		this.config = config;
		
		this.datamodel = ScenarioDatamodel.getInstance();
				
		//generate schema
		
		//timestamp attribute for all types
		SDFAttribute attribute = new SDFAttribute(null,"timestamp", SDFDatatype.LONG);
		this.schema.add(attribute);
		
		switch(type){
		case Factory:
			attribute = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"name", SDFDatatype.STRING);
			this.schema.add(attribute);
			
			break;
			
		case Machine:
			attribute = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"factoryId", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"name", SDFDatatype.STRING);
			this.schema.add(attribute);
			
			break;
			
		case Install_Pure:
			attribute = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"machineId", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"limit1", SDFDatatype.DOUBLE);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"limit2", SDFDatatype.DOUBLE);
			this.schema.add(attribute);
			
			break;
			
		case Install_DB:
			attribute = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"machineId", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"limit1", SDFDatatype.DOUBLE);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"limit2", SDFDatatype.DOUBLE);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"pastUsageTime", SDFDatatype.DOUBLE);
			this.schema.add(attribute);
			
			break;
			
		case Usage:
			attribute = new SDFAttribute(null,"machineId", SDFDatatype.INTEGER);
			this.schema.add(attribute);
			
			attribute = new SDFAttribute(null,"rate", SDFDatatype.DOUBLE);
			this.schema.add(attribute);
			
			break;
		default:
			throw new GeneratorException("Unknown tuple type");
		}
		
	}
	
	public RelationalTuple<IMetaAttribute> generateTuple() throws GeneratorException {
		this.datamodel.releaseResources();
		
		switch(this.genTyp){
		case Factory:
			return this.generateFactoryTuple();
		case Install_DB:
			return this.generateInstallDBTuple();
		case Install_Pure:
			return this.generateInstallPureTuple();
		case Machine:
			return this.generateMachineTuple();
		case Usage:
			return this.generateUsageTuple();
		default:
			return null;
		}
	}

	private RelationalTuple<IMetaAttribute> generateUsageTuple() throws GeneratorException {
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());	
		
		//timestamp, machineID, rate
		tuple.setAttribute(0, System.currentTimeMillis());
		
		Integer machineNo = this.datamodel.getOccupiedMachine();
		
		if (machineNo == null){
			//no machine has a tool installed
			return null;
		}
		tuple.setAttribute(1, machineNo);
		
		Double usageRate = this.datamodel.useTool(machineNo);
		
		if (usageRate == null){
			return null;
		}else{
			tuple.setAttribute(2, usageRate);
		}
		
		return tuple;
	}

	private RelationalTuple<IMetaAttribute> generateInstallPureTuple() throws GeneratorException {
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());		
		//timestamp, id, machineID, limit1, limit2
		tuple.setAttribute(0, System.currentTimeMillis());
		
		Integer machineNo = this.datamodel.getFreeMachine();
		if (machineNo == null){
			//no free machine avaiable, do not install
			return null;
		}	
		tuple.setAttribute(2, machineNo);
		
		Tool tool = this.datamodel.installFreeTool(machineNo);
		if (tool == null){
			return null;
		}
		tuple.setAttribute(1, tool.getId());
		
		
		tuple.setAttribute(3, tool.getLimit1());
		tuple.setAttribute(4,tool.getLimit2());
		
		return tuple;
	}

	private RelationalTuple<IMetaAttribute> generateMachineTuple() throws GeneratorException {
		//stop condition
		if (machineNo >= this.config.getNumberOfMachines()){
			throw new GeneratorException("No more machines to generate");
		}
		
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());

		//timestamp, id, factoryID, name
		tuple.setAttribute(0, System.currentTimeMillis());
		tuple.setAttribute(1, machineNo);
		
		Integer factoryID = this.datamodel.associateMachineToFactory(this.config.getMinNumberOfMachinesPerBuilding());
		if (factoryID == null){
			//no factory created yet, skip
			return null;
		}
		
		tuple.setAttribute(2, factoryID);
		tuple.setAttribute(3, "Machine"+machineNo);
		
		this.datamodel.addMachine(machineNo);
		machineNo++;
		
		return tuple;
	}

	private RelationalTuple<IMetaAttribute> generateInstallDBTuple() throws GeneratorException {
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		
		//timestamp, id, machineID, limit1, limit2, pastUsageTime
		tuple.setAttribute(0, System.currentTimeMillis());
		
		Integer machineNo = this.datamodel.getFreeMachine();
		if (machineNo == null){
			//no free machine avaiable, do not install
			return null;
		}	
		tuple.setAttribute(2, machineNo);
		
		Tool tool = this.datamodel.installFreeTool(machineNo);
		if (tool == null){
			return null;
		}
		tuple.setAttribute(1, tool.getId());
		
		tuple.setAttribute(3, tool.getLimit1());
		tuple.setAttribute(4,tool.getLimit2());
		tuple.setAttribute(5, tool.getUsageRate());
		
		return tuple;
	}

	private RelationalTuple<IMetaAttribute> generateFactoryTuple() throws GeneratorException  {
		//stop condition
		if (factoryNo >= this.config.getNumberOfBuildings()){
			throw new GeneratorException("No more factories to generate");
		}
		
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		
		//timestamp, id, name
		tuple.setAttribute(0, System.currentTimeMillis());
		tuple.setAttribute(1, factoryNo);
		tuple.setAttribute(2, "Factory"+factoryNo);
		
		this.datamodel.addFactory(factoryNo);
		factoryNo++;
		
		return tuple;
	}

	public SDFAttributeList getSchema() {
		return this.schema;
	}
	
	public GeneratorType getGenTyp() {
		return genTyp;
	}
}
