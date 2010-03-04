package de.uniol.inf.is.odysseus.action.dataSources.generator;

import de.uniol.inf.is.odysseus.action.benchmark.BenchmarkData;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class TupleGenerator {
	private SDFAttributeList schema;
	private GeneratorType genTyp;
	private GeneratorConfig config;
	
	private ScenarioDatamodel datamodel;
	
	private static int factoryNo = 0;
	private static int machineNo = 0;
	
	public enum GeneratorType{Factory, Machine, Install_Pure, Install_DB, Usage};

	public TupleGenerator(GeneratorConfig config, GeneratorType type) throws GeneratorException{
		this.schema = new SDFAttributeList();
		this.genTyp = type;
		this.config = config;
		
		this.datamodel = ScenarioDatamodel.getInstance();
				
		//generate schema
		
		//timestamp attribute for all types
		SDFAttribute attribute = new SDFAttribute("timestamp");
		attribute.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		this.schema.add(attribute);
		
		switch(type){
		case Factory:
			attribute = new SDFAttribute("ID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("name");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("String"));
			this.schema.add(attribute);
			
			break;
			
		case Machine:
			attribute = new SDFAttribute("ID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("FactoryID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("name");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("String"));
			this.schema.add(attribute);
			
			break;
			
		case Install_Pure:
			attribute = new SDFAttribute("ID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("MachineID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("Limit1");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("Limit2");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			break;
			
		case Install_DB:
			attribute = new SDFAttribute("ID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("MachineID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("Limit1");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("Limit2");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("PastUsageTime");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			break;
			
		case Usage:
			attribute = new SDFAttribute("MachineID");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("Rate");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			break;
		default:
			throw new GeneratorException("Unknown tuple type");
		}
		
	}
	
	public RelationalTuple<IMetaAttribute> generateTuple() {
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

	private RelationalTuple<IMetaAttribute> generateUsageTuple() {
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		return null;
	}

	private RelationalTuple<IMetaAttribute> generateInstallPureTuple() {
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		return null;
	}

	private RelationalTuple<IMetaAttribute> generateMachineTuple() {
		//stop condition
		if (machineNo >= this.config.getNumberOfMachines()){
			return null;
		}
		
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		BenchmarkData data = new BenchmarkData("MachineMaintenance_Machine");
		tuple.setMetadata(data);
		
		//id, factoryID, name
		tuple.setAttribute(0, System.currentTimeMillis());
		tuple.setAttribute(1, machineNo);
		tuple.setAttribute(2, this.datamodel.associateMachineToFactory(this.config.getMinNumberOfMachinesPerBuilding()));
		tuple.setAttribute(3, "Machine"+machineNo);
		
		this.datamodel.addMachine(machineNo);
		machineNo++;
		
		return tuple;
	}

	private RelationalTuple<IMetaAttribute> generateInstallDBTuple() {
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		BenchmarkData data = new BenchmarkData("MachineMaintenance_InstallDB");
		tuple.setMetadata(data);
		
		//id, machineID, limit1, limit2, pastUsageTime
		tuple.setAttribute(0, System.currentTimeMillis());
		tuple.setAttribute(1, this.datamodel.getFreeMachine());
	
		return tuple;
	}

	private RelationalTuple<IMetaAttribute> generateFactoryTuple()  {
		//stop condition
		if (factoryNo >= this.config.getNumberOfBuildings()){
			return null;
		}
		
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(this.schema.size());
		BenchmarkData data = new BenchmarkData("MachineMaintenance_Factory");
		tuple.setMetadata(data);
		
		//id, name
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
}
