package de.offis.scaiconnector.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlObject;

import de.offis.client.AbstractScaiDTO.Type;
import de.offis.client.ConfigurationParameter;
import de.offis.client.DataElement;
import de.offis.client.DataStreamType;
import de.offis.client.DataType;
import de.offis.client.Operator;
import de.offis.client.OperatorType;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorCategory;
import de.offis.client.SensorDomain;
import de.offis.client.SensorType;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
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
import de.offis.xml.schema.scai20.OperatorLinkDescription;
import de.offis.xml.schema.scai20.OperatorLinkDescription.Destination;
import de.offis.xml.schema.scai20.OperatorLinkDescription.Source;
import de.offis.xml.schema.scai20.OperatorTypeDescription;
import de.offis.xml.schema.scai20.Reference;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment.Reply;
import de.offis.xml.schema.scai20.SensorCategoryDescription;
import de.offis.xml.schema.scai20.SensorDescription;
import de.offis.xml.schema.scai20.SensorDomainDescription;
import de.offis.xml.schema.scai20.SensorTypeDescription;

/**
 * Utility class mainly for converting Reply-XML-Objects to Java models.
 *
 * @author Alexander Funk
 * 
 */
public final class ScaiUtil {    
    public static SCAIReference[] convertToSCAIReferences(List<String> data) {
        if(data == null)
            return new SCAIReference[0];
        
        List<String> list = new ArrayList<String>(data);
        SCAIReference[] refs = new SCAIReference[list.size()];
        int i = 0;
        for(String e : list){
            refs[i] = new SCAIReference(e, false);
            i++;
        }

        return refs;
    }
    
    public static List<ConfigurationParameter> extractConfigurationParameter(Reply reply){    	       
        List<ConfigurationParameter> list = new ArrayList<ConfigurationParameter>();

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
    
    public static List<DataElement> extractDataElements(Reply reply){    	
        List<DataElement> list = new ArrayList<DataElement>();

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

    public static List<DataStreamType> extractDataStreamTypes(Reply replyDataStreamTypes, Reply replyDataElements){
        List<DataStreamType> list = new ArrayList<DataStreamType>();

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
            for(DataElement de : extractDataElements(replyDataElements)){        	
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
    
    private static void getDataElementsRecHelper(List<String> dataElems, DataElement elem, Map<String, DataElement> dataElementsMap, String dir){
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
    
    public static List<DataType> extractDataTypes(Reply reply){
        List<DataType> list = new ArrayList<DataType>();

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
    
    public static List<ScaiLink> extractLinks(Reply reply){
        List<ScaiLink> list = new ArrayList<ScaiLink>();

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
            } else {
            	Logger.getLogger("ListAllClassServer").log(Level.INFO, "FOUND LINK ERROR. NAMES NOT SET"); // TODO remove?
            }
        }

        return list;
    }
        
    public static List<Operator> extractOperators(Reply reply){
        List<Operator> list = new ArrayList<Operator>();

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
    // TODO 
//    public static List<OperatorGroup> extractOperatorGroups(Reply reply){
//    	BuilderSCAI20 listCmd = new BuilderSCAI20();
//		listCmd.addListAllOperatorGroups(LIST_ALL_OPERATION_ID);
//		
//		List<OperatorGroup> list = new ArrayList<OperatorGroup>();
//		
//		ScaiCommand scai = factory.createScaiCommand(listCmd);
//		
//		try {
//			scai.commit();
//		} catch (Exception e) {
//			return list;
//		}
//
//        if(scai.isReplyResponse(LIST_ALL_OPERATION_ID)){
//        	for (XmlObject o : scai.getReplyResponse(LIST_ALL_OPERATION_ID).getDataArray()) {
//                OperatorGroupDescription desc = (OperatorGroupDescription) o;
//                
//                // pruefe ob die operator group grade laeuft ...
//                boolean running = false;              
//                running = factory.Get.getOperatorGroupStatus(desc.getName());
//                
//                list.add(new OperatorGroup(desc.getName(), running));
//            }
//        }        
//
//        return list;
//    }
    
    public static List<OperatorType> extractOperatorTypes(Reply reply){
        List<OperatorType> list = new ArrayList<OperatorType>();

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
    
    public static List<Sensor> extractSensors(Reply reply){
        List<Sensor> list = new ArrayList<Sensor>();

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
    
    public static List<SensorCategory> extractSensorCategorys(Reply reply){
        List<SensorCategory> list = new ArrayList<SensorCategory>();

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
    
    public static List<SensorDomain> extractSensorDomains(Reply reply){
        List<SensorDomain> list = new ArrayList<SensorDomain>();

        for (XmlObject o : reply.getDataArray()) {
            SensorDomainDescription desc = (SensorDomainDescription) o;
            list.add(new SensorDomain(desc.getName()));
        }

        return list;
    }
    
    public List<SensorType> extractSensorTypes(Reply reply){    	
        List<SensorType> list = new ArrayList<SensorType>();

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
