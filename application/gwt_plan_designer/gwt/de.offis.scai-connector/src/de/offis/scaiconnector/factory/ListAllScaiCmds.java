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
import de.offis.client.OperatorGroup;
import de.offis.client.OperatorType;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorCategory;
import de.offis.client.SensorDomain;
import de.offis.client.SensorType;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
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
import de.offis.xml.schema.scai20.OperatorGroupDescription;
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
 * Wrapper for SCAI ListAll-Operations.
 *
 * @author Alexander Funk
 * 
 */
public final class ListAllScaiCmds {
	
	private static final String LIST_ALL_OPERATION_ID = "ListAllOPID";
	private ScaiFactory factory;
	
	public ListAllScaiCmds(ScaiFactory factory) {
		this.factory = factory;
	}

	public List<ConfigurationParameter> ConfigurationParameter(){
		BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllConfigurationParameters(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<ConfigurationParameter>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);
        
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
    
    public List<DataElement> DataElement(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllDataElements(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<DataElement>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
    
    public List<DataStreamType> DataStreamType(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllDataStreamTypes(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<DataStreamType>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

        List<DataStreamType> list = new ArrayList<DataStreamType>();

        if(reply == null){
        	return list;
        }
        
        for (XmlObject o : reply.getDataArray()) {
            DataStreamTypeDescription desc = (DataStreamTypeDescription) o;
            ArrayList<String> dataElements = new ArrayList<String>();
            for (Reference r : desc.getDataElementArray()) {
                if (r.isSetName()) {
                    dataElements.add(r.getName());
                }
            }
            
            ArrayList<String> dataElementsFlat = new ArrayList<String>();
            
            Map<String, DataElement> dataElementsMap = new HashMap<String, DataElement>();
            for(DataElement de : DataElement()){        	
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
    
    public List<DataType> DataType(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllDataTypes(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<DataType>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
    
    public List<ScaiLink> Links(String operatorGroup){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllLinks(new SCAIReference(operatorGroup, false), LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<ScaiLink>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
            	Logger.getLogger("ListAllClassServer").log(Level.INFO, "FOUND LINK FROM " + sourceName + " TO " + destName); // TODO remove
                list.add(new ScaiLink(sourceName, destName));
            } else {
            	Logger.getLogger("ListAllClassServer").log(Level.INFO, "FOUND LINK ERROR. NAMES NOT SET"); // TODO remove
            }
        }

        return list;
    }
        
    public List<Operator> Operators(String operatorGroup){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllOperators(new SCAIReference(operatorGroup, false), LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<Operator>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

        List<Operator> list = new ArrayList<Operator>();
        
        if(reply == null){
        	return list;
        }

        for (XmlObject o : reply.getDataArray()) {
//        	Logger.getLogger("ListAllClassServer").log(Level.INFO, "FOUND OPERATOR (on serverside)"); // TODO remove
            OperatorDescription op = (OperatorDescription) o;
            
            String name = op.getName();
            Map<String, String> properties = new HashMap<String, String>();
            for(Property p : op.getPropertyArray()){
            	properties.put(p.getKey(), p.getValue());
            }
            
            if(op.isSetInputOperator() && op.getInputOperator().getSensor().isSetNameReference()){            	
            	String sensorName = op.getInputOperator().getSensor().getNameReference().getSensorName();
                String sensorDomain = op.getInputOperator().getSensor().getNameReference().getSensorDomainName();            	            
//                Logger.getLogger("ListAllClassServer").log(Level.INFO, " INPUT HAS NAME REF"); // TODO remove
                list.add(new Operator(name, sensorName, sensorDomain, properties, Operator.INPUT));
            } else if(op.isSetOutputOperator() && op.getOutputOperator().getSensor().isSetNameReference()){
            	String sensorName = op.getOutputOperator().getSensor().getNameReference().getSensorName();
            	String sensorDomain = op.getOutputOperator().getSensor().getNameReference().getSensorDomainName();
//            	Logger.getLogger("ListAllClassServer").log(Level.INFO, " OUTPUT HAS NAME REF"); // TODO remove
                list.add(new Operator(name, sensorName, sensorDomain, properties, Operator.OUTPUT));
            } else if(op.isSetServiceOperator()){
            	String serviceType = op.getServiceOperator().getOperatorType().getName();
//            	Logger.getLogger("ListAllClassServer").log(Level.INFO, " SERVICE HAS NAME REF"); // TODO remove
            	list.add(new Operator(name, serviceType, properties)); // serviceType = JumpingTimeWindow
            }
        }

        return list;
    }
    
    public List<OperatorGroup> OperatorGroup(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllOperatorGroups(LIST_ALL_OPERATION_ID);
		
		List<OperatorGroup> list = new ArrayList<OperatorGroup>();
		
		ScaiCommand scai = factory.createScaiCommand(listCmd);
		
		try {
			scai.commit();
		} catch (Exception e) {
			return list;
		}
		
        if(scai.isReplyResponse(LIST_ALL_OPERATION_ID)){
        	for (XmlObject o : scai.getReplyResponse(LIST_ALL_OPERATION_ID).getDataArray()) {
                OperatorGroupDescription desc = (OperatorGroupDescription) o;
                
                // pruefe ob die operator group grade laeuft ...
                boolean running = false;              
                running = factory.Get.getOperatorGroupStatus(desc.getName());
                
                list.add(new OperatorGroup(desc.getName(), running));
            }
        }        

        return list;
    }
    
    public List<OperatorType> OperatorType(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllOperatorTypes(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<OperatorType>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
    
    public List<Sensor> Sensor(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllSensors(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<Sensor>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
    
    public List<SensorCategory> SensorCategory(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllSensorCategories(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<SensorCategory>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
    
    public List<SensorDomain> SensorDomain(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllSensorDomains(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<SensorDomain>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
    
    public List<SensorType> SensorType(){
    	BuilderSCAI20 listCmd = new BuilderSCAI20();
		listCmd.addListAllSensorTypes(LIST_ALL_OPERATION_ID);
		
		ScaiCommand cmd = factory.createScaiCommand(listCmd);

        try {
        	cmd.commit();
        } catch (Exception ex) {
        	return new ArrayList<SensorType>(0);
        }
        
        Reply reply = cmd.getReplyResponse(LIST_ALL_OPERATION_ID);

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
