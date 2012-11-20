package de.offis.scaiconnector.factory;

import java.util.List;
import java.util.Map;

import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;

/**
 * Wrapper for SCAI Create-Operations.
 *
 * @author Alexander Funk
 * 
 */
public final class CreateScaiCmds {
	public BuilderSCAI20 ConfigurationParameterAtomic(BuilderSCAI20 builder, String name, Boolean optional, String uom, Boolean identifier, String dataTypeName){            
        String operationID = "createConfigurationParameterAtomic - ScaiFactory";
    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
        builder.addCreateConfigurationParameterAtomicParameter(name, optional, uom, identifier, dataType, operationID);
        return builder;
    }

    public BuilderSCAI20 ConfigurationParameterComplex(BuilderSCAI20 builder, String name, Boolean optional, List<String> configurationParameterNames){
    	String operationID = "createConfigurationParameterComplex - ScaiFactory";
    	SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);    	
        builder.addCreateConfigurationParameterComplexParameter(name, optional, configurationParameters, operationID);
        return builder;
    }
    
    public BuilderSCAI20 ConfigurationParameterSequence(BuilderSCAI20 builder, String name, Boolean optional, Integer min, Integer max, String configurationParameterName){
    	String operationID = "createConfigurationParameterSequence - ScaiFactory";
    	SCAIReference configurationParameter = new SCAIReference(configurationParameterName, false);        
        builder.addCreateConfigurationParameterSequenceParameter(name, optional, min, max, configurationParameter, operationID);
        return builder;
    }
    
    public BuilderSCAI20 DataElementAtomic(BuilderSCAI20 builder, String name, String dataTypeName){
    	String operationID = "craeteDataElementAtomic - ScaiFactory";
    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
    	builder.addCreateDataElementAtomicElement(name, dataType, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataElementComplex(BuilderSCAI20 builder, String name, List<String> dataElementNames){
    	String operationID = "craeteDataElementComplex - ScaiFactory";
    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
    	builder.addCreateDataElementComplexElement(name, dataElements, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataStreamType(BuilderSCAI20 builder, String name, List<String> dataElementNames){
    	String operationID = "createDataStreamType - ScaiFactory";
    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
        builder.addCreateDataStreamType(name, dataElements, operationID);
        return builder;
    }
    
    public BuilderSCAI20 DataTypeString(BuilderSCAI20 builder, String name, Long min, Long max, String regex, String defaultvalue){
    	String operationID = "createDataTypeString - ScaiFactory";
    	builder.addCreateDataTypeStringType(name, min, max, regex, defaultvalue, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataTypeBinary(BuilderSCAI20 builder, String name, Long min, Long max, String defaultvalue){
    	String operationID = "createDataTypeBinary - ScaiFactory";
    	builder.addCreateDataTypeBinaryType(name, min, max, defaultvalue.getBytes(), operationID);
    	return builder;
    }
    
    public BuilderSCAI20 DataTypeDecimal(BuilderSCAI20 builder, String name, Float min, Float max, Float scale, Float defaultvalue){
    	String operationID = "createDataTypeDecimal - ScaiFactory";
    	builder.addCreateDataTypeDecimalType(name, min, max, scale, defaultvalue, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 OperatorGroup(BuilderSCAI20 builder, String name){
    	String operationID = "createOperatorGroup - ScaiFactory";
    	builder.addCreateOperatorGroup(name, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 OperatorType(BuilderSCAI20 builder, String name, String metaType, Map<String, String> props, Map<String, Boolean> readOnly, String desc){
    	String operationID = "createOperatorType - ScaiFactory";
    	builder.addCreateOperatorType(name, metaType, props, readOnly, desc, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 Sensor(BuilderSCAI20 builder, String name, String sensorDomainName, String sensorTypeName, boolean virtual){
    	String operationID = "createSensor - ScaiFactory";
    	SCAIReference sensorDomain = new SCAIReference(sensorDomainName, false);
        SCAIReference sensorType = new SCAIReference(sensorTypeName, false);
        builder.addCreateSensor(name, sensorDomain, sensorType, virtual, operationID);         
    	return builder;
    }
    
    public BuilderSCAI20 SensorCategory(BuilderSCAI20 builder, String name, String parentSensorReferenceName, List<String> sensorDomainNames){
    	String operationID = "createSensorCategory - ScaiFactory";
    	SCAIReference parentSensorReference = new SCAIReference(parentSensorReferenceName, false);
        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
        builder.addCreateSensorCategory(name, parentSensorReference, sensorDomains, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 SensorDomain(BuilderSCAI20 builder, String name){
    	String operationID = "createSensorDomain - ScaiFactory";
    	builder.addCreateSensorDomain(name, operationID);
    	return builder;
    }
    
    public BuilderSCAI20 SensorType(BuilderSCAI20 builder, String name, String adapter, String dataStreamTypeName, List<String> configurationParameterNames, List<String> sensorCategoryNames, List<String> sensorDomainNames){
    	String operationID = "createSensorType - ScaiFactory";
    	SCAIReference dataStreamType = new SCAIReference(dataStreamTypeName, false);
        SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);
        SCAIReference[] sensorCategories = ScaiUtil.convertToSCAIReferences(sensorCategoryNames);
        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
        builder.addCreateSensorType(name, adapter, dataStreamType, configurationParameters, sensorCategories,sensorDomains, operationID);
    	return builder;
    }
}
