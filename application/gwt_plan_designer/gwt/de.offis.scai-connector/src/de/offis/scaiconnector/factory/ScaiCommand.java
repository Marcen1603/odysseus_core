package de.offis.scaiconnector.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;

import de.offis.client.AbstractScaiDTO.Type;
import de.offis.client.ConfigurationParameter;
import de.offis.client.DataElement;
import de.offis.client.DataStreamType;
import de.offis.client.DataType;
import de.offis.client.Operator;
import de.offis.client.OperatorGroup;
import de.offis.client.OperatorType;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorCategory;
import de.offis.client.SensorDomain;
import de.offis.client.SensorType;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;
import de.offis.xml.schema.scai20.ConfigurationParameterDescription;
import de.offis.xml.schema.scai20.ConfigurationParameterDescription.AtomicParameter;
import de.offis.xml.schema.scai20.ConfigurationParameterDescription.ComplexParameter;
import de.offis.xml.schema.scai20.ConfigurationParameterDescription.SequenceParameter;
import de.offis.xml.schema.scai20.DataElementDescription;
import de.offis.xml.schema.scai20.DataElementDescription.AtomicElement;
import de.offis.xml.schema.scai20.DataElementDescription.ComplexElement;
import de.offis.xml.schema.scai20.DataStreamTypeDescription;
import de.offis.xml.schema.scai20.DataTypeDescription;
import de.offis.xml.schema.scai20.DataTypeDescription.BinaryType;
import de.offis.xml.schema.scai20.DataTypeDescription.DecimalType;
import de.offis.xml.schema.scai20.DataTypeDescription.StringType;
import de.offis.xml.schema.scai20.OperatorDescription;
import de.offis.xml.schema.scai20.OperatorDescription.Property;
import de.offis.xml.schema.scai20.OperatorGroupDescription;
import de.offis.xml.schema.scai20.OperatorLinkDescription;
import de.offis.xml.schema.scai20.OperatorLinkDescription.Destination;
import de.offis.xml.schema.scai20.OperatorLinkDescription.Source;
import de.offis.xml.schema.scai20.OperatorTypeDescription;
import de.offis.xml.schema.scai20.Reference;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment.Exception;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment.Reply;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment.Success;
import de.offis.xml.schema.scai20.SensorCategoryDescription;
import de.offis.xml.schema.scai20.SensorDescription;
import de.offis.xml.schema.scai20.SensorDomainDescription;
import de.offis.xml.schema.scai20.SensorTypeDescription;

/**
 * Class to hold operations, build up XML request and to hold the response.
 *
 * @author Alexander Funk
 * 
 */
public class ScaiCommand {
	
	public class Utility {
		public List<ConfigurationParameter> extractConfigurationParameter(Reply reply){
			List<ConfigurationParameter> list = new ArrayList<ConfigurationParameter>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            ConfigurationParameterDescription desc = (ConfigurationParameterDescription) o;

	            if (desc.isSetAtomicParameter()) {
	                AtomicParameter type = desc.getAtomicParameter();                
	                list.add(new ConfigurationParameter(desc.getName(), desc.getOptional(), type.getUom(), type.getIdentifier(), type.getDataType().getName()));
	            } else if (desc.isSetComplexParameter()) {
	                ComplexParameter type = desc.getComplexParameter();
	                ArrayList<String> config = new ArrayList<String>();
	                for (Reference r : type.getConfigurationParameterArray()) {
	                    if (r.isSetName()) {
	                        config.add(r.getName());
	                    }
	                }                
	                list.add(new ConfigurationParameter(desc.getName(), desc.getOptional(), config));
	            } else if (desc.isSetSequenceParameter()) {
	                SequenceParameter type = desc.getSequenceParameter();                
	                list.add(new ConfigurationParameter(desc.getName(), desc.getOptional(), type.getMinlength(), type.getMaxlength(), type.getConfigurationParameter().getName()));
	            }
	        }

	        return list;
	    }
	    
	    public List<DataElement> extractDataElement(Reply reply){
	    	List<DataElement> list = new ArrayList<DataElement>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            DataElementDescription desc = (DataElementDescription) o;

	            if (desc.isSetAtomicElement()) {
	                AtomicElement type = desc.getAtomicElement();
	                list.add(new DataElement(type.getName(), type.getDataType().isSetName() ? type.getDataType().getName() : null ));
	            } else if (desc.isSetComplexElement()) {
	                ComplexElement type = desc.getComplexElement();
	                ArrayList<String> dataElements = new ArrayList<String>();
	                for (Reference r : type.getDataElementArray()) {
	                    if(r.isSetName()){
	                        dataElements.add(r.getName());
	                    }
	                }
	                list.add(new DataElement(type.getName(), dataElements));
	            }
	        }

	        return list;
	    }
	    
	    public List<DataStreamType> extractDataStreamType(Reply replyDataStreamTypes, Reply replyDataElements){
	    	List<DataStreamType> list = new ArrayList<DataStreamType>();

	        if(replyDataStreamTypes == null || replyDataElements == null){
	        	return list;
	        }
	        
	        for (XmlObject o : replyDataStreamTypes.getDataArray()) {
	            DataStreamTypeDescription desc = (DataStreamTypeDescription) o;
	            ArrayList<String> dataElements = new ArrayList<String>();
	            for (Reference r : desc.getDataElementArray()) {
	                if (r.isSetName()) {
	                    dataElements.add(r.getName());
	                }
	            }
	            
	            ArrayList<String> dataElementsFlat = new ArrayList<String>();
	            
	            Map<String, DataElement> dataElementsMap = new HashMap<String, DataElement>();
	            
	            for(DataElement de : extractDataElement(replyDataElements)){        	
	            	dataElementsMap.put(de.getName(), de);
	            }
	            
	            // komplexe datentypen flach klopfen --> ../../..
	            for(String str : dataElements){
	            	if(dataElementsMap.containsKey(str)){
	            		DataElement elem = dataElementsMap.get(str);
	            		getDataElementsRecHelper(dataElementsFlat, elem, dataElementsMap, "");
	            	}
	            }
	            list.add(new DataStreamType(desc.getName(), dataElementsFlat));
	        }

	        return list;
	    }
	    
	    private void getDataElementsRecHelper(List<String> dataElems, DataElement elem, Map<String, DataElement> dataElementsMap, String dir){
	    	if(elem.getType().equals(Type.ATOMIC)){
	    		dataElems.add(dir + elem.getName());
	    		return;
	    	} else if (elem.getType().equals(Type.COMPLEX)){
	    		dir += elem.getName() + "/";
	    		for(String de : elem.getDataElementNamesC()){
	    			DataElement el = dataElementsMap.get(de);
	    			getDataElementsRecHelper(dataElems, el, dataElementsMap, dir);
	    		}
	    	}
	    }
	    
	    public List<DataType> extractDataType(Reply reply){
	    	List<DataType> list = new ArrayList<DataType>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            DataTypeDescription desc = (DataTypeDescription) o;

	            if (desc.isSetDecimalType()) {
	                DecimalType type = desc.getDecimalType();
	                list.add(new DataType(desc.getName(), 
	                				type.isSetMin() ? type.getMin() : null, 
	                						type.isSetMax() ? type.getMax() : null, 
	                								type.isSetScale() ? type.getScale() : null, type.getDefaultvalue()));
	            } else if (desc.isSetBinaryType()) {
	                BinaryType type = desc.getBinaryType();
	                list.add(new DataType(desc.getName(), type.getDefaultvalue() + ""));
	            } else if (desc.isSetStringType()) {
	                StringType type = desc.getStringType();
	                list.add(new DataType(desc.getName(), 
	        				type.isSetMin() ? (int)type.getMin() : null, 
	        						type.isSetMax() ? (int)type.getMax() : null, 
	        								type.isSetRegex() ? type.getRegex() : null, type.getDefaultvalue()));
	            }

	            
	        }

	        return list;
	    }
	    
	    public List<ScaiLink> extractLinks(Reply reply){
	    	List<ScaiLink> list = new ArrayList<ScaiLink>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            OperatorLinkDescription link = (OperatorLinkDescription) o;

	            Destination dest = link.getDestination();
	            Source source = link.getSource();
	            
	            String destName = null;
	            String sourceName = null;
	            
	            if(dest.isSetOutputOperator() && dest.getOutputOperator().isSetName()){
	            	destName = dest.getOutputOperator().getName();            	
	            } else if (dest.isSetServiceOperator() && dest.getServiceOperator().isSetName()){
	            	destName = dest.getServiceOperator().getName();
	            }
	            
	            if(source.isSetInputOperator() && source.getInputOperator().isSetName()){
	            	sourceName = source.getInputOperator().getName();
	            }  else if (source.isSetServiceOperator() && source.getServiceOperator().isSetName()){
	            	sourceName = source.getServiceOperator().getName();
	            }
	            
	            if(destName != null && sourceName != null){
	                list.add(new ScaiLink(sourceName, destName));
	            } 
	        }

	        return list;
	    }
	        
	    public List<Operator> extractOperators(Reply reply){
	    	List<Operator> list = new ArrayList<Operator>();
	        
	        if(reply == null){
	        	return list;
	        }

	        for (XmlObject o : reply.getDataArray()) {
	            OperatorDescription op = (OperatorDescription) o;
	            
	            String name = op.getName();
	            Map<String, String> properties = new HashMap<String, String>();
	            for(Property p : op.getPropertyArray()){
	            	properties.put(p.getKey(), p.getValue());
	            }
	            
	            if(op.isSetInputOperator() && op.getInputOperator().getSensor().isSetNameReference()){            	
	            	String sensorName = op.getInputOperator().getSensor().getNameReference().getSensorName();
	                String sensorDomain = op.getInputOperator().getSensor().getNameReference().getSensorDomainName();
	                list.add(new Operator(name, sensorName, sensorDomain, properties, Operator.INPUT));
	            } else if(op.isSetOutputOperator() && op.getOutputOperator().getSensor().isSetNameReference()){
	            	String sensorName = op.getOutputOperator().getSensor().getNameReference().getSensorName();
	            	String sensorDomain = op.getOutputOperator().getSensor().getNameReference().getSensorDomainName();
	                list.add(new Operator(name, sensorName, sensorDomain, properties, Operator.OUTPUT));
	            } else if(op.isSetServiceOperator()){
	            	String serviceType = op.getServiceOperator().getOperatorType().getName();
	            	list.add(new Operator(name, serviceType, properties)); // serviceType = JumpingTimeWindow
	            }
	        }

	        return list;
	    }
	    
	    public List<OperatorGroup> extractOperatorGroup(Reply reply){
			List<OperatorGroup> list = new ArrayList<OperatorGroup>();
			
	        
	        	for (XmlObject o : reply.getDataArray()) {
	                OperatorGroupDescription desc = (OperatorGroupDescription) o;
	                
	                // pruefe ob die operator group grade laeuft ...
	                boolean running = false;              
	                running = factory.Get.getOperatorGroupStatus(desc.getName());
	                
	                list.add(new OperatorGroup(desc.getName(), running));
	            }
	        

	        return list;
	    }
	    
	    public List<OperatorType> extractOperatorType(Reply reply){
	    	List<OperatorType> list = new ArrayList<OperatorType>();
	        
	        if(reply == null){
	        	return list;
	        }

	        for (XmlObject o : reply.getDataArray()) {
	            OperatorTypeDescription desc = (OperatorTypeDescription) o;

	            Map<String, String> props = new HashMap<String, String>();
	            for (OperatorTypeDescription.Property p : desc.getPropertyArray()) {
	                props.put(p.getKey(), p.getValue());
	            }

	            Map<String, Boolean> readOnly = new HashMap<String, Boolean>();
	            for (OperatorTypeDescription.Property p : desc.getPropertyArray()) {
	                readOnly.put(p.getKey(), p.getReadOnly());
	            }
	            
	            if (!props.containsKey("inputSlots")) {
	                props.put("inputSlots", "1");
	            }

	            if (!props.containsKey("outputSlots")) {
	            	props.put("outputSlots", "1");
	            }

	            list.add(new OperatorType(desc.getName(), desc.getMetaType(), props, readOnly, desc.isSetDescription()? desc.getDescription() : ""));
	        }
	        
	        return list;
	    }
	    
	    public List<Sensor> extractSensor(Reply reply){
	    	List<Sensor> list = new ArrayList<Sensor>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            SensorDescription desc = (SensorDescription) o;
	            
	            list.add(
	            		new Sensor(desc.getName(), 
	            				desc.getSensorDomain().isSetName() ? desc.getSensorDomain().getName() : null, 
	            						desc.getSensorType().isSetName() ? desc.getSensorType().getName() : null, 
	            						desc.getVirtual()));
	        }

	        return list;
	    }
	    
	    public List<SensorCategory> extractSensorCategory(Reply reply){
	    	List<SensorCategory> list = new ArrayList<SensorCategory>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            SensorCategoryDescription desc = (SensorCategoryDescription) o;

	            String parentSensorReferenceName = null;
	            if (desc.isSetParentSensorCategory()) {
	                if (desc.getParentSensorCategory().isSetName()) {
	                	parentSensorReferenceName = desc.getParentSensorCategory().getName();
	                }
	            }

	            ArrayList<String> sensorCategoryDomains = new ArrayList<String>();
	            if (desc.getSensorDomainArray() != null) {
	                for (Reference r : desc.getSensorDomainArray()) {
	                    if(r.isSetName()){
	                        sensorCategoryDomains.add(r.getName());
	                    }
	                }
	            }

	            list.add(new SensorCategory(desc.getName(), parentSensorReferenceName, sensorCategoryDomains));
	        }

	        return list;
	    }
	    
	    public List<SensorDomain> extractSensorDomain(Reply reply){
	    	List<SensorDomain> list = new ArrayList<SensorDomain>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            SensorDomainDescription desc = (SensorDomainDescription) o;
	            list.add(new SensorDomain(desc.getName()));
	        }

	        return list;
	    }
	    
	    public List<SensorType> extractSensorType(Reply reply){
	    	List<SensorType> list = new ArrayList<SensorType>();

	        if(reply == null){
	        	return list;
	        }
	        
	        for (XmlObject o : reply.getDataArray()) {
	            SensorTypeDescription desc = (SensorTypeDescription) o;

	            String dataStreamTypeName = null;
	            if(desc.getDataStreamType().isSetName()){
	            	dataStreamTypeName = desc.getDataStreamType().getName();
	            }

	            ArrayList<String> configs = new ArrayList<String>();
	            for (Reference r : desc.getConfigurationParameterArray()) {
	                if(r.isSetName()){
	                   configs.add(r.getName());
	                }
	            }

	            ArrayList<String> sensorCategories = new ArrayList<String>();
	            for (Reference r : desc.getSensorCategoryArray()) {
	                if(r.isSetName()){
	                   sensorCategories.add(r.getName());
	                }
	            }

	            ArrayList<String> sensorDomains = new ArrayList<String>();
	            for (Reference r : desc.getSensorDomainArray()) {
	                if(r.isSetName()){
	                   sensorDomains.add(r.getName());
	                }
	            }

	            list.add(new SensorType(desc.getName(), desc.getAdapter(), dataStreamTypeName, configs, sensorCategories, sensorDomains));
	        }

	        return list;
	    }
	}
	
	public class Builder {
		public Builder deployOperatorGroup(String operatorGroup, String operationId){			
			cmd.addDeployOperatorGroup(new SCAIReference(operatorGroup, false), null, operationId);
			return this;
		}
		
		public Builder undeployOperatorGroup(String operatorGroup, String operationId){			
			cmd.addUndeployOperatorGroup(new SCAIReference(operatorGroup, false), operationId);
			return this;
		}
		
		public Builder addUpdateConfigurationParameterAtomicParameter(String oldName, String name, Boolean optional, String uom, Boolean identifier, String dataTypeName, String operationId){            
			SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
	        cmd.addUpdateConfigurationParameterAtomicParameter(reference, name, optional, uom, identifier, dataType, operationId);
	        return this;
	    }

	    public Builder addUpdateConfigurationParameterComplexParameter(String oldName, String name, Boolean optional, List<String> configurationParameterNames, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);    	
	    	cmd.addUpdateConfigurationParameterComplexParameter(reference, name, optional, configurationParameters, operationId);
	        return this;
	    }
	    
	    public Builder addUpdateConfigurationParameterSequenceParameter(String oldName, String name, Boolean optional, Integer min, Integer max, String configurationParameterName, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference configurationParameter = new SCAIReference(configurationParameterName, false);        
	    	cmd.addUpdateConfigurationParameterSequenceParameter(reference, name, optional, min, max, configurationParameter, operationId);
	        return this;
	    }
	    
	    public Builder addUpdateDataElementAtomicElement(String oldName, String name, String dataTypeName, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
	    	cmd.addUpdateDataElementAtomicElement(reference, name, dataType, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateDataElementComplexElement(String oldName, String name, List<String> dataElementNames, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
	    	cmd.addUpdateDataElementComplexElement(reference, name, dataElements, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateDataStreamType(String oldName, String name, List<String> dataElementNames, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
	    	cmd.addUpdateDataStreamType(reference, name, dataElements, operationId);
	        return this;
	    }
	    
	    public Builder addUpdateDataTypeStringType(String oldName, String name, Long min, Long max, String regex, String defaultvalue, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	cmd.addUpdateDataTypeStringType(reference, name, min, max, regex, defaultvalue, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateDataTypeBinaryType(String oldName, String name, Long min, Long max, String defaultvalue, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	cmd.addUpdateDataTypeBinaryType(reference, name, min, max, defaultvalue.getBytes(), operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateDataTypeDecimalType(String oldName, String name, Float min, Float max, Float scale, Float defaultvalue, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	cmd.addUpdateDataTypeDecimalType(reference, name, min, max, scale, defaultvalue, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateOperatorGroup(String oldName, String name, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	cmd.addUpdateOperatorGroup(reference, name, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateOperatorType(String oldName, String name, String metaType, Map<String, String> props, Map<String, Boolean> readOnly, String desc, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	cmd.addUpdateOperatorType(reference, name, metaType, props, readOnly, desc, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateSensor(String oldName, String oldDomain, String name, String sensorDomainName, String sensorTypeName, boolean virtual, String operationId){
	    	SCAISensorReference reference = new SCAISensorReference(oldName, oldDomain);
	    	SCAIReference sensorDomain = new SCAIReference(sensorDomainName, false);
	        SCAIReference sensorType = new SCAIReference(sensorTypeName, false);
	        cmd.addUpdateSensor(reference, name, sensorDomain, sensorType, virtual, operationId);         
	    	return this;
	    }
	    
	    public Builder addUpdateSensorCategory(String oldName, String name, String parentSensorReferenceName, List<String> sensorDomainNames, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	SCAIReference parentSensorReference = new SCAIReference(parentSensorReferenceName, false);
	        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
	        cmd.addUpdateSensorCategory(reference, name, parentSensorReference, sensorDomains, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateSensorDomain(String oldName, String name, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);
	    	cmd.addUpdateSensorDomain(reference, name, operationId);
	    	return this;
	    }
	    
	    public Builder addUpdateSensorType(String oldName, String name, String adapter, String dataStreamTypeName, List<String> configurationParameterNames, List<String> sensorCategoryNames, List<String> sensorDomainNames, String operationId){
	    	SCAIReference reference = new SCAIReference(oldName, false);	    	
	    	SCAIReference dataStreamType = new SCAIReference(dataStreamTypeName, false);
	        SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);
	        SCAIReference[] sensorCategories = ScaiUtil.convertToSCAIReferences(sensorCategoryNames);
	        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
	        cmd.addUpdateSensorType(reference, name, adapter, dataStreamType, configurationParameters, sensorCategories,sensorDomains, operationId);
	    	return this;
	    }	
		
		public Builder addCreateConfigurationParameterAtomicParameter(String name, Boolean optional, String uom, Boolean identifier, String dataTypeName, String operationId){
	    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
	    	cmd.addCreateConfigurationParameterAtomicParameter(name, optional, uom, identifier, dataType, operationId);
	        return this;
	    }

	    public Builder addCreateConfigurationParameterComplexParameter(String name, Boolean optional, List<String> configurationParameterNames, String operationId){
	    	SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);    	
	    	cmd.addCreateConfigurationParameterComplexParameter(name, optional, configurationParameters, operationId);
	        return this;
	    }
	    
	    public Builder addCreateConfigurationParameterSequenceParameter(String name, Boolean optional, Integer min, Integer max, String configurationParameterName, String operationId){
	    	SCAIReference configurationParameter = new SCAIReference(configurationParameterName, false);        
	    	cmd.addCreateConfigurationParameterSequenceParameter(name, optional, min, max, configurationParameter, operationId);
	        return this;
	    }
	    
	    public Builder addCreateDataElementAtomicElement(String name, String dataTypeName, String operationId){
	    	SCAIReference dataType = new SCAIReference(dataTypeName, false);
	    	cmd.addCreateDataElementAtomicElement(name, dataType, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateDataElementComplexElement(String name, List<String> dataElementNames, String operationId){
	    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
	    	cmd.addCreateDataElementComplexElement(name, dataElements, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateDataStreamType(String name, List<String> dataElementNames, String operationId){
	    	SCAIReference[] dataElements = ScaiUtil.convertToSCAIReferences(dataElementNames);
	    	cmd.addCreateDataStreamType(name, dataElements, operationId);
	        return this;
	    }
	    
	    public Builder addCreateDataTypeStringType(String name, Long min, Long max, String regex, String defaultvalue, String operationId){
	    	cmd.addCreateDataTypeStringType(name, min, max, regex, defaultvalue, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateDataTypeBinaryType(String name, Long min, Long max, String defaultvalue, String operationId){
	    	cmd.addCreateDataTypeBinaryType(name, min, max, defaultvalue.getBytes(), operationId);
	    	return this;
	    }
	    
	    public Builder addCreateDataTypeDecimalType(String name, Float min, Float max, Float scale, Float defaultvalue, String operationId){
	    	cmd.addCreateDataTypeDecimalType(name, min, max, scale, defaultvalue, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateOperatorGroup(String name, String operationId){
	    	cmd.addCreateOperatorGroup(name, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateOperatorType(String name, String metaType, Map<String, String> props, Map<String, Boolean> readOnly, String desc, String operationId){
	    	cmd.addCreateOperatorType(name, metaType, props, readOnly, desc, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateSensor(String name, String sensorDomainName, String sensorTypeName, boolean virtual, String operationId){
	    	SCAIReference sensorDomain = new SCAIReference(sensorDomainName, false);
	        SCAIReference sensorType = new SCAIReference(sensorTypeName, false);
	        cmd.addCreateSensor(name, sensorDomain, sensorType, virtual, operationId);         
	    	return this;
	    }
	    
	    public Builder addCreateSensorCategory(String name, String parentSensorReferenceName, List<String> sensorDomainNames, String operationId){
	    	SCAIReference parentSensorReference = new SCAIReference(parentSensorReferenceName, false);
	        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
	        cmd.addCreateSensorCategory(name, parentSensorReference, sensorDomains, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateSensorDomain(String name, String operationId){
	    	cmd.addCreateSensorDomain(name, operationId);
	    	return this;
	    }
	    
	    public Builder addCreateSensorType(String name, String adapter, String dataStreamTypeName, List<String> configurationParameterNames, List<String> sensorCategoryNames, List<String> sensorDomainNames, String operationId){
	    	SCAIReference dataStreamType = new SCAIReference(dataStreamTypeName, false);
	        SCAIReference[] configurationParameters = ScaiUtil.convertToSCAIReferences(configurationParameterNames);
	        SCAIReference[] sensorCategories = ScaiUtil.convertToSCAIReferences(sensorCategoryNames);
	        SCAIReference[] sensorDomains = ScaiUtil.convertToSCAIReferences(sensorDomainNames);
	        cmd.addCreateSensorType(name, adapter, dataStreamType, configurationParameters, sensorCategories,sensorDomains, operationId);
	    	return this;
	    }	    
	    
		public Builder addRemoveConfigurationParameter(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	        cmd.addRemoveConfigurationParameter(reference, operationId);
	        return this;
	    }
		
		public Builder DataElement(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveDataElement(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addRemoveDataStreamType(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveDataStreamType(reference, operationId);
	        return this;
	    }
	    
	    public Builder addRemoveDataType(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveDataType(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addRemoveOperatorGroup(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveOperatorGroup(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addRemoveOperatorType(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveOperatorType(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addRemoveSensor(String name, String domain, String operationId){
	    	SCAISensorReference reference = new SCAISensorReference(name, domain);
	    	cmd.addRemoveSensor(reference, operationId);         
	    	return this;
	    }
	    
	    public Builder addRemoveSensorCategory(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveSensorCategory(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addRemoveSensorDomain(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	    	cmd.addRemoveSensorDomain(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addRemoveSensorType(String name, String operationId){
	    	SCAIReference reference = new SCAIReference(name, false);
	        cmd.addRemoveSensorType(reference, operationId);
	    	return this;
	    }
	    
	    public Builder addListAllSensors(String operationId){
	    	cmd.addListAllSensors(operationId);
	    	return this;
	    }
	    
	    public Builder addListAllSensorTypes(String operationId){
	    	cmd.addListAllSensorTypes(operationId);
	    	return this;
	    }
	    
	    public Builder addListAllDataStreamTypes(String operationId){
	    	cmd.addListAllDataStreamTypes(operationId);
	    	return this;
	    }
	    
	    public Builder addListAllOperatorTypes(String operationId){
	    	cmd.addListAllOperatorTypes(operationId);
	    	return this;
	    }
	    
	    public Builder addListAllOperators(String operatorGroup, String operationId){
	    	cmd.addListAllOperators(new SCAIReference(operatorGroup, false), operationId);	    	
	    	return this;
	    }
	    
	    public Builder addListAllDataElements(String operationId){
	    	cmd.addListAllDataElements(operationId);
	    	return this;
	    }
	}
	
	public Builder Builder = new Builder();
	public Utility Util = new Utility();
	private ScaiFactory factory;
	private BuilderSCAI20 cmd;
	
	private SCAIDocument scaiDoc = null;
	
	public ScaiCommand(ScaiFactory factory) {
		this(factory, new BuilderSCAI20());
	}
	
	public ScaiCommand(ScaiFactory factory, BuilderSCAI20 cmd) {
		this.factory = factory;
		this.cmd = cmd;
	}
	
	public void commit() throws java.lang.Exception {
		// TODO iwo in dieser klasse aufpassen das nicht 2mal die gleiche operation id benutzt wird?
		scaiDoc = factory.sendScaiBuilder(cmd);       
	}
	
	public boolean isExceptionResponse(String operationId){
		assert(scaiDoc != null); // TODO assert vllt die falsche loesung als fehlerbehandlung wenn command noch nicht committed		
		for(Exception e : scaiDoc.getSCAI().getPayload().getAcknowledgment().getExceptionArray()){
			if(e.getOperationID().equals(operationId)){				
				return true;
			}
		}
		return false;
	}
	
	public boolean isSuccessResponse(String operationId){
		assert(scaiDoc != null);
		for(Success s : scaiDoc.getSCAI().getPayload().getAcknowledgment().getSuccessArray()){
			if(s.getOperationID().equals(operationId)){				
				return true;
			}
		}
		return false;
	}
	
	public boolean isReplyResponse(String operationId){
		assert(scaiDoc != null);
		for(Reply r : scaiDoc.getSCAI().getPayload().getAcknowledgment().getReplyArray()){
			if(r.getOperationID().equals(operationId)){
				return true;
			}
		}
		return false;
	}
	
	public Success getSuccessResponse(String operationId){
		assert(scaiDoc != null);
		for(Success s : scaiDoc.getSCAI().getPayload().getAcknowledgment().getSuccessArray()){
			if(s.getOperationID().equals(operationId)){
				return s;
			}
		}
		return null;
	}
	
	public Exception getExceptionResponse(String operationId){
		assert(scaiDoc != null);
		for(Exception e : scaiDoc.getSCAI().getPayload().getAcknowledgment().getExceptionArray()){
			if(e.getOperationID().equals(operationId)){				
				return e;
			}
		}
		return null;
	}

	public Reply getReplyResponse(String operationId){
		assert(scaiDoc != null);
		for(Reply r : scaiDoc.getSCAI().getPayload().getAcknowledgment().getReplyArray()){
			if(r.getOperationID().equals(operationId)){
				return r;
			}
		}
		return null;
	}
	
	public int sizeExceptionArray(){
		assert(scaiDoc != null);
		return scaiDoc.getSCAI().getPayload().getAcknowledgment().getExceptionArray().length;
	}
	
	public int sizeSuccessArray(){
		assert(scaiDoc != null);
		return scaiDoc.getSCAI().getPayload().getAcknowledgment().getSuccessArray().length;
	}
	
	public int sizeReplyArray(){
		assert(scaiDoc != null);
		return scaiDoc.getSCAI().getPayload().getAcknowledgment().getReplyArray().length;
	}
	
	@Override
	public String toString() {
		if(scaiDoc == null){
			return "[ScaiCommand: Not Committed]";
		} else {
			int e = scaiDoc.getSCAI().getPayload().getAcknowledgment().getExceptionArray().length;
			int s = scaiDoc.getSCAI().getPayload().getAcknowledgment().getSuccessArray().length;
			int r = scaiDoc.getSCAI().getPayload().getAcknowledgment().getReplyArray().length;
			return "[ScaiCommand: Committed " + "R:" + r + " E:" + e + " S:" + s + " ]";
		}
	}
}
