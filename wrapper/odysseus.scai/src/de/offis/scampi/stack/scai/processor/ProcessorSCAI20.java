/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.processor;

import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.IProcessor;
import de.offis.scampi.stack.ProtocolObject;
import de.offis.scampi.stack.scai.builder.types.SCAIConfigurationValue;
import de.offis.scampi.stack.scai.builder.types.SCAIOperator;
import de.offis.scampi.stack.scai.builder.types.SCAIPermission;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;
import de.offis.xml.schema.scai20.ConfigurationParameterDescription;
import de.offis.xml.schema.scai20.ConfigurationValueDescription;
import de.offis.xml.schema.scai20.DataElementDescription;
import de.offis.xml.schema.scai20.DataStreamTypeDescription;
import de.offis.xml.schema.scai20.DataTypeDescription;
import de.offis.xml.schema.scai20.ModulesDescription;
import de.offis.xml.schema.scai20.OperatorDescription;
import de.offis.xml.schema.scai20.OperatorGroupDescription;
import de.offis.xml.schema.scai20.OperatorGroupStatus;
import de.offis.xml.schema.scai20.OperatorLinkDescription;
import de.offis.xml.schema.scai20.OperatorTypeDescription;
import de.offis.xml.schema.scai20.PermissionDescription;
import de.offis.xml.schema.scai20.PermissionDescription.Permission;
import de.offis.xml.schema.scai20.ProcessingUnitDescription;
import de.offis.xml.schema.scai20.Reference;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Identification;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.AccessControl.*;
import de.offis.xml.schema.scai20.SensorCategoryDescription;
import de.offis.xml.schema.scai20.SensorDataDescription;
import de.offis.xml.schema.scai20.SensorDescription;
import de.offis.xml.schema.scai20.SensorDomainDescription;
import de.offis.xml.schema.scai20.SensorTypeDescription;
import de.offis.xml.schema.scai20.UserDescription;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.*;

/**
 *
 * @author Claas
 */
public class ProcessorSCAI20 implements IProcessor {

    private SCAIDocument data = null;
    //private SCAIDocument response = null;
    private ProtocolObject scaiObject = null;

    @Override
    public Object process(ProtocolObject scaiObject) throws Exception {
        try {
            this.scaiObject = scaiObject;
            this.data = (SCAIDocument) scaiObject.getContent();

            this.scaiObject.getBuilder().clearDocument();
            if (data.getSCAI().isSetIdentification()) {
                processIdentification(data.getSCAI().getIdentification());
            }
            if (data.getSCAI().getPayload().isSetMeasurements()) {
                processMeasurements(data.getSCAI().getPayload().getMeasurements());
            } else if (data.getSCAI().getPayload().isSetControlData()) {
                processControlData(data.getSCAI().getPayload().getControlData());
            } else if (data.getSCAI().getPayload().isSetAcknowledgment()) {
                processAcknowledgment(data.getSCAI().getPayload().getAcknowledgment());
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessorSCAI20.class.getName()).log(Level.INFO, null, e);
            throw new Exception(e.getMessage());
        }

        return scaiObject.getTranslator().translate(scaiObject.getBuilder().getDocument());
    }

    private void processControlData(de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData data)
    {
        if(data.getAccessControlArray().length > 0)
        {
            for(SCAIDocument.SCAI.Payload.ControlData.AccessControl cmd: data.getAccessControlArray())
                processAccessControl(cmd);
        }
        else if(data.getSensorControlArray().length > 0)
        {
            for(SCAIDocument.SCAI.Payload.ControlData.SensorControl cmd: data.getSensorControlArray())
                processSensorControl(cmd);
        }
        else if(data.getSensorRegistryControlArray().length > 0)
        {
            for(SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl cmd: data.getSensorRegistryControlArray())
                processSensorRegistryControl(cmd);
        }
    }

    private void processSensorControl(de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorControl data)
    {
        boolean suc = false;
        Object tempData = null;
        if (data.isSetConfigureSensor()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.ConfigureSensor cmd = data.getConfigureSensor();
            suc = false;
            suc = scaiObject.getControlModules().getSensorControl().configureSensor(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()), convertToSCAIConfigurationValue(cmd.getConfigurationValue()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentData(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be configured.", null, cmd.getOperationID());
                }
            }
        }
        else if(data.isSetGetConfiguration())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.GetConfiguration cmd = data.getGetConfiguration();
            tempData = null;
            tempData = scaiObject.getControlModules().getSensorControl().getConfiguration(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()), scaiObject.getBuilder());
            if(tempData != null)
                scaiObject.getBuilder().addAcknowledgmentData((ConfigurationValueDescription)tempData, cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("SensorConfiguration could not be recieved.", null, cmd.getOperationID());
        }
        else if(data.isSetGetSupportedModules())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.GetSupportedModules cmd = data.getGetSupportedModules();
            tempData = null;
            tempData = scaiObject.getControlModules().getSensorControl().getSupportedModules(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()), scaiObject.getBuilder());
            if(tempData != null)
                scaiObject.getBuilder().addAcknowledgmentData((ModulesDescription)tempData, cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("SupportedModules could not be recieved.", null, cmd.getOperationID());
        }
        else if(data.isSetGetValue())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.GetValue cmd = data.getGetValue();
            tempData = null;
            tempData = scaiObject.getControlModules().getSensorControl().getValue(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()), scaiObject.getBuilder());
            if(tempData != null)
                scaiObject.getBuilder().addAcknowledgmentData((SensorDataDescription)tempData, cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("SensorData could not be recieved.", null, cmd.getOperationID());
        }
        else if(data.isSetStartSensor())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.StartSensor cmd = data.getStartSensor();
            suc = false;
            suc = scaiObject.getControlModules().getSensorControl().startSensor(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be started.", null, cmd.getOperationID());
            }
        }
        else if(data.isSetStopSensor())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.StopSensor cmd = data.getStopSensor();
            suc = false;
            suc = scaiObject.getControlModules().getSensorControl().stopSensor(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be stoped.", null, cmd.getOperationID());
            }
        }
        else if(data.isSetSubscribeDatastream())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.SubscribeDatastream cmd = data.getSubscribeDatastream();
            suc = false;
            suc = scaiObject.getControlModules().getSensorControl().subscribeDatastream(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()));
            if(cmd.isSetOperationID())
            {
                if (suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Datastream not subscribed.", null, cmd.getOperationID());
            }
        }
        else if(data.isSetUnsubscribeDatastream())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorControl.UnsubscribeDatastream cmd = data.getUnsubscribeDatastream();
            suc = false;
            suc = scaiObject.getControlModules().getSensorControl().unsubscribeDatastream(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()));
            if(cmd.isSetOperationID())
            {
                if (suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Datastream not unsubscribed.", null, cmd.getOperationID());
            }
        }
    }

    
    private void processSensorRegistryControl(de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl data)
    {
        boolean suc = false;
        Object tempData = null;
        int garbageCounter = 0;
        
        if (data.isSetAddChildCategoryToSensorCategory()){
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.AddChildCategoryToSensorCategory cmd = data.getAddChildCategoryToSensorCategory();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().addChildCategoryToSensorCategory(new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()), new SCAIReference(cmd.getParentSensorCategory().getId(), cmd.getParentSensorCategory().getName()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Childcategory could not be addded to Category.", null, cmd.getOperationID());
            }
        } else if (data.isSetAddDataElementToDataStreamType()){
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.AddDataElementToDataStreamType cmd = data.getAddDataElementToDataStreamType();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().addDataElementToDataStreamType(new SCAIReference(cmd.getDataElement().getId(), cmd.getDataElement().getName()), new SCAIReference(cmd.getDataStreamType().getId(), cmd.getDataStreamType().getName()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataElement could not be addded to DataStreamType.", null, cmd.getOperationID());
            }
        } else if (data.isSetAddSensorCategoryToSensorDomain()){
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.AddSensorCategoryToSensorDomain cmd = data.getAddSensorCategoryToSensorDomain();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().addSensorCategoryToSensorDomain(new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()), new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Category could not be addded to Domain.", null, cmd.getOperationID());
            }
        } else if (data.isSetAddSensorTypeToSensorCategory()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.AddSensorTypeToSensorCategory cmd = data.getAddSensorTypeToSensorCategory();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().addSensorTypeToSensorCategory(new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName()), new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Type could not be added to Category.", null, cmd.getOperationID());
                }
            }
        }
        else if (data.isSetAddSensorTypeToSensorDomain()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.AddSensorTypeToSensorDomain cmd = data.getAddSensorTypeToSensorDomain();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().addSensorTypeToSensorDomain(new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName()), new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Type could not be added to Domain.", null, cmd.getOperationID());
                }
            }
        }
        else if(data.isSetCreateConfigurationParameter()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateConfigurationParameter cmd = data.getCreateConfigurationParameter();
            suc = false;

            if(cmd.getConfigurationParameter().isSetAtomicParameter())
            {
                suc = scaiObject.getControlModules().getSensorRegistryControl().createConfigurationParameterAtomic(cmd.getConfigurationParameter().getName(), cmd.getConfigurationParameter().getOptional(), cmd.getConfigurationParameter().getAtomicParameter().getUom(), cmd.getConfigurationParameter().getAtomicParameter().getIdentifier(), new SCAIReference(cmd.getConfigurationParameter().getAtomicParameter().getDataType().getId(), cmd.getConfigurationParameter().getAtomicParameter().getDataType().getName()));
            }
            else if(cmd.getConfigurationParameter().isSetComplexParameter())
            {
                suc = scaiObject.getControlModules().getSensorRegistryControl().createConfigurationParameterComplex(cmd.getConfigurationParameter().getName(), cmd.getConfigurationParameter().getOptional(), convertToReferenceArray(cmd.getConfigurationParameter().getComplexParameter().getConfigurationParameterArray()));
            }
            else if(cmd.getConfigurationParameter().isSetSequenceParameter())
            {
                suc = scaiObject.getControlModules().getSensorRegistryControl().createConfigurationParameterSequence(cmd.getConfigurationParameter().getName(), cmd.getConfigurationParameter().getOptional(), cmd.getConfigurationParameter().getSequenceParameter().getMinlength(), cmd.getConfigurationParameter().getSequenceParameter().getMaxlength(), new SCAIReference(cmd.getConfigurationParameter().getSequenceParameter().getConfigurationParameter().getId(), cmd.getConfigurationParameter().getSequenceParameter().getConfigurationParameter().getName()));
            }

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("ConfigurationParameter could not be created.", null, cmd.getOperationID());
            }
        }else if(data.isSetCreateDataElement())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateDataElement cmd = data.getCreateDataElement();
            suc = false;
            if(cmd.getDataElement().isSetAtomicElement())
            {
                suc = false;
                
                if(cmd.getDataElement().isSetAtomicElement())
                {
                    suc = scaiObject.getControlModules().getSensorRegistryControl().createDataElementAtomic(cmd.getDataElement().getAtomicElement().getName(), new SCAIReference(cmd.getDataElement().getAtomicElement().getDataType().getId(), cmd.getDataElement().getAtomicElement().getDataType().getName()));
                }
                else if(cmd.getDataElement().isSetComplexElement())
                {
                    suc = scaiObject.getControlModules().getSensorRegistryControl().createDataElementComplex(cmd.getDataElement().getComplexElement().getName(), convertToReferenceArray(cmd.getDataElement().getComplexElement().getDataElementArray()));
                }
            }
            else if(cmd.getDataElement().isSetComplexElement())
            {
                suc = scaiObject.getControlModules().getSensorRegistryControl().createDataElementComplex(cmd.getDataElement().getComplexElement().getName(), convertToReferenceArray(cmd.getDataElement().getComplexElement().getDataElementArray()));
            }

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataElement could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateDataStreamType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateDataStreamType cmd = data.getCreateDataStreamType();
            suc = false;

            suc=scaiObject.getControlModules().getSensorRegistryControl().createDataStreamType(cmd.getDataStreamType().getName(), convertToReferenceArray(cmd.getDataStreamType().getDataElementArray()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataStreamType could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateDataType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateDataType cmd = data.getCreateDataType();

            suc = false;
            if(cmd.getDataType().isSetDecimalType())
                suc = scaiObject.getControlModules().getSensorRegistryControl().createDataTypeDecimal(
                        cmd.getDataType().getName(),
                        cmd.getDataType().getDecimalType().isSetMin(),
                        cmd.getDataType().getDecimalType().getMin(),
                        cmd.getDataType().getDecimalType().isSetMax(),
                        cmd.getDataType().getDecimalType().getMax(),
                        cmd.getDataType().getDecimalType().isSetScale(),
                        cmd.getDataType().getDecimalType().getScale(),
                        cmd.getDataType().getDecimalType().getDefaultvalue()
                        );
            else if(cmd.getDataType().isSetStringType())
                suc = scaiObject.getControlModules().getSensorRegistryControl().createDataTypeString(
                        cmd.getDataType().getName(),
                        cmd.getDataType().getStringType().isSetMin(),
                        cmd.getDataType().getStringType().getMin(),
                        cmd.getDataType().getStringType().isSetMax(),
                        cmd.getDataType().getStringType().getMax(),
                        cmd.getDataType().getStringType().isSetRegex(),
                        cmd.getDataType().getStringType().getRegex(),
                        cmd.getDataType().getStringType().getDefaultvalue()
                        );
            else if(cmd.getDataType().isSetBinaryType())
                suc = scaiObject.getControlModules().getSensorRegistryControl().createDataTypeBinary(
                        cmd.getDataType().getName(),
                        cmd.getDataType().getBinaryType().isSetMin(),
                        cmd.getDataType().getBinaryType().getMin(),
                        cmd.getDataType().getBinaryType().isSetMax(),
                        cmd.getDataType().getBinaryType().getMax(),
                        cmd.getDataType().getBinaryType().getDefaultvalue()
                        );
            else if (cmd.getDataType().isSetEnumType()) {
                HashMap<Long, String> allowedValues = new HashMap<Long, String>();
                for (int i = 0; i < cmd.getDataType().getEnumType().getAllowedvalueArray().length; i++) {
                    allowedValues.put(cmd.getDataType().getEnumType().getAllowedvalueArray(i).getOrdinal(), cmd.getDataType().getEnumType().getAllowedvalueArray(i).getName());
                }
                suc = scaiObject.getControlModules().getSensorRegistryControl().createDataTypeEnum(
                        cmd.getDataType().getName(),
                        (cmd.getDataType().getEnumType().getAllowedvalueArray().length > 0),
                        allowedValues,
                        cmd.getDataType().getEnumType().getDefaultvalue());
            }
            else if (cmd.getDataType().isSetListType()) {
                suc = scaiObject.getControlModules().getSensorRegistryControl().createDataTypeList(
                        cmd.getDataType().getName(),
                        cmd.getDataType().getListType().isSetMin(),
                        cmd.getDataType().getListType().getMin(),
                        cmd.getDataType().getListType().isSetMax(),
                        cmd.getDataType().getListType().getMax(),
                        (cmd.getDataType().getListType().getAllowedvalueArray().length > 0),
                        cmd.getDataType().getListType().getAllowedvalueArray(),
                        cmd.getDataType().getListType().getDefaultvalueArray());
            }
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataType could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateOperator())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateOperator cmd = data.getCreateOperator();
            suc=false;
            SCAIOperator operator = convertToSCAIOperator(cmd.getOperator());

            suc = scaiObject.getControlModules().getProcessingControl().createOperator(operator, new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));

            if(cmd. isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Operator could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateOperatorGroup())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateOperatorGroup cmd = data.getCreateOperatorGroup();
            suc = false;

            suc=scaiObject.getControlModules().getProcessingControl().createOperatorGroup(cmd.getOperatorGroup().getName());

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateOperatorType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateOperatorType cmd = data.getCreateOperatorType();
            suc = false;

            HashMap<String, String> proprties = new HashMap<String, String>();
            HashMap<String, Boolean> readOnly = new HashMap<String, Boolean>();
            for(OperatorTypeDescription.Property pro: cmd.getOperatorType().getPropertyArray())
            {
                proprties.put(pro.getKey(), pro.getValue());
                readOnly.put(pro.getKey(), pro.getReadOnly());
            }
            String desciption = null;
            if(cmd.getOperatorType().isSetDescription())
                desciption = cmd.getOperatorType().getDescription();

            suc=scaiObject.getControlModules().getProcessingControl().createOperatorType(cmd.getOperatorType().getName(), cmd.getOperatorType().getMetaType(), proprties, readOnly, desciption);

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorType could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateSensor())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateSensor cmd = data.getCreateSensor();
            suc = false;

            suc = scaiObject.getControlModules().getSensorRegistryControl().createSensor(cmd.getSensor().getName(), new SCAIReference(cmd.getSensor().getSensorDomain().getId(), cmd.getSensor().getSensorDomain().getName()), new SCAIReference(cmd.getSensor().getSensorType().getId(), cmd.getSensor().getSensorType().getName()), cmd.getSensor().getVirtual());

            if(cmd. isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateSensorCategory())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateSensorCategory cmd = data.getCreateSensorCategory();
            suc = false;

            SCAIReference parent = null;
            if(cmd.getSensorCategory().isSetParentSensorCategory())
                parent =  new SCAIReference(cmd.getSensorCategory().getParentSensorCategory().getId(), cmd.getSensorCategory().getParentSensorCategory().getName());

            suc = scaiObject.getControlModules().getSensorRegistryControl().createSensorCategory(cmd.getSensorCategory().getName(), parent, convertToReferenceArray(cmd.getSensorCategory().getSensorDomainArray()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("SensorCategory could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateSensorDomain())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateSensorDomain cmd = data.getCreateSensorDomain();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().createSensorDomain(cmd.getSensorDomain().getName());

            if(cmd. isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("SensorDomain could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetCreateSensorType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.CreateSensorType cmd = data.getCreateSensorType();
            suc = false;

            suc=scaiObject.getControlModules().getSensorRegistryControl().createSensorType(cmd.getSensorType().getName(), cmd.getSensorType().getAdapter(), new SCAIReference(cmd.getSensorType().getDataStreamType().getId(), cmd.getSensorType().getDataStreamType().getName()), convertToReferenceArray(cmd.getSensorType().getConfigurationParameterArray()), convertToReferenceArray(cmd.getSensorType().getSensorCategoryArray()), convertToReferenceArray(cmd.getSensorType().getSensorDomainArray()));

            if(cmd.isSetOperationID()) 
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("SensorType could not be created.", null, cmd.getOperationID());
            }
        } else if(data.isSetDeployOperatorGroup())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.DeployOperatorGroup cmd = data.getDeployOperatorGroup();
            suc = false;

            if(cmd.isSetProcessingUnit())
                suc=scaiObject.getControlModules().getProcessingControl().deployOperatorGroup(new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()), new SCAIReference(cmd.getProcessingUnit().getId(), cmd.getProcessingUnit().getName()));
            else
                suc=scaiObject.getControlModules().getProcessingControl().deployOperatorGroup(new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()), null);

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be deployed.", null, cmd.getOperationID());
            }
        }
        else if(data.isSetGetConfigurationParameter())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetConfigurationParameter cmd = data.getGetConfigurationParameter();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getConfigurationParameter(new SCAIReference(cmd.getConfigurationParameter().getId(), cmd.getConfigurationParameter().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((ConfigurationParameterDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("ConfigurationParameter could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetDataElement())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetDataElement cmd = data.getGetDataElement();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getDataElement(new SCAIReference(cmd.getDataElement().getId(), cmd.getDataElement().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((DataElementDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("DataElement could not be found.", null, cmd.getOperationID());
        }else if(data.isSetGetDataStreamType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetDataStreamType cmd = data.getGetDataStreamType();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getDataStreamType(new SCAIReference(cmd.getDataStreamType().getId(), cmd.getDataStreamType().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((DataStreamTypeDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("DataStreamType could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetDataType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetDataType cmd = data.getGetDataType();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getDataType(new SCAIReference(cmd.getDataType().getId(), cmd.getDataType().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((DataTypeDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("DataType could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetOperator())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetOperator cmd = data.getGetOperator();
            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().getOperator(new SCAIReference(cmd.getOperator().getId(), cmd.getOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((OperatorDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("Operator could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetOperatorGroup())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetOperatorGroup cmd = data.getGetOperatorGroup();

            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().getOperatorGroup(new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((OperatorGroupDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetOperatorGroupStatus())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetOperatorGroupStatus cmd = data.getGetOperatorGroupStatus();

            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().getOperatorGroupStatus(new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((OperatorGroupStatus)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetOperatorType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetOperatorType cmd = data.getGetOperatorType();

            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().getOperatorType(new SCAIReference(cmd.getOperatorType().getId(), cmd.getOperatorType().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((OperatorTypeDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("OperatorType could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetSensor())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetSensor cmd = data.getGetSensor();

            tempData = null;

            tempData = scaiObject.getControlModules().getSensorRegistryControl().getSensor(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((SensorDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetSensorDomain())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetSensorDomain cmd = data.getGetSensorDomain();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getSensorDomain(new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((SensorDomainDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("SensorDomain could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetSensorType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetSensorType cmd = data.getGetSensorType();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getSensorType(new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((SensorTypeDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("SensorType could not be found.", null, cmd.getOperationID());
        } else if(data.isSetGetSensorCategory())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.GetSensorCategory cmd = data.getGetSensorCategory();

            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().getSensorCategory(new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()), scaiObject.getBuilder());

            if(tempData!=null)
                scaiObject.getBuilder().addAcknowledgmentData((SensorCategoryDescription)tempData,cmd.getOperationID());
            else
                scaiObject.getBuilder().addAcknowledgmentException("SensorCategory could not be found.", null, cmd.getOperationID());
        } else if(data.isSetLinkOperators())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.LinkOperators cmd = data.getLinkOperators();
            suc=false;

            if(cmd.getOperatorLink().getSource().isSetInputOperator() && cmd.getOperatorLink().getDestination().isSetServiceOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().linkInputToOperator(new SCAIReference(cmd.getOperatorLink().getSource().getInputOperator().getId(), cmd.getOperatorLink().getSource().getInputOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getServiceOperator().getId(), cmd.getOperatorLink().getDestination().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }
            else if(cmd.getOperatorLink().getSource().isSetInputOperator() && cmd.getOperatorLink().getDestination().isSetOutputOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().linkInputToOutput(new SCAIReference(cmd.getOperatorLink().getSource().getInputOperator().getId(), cmd.getOperatorLink().getSource().getInputOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getOutputOperator().getId(), cmd.getOperatorLink().getDestination().getOutputOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }
            else if(cmd.getOperatorLink().getSource().isSetServiceOperator() && cmd.getOperatorLink().getDestination().isSetServiceOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().linkOperatorToOperator(new SCAIReference(cmd.getOperatorLink().getSource().getServiceOperator().getId(), cmd.getOperatorLink().getSource().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getServiceOperator().getId(), cmd.getOperatorLink().getDestination().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }
            else if(cmd.getOperatorLink().getSource().isSetServiceOperator() && cmd.getOperatorLink().getDestination().isSetOutputOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().linkOperatorToOutput(new SCAIReference(cmd.getOperatorLink().getSource().getServiceOperator().getId(), cmd.getOperatorLink().getSource().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getOutputOperator().getId(), cmd.getOperatorLink().getDestination().getOutputOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorLink could not be created.", null, cmd.getOperationID());
            }
        }
        else if(data.isSetListAllConfigurationParameters())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllConfigurationParameters(scaiObject.getBuilder());
            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                ConfigurationParameterDescription content[] = new ConfigurationParameterDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (ConfigurationParameterDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllConfigurationParameters().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegostry returnd "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllConfigurationParameters().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No ConfigurationParameters found.", null, data.getListAllConfigurationParameters().getOperationID());
        } else if(data.isSetListAllDataElements())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllDataElements(scaiObject.getBuilder());
            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                DataElementDescription content[] = new DataElementDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (DataElementDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllDataElements().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegostry returnd "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllDataElements().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No DataElements found.", null, data.getListAllDataElements().getOperationID());

        }else if(data.isSetListAllDataStreamTypes())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllDataStreamTypes(scaiObject.getBuilder());
            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                DataStreamTypeDescription content[] = new DataStreamTypeDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (DataStreamTypeDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllDataStreamTypes().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegostry returnd "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllDataStreamTypes().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No DataStreamTypes found.", null, data.getListAllDataStreamTypes().getOperationID());

        } else if(data.isSetListAllDataTypes())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllDataTypes(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                DataTypeDescription content[] = new DataTypeDescription[((Object[])tempData).length-garbageCounter];
                int j = 0;
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null) {
                        content[j] = (DataTypeDescription)(((Object[])tempData)[i]);
                        j++;
                    }
                }
                
                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllDataTypes().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllDataTypes().getOperationID());
            } else
                scaiObject.getBuilder().addAcknowledgmentException("No DataTypes found.", null, data.getListAllDataTypes().getOperationID());
        } else if (data.isSetListAllLinks()) {
            garbageCounter = 0;
            tempData = null;

            tempData = scaiObject.getControlModules().getProcessingControl().listAllLinks(scaiObject.getBuilder(), new SCAIReference(data.getListAllLinks().getOperatorGroup().getId(), data.getListAllLinks().getOperatorGroup().getName()));

            if (tempData != null && ((Object[]) tempData).length > 0) {
                for (int i = 0; i < ((Object[]) tempData).length; i++) {
                    if ((((Object[]) tempData)[i]) == null) {
                        garbageCounter++;
                    }
                }
                OperatorLinkDescription content[] = new OperatorLinkDescription[((Object[]) tempData).length - garbageCounter];
                for (int i = 0; i < ((Object[]) tempData).length; i++) {
                    if ((((Object[]) tempData)[i]) != null) {
                        content[i] = (OperatorLinkDescription) (((Object[]) tempData)[i]);
                    }
                }

                scaiObject.getBuilder().addAcknowledgmentData(content, data.getListAllLinks().getOperationID());

                if (garbageCounter > 0) {
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned " + garbageCounter + " Elements which could not be added to the Reply.", null, data.getListAllLinks().getOperationID());
                }
            } else {
                scaiObject.getBuilder().addAcknowledgmentException("No linked operators found.", null, data.getListAllLinks().getOperationID());
            }
        } else if (data.isSetListAllOperators()) {
            garbageCounter = 0;
            tempData = null;

            tempData = scaiObject.getControlModules().getProcessingControl().listAllOperatorsByGroup(scaiObject.getBuilder(), new SCAIReference(data.getListAllOperators().getOperatorGroup().getId(), data.getListAllOperators().getOperatorGroup().getName()));

            if (tempData != null && ((Object[]) tempData).length > 0) {
                for (int i = 0; i < ((Object[]) tempData).length; i++) {
                    if ((((Object[]) tempData)[i]) == null) {
                        garbageCounter++;
                    }
                }
                OperatorDescription content[] = new OperatorDescription[((Object[]) tempData).length - garbageCounter];
                for (int i = 0; i < ((Object[]) tempData).length; i++) {
                    SCAIOperator operator = (SCAIOperator) ((Object[]) tempData)[i];
                    if (operator.isOperator()) {
                        content[i] = (OperatorDescription) scaiObject.getBuilder().buildServiceOperator(operator.getName(), operator.getType(), operator.getProperties());
                    } else if (operator.isInput()) {
                        content[i] = (OperatorDescription) scaiObject.getBuilder().buildInputOperator(operator.getName(), operator.getSensor(), operator.getProperties());
                    } else if (operator.isOutput()) {
                        content[i] = (OperatorDescription) scaiObject.getBuilder().buildOutputOperator(operator.getName(), operator.getSensor(), operator.getProperties());
                    }
                }

                scaiObject.getBuilder().addAcknowledgmentData(content, data.getListAllOperators().getOperationID());

                if (garbageCounter > 0) {
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned " + garbageCounter + " Elements which could not be added to the Reply.", null, data.getListAllOperators().getOperationID());
                }
            } else {
                scaiObject.getBuilder().addAcknowledgmentException("No Operators found.", null, data.getListAllOperators().getOperationID());
            }
        }
        else if(data.isSetListAllOperatorGroups())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().listAllOperatorGroups(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                OperatorGroupDescription content[] = new OperatorGroupDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (OperatorGroupDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllOperatorGroups().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllOperatorGroups().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No OperatorGroups found.", null, data.getListAllOperatorGroups().getOperationID());
        }
        else if(data.isSetListAllOperatorTypes())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().listAllOperatorTypes(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                OperatorTypeDescription content[] = new OperatorTypeDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (OperatorTypeDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllOperatorTypes().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllOperatorTypes().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No OperatorTypes found.", null, data.getListAllOperatorTypes().getOperationID());
        }
        else if(data.isSetListAllProcessingUnits())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getProcessingControl().listAllProcessingUnits(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                ProcessingUnitDescription content[] = new ProcessingUnitDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (ProcessingUnitDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllProcessingUnits().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllProcessingUnits().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No ProcessingUnits found.", null, data.getListAllProcessingUnits().getOperationID());
        }
        else if(data.isSetListAllSensors())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensors(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorDescription content[] = new SensorDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensors().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensors().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No Sensor found.", null, data.getListAllSensors().getOperationID());
        }
        else if(data.isSetListAllSensorCategories())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensorCategories(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorCategoryDescription content[] = new SensorCategoryDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorCategoryDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensorCategories().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensorCategories().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No SensorCategory found.", null, data.getListAllSensorCategories().getOperationID());
        }
        else if(data.isSetListAllSensorCategoriesBySensorDomain())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensorCategoriesBySensorDomain(new SCAIReference(data.getListAllSensorCategoriesBySensorDomain().getSensorDomain().getId(), data.getListAllSensorCategoriesBySensorDomain().getSensorDomain().getName()), scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorCategoryDescription content[] = new SensorCategoryDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorCategoryDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensorCategoriesBySensorDomain().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensorCategoriesBySensorDomain().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No SensorCategory found in SensorDomain.", null, data.getListAllSensorCategoriesBySensorDomain().getOperationID());
        }
        else if(data.isSetListAllSensorDomains())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensorDomains(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorDomainDescription content[] = new SensorDomainDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorDomainDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensorDomains().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegostry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensorDomains().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No SensorDomain found.", null, data.getListAllSensorDomains().getOperationID());
        }
        else if(data.isSetListAllSensorTypes())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensorTypes(scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorTypeDescription content[] = new SensorTypeDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorTypeDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensorTypes().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returned "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensorTypes().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No SensorTypes found.", null, data.getListAllSensorTypes().getOperationID());
        }
        else if(data.isSetListAllSensorTypesBySensorCategory())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensorTypesBySensorCategory(new SCAIReference(data.getListAllSensorTypesBySensorCategory().getSensorCategory().getId(), data.getListAllSensorTypesBySensorCategory().getSensorCategory().getName()), scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorTypeDescription content[] = new SensorTypeDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorTypeDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensorTypesBySensorCategory().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returnd "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensorTypesBySensorCategory().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No SensorType found in SensorCategory.", null, data.getListAllSensorTypesBySensorCategory().getOperationID());
        }
        else if(data.isSetListAllSensorTypesBySensorDomain())
        {
            garbageCounter=0;
            tempData=null;
            tempData = scaiObject.getControlModules().getSensorRegistryControl().listAllSensorTypesBySensorDomain(new SCAIReference(data.getListAllSensorTypesBySensorDomain().getSensorDomain().getId(), data.getListAllSensorTypesBySensorDomain().getSensorDomain().getName()), scaiObject.getBuilder());

            if(tempData!=null && ((Object[])tempData).length > 0)
            {
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])==null)
                        garbageCounter++;
                }
                SensorTypeDescription content[] = new SensorTypeDescription[((Object[])tempData).length-garbageCounter];
                for(int i=0; i < ((Object[])tempData).length; i++)
                {
                    if((((Object[])tempData)[i])!=null)
                        content[i] = (SensorTypeDescription)(((Object[])tempData)[i]);
                }

                scaiObject.getBuilder().addAcknowledgmentData(content,data.getListAllSensorTypesBySensorDomain().getOperationID());

                if(garbageCounter>0)
                    scaiObject.getBuilder().addAcknowledgmentException("The SensorRegistry returnd "+garbageCounter+" Elements which could not be added to the Reply.", null, data.getListAllSensorTypesBySensorDomain().getOperationID());
            }
            else
                scaiObject.getBuilder().addAcknowledgmentException("No SensorType found in SensorDomain.", null, data.getListAllSensorTypesBySensorDomain().getOperationID());
        }
        else if(data.isSetRegisterProcessingUnit())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RegisterProcessingUnit cmd = data.getRegisterProcessingUnit();
            suc = false;

            suc=scaiObject.getControlModules().getProcessingControl().registerProcessingUnit(cmd.getProcessingUnit().getName(), URI.create(cmd.getProcessingUnit().getAddress()), cmd.getProcessingUnit().getVersion());

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("ProcessingUnit could not be registerd.", null, cmd.getOperationID());
            }
        }
        else if (data.isSetRemoveChildCategoryFromSensorCategory()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveChildCategoryFromSensorCategory cmd = data.getRemoveChildCategoryFromSensorCategory();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeChildCategoryFromSensorCategory(new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()), new SCAIReference(cmd.getParentSensorCategory().getId(), cmd.getParentSensorCategory().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Childcategory could not be removed from Category.", null, cmd.getOperationID());
                }
            }
        } else if(data.isSetRemoveConfigurationParameter())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveConfigurationParameter cmd = data.getRemoveConfigurationParameter();
            suc=false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeConfigurationParameter(new SCAIReference(cmd.getConfigurationParameter().getId(), cmd.getConfigurationParameter().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("ConfigurationParameter could not be removed.", null, cmd.getOperationID());
            }
        } else if(data.isSetRemoveDataElement())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveDataElement cmd = data.getRemoveDataElement();
            suc=false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeDataElement(new SCAIReference(cmd.getDataElement().getId(), cmd.getDataElement().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataElement could not be removed.", null, cmd.getOperationID());
            }
        } else if (data.isSetRemoveDataElementFromDataStreamType()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveDataElementFromDataStreamType cmd = data.getRemoveDataElementFromDataStreamType();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeDataElementFromDataStreamType(new SCAIReference(cmd.getDataElement().getId(), cmd.getDataElement().getName()), new SCAIReference(cmd.getDataStreamType().getId(), cmd.getDataStreamType().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("DataElement could not be removed from DataStreamType.", null, cmd.getOperationID());
                }
            }
        } else if(data.isSetRemoveDataStreamType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveDataStreamType cmd = data.getRemoveDataStreamType();
            suc=false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeDataStreamType(new SCAIReference(cmd.getDataStreamType().getId(), cmd.getDataStreamType().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataStreamType could not be removed.", null, cmd.getOperationID());
            }
        } else if(data.isSetRemoveDataType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveDataType cmd = data.getRemoveDataType();
            suc=false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeDataType(new SCAIReference(cmd.getDataType().getId(), cmd.getDataType().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("DataType could not be removed.", null, cmd.getOperationID());
            }
        } else if(data.isSetRemoveOperator())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveOperator cmd = data.getRemoveOperator();
            suc=false;
            suc = scaiObject.getControlModules().getProcessingControl().removeOperator(new SCAIReference(cmd.getOperator().getId(), cmd.getOperator().getName()),new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Operator could not be removed.", null, cmd.getOperationID());
            }
        } else if(data.isSetRemoveOperatorGroup())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveOperatorGroup cmd = data.getRemoveOperatorGroup();
            suc=false;
            suc = scaiObject.getControlModules().getProcessingControl().removeOperatorGroup(new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be removed.", null, cmd.getOperationID());
            }
        } else if(data.isSetRemoveOperatorType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveOperatorType cmd = data.getRemoveOperatorType();
            suc=false;
            suc = scaiObject.getControlModules().getProcessingControl().removeOperatorType(new SCAIReference(cmd.getOperatorType().getId(), cmd.getOperatorType().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorType could not be removed.", null, cmd.getOperationID());
            }
        } else if(data.isSetRemoveSensor())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensor cmd = data.getRemoveSensor();
            suc = false;

            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensor(new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be removed.", null, cmd.getOperationID());
            }
        } else if (data.isSetRemoveSensorCategory()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensorCategory cmd = data.getRemoveSensorCategory();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensorCategory(new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()));

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("SensorCategory could not be removed.", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetRemoveSensorCategoryFromSensorDomain()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensorCategoryFromSensorDomain cmd = data.getRemoveSensorCategoryFromSensorDomain();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensorCategoryFromSensorDomain(new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()), new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Category could not be removed from Domain.", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetRemoveSensorDomain()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensorDomain cmd = data.getRemoveSensorDomain();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensorDomain(new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName()));

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("SensorDomain could not be removed.", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetRemoveSensorType())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensorType cmd = data.getRemoveSensorType();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensorType(new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("SensorType could not be removed.", null, cmd.getOperationID());
            }
        } else if (data.isSetRemoveSensorTypeFromSensorCategory()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensorTypeFromSensorCategory cmd = data.getRemoveSensorTypeFromSensorCategory();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensorTypeFromSensorCategory(new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName()), new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Type could not be removed from Category.", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetRemoveSensorTypeFromSensorDomain()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.RemoveSensorTypeFromSensorDomain cmd = data.getRemoveSensorTypeFromSensorDomain();
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().removeSensorTypeFromSensorDomain(new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName()), new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Type could not be removed from Category.", null, cmd.getOperationID());
                }
            }
        } else if(data.isSetUndeployOperatorGroup())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UndeployOperatorGroup cmd = data.getUndeployOperatorGroup();
            suc = false;

            suc=scaiObject.getControlModules().getProcessingControl().undeployOperatorGroup(new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be undeployed.", null, cmd.getOperationID());
            }
        } else if(data.isSetUnlinkOperators())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UnlinkOperators cmd = data.getUnlinkOperators();
            suc=false;

            if(cmd.getOperatorLink().getSource().isSetInputOperator() && cmd.getOperatorLink().getDestination().isSetServiceOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().unlinkInputToOperator(new SCAIReference(cmd.getOperatorLink().getSource().getInputOperator().getId(), cmd.getOperatorLink().getSource().getInputOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getServiceOperator().getId(), cmd.getOperatorLink().getDestination().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }
            else if(cmd.getOperatorLink().getSource().isSetInputOperator() && cmd.getOperatorLink().getDestination().isSetOutputOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().unlinkInputToOutput(new SCAIReference(cmd.getOperatorLink().getSource().getInputOperator().getId(), cmd.getOperatorLink().getSource().getInputOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getOutputOperator().getId(), cmd.getOperatorLink().getDestination().getOutputOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }
            else if(cmd.getOperatorLink().getSource().isSetServiceOperator() && cmd.getOperatorLink().getDestination().isSetServiceOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().unlinkOperatorToOperator(new SCAIReference(cmd.getOperatorLink().getSource().getServiceOperator().getId(), cmd.getOperatorLink().getSource().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getServiceOperator().getId(), cmd.getOperatorLink().getDestination().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }
            else if(cmd.getOperatorLink().getSource().isSetServiceOperator() && cmd.getOperatorLink().getDestination().isSetOutputOperator())
            {
                suc = scaiObject.getControlModules().getProcessingControl().unlinkOperatorToOutput(new SCAIReference(cmd.getOperatorLink().getSource().getServiceOperator().getId(), cmd.getOperatorLink().getSource().getServiceOperator().getName()), new SCAIReference(cmd.getOperatorLink().getDestination().getOutputOperator().getId(), cmd.getOperatorLink().getDestination().getOutputOperator().getName()), new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName()));
            }

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Operator could not be unlinked.", null, cmd.getOperationID());
            }
        }  else if(data.isSetUnregisterProcessingUnit())
        {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UnregisterProcessingUnit cmd = data.getUnregisterProcessingUnit();
            suc = false;

            suc=scaiObject.getControlModules().getProcessingControl().unregisterProcessingUnit(new SCAIReference(cmd.getProcessingUnit().getId(), cmd.getProcessingUnit().getName()));

            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("ProcessingUnit could not be unregistered.", null, cmd.getOperationID());
            }
        } else if (data.isSetUpdateConfigurationParameter()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateConfigurationParameter cmd = data.getUpdateConfigurationParameter();
            suc = false;
            SCAIReference oldConfigurationParameter = new SCAIReference(cmd.getConfigurationParameter().getId(), cmd.getConfigurationParameter().getName());
            ConfigurationParameterDescription newConfigurationParameter = cmd.getNewConfigurationParameter();
            String name = newConfigurationParameter.getName();
            boolean optional = newConfigurationParameter.getOptional();

            if (cmd.getNewConfigurationParameter().isSetAtomicParameter()) {
                ConfigurationParameterDescription.AtomicParameter atomicParameter = newConfigurationParameter.getAtomicParameter();
                String uom = atomicParameter.getUom();
                boolean identifier = atomicParameter.getIdentifier();

                SCAIReference dataTypeReference = new SCAIReference(
                        atomicParameter.getDataType().getId(),
                        atomicParameter.getDataType().getName());

                suc = scaiObject.getControlModules().getSensorRegistryControl().updateConfigurationParameterAtomic(oldConfigurationParameter, name, optional, uom, identifier, dataTypeReference);

            } else if (cmd.getNewConfigurationParameter().isSetComplexParameter()) {

                SCAIReference[] configurationParameters = convertToReferenceArray(newConfigurationParameter.getComplexParameter().getConfigurationParameterArray());

                suc = scaiObject.getControlModules().getSensorRegistryControl().updateConfigurationParameterComplex(oldConfigurationParameter, name, optional, configurationParameters);
                
            } else if (cmd.getNewConfigurationParameter().isSetSequenceParameter()) {
                ConfigurationParameterDescription.SequenceParameter sequenceParameter = newConfigurationParameter.getSequenceParameter();
                int minlength = sequenceParameter.getMinlength();
                int maxlength = sequenceParameter.getMaxlength();

                SCAIReference configurationParameter = new SCAIReference(
                        sequenceParameter.getConfigurationParameter().getId(),
                        sequenceParameter.getConfigurationParameter().getName());

                suc = scaiObject.getControlModules().getSensorRegistryControl().updateConfigurationParameterSequence(oldConfigurationParameter, name, optional, minlength, maxlength, configurationParameter);
            }

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("ConfigurationParameter could not be updated", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetUpdateDataElement()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateDataElement cmd = data.getUpdateDataElement();
            suc = false;
            SCAIReference oldDataElement = new SCAIReference(cmd.getDataElement().getId(), cmd.getDataElement().getName());
            if (cmd.getNewDataElement().isSetAtomicElement()) {
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataElementAtomic(
                        oldDataElement,
                        cmd.getNewDataElement().getAtomicElement().getName(),
                        new SCAIReference(cmd.getNewDataElement().getAtomicElement().getDataType().getId(), cmd.getNewDataElement().getAtomicElement().getDataType().getName()));
            }
            else if (cmd.getNewDataElement().isSetComplexElement()) {
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataElementComplex(
                        oldDataElement,
                        cmd.getNewDataElement().getComplexElement().getName(),
                        convertToReferenceArray(cmd.getNewDataElement().getComplexElement().getDataElementArray()));
            }
        } else if (data.isSetUpdateDataStreamType()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateDataStreamType cmd = data.getUpdateDataStreamType();
            suc = false;
            SCAIReference oldDataStreamType = new SCAIReference(cmd.getDataStreamType().getId(), cmd.getDataStreamType().getName());
            suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataStreamType(oldDataStreamType,
                    cmd.getNewDataStreamType().getName(),
                    convertToReferenceArray(cmd.getNewDataStreamType().getDataElementArray()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("DataStreamtype could not be updated", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetUpdateDataType()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateDataType cmd = data.getUpdateDataType();
            suc = false;
            SCAIReference oldDataType = new SCAIReference(cmd.getDataType().getId(), cmd.getDataType().getName());
            if(cmd.getNewDataType().isSetDecimalType())
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataTypeDecimal(
                        oldDataType,
                        cmd.getNewDataType().getName(),
                        cmd.getNewDataType().getDecimalType().isSetMin(),
                        cmd.getNewDataType().getDecimalType().getMin(),
                        cmd.getNewDataType().getDecimalType().isSetMax(),
                        cmd.getNewDataType().getDecimalType().getMax(),
                        cmd.getNewDataType().getDecimalType().isSetScale(),
                        cmd.getNewDataType().getDecimalType().getScale(),
                        cmd.getNewDataType().getDecimalType().getDefaultvalue()
                        );
            else if(cmd.getNewDataType().isSetStringType())
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataTypeString(
                        oldDataType,
                        cmd.getNewDataType().getName(),
                        cmd.getNewDataType().getStringType().isSetMin(),
                        cmd.getNewDataType().getStringType().getMin(),
                        cmd.getNewDataType().getStringType().isSetMax(),
                        cmd.getNewDataType().getStringType().getMax(),
                        cmd.getNewDataType().getStringType().isSetRegex(),
                        cmd.getNewDataType().getStringType().getRegex(),
                        cmd.getNewDataType().getStringType().getDefaultvalue()
                        );
            else if(cmd.getNewDataType().isSetBinaryType())
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataTypeBinary(
                        oldDataType,
                        cmd.getNewDataType().getName(),
                        cmd.getNewDataType().getBinaryType().isSetMin(),
                        cmd.getNewDataType().getBinaryType().getMin(),
                        cmd.getNewDataType().getBinaryType().isSetMax(),
                        cmd.getNewDataType().getBinaryType().getMax(),
                        cmd.getNewDataType().getBinaryType().getDefaultvalue()
                        );
            else if (cmd.getNewDataType().isSetEnumType()) {
                HashMap<Long, String> allowedValues = new HashMap<Long, String>();
                for (int i = 0; i < cmd.getNewDataType().getEnumType().getAllowedvalueArray().length; i++) {
                    allowedValues.put(cmd.getNewDataType().getEnumType().getAllowedvalueArray(i).getOrdinal(), cmd.getNewDataType().getEnumType().getAllowedvalueArray(i).getName());
                }
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataTypeEnum(
                        oldDataType,
                        cmd.getNewDataType().getName(),
                        (cmd.getNewDataType().getEnumType().getAllowedvalueArray().length > 0),
                        allowedValues,
                        cmd.getNewDataType().getEnumType().getDefaultvalue());
            }
            else if (cmd.getNewDataType().isSetListType()) {
                suc = scaiObject.getControlModules().getSensorRegistryControl().updateDataTypeList(
                        oldDataType,
                        cmd.getNewDataType().getName(),
                        cmd.getNewDataType().getListType().isSetMin(),
                        cmd.getNewDataType().getListType().getMin(),
                        cmd.getNewDataType().getListType().isSetMax(),
                        cmd.getNewDataType().getListType().getMax(),
                        (cmd.getNewDataType().getListType().getAllowedvalueArray().length > 0),
                        cmd.getNewDataType().getListType().getAllowedvalueArray(),
                        cmd.getNewDataType().getListType().getDefaultvalueArray());
            }
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("DataType could not be updated", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetUpdateOperator()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateOperator cmd = data.getUpdateOperator();
            suc = false;
            SCAIOperator newOperator = convertToSCAIOperator(cmd.getOperator().getNewOperator());
            SCAIReference oldOperator = new SCAIReference(cmd.getOperator().getOldOperator().getId(), cmd.getOperator().getOldOperator().getName());
            SCAIReference operatorGroup = new SCAIReference(cmd.getOperatrorGroup().getId(), cmd.getOperatrorGroup().getName());

            suc = scaiObject.getControlModules().getProcessingControl().updateOperator(oldOperator, newOperator, operatorGroup);

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Operator could not be updated", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetUpdateOperatorGroup()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateOperatorGroup cmd = data.getUpdateOperatorGroup();
            suc = false;
            SCAIReference oldOperator = new SCAIReference(cmd.getOperatorGroup().getId(), cmd.getOperatorGroup().getName());
            suc = scaiObject.getControlModules().getProcessingControl().updateOperatorGroup(oldOperator, cmd.getNewOperatorGroup().getName());

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorGroup could not be updated", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetUpdateOperatorType()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateOperatorType cmd = data.getUpdateOperatorType();
            HashMap<String, String> properties = new HashMap<String, String>();
            HashMap<String, Boolean> readOnly = new HashMap<String, Boolean>();
            for(OperatorTypeDescription.Property pro: cmd.getNewOperatorType().getPropertyArray())
            {
                properties.put(pro.getKey(), pro.getValue());
                readOnly.put(pro.getKey(), pro.getReadOnly());
            }
            suc = false;
            SCAIReference oldOperatorType = new SCAIReference(cmd.getOperatorType().getId(), cmd.getOperatorType().getName());
            suc = scaiObject.getControlModules().getProcessingControl().updateOperatorType(
                    oldOperatorType,
                    cmd.getNewOperatorType().getName(),
                    cmd.getNewOperatorType().getMetaType(), 
                    properties,
                    readOnly,
                    cmd.getNewOperatorType().getDescription());

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("OperatorType could not be updated", null, cmd.getOperationID());
                }
            }
        } else if (data.isSetUpdateSensor()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateSensor cmd = data.getUpdateSensor();
            SCAISensorReference oldSensor = new SCAISensorReference(cmd.getSensor().getSensorID(), cmd.getSensor().getNameReference().getSensorName(), cmd.getSensor().getNameReference().getSensorDomainName());
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().updateSensor(oldSensor,
                    cmd.getNewSensor().getName(),
                    new SCAIReference(cmd.getNewSensor().getSensorDomain().getId(),
                    cmd.getNewSensor().getSensorDomain().getName()),
                    new SCAIReference(cmd.getNewSensor().getSensorType().getId(),
                    cmd.getNewSensor().getSensorType().getName()),
                    cmd.getNewSensor().getVirtual());
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("Sensor could not be updated.", null, cmd.getOperationID());
            }
        } else if (data.isSetUpdateSensorCategory()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateSensorCategory cmd = data.getUpdateSensorCategory();
            SCAIReference oldSensorCategory = new SCAIReference(cmd.getSensorCategory().getId(), cmd.getSensorCategory().getName());
            suc = false;
            SCAIReference parent = null;
            if(cmd.getNewSensorCategory().isSetParentSensorCategory())
                parent =  new SCAIReference(cmd.getNewSensorCategory().getParentSensorCategory().getId(), cmd.getNewSensorCategory().getParentSensorCategory().getName());
            suc = scaiObject.getControlModules().getSensorRegistryControl().updateSensorCategory(oldSensorCategory, cmd.getNewSensorCategory().getName(), parent, convertToReferenceArray(cmd.getNewSensorCategory().getSensorDomainArray()));
            if(cmd.isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("SensorCategory could not be updated.", null, cmd.getOperationID());
            }
        } else if (data.isSetUpdateSensorDomain()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateSensorDomain cmd = data.getUpdateSensorDomain();
            SCAIReference oldSensorDomain = new SCAIReference(cmd.getSensorDomain().getId(), cmd.getSensorDomain().getName());
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().updateSensorDomain(oldSensorDomain, cmd.getNewSensorDomain().getName());
            if(cmd. isSetOperationID())
            {
                if(suc)
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                else
                    scaiObject.getBuilder().addAcknowledgmentException("SensorDomain could not be updated.", null, cmd.getOperationID());
            }
        } else if (data.isSetUpdateSensorType()) {
            SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateSensorType cmd = data.getUpdateSensorType();
            SCAIReference oldSensorType = new SCAIReference(cmd.getSensorType().getId(), cmd.getSensorType().getName());
            suc = false;
            suc = scaiObject.getControlModules().getSensorRegistryControl().updateSensorType(
                    oldSensorType,
                    cmd.getNewSensorType().getName(),
                    cmd.getNewSensorType().getAdapter(),
                    new SCAIReference(cmd.getNewSensorType().getDataStreamType().getId(), cmd.getNewSensorType().getDataStreamType().getName()),
                    convertToReferenceArray(cmd.getNewSensorType().getConfigurationParameterArray()),
                    convertToReferenceArray(cmd.getNewSensorType().getSensorCategoryArray()),
                    convertToReferenceArray(cmd.getNewSensorType().getSensorDomainArray()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("SensorType could not be updated.", null, cmd.getOperationID());
                }
            }
        } else {
            scaiObject.getBuilder().addAcknowledgmentException("Unknown cmd was ignored", null, "Unknown");
        }
    }

    private void processAccessControl(de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.AccessControl data) {
        boolean suc = false;
        Object tempData = null;

        // <editor-fold desc="CreateUser">
        if (data.isSetCreateUser()) {
            suc = false;
            SCAIDocument.SCAI.Payload.ControlData.AccessControl.CreateUser cmd = data.getCreateUser();

            SCAIPermission[] permissions = processPermissions(data.getCreateUser().getUser());

            suc = scaiObject.getControlModules().getAccessControl().createUser(
                    data.getCreateUser().getUser().getUsername(),
                    new String(data.getCreateUser().getUser().getPassword()),
                    permissions);

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("User could not be created.", null, cmd.getOperationID());
                }
            }
        }
        // </editor-fold>
        // <editor-fold desc="RemoveUser">
        else if (data.isSetRemoveUser()) {
            suc = false;
            SCAIDocument.SCAI.Payload.ControlData.AccessControl.RemoveUser cmd = data.getRemoveUser();
            suc = scaiObject.getControlModules().getAccessControl().removeUser(
                    new SCAIReference(data.getRemoveUser().getUser().getId(),
                    data.getRemoveUser().getUser().getName()));
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("User " + data.getRemoveUser().getUser().getName() + " could not be removed.", null, cmd.getOperationID());
                }
            }
        }
        // </editor-fold>
        // <editor-fold desc="DestroySession">
        else if (data.isSetDestroySession()) {
            suc = false;
            SCAIDocument.SCAI.Payload.ControlData.AccessControl.DestroySession cmd = data.getDestroySession();
            suc = scaiObject.getControlModules().getAccessControl().destroySession(
                    data.getDestroySession().getSessionID());
            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("Session " + data.getDestroySession().getSessionID() + " could not be destroyed.", null, cmd.getOperationID());
                }
            }
        }
        // </editor-fold>
        // <editor-fold desc="UpdateUser">
        else if (data.isSetUpdateUser()) {
            suc = false;
            SCAIDocument.SCAI.Payload.ControlData.AccessControl.UpdateUser cmd = data.getUpdateUser();

            SCAIPermission[] permissions = processPermissions(data.getUpdateUser().getNewUser());

            suc = scaiObject.getControlModules().getAccessControl().updateUser(
                    new SCAIReference(
                        data.getUpdateUser().getUser().getId(),
                        data.getUpdateUser().getUser().getName()),
                    data.getUpdateUser().getNewUser().getUsername(),
                    new String(data.getUpdateUser().getNewUser().getPassword()),
                    permissions);

            if (cmd.isSetOperationID()) {
                if (suc) {
                    scaiObject.getBuilder().addAcknowledgmentSuccess(null, cmd.getOperationID());
                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("User could not be updated.", null, cmd.getOperationID());
                }
            }
        }
        // </editor-fold>
        // <editor-fold desc="AuthenticateUser">
        else if (data.isSetAuthenticateUser()) {
            AuthenticateUser cmd = data.getAuthenticateUser();

            tempData = (String) scaiObject.getControlModules().getAccessControl().authenticateUser(data.getAuthenticateUser().getUsername(), new String(data.getAuthenticateUser().getPassword()));
            if (cmd.isSetOperationID()) {
                if (tempData != null) {

                    scaiObject.getBuilder().addAcknowledgmentData(scaiObject.getBuilder().buildReplyDataSecurityToken((String) tempData), cmd.getOperationID());

                } else {
                    scaiObject.getBuilder().addAcknowledgmentException("User could not be authenticated.", null, cmd.getOperationID());
                }
            }
        }
        // </editor-fold>
        // <editor-fold desc="GetUserData">
        else if (data.isSetGetUserData()) {
            GetUserData cmd = data.getGetUserData();

            tempData = (UserDescription)scaiObject.getControlModules().getAccessControl().getUserData(
                    new SCAIReference(
                        data.getGetUserData().getUser().getId(),
                        data.getGetUserData().getUser().getName()),
                    scaiObject.getBuilder());
            if (tempData != null) {
                scaiObject.getBuilder().addAcknowledgmentData(tempData, cmd.getOperationID());
            } else {
                scaiObject.getBuilder().addAcknowledgmentException("Could not retrieve user data.", null, cmd.getOperationID());
            }
        }
        // </editor-fold>
    }

    private SCAIPermission[] processPermissions(UserDescription data) {
        if (data.getPermissionArray().length == 0) {
            return null;
        }
        SCAIPermission[] result = new SCAIPermission[data.getPermissionArray().length];
        for (int i = 0; i < data.getPermissionArray().length; i++) {
            PermissionDescription pd = data.getPermissionArray(i);
            Permission permission = pd.getPermission();
            result[i] = new SCAIPermission();
            if (permission.isSetGrant()) {
                result[i].setPrivilegeAction(SCAIPermission.PrivilegeActions.GRANT);
                result[i].setPrivilege(permission.getGrant());
            }
            else if (permission.isSetWithdraw()) {
                result[i].setPrivilegeAction(SCAIPermission.PrivilegeActions.WITHDRAW);
                result[i].setPrivilege(permission.getWithdraw());
            }
            if (pd.isSetInheritable()) {
                result[i].setInheritable(pd.getInheritable());
            }
            for (PermissionDescription.Property property : pd.getPropertyArray()) {
                result[i].getProperties().put(property.getKey(), property.getValue());
            }
        }
        return result;
    }


    private void processMeasurements(Measurements measurements)
    {
        for(SensorDataDescription sensorData: measurements.getDataStreamArray())
        {
            scaiObject.getControlModules().getProcessingControl().processSensorData(sensorData);
        }
    }

    private void processAcknowledgment(Acknowledgment data)
    {
        if(data.getExceptionArray().length > 0)
        {
            //TODO: implement
        }
        else if(data.getSuccessArray().length > 0)
        {
            //TODO: implement
        }
        else if(data.getReplyArray().length > 0)
        {
            //TODO: implement
        }
    }

    private void processIdentification(Identification data) {
        String securityToken = null;
        System.out.println("DEBUG: processIdentification");
        if (data.isSetSecurityToken()) {
            securityToken = data.getSecurityToken();
        }
        else if (data.isSetLoginIdentification()) {
            securityToken = (String)scaiObject.getControlModules().getAccessControl().authenticateUser(
                    data.getLoginIdentification().getUsername(),
                    new String(data.getLoginIdentification().getPassword()));
        }
        System.out.println("securityToken: " + securityToken);
        scaiObject.getControlModules().getAccessControl().setSecurityToken(securityToken);
        scaiObject.getControlModules().getSensorControl().setSecurityToken(securityToken);
        scaiObject.getControlModules().getSensorRegistryControl().setSecurityToken(securityToken);
        scaiObject.getControlModules().getProcessingControl().setSecurityToken(securityToken);
    }

    private SCAIReference[] convertToReferenceArray(Reference[] ref)
    {
        try{
            SCAIReference[] ret = new SCAIReference[ref.length];
            for(int i=0; i < ref.length; i++)
            {
                ret[i] = new SCAIReference(ref[i].getId(), ref[i].getName());
            }
            return ret;
        }catch (Exception ex)
        {
            return null;
        }

    }

    // TODO: move these helpers to some adaptor package or factory patterns
    private SCAIConfigurationValue convertToSCAIConfigurationValue(ConfigurationValueDescription configurationValue) {
        SCAIConfigurationValue scaiConfigurationValue = new SCAIConfigurationValue();
        scaiConfigurationValue.setName(configurationValue.getName());
        if (configurationValue.isSetIndex()) {
            scaiConfigurationValue.setIndex(configurationValue.getIndex());
        }
        if (configurationValue.isSetValue()) {
            scaiConfigurationValue.setValue(configurationValue.getValue());
        } else {
            ArrayList<SCAIConfigurationValue> configurationValues = new ArrayList<SCAIConfigurationValue>();
            for (ConfigurationValueDescription configurationValueItem : configurationValue.getConfigurationValueArray()) {
                configurationValues.add(convertToSCAIConfigurationValue(configurationValueItem));
            }
            scaiConfigurationValue.setConfigurationValues(configurationValues);
        }
        return scaiConfigurationValue;
    }

    private SCAIOperator convertToSCAIOperator(OperatorDescription opd) {
        SCAIOperator result = new SCAIOperator();

        HashMap<String, String> properties = new HashMap<String, String>();
        for (OperatorDescription.Property pro : opd.getPropertyArray()) {
            properties.put(pro.getKey(), pro.getValue());
        }

        if (opd.isSetServiceOperator()) {
            result.setOperator();
            result.setName(opd.getName());
            result.setType(new SCAIReference(opd.getServiceOperator().getOperatorType().getId(), opd.getServiceOperator().getOperatorType().getName()));
            result.setProperties(properties);

        } else if (opd.isSetInputOperator()) {
            result.setInput();
            result.setName(opd.getName());
            result.setSensor(new SCAISensorReference(opd.getInputOperator().getSensor().getSensorID(), opd.getInputOperator().getSensor().getNameReference().getSensorName(), opd.getInputOperator().getSensor().getNameReference().getSensorDomainName()));

            result.setProperties(properties);

        } else if (opd.isSetOutputOperator()) {
            result.setOutput();
            result.setName(opd.getName());
            result.setSensor(new SCAISensorReference(opd.getOutputOperator().getSensor().getSensorID(), opd.getOutputOperator().getSensor().getNameReference().getSensorName(), opd.getOutputOperator().getSensor().getNameReference().getSensorDomainName()));

            result.setProperties(properties);
        }
        return result;
    }
}
