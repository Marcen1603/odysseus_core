package de.uniol.inf.is.odysseus.action.dataSources.generator;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class TupleGenerator {
	private SDFAttributeList schema;
	private GeneratorType genTyp;
	private GeneratorConfig config;
	
	public enum GeneratorType{Factory, Machine, Install_Pure, Install_DB, Usage};

	public TupleGenerator(GeneratorConfig config, GeneratorType type) throws GeneratorException{
		this.schema = new SDFAttributeList();
		this.genTyp = type;
		this.config = config;
				
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
			
			attribute = new SDFAttribute("Limit1");
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			this.schema.add(attribute);
			
			attribute = new SDFAttribute("Limit2");
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
	
	public RelationalTuple<IMetaAttribute> generateTuple(){
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
		// TODO Auto-generated method stub
		return null;
	}

	private RelationalTuple<IMetaAttribute> generateInstallPureTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	private RelationalTuple<IMetaAttribute> generateMachineTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	private RelationalTuple<IMetaAttribute> generateInstallDBTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	private RelationalTuple<IMetaAttribute> generateFactoryTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	public SDFAttributeList getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
