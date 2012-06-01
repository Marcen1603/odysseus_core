/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.translator;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.ITranslator;
import de.offis.xml.schema.scai20.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.*;
import de.offis.scampi.stack.scai.builder.types.*;
import de.offis.scampi.stack.scai.interpreter.interpreterText.Constants;
import de.offis.xml.schema.scai20.DataTypeDescription.EnumType;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Identification;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.AccessControl;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorControl;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.util.Base64;

public class TranslatorText implements ITranslator, IBuilder {
    private StringBuilder sb = new StringBuilder();
    private ArrayList<String> lines = new ArrayList();
    
    @Override
    public Object translate(Object data) {
        if (data == null)
            return null;
        else
            return assembleTranslation((SCAIDocument)data);
    }

    private void appendReference(SCAIReference reference) {
        if (reference.isReferenceByID()) {
            appendVariable("ri", reference.getId());
        }
        else if (reference.getName() != null) {
            appendVariable("rn", reference.getName());
        }
    }

    private void appendSensorReference(SCAISensorReference sensorReference) {
        if (sensorReference.isReferenceByID()) {
            appendVariable("ri", sensorReference.getId());
        }
        else if (sensorReference.getName() != null && sensorReference.getDomainName() != null) {
            appendVariable("rn", sensorReference.getName());
            appendVariable("dn", sensorReference.getDomainName());
        }
    }

    // TODO: move this to some factory pattern for SCAI Adapter Objects
    private SCAIReference convertReference(Reference reference) {
        SCAIReference result = null;
        if (reference.isSetName()) {
            result = new SCAIReference(reference.getName(), false);
        }
        else if (reference.isSetId()) {
            result = new SCAIReference(reference.getId(), true);
        }
        return result;
    }

    private SCAISensorReference convertSensorReference(SensorReference reference) {
        SCAISensorReference result = null;
        if (reference.isSetNameReference()) {
            result = new SCAISensorReference(reference.getNameReference().getSensorName(), reference.getNameReference().getSensorDomainName());
        }
        else if (reference.isSetSensorID()) {
            result = new SCAISensorReference(reference.getSensorID());
        }
        return result;
    }

    private SCAIConfigurationValue convertConfigurationValues(ConfigurationValueDescription configurationValue) {
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
                configurationValues.add(convertConfigurationValues(configurationValueItem));
            }
            scaiConfigurationValue.setConfigurationValues(configurationValues);
        }
        return scaiConfigurationValue;
    }
    
    private void translateAccessControl(AccessControl accessControl) {
        if (accessControl.isSetAuthenticateUser()) {
            addAuthenticateUser(accessControl.getAuthenticateUser().getUsername(), accessControl.getAuthenticateUser().getPassword(), accessControl.getAuthenticateUser().getOperationID());
        }
        else if (accessControl.isSetCreateUser()) {
            ArrayList<SCAIPermission> permissions = new ArrayList<SCAIPermission>();
            for (PermissionDescription permission : accessControl.getCreateUser().getUser().getPermissionArray()) {
                SCAIPermission scaiPermission = new SCAIPermission();
                if (permission.getPermission().isSetGrant()) {
                    scaiPermission.setPrivilege(permission.getPermission().getGrant());
                    scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.GRANT);
                }
                else if(permission.getPermission().isSetWithdraw()) {
                    scaiPermission.setPrivilege(permission.getPermission().getWithdraw());
                    scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.WITHDRAW);
                }
                for (PermissionDescription.Property property : permission.getPropertyArray()) {
                    scaiPermission.getProperties().put(property.getKey(), property.getValue());
                }
                if (permission.isSetInheritable()) {
                    scaiPermission.setInheritable(permission.getInheritable());
                }
            }
            addCreateUser(accessControl.getCreateUser().getUser().getUsername(), accessControl.getCreateUser().getUser().getPassword(), permissions.toArray(new SCAIPermission[0]), null, accessControl.getCreateUser().getOperationID());  // TODO: retrieve or remove user roles
        }
        else if (accessControl.isSetDestroySession()) {
            addDestroySession(accessControl.getDestroySession().getSessionID(), accessControl.getDestroySession().getOperationID());
        }
        else if (accessControl.isSetRemoveUser()) {
            addRemoveUser(new SCAIReference(accessControl.getRemoveUser().getUser().getId(), accessControl.getRemoveUser().getUser().getName()), accessControl.getRemoveUser().getOperationID());
        }
        else if (accessControl.isSetGetUserData()) {
            addGetUserData(new SCAIReference(accessControl.getGetUserData().getUser().getId(), accessControl.getGetUserData().getUser().getName()), accessControl.getGetUserData().getOperationID());
        }
        else if (accessControl.isSetGrantSensorAccess()) {
            ArrayList<SCAIReference> userAccessors = new ArrayList<SCAIReference>();
            ArrayList<SCAIReference> domainAccessors = new ArrayList<SCAIReference>();
            for (AccessorDescription accessor : accessControl.getGrantSensorAccess().getAccessorArray()) {
                if (accessor.isSetUser()) {
                    userAccessors.add(new SCAIReference(accessor.getUser().getId(), accessor.getUser().getName()));
                } else if (accessor.isSetSensorDomain()) {
                    domainAccessors.add(new SCAIReference(accessor.getSensorDomain().getId(), accessor.getSensorDomain().getName()));
                }
            }
            addGrantSensorAccess(convertSensorReference(accessControl.getGrantSensorAccess().getSensor()), userAccessors.toArray(new SCAIReference[0]), domainAccessors.toArray(new SCAIReference[0]), accessControl.getGrantSensorAccess().getOperationID());
        }
        else if (accessControl.isSetListAllSensorAccessors()) {
            addListAllSensorAccessors(convertSensorReference(accessControl.getListAllSensorAccessors().getSensor()), accessControl.getListAllSensorAccessors().getOperationID());
        }
        else if (accessControl.isSetUpdateUser()) {
            ArrayList<SCAIPermission> permissions = new ArrayList<SCAIPermission>();
            for (PermissionDescription permission : accessControl.getUpdateUser().getNewUser().getPermissionArray()) {
                SCAIPermission scaiPermission = new SCAIPermission();
                if (permission.getPermission().isSetGrant()) {
                    scaiPermission.setPrivilege(permission.getPermission().getGrant());
                    scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.GRANT);
                }
                else if(permission.getPermission().isSetWithdraw()) {
                    scaiPermission.setPrivilege(permission.getPermission().getWithdraw());
                    scaiPermission.setPrivilegeAction(SCAIPermission.PrivilegeActions.WITHDRAW);
                }
                for (PermissionDescription.Property property : permission.getPropertyArray()) {
                    scaiPermission.getProperties().put(property.getKey(), property.getValue());
                }
                if (permission.isSetInheritable()) {
                    scaiPermission.setInheritable(permission.getInheritable());
                }
            }
            addUpdateUser(new SCAIReference(accessControl.getUpdateUser().getUser().getId(), accessControl.getUpdateUser().getUser().getName()), accessControl.getUpdateUser().getNewUser().getUsername(), accessControl.getUpdateUser().getNewUser().getPassword(), permissions.toArray(new SCAIPermission[0]), null, accessControl.getUpdateUser().getOperationID());  // TODO: retrieve or remove user roles
        }
        else if (accessControl.isSetWithdrawSensorAccess()) {
            ArrayList<SCAIReference> userAccessors = new ArrayList<SCAIReference>();
            ArrayList<SCAIReference> domainAccessors = new ArrayList<SCAIReference>();
            for (AccessorDescription accessor : accessControl.getWithdrawSensorAccess().getAccessorArray()) {
                if (accessor.isSetUser()) {
                    userAccessors.add(new SCAIReference(accessor.getUser().getId(), accessor.getUser().getName()));
                } else if (accessor.isSetSensorDomain()) {
                    domainAccessors.add(new SCAIReference(accessor.getSensorDomain().getId(), accessor.getSensorDomain().getName()));
                }
            }
            addWithdrawSensorAccess(convertSensorReference(accessControl.getWithdrawSensorAccess().getSensor()), userAccessors.toArray(new SCAIReference[0]), domainAccessors.toArray(new SCAIReference[0]), accessControl.getWithdrawSensorAccess().getOperationID());
        }
    }

    private void translateSensorControl(SensorControl sensorControl) {
        if (sensorControl.isSetConfigureSensor()) {
            SCAIConfigurationValue[] scaiConfigurationValue = new SCAIConfigurationValue[1]; // TODO: fix the interface this does not have to be an array
            scaiConfigurationValue[0] = convertConfigurationValues(sensorControl.getConfigureSensor().getConfigurationValue());
            addConfigureSensor(convertSensorReference(sensorControl.getConfigureSensor().getSensor()), scaiConfigurationValue, sensorControl.getConfigureSensor().getOperationID());
        }
        else if (sensorControl.isSetGetConfiguration()) {
            addGetConfiguration(convertSensorReference(sensorControl.getGetConfiguration().getSensor()), sensorControl.getGetConfiguration().getOperationID());
        }
        else if (sensorControl.isSetGetSupportedModules()) {
            addGetSupportedModules(convertSensorReference(sensorControl.getGetSupportedModules().getSensor()), sensorControl.getGetSupportedModules().getOperationID());
        }
        else if (sensorControl.isSetGetValue()) {
            addGetValue(convertSensorReference(sensorControl.getGetValue().getSensor()), sensorControl.getGetValue().getOperationID());
        }
        else if (sensorControl.isSetUnsubscribeDatastream()) {
            addUnsubscribeDatastream(convertSensorReference(sensorControl.getUnsubscribeDatastream().getSensor()), sensorControl.getUnsubscribeDatastream().getOperationID());
        }
        else if (sensorControl.isSetStartSensor()) {
            addStartSensor(convertSensorReference(sensorControl.getStartSensor().getSensor()), sensorControl.getStartSensor().getOperationID());
        }
        else if (sensorControl.isSetStopSensor()) {
            addStopSensor(convertSensorReference(sensorControl.getStopSensor().getSensor()), sensorControl.getStopSensor().getOperationID());
        }
        else if (sensorControl.isSetSubscribeDatastream()) {
            addSubscribeDatastream(convertSensorReference(sensorControl.getSubscribeDatastream().getSensor()), sensorControl.getSubscribeDatastream().getOperationID());
        }
    }

    private void translateSensorRegistryControl(SensorRegistryControl sensorRegistryControl) {
        if (sensorRegistryControl.isSetAddChildCategoryToSensorCategory()) {
            addAddChildCategoryToSensorCategory(new SCAIReference(sensorRegistryControl.getAddChildCategoryToSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getAddChildCategoryToSensorCategory().getSensorCategory().getName()), new SCAIReference(sensorRegistryControl.getAddChildCategoryToSensorCategory().getParentSensorCategory().getId(), sensorRegistryControl.getAddChildCategoryToSensorCategory().getParentSensorCategory().getName()), sensorRegistryControl.getAddChildCategoryToSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetAddDataElementToDataStreamType()) {
            addAddDataElementToDataStreamType(new SCAIReference(sensorRegistryControl.getAddDataElementToDataStreamType().getDataElement().getId(), sensorRegistryControl.getAddDataElementToDataStreamType().getDataElement().getName()), new SCAIReference(sensorRegistryControl.getAddDataElementToDataStreamType().getDataStreamType().getId(), sensorRegistryControl.getAddDataElementToDataStreamType().getDataStreamType().getName()), sensorRegistryControl.getAddDataElementToDataStreamType().getOperationID());
        }
        else if (sensorRegistryControl.isSetAddSensorCategoryToSensorDomain()) {
            addAddSensorCategoryToSensorDomain(new SCAIReference(sensorRegistryControl.getAddSensorCategoryToSensorDomain().getSensorCategory().getId(), sensorRegistryControl.getAddSensorCategoryToSensorDomain().getSensorCategory().getName()), new SCAIReference(sensorRegistryControl.getAddSensorCategoryToSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getAddSensorCategoryToSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getAddSensorCategoryToSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetAddSensorTypeToSensorCategory()) {
            addAddSensorTypeToSensorCategory(new SCAIReference(sensorRegistryControl.getAddSensorTypeToSensorCategory().getSensorType().getId(), sensorRegistryControl.getAddSensorTypeToSensorCategory().getSensorType().getName()), new SCAIReference(sensorRegistryControl.getAddSensorTypeToSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getAddSensorTypeToSensorCategory().getSensorCategory().getName()), sensorRegistryControl.getAddSensorTypeToSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetAddSensorTypeToSensorDomain()) {
            addAddSensorTypeToSensorDomain(new SCAIReference(sensorRegistryControl.getAddSensorTypeToSensorDomain().getSensorType().getId(), sensorRegistryControl.getAddSensorTypeToSensorDomain().getSensorType().getName()), new SCAIReference(sensorRegistryControl.getAddSensorTypeToSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getAddSensorTypeToSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getAddSensorTypeToSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetCreateConfigurationParameter()) {
            ConfigurationParameterDescription cfgpm = sensorRegistryControl.getCreateConfigurationParameter().getConfigurationParameter();
            if (sensorRegistryControl.getCreateConfigurationParameter().getConfigurationParameter().isSetAtomicParameter()) {
                addCreateConfigurationParameterAtomicParameter(cfgpm.getName(), cfgpm.getOptional(), cfgpm.getAtomicParameter().getUom(), cfgpm.getAtomicParameter().getIdentifier(), new SCAIReference(cfgpm.getAtomicParameter().getDataType().getId(), cfgpm.getAtomicParameter().getDataType().getName()), sensorRegistryControl.getCreateConfigurationParameter().getOperationID());
            }
            else if (sensorRegistryControl.getCreateConfigurationParameter().getConfigurationParameter().isSetComplexParameter()) {
                SCAIReference[] configurationParameters = new SCAIReference[cfgpm.getComplexParameter().getConfigurationParameterArray().length];
                for (int i = 0; i < configurationParameters.length; i++) {
                    configurationParameters[i] = new SCAIReference(cfgpm.getComplexParameter().getConfigurationParameterArray(i).getId(), cfgpm.getComplexParameter().getConfigurationParameterArray(i).getName());
                }
                addCreateConfigurationParameterComplexParameter(cfgpm.getName(), cfgpm.getOptional(), configurationParameters, sensorRegistryControl.getCreateConfigurationParameter().getOperationID());
            }
            else if (sensorRegistryControl.getCreateConfigurationParameter().getConfigurationParameter().isSetSequenceParameter()) {
                addCreateConfigurationParameterSequenceParameter(cfgpm.getName(), cfgpm.getOptional(), cfgpm.getSequenceParameter().getMinlength(), cfgpm.getSequenceParameter().getMaxlength(), new SCAIReference(cfgpm.getSequenceParameter().getConfigurationParameter().getId(), cfgpm.getSequenceParameter().getConfigurationParameter().getName()), sensorRegistryControl.getCreateConfigurationParameter().getOperationID());
            }
        }
        else if (sensorRegistryControl.isSetCreateDataElement()) {
            DataElementDescription datel = sensorRegistryControl.getCreateDataElement().getDataElement();
            if (datel.isSetAtomicElement()) {
                addCreateDataElementAtomicElement(datel.getAtomicElement().getName(), new SCAIReference(datel.getAtomicElement().getDataType().getId(), datel.getAtomicElement().getDataType().getName()), sensorRegistryControl.getCreateDataElement().getOperationID());
            }
            else if (datel.isSetComplexElement()) {
                SCAIReference[] dataElements = new SCAIReference[datel.getComplexElement().getDataElementArray().length];
                for (int i = 0; i < dataElements.length; i++) {
                    dataElements[i] = new SCAIReference(datel.getComplexElement().getDataElementArray(i).getId(), datel.getComplexElement().getDataElementArray(i).getName());
                }
                addCreateDataElementComplexElement(datel.getComplexElement().getName(), dataElements, sensorRegistryControl.getCreateDataElement().getOperationID());
            }
        }
        else if (sensorRegistryControl.isSetCreateDataStreamType()) {
            DataStreamTypeDescription dstyp = sensorRegistryControl.getCreateDataStreamType().getDataStreamType();
            SCAIReference[] dataElements = new SCAIReference[dstyp.getDataElementArray().length];
            for (int i = 0; i < dataElements.length; i++) {
                dataElements[i] = new SCAIReference(dstyp.getDataElementArray(i).getId(), dstyp.getDataElementArray(i).getName());
            }
            addCreateDataStreamType(dstyp.getName(), dataElements, sensorRegistryControl.getCreateDataStreamType().getOperationID());
        }
        // <editor-fold desc="createDataType">
        else if (sensorRegistryControl.isSetCreateDataType()) {
            DataTypeDescription datyp = sensorRegistryControl.getCreateDataType().getDataType();
            if (sensorRegistryControl.getCreateDataType().getDataType().isSetBinaryType()) {
                addCreateDataTypeBinaryType(datyp.getName(), datyp.getBinaryType().isSetMin()?datyp.getBinaryType().getMin():null, datyp.getBinaryType().isSetMax()?datyp.getBinaryType().getMax():null, datyp.getBinaryType().getDefaultvalue(), sensorRegistryControl.getCreateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getCreateDataType().getDataType().isSetDecimalType()) {
                addCreateDataTypeDecimalType(datyp.getName(), datyp.getDecimalType().isSetMin()?datyp.getDecimalType().getMin():null, datyp.getDecimalType().isSetMax()?datyp.getDecimalType().getMax():null, datyp.getDecimalType().isSetScale()?datyp.getDecimalType().getScale():null, datyp.getDecimalType().getDefaultvalue(), sensorRegistryControl.getCreateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getCreateDataType().getDataType().isSetEnumType()) {
                HashMap<Long,String> allowedValues = new HashMap<Long,String>();
                for (EnumType.Allowedvalue allowedValue : datyp.getEnumType().getAllowedvalueArray()) {
                    allowedValues.put(allowedValue.getOrdinal(), allowedValue.getName());
                }
                addCreateDataTypeEnumType(datyp.getName(), allowedValues, datyp.getEnumType().getDefaultvalue(), sensorRegistryControl.getCreateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getCreateDataType().getDataType().isSetListType()) {
                String[] allowedValues = new String[datyp.getListType().getAllowedvalueArray().length];
                for (int i = 0; i < allowedValues.length; i++) {
                    allowedValues[i] = datyp.getListType().getAllowedvalueArray(i);

                }
                String[] defaultValues = new String[datyp.getListType().getDefaultvalueArray().length];
                for (int i = 0; i < defaultValues.length; i++) {
                    defaultValues[i] = datyp.getListType().getDefaultvalueArray(i);

                }
                addCreateDataTypeListType(datyp.getName(), datyp.getListType().isSetMin()?datyp.getListType().getMin():null, datyp.getListType().isSetMin()?datyp.getListType().getMax():null, allowedValues, defaultValues, sensorRegistryControl.getCreateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getCreateDataType().getDataType().isSetStringType()) {
                addCreateDataTypeStringType(datyp.getName(), datyp.getStringType().getMin(), datyp.getStringType().getMax(), datyp.getStringType().getRegex(), datyp.getStringType().getDefaultvalue(), sensorRegistryControl.getCreateDataType().getOperationID());
            }
        }
        // </editor-fold>
        else if (sensorRegistryControl.isSetCreateOperator()) {
            OperatorDescription op = sensorRegistryControl.getCreateOperator().getOperator();
            SCAIReference group = new SCAIReference(sensorRegistryControl.getCreateOperator().getOperatorGroup().getId(), sensorRegistryControl.getCreateOperator().getOperatorGroup().getName());
            HashMap<String, String> properties = new HashMap<String, String>();
            for (OperatorDescription.Property property : op.getPropertyArray()) {
                properties.put(property.getKey(), property.getValue());
            }
            if (op.isSetInputOperator()) {
                addCreateOperatorInputOperator(group, op.getName(), properties, convertSensorReference(op.getInputOperator().getSensor()), sensorRegistryControl.getCreateOperator().getOperationID());
            }
            else if (op.isSetOutputOperator()) {
                addCreateOperatorOutputOperator(group, op.getName(), properties, convertSensorReference(op.getOutputOperator().getSensor()), sensorRegistryControl.getCreateOperator().getOperationID());
            }
            else if (op.isSetServiceOperator()) {
                addCreateOperatorServiceOperator(group, op.getName(), properties, new SCAIReference(op.getServiceOperator().getOperatorType().getId(), op.getServiceOperator().getOperatorType().getName()), sensorRegistryControl.getCreateOperator().getOperationID());
            }
        }
        else if (sensorRegistryControl.isSetCreateOperatorGroup()) {
            addCreateOperatorGroup(sensorRegistryControl.getCreateOperatorGroup().getOperatorGroup().getName(), sensorRegistryControl.getCreateOperatorGroup().getOperationID());
        }
        else if (sensorRegistryControl.isSetCreateOperatorType()) {
            HashMap<String, String> properties = new HashMap<String, String>();
            HashMap<String, Boolean> readonly = new HashMap<String, Boolean>();
            for (OperatorTypeDescription.Property property : sensorRegistryControl.getCreateOperatorType().getOperatorType().getPropertyArray()) {
                properties.put(property.getKey(), property.getValue());
                readonly.put(property.getKey(), property.getReadOnly());
            }
            addCreateOperatorType(sensorRegistryControl.getCreateOperatorType().getOperatorType().getName(), sensorRegistryControl.getCreateOperatorType().getOperatorType().getMetaType(), properties, readonly, sensorRegistryControl.getCreateOperatorType().getOperatorType().getDescription(), sensorRegistryControl.getCreateOperatorType().getOperationID());
        }
        else if (sensorRegistryControl.isSetCreateSensor()) {
            addCreateSensor(sensorRegistryControl.getCreateSensor().getSensor().getName(), new SCAIReference(sensorRegistryControl.getCreateSensor().getSensor().getSensorDomain().getId(), sensorRegistryControl.getCreateSensor().getSensor().getSensorDomain().getName()), new SCAIReference(sensorRegistryControl.getCreateSensor().getSensor().getSensorType().getId(), sensorRegistryControl.getCreateSensor().getSensor().getSensorType().getName()), sensorRegistryControl.getCreateSensor().getSensor().getVirtual(), sensorRegistryControl.getCreateSensor().getOperationID());
        }
        else if (sensorRegistryControl.isSetCreateSensorCategory()) {
            SCAIReference parentCategory = null;
            if (sensorRegistryControl.getCreateSensorCategory().getSensorCategory().isSetParentSensorCategory()) {
                parentCategory = new SCAIReference(sensorRegistryControl.getCreateSensorCategory().getSensorCategory().getParentSensorCategory().getId(), sensorRegistryControl.getCreateSensorCategory().getSensorCategory().getParentSensorCategory().getName());
            }
            SCAIReference[] domains = new SCAIReference[sensorRegistryControl.getCreateSensorCategory().getSensorCategory().getSensorDomainArray().length];
            for (int i = 0; i < domains.length; i++) {
                domains[i] = new SCAIReference(sensorRegistryControl.getCreateSensorCategory().getSensorCategory().getSensorDomainArray(i).getId(), sensorRegistryControl.getCreateSensorCategory().getSensorCategory().getSensorDomainArray(i).getName());
            }
            addCreateSensorCategory(sensorRegistryControl.getCreateSensorCategory().getSensorCategory().getName(), parentCategory, domains, sensorRegistryControl.getCreateSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetCreateSensorDomain()) {
            addCreateSensorDomain(sensorRegistryControl.getCreateSensorDomain().getSensorDomain().getName(), sensorRegistryControl.getCreateSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetCreateSensorType()) {
            SCAIReference[] parameters = new SCAIReference[sensorRegistryControl.getCreateSensorType().getSensorType().getConfigurationParameterArray().length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new SCAIReference(sensorRegistryControl.getCreateSensorType().getSensorType().getConfigurationParameterArray(i).getId(), sensorRegistryControl.getCreateSensorType().getSensorType().getConfigurationParameterArray(i).getName());
            }
            SCAIReference[] categories = new SCAIReference[sensorRegistryControl.getCreateSensorType().getSensorType().getSensorCategoryArray().length];
            for (int i = 0; i < categories.length; i++) {
                categories[i] = new SCAIReference(sensorRegistryControl.getCreateSensorType().getSensorType().getSensorCategoryArray(i).getId(), sensorRegistryControl.getCreateSensorType().getSensorType().getSensorCategoryArray(i).getName());
            }
            SCAIReference[] domains = new SCAIReference[sensorRegistryControl.getCreateSensorType().getSensorType().getSensorDomainArray().length];
            for (int i = 0; i < domains.length; i++) {
                domains[i] = new SCAIReference(sensorRegistryControl.getCreateSensorType().getSensorType().getSensorDomainArray(i).getId(), sensorRegistryControl.getCreateSensorType().getSensorType().getSensorDomainArray(i).getName());
            }
            SCAIReference dstyp = new SCAIReference(sensorRegistryControl.getCreateSensorType().getSensorType().getDataStreamType().getId(), sensorRegistryControl.getCreateSensorType().getSensorType().getDataStreamType().getName());
            addCreateSensorType(sensorRegistryControl.getCreateSensorType().getSensorType().getName(), sensorRegistryControl.getCreateSensorType().getSensorType().getAdapter(), dstyp, parameters, categories, domains, sensorRegistryControl.getCreateSensorType().getOperationID());
        }

        else if (sensorRegistryControl.isSetDeployOperatorGroup()) {
            SCAIReference pru = null;
            if (sensorRegistryControl.getDeployOperatorGroup().isSetProcessingUnit()) {
                pru = new SCAIReference(sensorRegistryControl.getDeployOperatorGroup().getProcessingUnit().getId(), sensorRegistryControl.getDeployOperatorGroup().getProcessingUnit().getName());
            }
            addDeployOperatorGroup(new SCAIReference(sensorRegistryControl.getDeployOperatorGroup().getOperatorGroup().getId(), sensorRegistryControl.getDeployOperatorGroup().getOperatorGroup().getName()), pru, sensorRegistryControl.getDeployOperatorGroup().getOperationID());
        }

        else if (sensorRegistryControl.isSetGetConfigurationParameter()) {
            addGetConfigurationParameter(new SCAIReference(sensorRegistryControl.getGetConfigurationParameter().getConfigurationParameter().getId(), sensorRegistryControl.getGetConfigurationParameter().getConfigurationParameter().getName()), sensorRegistryControl.getGetConfigurationParameter().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetDataElement()) {
            addGetDataElement(new SCAIReference(sensorRegistryControl.getGetDataElement().getDataElement().getId(), sensorRegistryControl.getGetDataElement().getDataElement().getName()), sensorRegistryControl.getGetDataElement().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetDataStreamType()) {
            addGetDataStreamType(new SCAIReference(sensorRegistryControl.getGetDataStreamType().getDataStreamType().getId(), sensorRegistryControl.getGetDataStreamType().getDataStreamType().getName()), sensorRegistryControl.getGetDataStreamType().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetDataType()) {
            addGetDataType(new SCAIReference(sensorRegistryControl.getGetDataType().getDataType().getId(), sensorRegistryControl.getGetDataType().getDataType().getName()), sensorRegistryControl.getGetDataType().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetOperator()) {
            addGetOperator(new SCAIReference(sensorRegistryControl.getGetOperator().getOperator().getId(), sensorRegistryControl.getGetOperator().getOperator().getName()), new SCAIReference(sensorRegistryControl.getGetOperator().getOperatorGroup().getId(), sensorRegistryControl.getGetOperator().getOperatorGroup().getName()), sensorRegistryControl.getGetOperator().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetOperatorGroup()) {
            addGetOperatorGroup(new SCAIReference(sensorRegistryControl.getGetOperatorGroup().getOperatorGroup().getId(), sensorRegistryControl.getGetOperatorGroup().getOperatorGroup().getName()), sensorRegistryControl.getGetOperatorGroup().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetOperatorGroupStatus()) {
            addGetOperatorGroupStatus(new SCAIReference(sensorRegistryControl.getGetOperatorGroupStatus().getOperatorGroup().getId(), sensorRegistryControl.getGetOperatorGroupStatus().getOperatorGroup().getName()), sensorRegistryControl.getGetOperatorGroupStatus().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetOperatorType()) {
            addGetOperatorType(new SCAIReference(sensorRegistryControl.getGetOperatorType().getOperatorType().getId(), sensorRegistryControl.getGetOperatorType().getOperatorType().getName()), sensorRegistryControl.getGetOperatorType().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetProcessingUnit()) {
            addGetProcessingUnit(new SCAIReference(sensorRegistryControl.getGetProcessingUnit().getOperatorGroup().getId(), sensorRegistryControl.getGetProcessingUnit().getOperatorGroup().getName()), sensorRegistryControl.getGetProcessingUnit().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetSensor()) {
            addGetSensor(convertSensorReference(sensorRegistryControl.getGetSensor().getSensor()), sensorRegistryControl.getGetSensor().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetSensorCategory()) {
            addGetSensorCategory(new SCAIReference(sensorRegistryControl.getGetSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getGetSensorCategory().getSensorCategory().getName()), sensorRegistryControl.getGetSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetSensorDomain()) {
            addGetSensorDomain(new SCAIReference(sensorRegistryControl.getGetSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getGetSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getGetSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetGetSensorType()) {
            addGetSensorType(new SCAIReference(sensorRegistryControl.getGetSensorType().getSensorType().getId(), sensorRegistryControl.getGetSensorType().getSensorType().getName()), sensorRegistryControl.getGetSensorType().getOperationID());
        }

        else if (sensorRegistryControl.isSetLinkOperators()) {
            SCAIReference group = new SCAIReference(sensorRegistryControl.getLinkOperators().getOperatorGroup().getId(), sensorRegistryControl.getLinkOperators().getOperatorGroup().getName());
            OperatorLinkDescription link = sensorRegistryControl.getLinkOperators().getOperatorLink();
            if (link.getSource().isSetInputOperator()) {
                if (link.getDestination().isSetOutputOperator()) {
                    addLinkOperatorsInputToOutput(group, new SCAIReference(link.getSource().getInputOperator().getId(), link.getSource().getInputOperator().getName()), new SCAIReference(link.getDestination().getOutputOperator().getId(), link.getDestination().getOutputOperator().getName()), sensorRegistryControl.getLinkOperators().getOperationID());
                }
                else if(link.getDestination().isSetServiceOperator()) {
                    addLinkOperatorsInputToService(group,  new SCAIReference(link.getSource().getServiceOperator().getId(), link.getSource().getServiceOperator().getName()), new SCAIReference(link.getDestination().getServiceOperator().getId(), link.getDestination().getServiceOperator().getName()), sensorRegistryControl.getLinkOperators().getOperationID());
                }
            }
            else if(link.getSource().isSetServiceOperator()) {
                if (link.getDestination().isSetOutputOperator()) {
                    addLinkOperatorsServiceToOutput(group,  new SCAIReference(link.getSource().getServiceOperator().getId(), link.getSource().getServiceOperator().getName()), new SCAIReference(link.getDestination().getOutputOperator().getId(), link.getDestination().getOutputOperator().getName()), sensorRegistryControl.getLinkOperators().getOperationID());
                }
                else if(link.getDestination().isSetServiceOperator()) {
                    addLinkOperatorsServiceToService(group,  new SCAIReference(link.getSource().getServiceOperator().getId(), link.getSource().getServiceOperator().getName()), new SCAIReference(link.getDestination().getServiceOperator().getId(), link.getDestination().getServiceOperator().getName()), sensorRegistryControl.getLinkOperators().getOperationID());
                }
            }
        }

        else if (sensorRegistryControl.isSetListAllConfigurationParameters()) {
            addListAllConfigurationParameters(sensorRegistryControl.getListAllConfigurationParameters().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllDataElements()) {
            addListAllDataElements(sensorRegistryControl.getListAllDataElements().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllDataStreamTypes()) {
            addListAllDataStreamTypes(sensorRegistryControl.getListAllDataStreamTypes().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllDataTypes()) {
            addListAllDataTypes(sensorRegistryControl.getListAllDataTypes().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllLinks()) {
            addListAllLinks(new SCAIReference(sensorRegistryControl.getListAllLinks().getOperatorGroup().getId(), sensorRegistryControl.getListAllLinks().getOperatorGroup().getName()), sensorRegistryControl.getListAllLinks().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllOperators()) {
            addListAllOperators(new SCAIReference(sensorRegistryControl.getListAllOperators().getOperatorGroup().getId(), sensorRegistryControl.getListAllOperators().getOperatorGroup().getName()), sensorRegistryControl.getListAllOperators().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllOperatorGroups()) {
            addListAllOperatorGroups(sensorRegistryControl.getListAllOperatorGroups().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllOperatorTypes()) {
            addListAllOperatorTypes(sensorRegistryControl.getListAllOperatorTypes().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllProcessingUnits()) {
            addListAllProcessingUnits(sensorRegistryControl.getListAllProcessingUnits().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensors()) {
            addListAllSensors(sensorRegistryControl.getListAllSensors().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensorCategories()) {
            addListAllSensorCategories(sensorRegistryControl.getListAllSensorCategories().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensorCategoriesBySensorDomain()) {
            addListAllSensorCategoriesBySensorDomain(new SCAIReference(sensorRegistryControl.getListAllSensorCategoriesBySensorDomain().getSensorDomain().getId(), sensorRegistryControl.getListAllSensorCategoriesBySensorDomain().getSensorDomain().getName()), sensorRegistryControl.getListAllSensorCategoriesBySensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensorDomains()) {
            addListAllSensorDomains(sensorRegistryControl.getListAllSensorDomains().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensorTypes()) {
            addListAllSensorTypes(sensorRegistryControl.getListAllSensorTypes().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensorTypesBySensorCategory()) {
            addListAllSensorTypesBySensorCategory(new SCAIReference(sensorRegistryControl.getListAllSensorTypesBySensorCategory().getSensorCategory().getId(), sensorRegistryControl.getListAllSensorTypesBySensorCategory().getSensorCategory().getName()), sensorRegistryControl.getListAllSensorTypesBySensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetListAllSensorTypesBySensorDomain()) {
            addListAllSensorTypesBySensorDomain(new SCAIReference(sensorRegistryControl.getListAllSensorTypesBySensorDomain().getSensorDomain().getId(), sensorRegistryControl.getListAllSensorTypesBySensorDomain().getSensorDomain().getName()), sensorRegistryControl.getListAllSensorTypesBySensorDomain().getOperationID());
        }

        else if (sensorRegistryControl.isSetRegisterProcessingUnit()) {
            ProcessingUnitDescription prunt = sensorRegistryControl.getRegisterProcessingUnit().getProcessingUnit();
            addRegisterProcessingUnit(prunt.getName(), prunt.getAddress(), prunt.getVersion(), sensorRegistryControl.getRegisterProcessingUnit().getOperationID());
        }

        else if (sensorRegistryControl.isSetRemoveChildCategoryFromSensorCategory()) {
            addRemoveChildCategoryFromSensorCategory(new SCAIReference(sensorRegistryControl.getRemoveChildCategoryFromSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getRemoveChildCategoryFromSensorCategory().getSensorCategory().getName()), new SCAIReference(sensorRegistryControl.getRemoveChildCategoryFromSensorCategory().getParentSensorCategory().getId(), sensorRegistryControl.getRemoveChildCategoryFromSensorCategory().getParentSensorCategory().getName()), sensorRegistryControl.getRemoveChildCategoryFromSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveConfigurationParameter()) {
            addRemoveConfigurationParameter(new SCAIReference(sensorRegistryControl.getRemoveConfigurationParameter().getConfigurationParameter().getId(), sensorRegistryControl.getRemoveConfigurationParameter().getConfigurationParameter().getName()), sensorRegistryControl.getRemoveConfigurationParameter().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveDataElement()) {
            addRemoveDataElement(new SCAIReference(sensorRegistryControl.getRemoveDataElement().getDataElement().getId(), sensorRegistryControl.getRemoveDataElement().getDataElement().getName()), sensorRegistryControl.getRemoveDataElement().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveDataElementFromDataStreamType()) {
            addRemoveDataElementFromDataStreamType(new SCAIReference(sensorRegistryControl.getRemoveDataElementFromDataStreamType().getDataElement().getId(), sensorRegistryControl.getRemoveDataElementFromDataStreamType().getDataElement().getName()), new SCAIReference(sensorRegistryControl.getRemoveDataElementFromDataStreamType().getDataStreamType().getId(), sensorRegistryControl.getRemoveDataElementFromDataStreamType().getDataStreamType().getName()), sensorRegistryControl.getRemoveDataElementFromDataStreamType().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveDataStreamType()) {
            addRemoveDataStreamType(new SCAIReference(sensorRegistryControl.getRemoveDataStreamType().getDataStreamType().getId(), sensorRegistryControl.getRemoveDataStreamType().getDataStreamType().getName()), sensorRegistryControl.getRemoveDataStreamType().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveDataType()) {
            addRemoveDataType(new SCAIReference(sensorRegistryControl.getRemoveDataType().getDataType().getId(), sensorRegistryControl.getRemoveDataType().getDataType().getName()), sensorRegistryControl.getRemoveDataType().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveOperator()) {
            addRemoveOperator(new SCAIReference(sensorRegistryControl.getRemoveOperator().getOperatorGroup().getId(), sensorRegistryControl.getRemoveOperator().getOperatorGroup().getName()), new SCAIReference(sensorRegistryControl.getRemoveOperator().getOperator().getId(), sensorRegistryControl.getRemoveOperator().getOperator().getName()), sensorRegistryControl.getRemoveOperator().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveOperatorGroup()) {
            addRemoveOperatorGroup(new SCAIReference(sensorRegistryControl.getRemoveOperatorGroup().getOperatorGroup().getId(), sensorRegistryControl.getRemoveOperatorGroup().getOperatorGroup().getName()), sensorRegistryControl.getRemoveOperatorGroup().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveOperatorType()) {
            addRemoveOperatorType(new SCAIReference(sensorRegistryControl.getRemoveOperatorType().getOperatorType().getId(), sensorRegistryControl.getRemoveOperatorType().getOperatorType().getName()), sensorRegistryControl.getRemoveOperatorType().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensor()) {
            addRemoveSensor(convertSensorReference(sensorRegistryControl.getRemoveSensor().getSensor()), sensorRegistryControl.getRemoveSensor().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensorCategory()) {
            addRemoveSensorCategory(new SCAIReference(sensorRegistryControl.getRemoveSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getRemoveSensorCategory().getSensorCategory().getName()), sensorRegistryControl.getRemoveSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensorCategoryFromSensorDomain()) {
            addRemoveSensorCategoryFromSensorDomain(new SCAIReference(sensorRegistryControl.getRemoveSensorCategoryFromSensorDomain().getSensorCategory().getId(), sensorRegistryControl.getRemoveSensorCategoryFromSensorDomain().getSensorCategory().getName()), new SCAIReference(sensorRegistryControl.getRemoveSensorCategoryFromSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getRemoveSensorCategoryFromSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getRemoveSensorCategoryFromSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensorDomain()) {
            addRemoveSensorDomain(new SCAIReference(sensorRegistryControl.getRemoveSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getRemoveSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getRemoveSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensorType()) {
            addRemoveSensorType(new SCAIReference(sensorRegistryControl.getRemoveSensorType().getSensorType().getId(), sensorRegistryControl.getRemoveSensorType().getSensorType().getName()), sensorRegistryControl.getRemoveSensorType().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensorTypeFromSensorCategory()) {
            addRemoveSensorTypeFromSensorCategory(new SCAIReference(sensorRegistryControl.getRemoveSensorTypeFromSensorCategory().getSensorType().getId(), sensorRegistryControl.getRemoveSensorTypeFromSensorCategory().getSensorType().getName()), new SCAIReference(sensorRegistryControl.getRemoveSensorTypeFromSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getRemoveSensorTypeFromSensorCategory().getSensorCategory().getName()), sensorRegistryControl.getRemoveSensorTypeFromSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetRemoveSensorTypeFromSensorDomain()) {
            addRemoveSensorTypeFromSensorDomain(new SCAIReference(sensorRegistryControl.getRemoveSensorTypeFromSensorDomain().getSensorType().getId(), sensorRegistryControl.getRemoveSensorTypeFromSensorDomain().getSensorType().getName()), new SCAIReference(sensorRegistryControl.getRemoveSensorTypeFromSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getRemoveSensorTypeFromSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getRemoveSensorTypeFromSensorDomain().getOperationID());
        }

        else if (sensorRegistryControl.isSetUndeployOperatorGroup()) {
            addUndeployOperatorGroup(new SCAIReference(sensorRegistryControl.getUndeployOperatorGroup().getOperatorGroup().getId(), sensorRegistryControl.getUndeployOperatorGroup().getOperatorGroup().getName()), sensorRegistryControl.getUndeployOperatorGroup().getOperationID());
        }

        else if (sensorRegistryControl.isSetUnlinkOperators()) {
            SCAIReference group = new SCAIReference(sensorRegistryControl.getUnlinkOperators().getOperatorGroup().getId(), sensorRegistryControl.getUnlinkOperators().getOperatorGroup().getName());
            OperatorLinkDescription link = sensorRegistryControl.getUnlinkOperators().getOperatorLink();
            if (link.getSource().isSetInputOperator()) {
                if (link.getDestination().isSetOutputOperator()) {
                    addUnlinkOperatorsInputToOutput(group, new SCAIReference(link.getSource().getInputOperator().getId(), link.getSource().getInputOperator().getName()), new SCAIReference(link.getDestination().getOutputOperator().getId(), link.getDestination().getOutputOperator().getName()), sensorRegistryControl.getUnlinkOperators().getOperationID());
                }
                else if(link.getDestination().isSetServiceOperator()) {
                    addUnlinkOperatorsInputToService(group,  new SCAIReference(link.getSource().getServiceOperator().getId(), link.getSource().getServiceOperator().getName()), new SCAIReference(link.getDestination().getServiceOperator().getId(), link.getDestination().getServiceOperator().getName()), sensorRegistryControl.getUnlinkOperators().getOperationID());
                }
            }
            else if(link.getSource().isSetServiceOperator()) {
                if (link.getDestination().isSetOutputOperator()) {
                    addUnlinkOperatorsServiceToOutput(group,  new SCAIReference(link.getSource().getServiceOperator().getId(), link.getSource().getServiceOperator().getName()), new SCAIReference(link.getDestination().getOutputOperator().getId(), link.getDestination().getOutputOperator().getName()), sensorRegistryControl.getUnlinkOperators().getOperationID());
                }
                else if(link.getDestination().isSetServiceOperator()) {
                    addUnlinkOperatorsServiceToService(group,  new SCAIReference(link.getSource().getServiceOperator().getId(), link.getSource().getServiceOperator().getName()), new SCAIReference(link.getDestination().getServiceOperator().getId(), link.getDestination().getServiceOperator().getName()), sensorRegistryControl.getUnlinkOperators().getOperationID());
                }
            }
        }

        else if (sensorRegistryControl.isSetUnregisterProcessingUnit()) {
            addUnregisterProcessingUnit(new SCAIReference(sensorRegistryControl.getUnregisterProcessingUnit().getProcessingUnit().getId(), sensorRegistryControl.getUnregisterProcessingUnit().getProcessingUnit().getName()), sensorRegistryControl.getUnregisterProcessingUnit().getOperationID());
        }

        else if (sensorRegistryControl.isSetUpdateConfigurationParameter()) {
            ConfigurationParameterDescription cfgpm = sensorRegistryControl.getUpdateConfigurationParameter().getNewConfigurationParameter();
            if (sensorRegistryControl.getUpdateConfigurationParameter().getNewConfigurationParameter().isSetAtomicParameter()) {
                addUpdateConfigurationParameterAtomicParameter(new SCAIReference(sensorRegistryControl.getUpdateConfigurationParameter().getConfigurationParameter().getId(), sensorRegistryControl.getUpdateConfigurationParameter().getConfigurationParameter().getName()), cfgpm.getName(), cfgpm.getOptional(), cfgpm.getAtomicParameter().getUom(), cfgpm.getAtomicParameter().getIdentifier(), new SCAIReference(cfgpm.getAtomicParameter().getDataType().getId(), cfgpm.getAtomicParameter().getDataType().getName()), sensorRegistryControl.getUpdateConfigurationParameter().getOperationID());
            }
            else if (sensorRegistryControl.getUpdateConfigurationParameter().getNewConfigurationParameter().isSetComplexParameter()) {
                SCAIReference[] configurationParameters = new SCAIReference[cfgpm.getComplexParameter().getConfigurationParameterArray().length];
                for (int i = 0; i < configurationParameters.length; i++) {
                    configurationParameters[i] = new SCAIReference(cfgpm.getComplexParameter().getConfigurationParameterArray(i).getId(), cfgpm.getComplexParameter().getConfigurationParameterArray(i).getName());
                }
                addUpdateConfigurationParameterComplexParameter(new SCAIReference(sensorRegistryControl.getUpdateConfigurationParameter().getConfigurationParameter().getId(), sensorRegistryControl.getUpdateConfigurationParameter().getConfigurationParameter().getName()), cfgpm.getName(), cfgpm.getOptional(), configurationParameters, sensorRegistryControl.getUpdateConfigurationParameter().getOperationID());
            }
            else if (sensorRegistryControl.getUpdateConfigurationParameter().getNewConfigurationParameter().isSetSequenceParameter()) {
                addUpdateConfigurationParameterSequenceParameter(new SCAIReference(sensorRegistryControl.getUpdateConfigurationParameter().getConfigurationParameter().getId(), sensorRegistryControl.getUpdateConfigurationParameter().getConfigurationParameter().getName()), cfgpm.getName(), cfgpm.getOptional(), cfgpm.getSequenceParameter().getMinlength(), cfgpm.getSequenceParameter().getMaxlength(), new SCAIReference(cfgpm.getSequenceParameter().getConfigurationParameter().getId(), cfgpm.getSequenceParameter().getConfigurationParameter().getName()), sensorRegistryControl.getUpdateConfigurationParameter().getOperationID());
            }
        }
        else if (sensorRegistryControl.isSetUpdateDataElement()) {
            DataElementDescription datel = sensorRegistryControl.getUpdateDataElement().getNewDataElement();
            if (datel.isSetAtomicElement()) {
                addUpdateDataElementAtomicElement(new SCAIReference(sensorRegistryControl.getUpdateDataElement().getDataElement().getId(), sensorRegistryControl.getUpdateDataElement().getDataElement().getName()), datel.getAtomicElement().getName(), new SCAIReference(datel.getAtomicElement().getDataType().getId(), datel.getAtomicElement().getDataType().getName()), sensorRegistryControl.getUpdateDataElement().getOperationID());
            }
            else if (datel.isSetComplexElement()) {
                SCAIReference[] dataElements = new SCAIReference[datel.getComplexElement().getDataElementArray().length];
                for (int i = 0; i < dataElements.length; i++) {
                    dataElements[i] = new SCAIReference(datel.getComplexElement().getDataElementArray(i).getId(), datel.getComplexElement().getDataElementArray(i).getName());
                }
                addUpdateDataElementComplexElement(new SCAIReference(sensorRegistryControl.getUpdateDataElement().getDataElement().getId(), sensorRegistryControl.getUpdateDataElement().getDataElement().getName()), datel.getComplexElement().getName(), dataElements, sensorRegistryControl.getUpdateDataElement().getOperationID());
            }
        }
        else if (sensorRegistryControl.isSetUpdateDataStreamType()) {
            DataStreamTypeDescription dstyp = sensorRegistryControl.getUpdateDataStreamType().getNewDataStreamType();
            SCAIReference[] dataElements = new SCAIReference[dstyp.getDataElementArray().length];
            for (int i = 0; i < dataElements.length; i++) {
                dataElements[i] = new SCAIReference(dstyp.getDataElementArray(i).getId(), dstyp.getDataElementArray(i).getName());
            }
            addUpdateDataStreamType(new SCAIReference(sensorRegistryControl.getUpdateDataStreamType().getDataStreamType().getId(), sensorRegistryControl.getUpdateDataStreamType().getDataStreamType().getName()), dstyp.getName(), dataElements, sensorRegistryControl.getUpdateDataStreamType().getOperationID());
        }
        // <editor-fold desc="updateDataType">
        else if (sensorRegistryControl.isSetUpdateDataType()) {
            DataTypeDescription datyp = sensorRegistryControl.getUpdateDataType().getNewDataType();
            if (sensorRegistryControl.getUpdateDataType().getNewDataType().isSetBinaryType()) {
                addUpdateDataTypeBinaryType(new SCAIReference(sensorRegistryControl.getUpdateDataType().getDataType().getId(), sensorRegistryControl.getUpdateDataType().getDataType().getName()), datyp.getName(), datyp.getBinaryType().isSetMin()?datyp.getBinaryType().getMin():null, datyp.getBinaryType().isSetMax()?datyp.getBinaryType().getMax():null, datyp.getBinaryType().getDefaultvalue(), sensorRegistryControl.getUpdateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getUpdateDataType().getNewDataType().isSetDecimalType()) {
                addUpdateDataTypeDecimalType(new SCAIReference(sensorRegistryControl.getUpdateDataType().getDataType().getId(), sensorRegistryControl.getUpdateDataType().getDataType().getName()), datyp.getName(), datyp.getDecimalType().isSetMin()?datyp.getDecimalType().getMin():null, datyp.getDecimalType().isSetMax()?datyp.getDecimalType().getMax():null, datyp.getDecimalType().isSetScale()?datyp.getDecimalType().getScale():null, datyp.getDecimalType().getDefaultvalue(), sensorRegistryControl.getUpdateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getUpdateDataType().getNewDataType().isSetEnumType()) {
                HashMap<Long,String> allowedValues = new HashMap<Long,String>();
                for (EnumType.Allowedvalue allowedValue : datyp.getEnumType().getAllowedvalueArray()) {
                    allowedValues.put(allowedValue.getOrdinal(), allowedValue.getName());
                }
                addUpdateDataTypeEnumType(new SCAIReference(sensorRegistryControl.getUpdateDataType().getDataType().getId(), sensorRegistryControl.getUpdateDataType().getDataType().getName()), datyp.getName(), allowedValues, datyp.getEnumType().getDefaultvalue(), sensorRegistryControl.getUpdateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getUpdateDataType().getNewDataType().isSetListType()) {
                String[] allowedValues = new String[datyp.getListType().getAllowedvalueArray().length];
                for (int i = 0; i < allowedValues.length; i++) {
                    allowedValues[i] = datyp.getListType().getAllowedvalueArray(i);

                }
                String[] defaultValues = new String[datyp.getListType().getDefaultvalueArray().length];
                for (int i = 0; i < defaultValues.length; i++) {
                    defaultValues[i] = datyp.getListType().getDefaultvalueArray(i);

                }
                addUpdateDataTypeListType(new SCAIReference(sensorRegistryControl.getUpdateDataType().getDataType().getId(), sensorRegistryControl.getUpdateDataType().getDataType().getName()), datyp.getName(), datyp.getListType().isSetMin()?datyp.getListType().getMin():null, datyp.getListType().isSetMax()?datyp.getListType().getMax():null, allowedValues, defaultValues, sensorRegistryControl.getUpdateDataType().getOperationID());
            }
            else if(sensorRegistryControl.getUpdateDataType().getNewDataType().isSetStringType()) {
                addUpdateDataTypeStringType(new SCAIReference(sensorRegistryControl.getUpdateDataType().getDataType().getId(), sensorRegistryControl.getUpdateDataType().getDataType().getName()), datyp.getName(), datyp.getStringType().isSetMin()?datyp.getStringType().getMin():null, datyp.getStringType().isSetMax()?datyp.getStringType().getMax():null, datyp.getStringType().getRegex(), datyp.getStringType().getDefaultvalue(), sensorRegistryControl.getUpdateDataType().getOperationID());
            }
        }
        // </editor-fold>
        else if (sensorRegistryControl.isSetUpdateOperator()) {
            OperatorDescription op = sensorRegistryControl.getUpdateOperator().getOperator().getNewOperator();
            SCAIReference group = new SCAIReference(sensorRegistryControl.getUpdateOperator().getOperatrorGroup().getId(), sensorRegistryControl.getUpdateOperator().getOperatrorGroup().getName());
            HashMap<String, String> properties = new HashMap<String, String>();
            for (OperatorDescription.Property property : op.getPropertyArray()) {
                properties.put(property.getKey(), property.getValue());
            }
            if (op.isSetInputOperator()) {
                addUpdateOperatorInputOperator(new SCAIReference(sensorRegistryControl.getUpdateOperator().getOperator().getOldOperator().getId(), sensorRegistryControl.getUpdateOperator().getOperator().getOldOperator().getName()), group, op.getName(), properties, convertSensorReference(op.getInputOperator().getSensor()), sensorRegistryControl.getUpdateOperator().getOperationID());
            }
            else if (op.isSetOutputOperator()) {
                addUpdateOperatorOutputOperator(new SCAIReference(sensorRegistryControl.getUpdateOperator().getOperator().getOldOperator().getId(), sensorRegistryControl.getUpdateOperator().getOperator().getOldOperator().getName()), group, op.getName(), properties, convertSensorReference(op.getOutputOperator().getSensor()), sensorRegistryControl.getUpdateOperator().getOperationID());
            }
            else if (op.isSetServiceOperator()) {
                addUpdateOperatorServiceOperator(new SCAIReference(sensorRegistryControl.getUpdateOperator().getOperator().getOldOperator().getId(), sensorRegistryControl.getUpdateOperator().getOperator().getOldOperator().getName()), group, op.getName(), properties, new SCAIReference(op.getServiceOperator().getOperatorType().getId(), op.getServiceOperator().getOperatorType().getName()), sensorRegistryControl.getUpdateOperator().getOperationID());
            }
        }
        else if (sensorRegistryControl.isSetUpdateOperatorGroup()) {
            addUpdateOperatorGroup(new SCAIReference(sensorRegistryControl.getUpdateOperatorGroup().getOperatorGroup().getId(), sensorRegistryControl.getUpdateOperatorGroup().getOperatorGroup().getName()), sensorRegistryControl.getUpdateOperatorGroup().getNewOperatorGroup().getName(), sensorRegistryControl.getUpdateOperatorGroup().getOperationID());
        }
        else if (sensorRegistryControl.isSetUpdateOperatorType()) {
            HashMap<String, String> properties = new HashMap<String, String>();
            HashMap<String, Boolean> readonly = new HashMap<String, Boolean>();
            for (OperatorTypeDescription.Property property : sensorRegistryControl.getUpdateOperatorType().getNewOperatorType().getPropertyArray()) {
                properties.put(property.getKey(), property.getValue());
                readonly.put(property.getKey(), property.getReadOnly());
            }
            addUpdateOperatorType(new SCAIReference(sensorRegistryControl.getUpdateOperatorType().getOperatorType().getId(), sensorRegistryControl.getUpdateOperatorType().getOperatorType().getName()), sensorRegistryControl.getUpdateOperatorType().getNewOperatorType().getName(), sensorRegistryControl.getUpdateOperatorType().getNewOperatorType().getMetaType(), properties, readonly, sensorRegistryControl.getUpdateOperatorType().getNewOperatorType().getDescription(), sensorRegistryControl.getUpdateOperatorType().getOperationID());
        }
        else if (sensorRegistryControl.isSetUpdateSensor()) {
            addUpdateSensor(convertSensorReference(sensorRegistryControl.getUpdateSensor().getSensor()), sensorRegistryControl.getUpdateSensor().getNewSensor().getName(), new SCAIReference(sensorRegistryControl.getUpdateSensor().getNewSensor().getSensorDomain().getId(), sensorRegistryControl.getUpdateSensor().getNewSensor().getSensorDomain().getName()), new SCAIReference(sensorRegistryControl.getUpdateSensor().getNewSensor().getSensorType().getId(), sensorRegistryControl.getUpdateSensor().getNewSensor().getSensorType().getName()), sensorRegistryControl.getUpdateSensor().getNewSensor().getVirtual(), sensorRegistryControl.getUpdateSensor().getOperationID());
        }
        else if (sensorRegistryControl.isSetUpdateSensorCategory()) {
            SCAIReference parentCategory = null;
            if (sensorRegistryControl.getUpdateSensorCategory().getNewSensorCategory().isSetParentSensorCategory()) {
                parentCategory = new SCAIReference(sensorRegistryControl.getUpdateSensorCategory().getNewSensorCategory().getParentSensorCategory().getId(), sensorRegistryControl.getUpdateSensorCategory().getNewSensorCategory().getParentSensorCategory().getName());
            }
            SCAIReference[] domains = new SCAIReference[sensorRegistryControl.getUpdateSensorCategory().getNewSensorCategory().getSensorDomainArray().length];
            for (int i = 0; i < domains.length; i++) {
                domains[i] = new SCAIReference(sensorRegistryControl.getUpdateSensorCategory().getNewSensorCategory().getSensorDomainArray(i).getId(), sensorRegistryControl.getUpdateSensorCategory().getNewSensorCategory().getSensorDomainArray(i).getName());
            }
            addUpdateSensorCategory(new SCAIReference(sensorRegistryControl.getUpdateSensorCategory().getSensorCategory().getId(), sensorRegistryControl.getUpdateSensorCategory().getSensorCategory().getName()), sensorRegistryControl.getUpdateSensorCategory().getSensorCategory().getName(), parentCategory, domains, sensorRegistryControl.getUpdateSensorCategory().getOperationID());
        }
        else if (sensorRegistryControl.isSetUpdateSensorDomain()) {
            addUpdateSensorDomain(new SCAIReference(sensorRegistryControl.getUpdateSensorDomain().getSensorDomain().getId(), sensorRegistryControl.getUpdateSensorDomain().getSensorDomain().getName()), sensorRegistryControl.getUpdateSensorDomain().getNewSensorDomain().getName(), sensorRegistryControl.getUpdateSensorDomain().getOperationID());
        }
        else if (sensorRegistryControl.isSetUpdateSensorType()) {
            SCAIReference[] parameters = new SCAIReference[sensorRegistryControl.getUpdateSensorType().getNewSensorType().getConfigurationParameterArray().length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new SCAIReference(sensorRegistryControl.getUpdateSensorType().getNewSensorType().getConfigurationParameterArray(i).getId(), sensorRegistryControl.getUpdateSensorType().getNewSensorType().getConfigurationParameterArray(i).getName());
            }
            SCAIReference[] categories = new SCAIReference[sensorRegistryControl.getUpdateSensorType().getNewSensorType().getSensorCategoryArray().length];
            for (int i = 0; i < categories.length; i++) {
                categories[i] = new SCAIReference(sensorRegistryControl.getUpdateSensorType().getNewSensorType().getSensorCategoryArray(i).getId(), sensorRegistryControl.getUpdateSensorType().getNewSensorType().getSensorCategoryArray(i).getName());
            }
            SCAIReference[] domains = new SCAIReference[sensorRegistryControl.getUpdateSensorType().getNewSensorType().getSensorDomainArray().length];
            for (int i = 0; i < domains.length; i++) {
                domains[i] = new SCAIReference(sensorRegistryControl.getUpdateSensorType().getNewSensorType().getSensorDomainArray(i).getId(), sensorRegistryControl.getUpdateSensorType().getNewSensorType().getSensorDomainArray(i).getName());
            }
            SCAIReference dstyp = new SCAIReference(sensorRegistryControl.getUpdateSensorType().getNewSensorType().getDataStreamType().getId(), sensorRegistryControl.getUpdateSensorType().getNewSensorType().getDataStreamType().getName());
            addUpdateSensorType(new SCAIReference(sensorRegistryControl.getUpdateSensorType().getSensorType().getId(), sensorRegistryControl.getUpdateSensorType().getSensorType().getName()), sensorRegistryControl.getUpdateSensorType().getNewSensorType().getName(), sensorRegistryControl.getUpdateSensorType().getNewSensorType().getAdapter(), dstyp, parameters, categories, domains, sensorRegistryControl.getUpdateSensorType().getOperationID());
        }
    }

    private String assembleTranslation(SCAIDocument data) {
        clearDocument();
        Identification identification = data.getSCAI().getIdentification();
        if (identification != null) {
            String username = null;
            byte[] password = null;
            if (identification.isSetLoginIdentification()) {
                username = identification.getLoginIdentification().getUsername();
                password = identification.getLoginIdentification().getPassword();
            }
            addIdentification(identification.getSecurityToken(), username, password);
        }
        Measurements measurements  = data.getSCAI().getPayload().getMeasurements();
        if (measurements != null) {
            for (SensorDataDescription dataStream : measurements.getDataStreamArray()) {
                ArrayList<SCAIDataStreamElement> dataStreamElements = new ArrayList<SCAIDataStreamElement>();
                for (DataElementValueDescription dataStreamElement : dataStream.getDataStreamElementArray()) {
                    SCAIDataStreamElement scaiDataStreamElement = new SCAIDataStreamElement();
                    scaiDataStreamElement.setData(dataStreamElement.getData());
                    scaiDataStreamElement.setPath(dataStreamElement.getPath());
                    if (dataStreamElement.isSetQuality()) {
                        scaiDataStreamElement.setQuality(dataStreamElement.getQuality().floatValue());
                    }
                    if (dataStreamElement.isSetError()) {
                        if (dataStreamElement.getError().isSetBatteryDead()) {
                            scaiDataStreamElement.setErrorMessage(dataStreamElement.getError().getBatteryDead());
                            scaiDataStreamElement.setErrortype(SCAIDataStreamElement.Errortype.BATTERYDEAD);
                        }
                        else if(dataStreamElement.getError().isSetNAN()) {
                            scaiDataStreamElement.setErrorMessage(dataStreamElement.getError().getNAN());
                            scaiDataStreamElement.setErrortype(SCAIDataStreamElement.Errortype.NAN);
                        }
                        else if(dataStreamElement.getError().isSetSensorDead()) {
                            scaiDataStreamElement.setErrorMessage(dataStreamElement.getError().getSensorDead());
                            scaiDataStreamElement.setErrortype(SCAIDataStreamElement.Errortype.SENSORDEAD);
                        }
                        else if(dataStreamElement.getError().isSetUnknown()) {
                            scaiDataStreamElement.setErrorMessage(dataStreamElement.getError().getUnknown());
                            scaiDataStreamElement.setErrortype(SCAIDataStreamElement.Errortype.UNKNOWN);
                        }
                    }
                    dataStreamElements.add(scaiDataStreamElement);
                }
                addDataStream(dataStream.getTimeStamp(), dataStream.getSensorName(), dataStream.getSensorDomainName(), dataStream.isSetOlderValues()?dataStream.getOlderValues():null, dataStreamElements.toArray(new SCAIDataStreamElement[0]));
            }
        }
        ControlData controlData = data.getSCAI().getPayload().getControlData();
        if (controlData != null) {
            for (AccessControl accessControl : controlData.getAccessControlArray()) {
                translateAccessControl(accessControl);
            }
            for (SensorControl sensorControl : controlData.getSensorControlArray()) {
                translateSensorControl(sensorControl);
            }
            for (SensorRegistryControl sensorRegistryControl : controlData.getSensorRegistryControlArray()) {
                translateSensorRegistryControl(sensorRegistryControl);
            }
        }
        Acknowledgment ack = data.getSCAI().getPayload().getAcknowledgment();
        if (ack != null) {
            for (Acknowledgment.Exception ackException : ack.getExceptionArray()) {
                addAcknowledgmentException(ackException.getText(), ackException.isSetErrorCode()?ackException.getErrorCode():null, ackException.getOperationID());
            }
            for (Acknowledgment.Success ackSuccess : ack.getSuccessArray()) {
                addAcknowledgmentSuccess(ackSuccess.getText(), ackSuccess.getOperationID());
            }
            for (Acknowledgment.Reply ackReply : ack.getReplyArray()) {
                addAcknowledgmentData(ackReply.getDataArray(), ackReply.getOperationID());
            }
        }
        return (String)getDocument();
    }

    private void appendString(String desc, String parameter) {
        if (sb.charAt(sb.length()-1) != '(') {
            sb.append(" ");
        }
        sb.append(desc);
        sb.append("=\"");
        sb.append(parameter);
        sb.append("\"");
    }

    private void appendBinary(String desc, byte[] parameter) {
        byte[] encoded = Base64.encode(parameter);
        if (sb.charAt(sb.length()-1) != '(') {
            sb.append(" ");
        }
        sb.append(desc);
        sb.append("=\'");
        sb.append(new String(encoded));
        sb.append("\'");
    }

    private void appendTimestamp(String desc, Calendar parameter) {
        // TODO: fix timestamp output
        GregorianCalendar gc = (GregorianCalendar)parameter;
        if (sb.charAt(sb.length()-1) != '(') {
            sb.append(" ");
        }
        sb.append(desc);
        sb.append("=");
        sb.append(gc.get(GregorianCalendar.YEAR));
        sb.append("-");
        sb.append(gc.get(GregorianCalendar.MONTH) + 1);
        sb.append("-");
        sb.append(gc.get(GregorianCalendar.DAY_OF_MONTH));
        sb.append("T");
        sb.append(gc.get(GregorianCalendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(gc.get(GregorianCalendar.MINUTE));
        sb.append(":");
        sb.append(gc.get(GregorianCalendar.SECOND));
        sb.append(".");
        sb.append(gc.get(GregorianCalendar.MILLISECOND));
    }

    private void appendVariable(String desc, String parameter) {
        if (sb.charAt(sb.length()-1) != '(') {
            sb.append(" ");
        }
        sb.append(desc);
        sb.append("=");
        sb.append(parameter);
    }

    private void appendConfigurationValue(SCAIConfigurationValue parameter) {
        if (sb.charAt(sb.length()-1) != '(') {
            sb.append(" ");
        }
        sb.append("cv=(");
        appendVariable("n", parameter.getName());
        if (parameter.getIndex() != null) {
            appendVariable("ix", parameter.getIndex().toString());
        }
        if (parameter.getValue() != null) {
            appendString("va", parameter.getValue());
        } else {
            sb.append(" cva=(");
            for (SCAIConfigurationValue configurationValue : parameter.getConfigurationValues()) {
                appendConfigurationValue(configurationValue);
            }
            sb.append(")");
        }
        sb.append(")");
    }

    private void applyChanges() {
        if (lines.isEmpty()) {
            lines.add(Constants.PROTOCOL_IDENTIFIER + ":");
        }
        sb.append(Constants.SEPARATOR);
        lines.add(sb.toString());
        sb = new StringBuilder();
    }

    private void appendOpenP(String parameterName) {
        sb.append(" ").append(parameterName).append("=(");
    }

    private void translateComplexTypes(XmlObject data) {
//        System.out.println(data.xmlText());
        String type = data.getDomNode().getAttributes().getNamedItem("xsi:type").getNodeValue();
        String[] typeParts = type.split(":");
        type = typeParts[typeParts.length - 1];
//        System.out.println(type);
        // TODO: put missing complex types
        try {
            if (type.equals("AccessorDescription")) {
                appendOpenP(Constants.COMPLEX[0]);
                AccessorDescription scaiData = (AccessorDescription)data;
                sb.append(")");
            }
            else if (type.equals("ConfigurationParameterDescription")) {
                appendOpenP(Constants.COMPLEX[1]);
                ConfigurationParameterDescription scaiData = (ConfigurationParameterDescription)data;
                if (scaiData.isSetAtomicParameter()) {
                    buildConfigurationParameterAtomicParameter(scaiData.getName(), scaiData.getOptional(), scaiData.getAtomicParameter().getUom(), scaiData.getAtomicParameter().getIdentifier(), convertReference(scaiData.getAtomicParameter().getDataType()));
                }
                else if (scaiData.isSetComplexParameter()) {
                    ArrayList<SCAIReference> configurationParameters = new ArrayList<SCAIReference>();
                    for (int i = 0; i < scaiData.getComplexParameter().getConfigurationParameterArray().length; i++) {
                        configurationParameters.add(convertReference(scaiData.getComplexParameter().getConfigurationParameterArray(i)));
                    }
                    buildConfigurationParameterComplexParameter(scaiData.getName(), scaiData.getOptional(), configurationParameters);
                } 
                else if (scaiData.isSetSequenceParameter()) {
                    buildConfigurationParameterSequenceParameter(scaiData.getName(), scaiData.getOptional(), scaiData.getSequenceParameter().getMinlength(), scaiData.getSequenceParameter().getMaxlength(), convertReference(scaiData.getSequenceParameter().getConfigurationParameter()));
                }
                sb.append(")");
            }
            else if (type.equals(ConfigurationValueDescription.type.getShortJavaName())) {
                appendOpenP(Constants.COMPLEX[2]);
                ConfigurationValueDescription scaiData = (ConfigurationValueDescription)data;
                SCAIConfigurationValue scaiConfigurationValue = convertConfigurationValues(scaiData);
                buildConfigurationValue(scaiConfigurationValue);
                sb.append(")");
            }
        } catch (Exception ex) {
            Logger.getLogger(TranslatorText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addAcknowledgmentData(Object data, String operationID) {
        sb.append(Constants.ACKNOWLEDGMENTS[2]);
        if (data instanceof XmlObject[]) {
            for (XmlObject item : (XmlObject[])data) {
                translateComplexTypes(item);
            }
        }

        if (data instanceof XmlObject) {
//            System.out.println(((XmlObject)data).xmlText());
            translateComplexTypes((XmlObject)data);
        }
        appendVariable("id", operationID);

        applyChanges();
    }

    @Override
    public void addAcknowledgmentException(String text, Integer errorCode, String operationID) {
        sb.append(Constants.ACKNOWLEDGMENTS[0]);
        appendString("tx", text);
        if (errorCode != null) {
            appendVariable("ec", errorCode.toString());
        }
        appendVariable("id", operationID);

        applyChanges();
    }

    @Override
    public void addAcknowledgmentSuccess(String text, String operationID) {
        sb.append(Constants.ACKNOWLEDGMENTS[1]);
        if (text != null) {
            appendString("tx", text);
        }
        appendVariable("id", operationID);

        applyChanges();
    }

    @Override
    public void addAddChildCategoryToSensorCategory(SCAIReference category, SCAIReference parentCategory, String operationID) {
        sb.append(Constants.REGISTRYCMDS[71]);
        appendReference(category);
        appendReference(parentCategory);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addAddDataElementToDataStreamType(SCAIReference element, SCAIReference streamType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[69]);
        appendReference(element);
        appendReference(streamType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addAddSensorCategoryToSensorDomain(SCAIReference category, SCAIReference domain, String operationID) {
        sb.append(Constants.REGISTRYCMDS[63]);
        appendReference(category);
        appendReference(domain);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addAddSensorTypeToSensorCategory(SCAIReference type, SCAIReference category, String operationID) {
        sb.append(Constants.REGISTRYCMDS[65]);
        appendReference(type);
        appendReference(category);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addAddSensorTypeToSensorDomain(SCAIReference type, SCAIReference domain, String operationID) {
        sb.append(Constants.REGISTRYCMDS[67]);
        appendReference(type);
        appendReference(domain);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addAuthenticateUser(String username, byte[] password, String operationID) {
        sb.append(Constants.ACCESSCMDS[3]);
        appendString("un", username);
        appendBinary("pw", password);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addConfigureSensor(SCAISensorReference reference, SCAIConfigurationValue[] configurationValues, String operationID) {
        sb.append(Constants.SENSORCMDS[2]);
        appendSensorReference(reference);
        appendConfigurationValue(configurationValues[0]);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateConfigurationParameterAtomicParameter(String name, Boolean optional, String uom, Boolean identifier, SCAIReference dataType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[12]);
        buildConfigurationParameterAtomicParameter(name, optional, uom, identifier, dataType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateConfigurationParameterComplexParameter(String name, Boolean optional, SCAIReference[] configurationParameters, String operationID) {
        sb.append(Constants.REGISTRYCMDS[12]);
        buildConfigurationParameterComplexParameter(name, optional, (ArrayList<SCAIReference>)Arrays.asList(configurationParameters));
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateConfigurationParameterSequenceParameter(String name, Boolean optional, Integer min, Integer max, SCAIReference configurationParameter, String operationID) {
        sb.append(Constants.REGISTRYCMDS[12]);
        buildConfigurationParameterSequenceParameter(name, optional, min, max, configurationParameter);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataElementAtomicElement(String name, SCAIReference dataType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[4]);
        appendVariable("n", name);
        appendReference(dataType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataElementComplexElement(String name, SCAIReference[] dataElements, String operationID) {
        sb.append(Constants.REGISTRYCMDS[4]);
        appendVariable("n", name);
        for (SCAIReference dataElement : dataElements) {
            appendReference(dataElement);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataStreamType(String name, SCAIReference[] dataElements, String operationID) {
        sb.append(Constants.REGISTRYCMDS[8]);
        appendVariable("n", name);
        for (SCAIReference dataElement : dataElements) {
            appendReference(dataElement);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataTypeDecimalType(String name, Float min, Float max, Float scale, Float defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[0]);
        appendVariable("n", name);
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        if (scale != null) {
            appendVariable("s", scale.toString());
        }
        appendVariable("d", defaultvalue.toString());
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataTypeStringType(String name, Long min, Long max, String regex, String defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[0]);
        appendVariable("n", name);
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        if (regex != null) {
            appendString("r", regex);
        }
        appendString("d", defaultvalue);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateOperatorGroup(String name, String operationID) {
        sb.append(Constants.REGISTRYCMDS[32]);
        appendVariable("n", name);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }


    @Override
    public void addCreateOperatorType(String name, String metaType, Map<String, String> properties, Map<String, Boolean> readOnly, String description, String operationID) {
        sb.append(Constants.REGISTRYCMDS[36]);
        appendVariable("n", name);
        appendVariable("m", metaType);
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey());
            appendString("v", property.getValue());
            appendVariable("w", readOnly.get(property.getKey()).toString());
            sb.append(")");
        }
        if (description != null) {
            appendString("ds", description);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType, boolean virtual, String operationID) {
        sb.append(Constants.REGISTRYCMDS[28]);
        appendVariable("n", name);
        appendReference(sensorDomain);
        appendReference(sensorType);
        appendVariable("vi", ((Boolean)virtual).toString());
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateSensorCategory(String name, SCAIReference parentSensorCategory, SCAIReference[] sensorDomains, String operationID) {
        sb.append(Constants.REGISTRYCMDS[24]);
        appendVariable("n", name);
        if (parentSensorCategory != null) {
            appendReference(parentSensorCategory);
        }
        if (sensorDomains != null && sensorDomains.length > 0) {
            sb.append(" d=(");
            for (SCAIReference domain : sensorDomains) {
                appendReference(domain);
            }
            sb.append(")");
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateSensorDomain(String name, String operationID) {
        sb.append(Constants.REGISTRYCMDS[20]);
        appendVariable("n", name);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateSensorType(String name, String adapter, SCAIReference dataStreamType, SCAIReference[] configurationParameters, SCAIReference[] sensorCategories, SCAIReference[] sensorDomains, String operationID) {
        sb.append(Constants.REGISTRYCMDS[16]);
        appendVariable("n", name);
        appendVariable("a", adapter);
        appendReference(dataStreamType);
        if (configurationParameters != null && configurationParameters.length > 0) {
            sb.append(" p=(");
            for (SCAIReference parameter : configurationParameters) {
                appendReference(parameter);
            }
            sb.append(")");
        }
        if (sensorCategories != null && sensorCategories.length > 0) {
            sb.append(" c=(");
            for (SCAIReference category : sensorCategories) {
                appendReference(category);
            }
            sb.append(")");
        }
        if (sensorDomains != null && sensorDomains.length > 0) {
            sb.append(" d=(");
            for (SCAIReference domain : sensorDomains) {
                appendReference(domain);
            }
            sb.append(")");
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addDataStream(Calendar timeStamp, String sensorName, String sensorDomainName, Integer olderValues, SCAIDataStreamElement[] dataStreamElements) {
        sb.append(Constants.MEASUREMENTS[0]);
        appendTimestamp("ts", timeStamp);
        appendVariable("n", sensorName);
        appendVariable("n", sensorDomainName);
        if (olderValues != null) {
            appendVariable("ov", olderValues.toString());
        }

        for (SCAIDataStreamElement scaiDataStreamElement : dataStreamElements) {
            sb.append(" ds=(");

            appendString("pa", scaiDataStreamElement.getPath());

            if (scaiDataStreamElement.getQuality() != null) {
                appendVariable("q", scaiDataStreamElement.getQuality().toString());
            }

            if (scaiDataStreamElement.getErrorMessage() != null) {
                switch (scaiDataStreamElement.getErrortype()) {
                case SENSORDEAD:
                    appendVariable("et", "s");

                    break;

                case NAN:
                    appendVariable("et", "n");

                    break;

                case BATTERYDEAD:
                    appendVariable("et", "b");

                    break;

                case UNKNOWN:
                    appendVariable("et", "u");

                    break;

                default:
                    break;
                }
                appendString("em", scaiDataStreamElement.getErrorMessage());
            }

            appendString("da", scaiDataStreamElement.getData());

            sb.append(")");
        }
        applyChanges();
    }

    @Override
    public void addDeployOperatorGroup(SCAIReference operatorGroup, SCAIReference processingUnit, String operationID) {
        sb.append(Constants.REGISTRYCMDS[61]);
        appendReference(operatorGroup);
        if (processingUnit != null) {
            appendReference(processingUnit);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addDestroySession(String sessionID, String operationID) {
        sb.append(Constants.ACCESSCMDS[4]);
        appendVariable("se", sessionID);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addGetConfiguration(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[3]);
        appendSensorReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetConfigurationParameter(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[15]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetDataElement(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[7]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetDataStreamType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[11]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetDataType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[3]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetOperator(SCAIReference reference, SCAIReference group, String operationID) {
        sb.append(Constants.REGISTRYCMDS[43]);
        appendReference(reference);
        appendReference(group);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetOperatorGroup(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[35]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetOperatorType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[39]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetProcessingUnit(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[73]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetSensor(SCAISensorReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[31]);
        appendSensorReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetSensorCategory(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[27]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetSensorDomain(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[23]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetSensorType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[19]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetSupportedModules(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[6]);
        appendSensorReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetUserData(SCAIReference reference, String operationID) {
        sb.append(Constants.ACCESSCMDS[5]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGetValue(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[7]);
        appendSensorReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addGrantSensorAccess(SCAISensorReference sensorReference, SCAIReference[] user, SCAIReference[] domain, String operationID) {
        sb.append(Constants.ACCESSCMDS[6]);
        appendSensorReference(sensorReference);
        for (SCAIReference userReference : user) {
            sb.append(" u=(");
            appendReference(userReference);
            sb.append(")");
        }
        for (SCAIReference domainReference : domain) {
            sb.append(" d=(");
            appendReference(domainReference);
            sb.append(")");
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addIdentification(String securityToken, String username, byte[] password) {
        sb.append(Constants.IDENTIFICATION[0]);
        if (securityToken != null && !securityToken.isEmpty()) {
            appendVariable("st", securityToken);
        }
        else {
            appendVariable("un", username);
            appendBinary("pw", password);
        }
        applyChanges();
    }

    @Override
    public void addLinkOperatorsInputToOutput(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[57]);
        appendReference(group);
        sb.append(" io=(");
        appendReference(source);
        sb.append(") oo=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addLinkOperatorsInputToService(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[57]);
        appendReference(group);
        sb.append(" io=(");
        appendReference(source);
        sb.append(") so=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addLinkOperatorsServiceToOutput(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[57]);
        appendReference(group);
        sb.append(" so=(");
        appendReference(source);
        sb.append(") oo=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addLinkOperatorsServiceToService(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[57]);
        appendReference(group);
        sb.append(" so=(");
        appendReference(source);
        sb.append(") so=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllConfigurationParameters(String operationID) {
        sb.append(Constants.REGISTRYCMDS[45]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllDataElements(String operationID) {
        sb.append(Constants.REGISTRYCMDS[54]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllDataStreamTypes(String operationID) {
        sb.append(Constants.REGISTRYCMDS[46]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllDataTypes(String operationID) {
        sb.append(Constants.REGISTRYCMDS[44]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllLinks(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[56]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllOperatorGroups(String operationID) {
        sb.append(Constants.REGISTRYCMDS[52]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllOperatorTypes(String operationID) {
        sb.append(Constants.REGISTRYCMDS[53]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllOperators(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[51]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllProcessingUnits(String operationID) {
        sb.append(Constants.REGISTRYCMDS[55]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorAccessors(SCAISensorReference sensorReference, String operationID) {
        sb.append(Constants.ACCESSCMDS[8]);
        appendSensorReference(sensorReference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorCategories(String operationID) {
        sb.append(Constants.REGISTRYCMDS[49]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorCategoriesBySensorDomain(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[49]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorDomains(String operationID) {
        sb.append(Constants.REGISTRYCMDS[48]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorTypes(String operationID) {
        sb.append(Constants.REGISTRYCMDS[47]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensors(String operationID) {
        sb.append(Constants.REGISTRYCMDS[50]);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addRegisterProcessingUnit(String name, String address, Double version, String operationID) {
        sb.append(Constants.REGISTRYCMDS[59]);
        appendVariable("n", name);
        appendVariable("adr", address);
        appendVariable("v", version.toString());
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveChildCategoryFromSensorCategory(SCAIReference category, SCAIReference parentCategory, String operationID) {
        sb.append(Constants.REGISTRYCMDS[72]);
        appendReference(category);
        appendReference(parentCategory);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveConfigurationParameter(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[13]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveDataElement(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[5]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveDataElementFromDataStreamType(SCAIReference element, SCAIReference streamType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[70]);
        appendReference(element);
        appendReference(streamType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveDataStreamType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[9]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveDataType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[1]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveOperator(SCAIReference reference, SCAIReference group, String operationID) {
        sb.append(Constants.REGISTRYCMDS[41]);
        appendReference(group);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveOperatorGroup(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[33]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveOperatorType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[37]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensor(SCAISensorReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[29]);
        appendSensorReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensorCategory(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[25]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensorCategoryFromSensorDomain(SCAIReference category, SCAIReference domain, String operationID) {
        sb.append(Constants.REGISTRYCMDS[64]);
        appendReference(category);
        appendReference(domain);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensorDomain(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[21]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensorType(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[17]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensorTypeFromSensorCategory(SCAIReference type, SCAIReference category, String operationID) {
        sb.append(Constants.REGISTRYCMDS[66]);
        appendReference(type);
        appendReference(category);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveSensorTypeFromSensorDomain(SCAIReference type, SCAIReference domain, String operationID) {
        sb.append(Constants.REGISTRYCMDS[68]);
        appendReference(type);
        appendReference(domain);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addRemoveUser(SCAIReference reference, String operationID) {
        sb.append(Constants.ACCESSCMDS[1]);
        appendReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addStartSensor(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[0]);
        appendSensorReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addStopSensor(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[1]);
        appendSensorReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addSubscribeDatastream(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[4]);
        appendSensorReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUndeployOperatorGroup(SCAIReference operatorGroup, String operationID) {
        sb.append(Constants.REGISTRYCMDS[62]);
        appendReference(operatorGroup);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsInputToOutput(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[58]);
        appendReference(group);
        sb.append(" io=(");
        appendReference(source);
        sb.append(") oo=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsInputToService(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[58]);
        appendReference(group);
        sb.append(" io=(");
        appendReference(source);
        sb.append(") so=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsServiceToOutput(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[58]);
        appendReference(group);
        sb.append(" so=(");
        appendReference(source);
        sb.append(") oo=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsServiceToService(SCAIReference group, SCAIReference source, SCAIReference destination, String operationID) {
        sb.append(Constants.REGISTRYCMDS[58]);
        appendReference(group);
        sb.append(" so=(");
        appendReference(source);
        sb.append(") so=(");
        appendReference(destination);
        sb.append(")");
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addUnregisterProcessingUnit(SCAIReference processingUnit, String operationID) {
        sb.append(Constants.REGISTRYCMDS[60]);
        appendReference(processingUnit);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUnsubscribeDatastream(SCAISensorReference reference, String operationID) {
        sb.append(Constants.SENSORCMDS[5]);
        appendSensorReference(reference);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateConfigurationParameterAtomicParameter(SCAIReference reference, String name, Boolean optional, String uom, Boolean identifier, SCAIReference dataType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[14]);
        appendReference(reference);
        appendVariable("n", name);
        if (optional != null) {
            appendVariable("o", optional.toString());
        }
        if (uom != null) {
            appendVariable("u", uom);
        }
        if (identifier != null) {
            appendVariable("i", identifier.toString());
        }
        appendReference(dataType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateConfigurationParameterComplexParameter(SCAIReference reference, String name, Boolean optional, SCAIReference[] configurationParameters, String operationID) {
        sb.append(Constants.REGISTRYCMDS[14]);
        appendReference(reference);
        appendVariable("n", name);
        if (optional != null) {
            appendVariable("o", optional.toString());
        }
        for (SCAIReference configurationParameter : configurationParameters) {
            appendReference(configurationParameter);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateConfigurationParameterSequenceParameter(SCAIReference reference, String name, Boolean optional, Integer min, Integer max, SCAIReference configurationParameter, String operationID) {
        sb.append(Constants.REGISTRYCMDS[14]);
        appendReference(reference);
        appendVariable("n", name);
        if (optional != null) {
            appendVariable("o", optional.toString());
        }
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        appendReference(configurationParameter);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataElementAtomicElement(SCAIReference reference, String name, SCAIReference dataType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[6]);
        appendReference(reference);
        appendVariable("n", name);
        appendReference(dataType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataElementComplexElement(SCAIReference reference, String name, SCAIReference[] dataElements, String operationID) {
        sb.append(Constants.REGISTRYCMDS[6]);
        appendReference(reference);
        appendVariable("n", name);
        for (SCAIReference dataElement : dataElements) {
            appendReference(dataElement);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataStreamType(SCAIReference reference, String name, SCAIReference[] dataElements, String operationID) {
        sb.append(Constants.REGISTRYCMDS[10]);
        appendReference(reference);
        appendVariable("n", name);
        for (SCAIReference dataElement : dataElements) {
            appendReference(dataElement);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataTypeBinaryType(SCAIReference reference, String name, Long min, Long max, byte[] defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[2]);
        appendReference(reference);
        appendVariable("n", name);
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        appendBinary("d", defaultvalue);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataTypeDecimalType(SCAIReference reference, String name, Float min, Float max, Float scale, Float defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[2]);
        appendReference(reference);
        appendVariable("n", name);
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        if (scale != null) {
            appendVariable("s", scale.toString());
        }
        appendVariable("d", defaultvalue.toString());
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataTypeStringType(SCAIReference reference, String name, Long min, Long max, String regex, String defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[2]);
        appendReference(reference);
        appendVariable("n", name);
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        if (regex != null) {
            appendString("r", regex);
        }
        appendString("d", defaultvalue);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateOperatorGroup(SCAIReference reference, String name, String operationID) {
        sb.append(Constants.REGISTRYCMDS[34]);
        appendReference(reference);
        appendVariable("n", name);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateOperatorType(SCAIReference reference, String name, String metaType, Map<String, String> properties, Map<String, Boolean> readOnly, String description, String operationID) {
        sb.append(Constants.REGISTRYCMDS[38]);
        appendReference(reference);
        appendVariable("n", name);
        appendVariable("m", metaType);
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey());
            appendString("v", property.getValue());
            appendVariable("w", readOnly.get(property.getKey()).toString());
            sb.append(")");
        }
        if (description != null) {
            appendString("ds", description);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateSensor(SCAISensorReference reference, String name, SCAIReference sensorDomain, SCAIReference sensorType, boolean virtual, String operationID) {
        sb.append(Constants.REGISTRYCMDS[30]);
        appendSensorReference(reference);
        appendVariable("n", name);
        appendReference(sensorDomain);
        appendReference(sensorType);
        appendVariable("vi", ((Boolean)virtual).toString());
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateSensorCategory(SCAIReference reference, String name, SCAIReference parentSensorCategory, SCAIReference[] sensorDomains, String operationID) {
        sb.append(Constants.REGISTRYCMDS[26]);
        appendReference(reference);
        appendVariable("n", name);
        if (parentSensorCategory != null) {
            appendReference(parentSensorCategory);
        }
        if (sensorDomains != null && sensorDomains.length > 0) {
            sb.append(" d=(");
            for (SCAIReference domain : sensorDomains) {
                appendReference(domain);
            }
            sb.append(")");
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateSensorDomain(SCAIReference reference, String name, String operationID) {
        sb.append(Constants.REGISTRYCMDS[22]);
        appendReference(reference);
        appendVariable("n", name);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateSensorType(SCAIReference reference, String name, String adapter, SCAIReference dataStreamType, SCAIReference[] configurationParameters, SCAIReference[] sensorCategories, SCAIReference[] sensorDomains, String operationID) {
        sb.append(Constants.REGISTRYCMDS[18]);
        appendReference(reference);
        appendVariable("n", name);
        appendVariable("a", adapter);
        appendReference(dataStreamType);
        if (configurationParameters != null && configurationParameters.length > 0) {
            sb.append(" p=(");
            for (SCAIReference parameter : configurationParameters) {
                appendReference(parameter);
            }
            sb.append(")");
        }
        if (sensorCategories != null && sensorCategories.length > 0) {
            sb.append(" c=(");
            for (SCAIReference category : sensorCategories) {
                appendReference(category);
            }
            sb.append(")");
        }
        if (sensorDomains != null && sensorDomains.length > 0) {
            sb.append(" d=(");
            for (SCAIReference domain : sensorDomains) {
                appendReference(domain);
            }
            sb.append(")");
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addWithdrawSensorAccess(SCAISensorReference sensorReference, SCAIReference[] user, SCAIReference[] domain, String operationID) {
        sb.append(Constants.ACCESSCMDS[7]);
        appendSensorReference(sensorReference);
        for (SCAIReference userReference : user) {
            sb.append(" u=(");
            appendReference(userReference);
            sb.append(")");
        }
        for (SCAIReference domainReference : domain) {
            sb.append(" d=(");
            appendReference(domainReference);
            sb.append(")");
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public Object buildConfigurationParameterAtomicParameter(String name, boolean optional,
        String uom, boolean identifier, SCAIReference dataType) {
        appendVariable("n", name);
        appendVariable("o", String.valueOf(optional));
        appendVariable("u", uom);
        appendVariable("i", String.valueOf(identifier));
        appendReference(dataType);
        return null;
    }

    @Override
    public Object buildConfigurationParameterComplexParameter(String name, boolean optional, ArrayList<SCAIReference> configurationParameters) {
        appendVariable("n", name);
        appendVariable("o", String.valueOf(optional));
        for (SCAIReference configurationParameter : configurationParameters) {
            appendReference(configurationParameter);
        }
        return null;
    }

    @Override
    public Object buildConfigurationParameterSequenceParameter(String name, boolean optional, int minLength, int maxLength, SCAIReference configurationParameter) {
        appendVariable("n", name);
        appendVariable("o", String.valueOf(optional));
        appendVariable("l", String.valueOf(minLength));
        appendVariable("h", String.valueOf(maxLength));
        appendReference(configurationParameter);
        return null;
    }

    @Override
    public Object buildDataElementAtomic(String name, SCAIReference dataType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildDataElementComplex(String name, ArrayList<SCAIReference> dataElements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildDataStreamType(String name, ArrayList<SCAIReference> dataElements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildDataTypeDescriptionDecimal(String name, boolean minSet, float min, boolean maxSet, float max, boolean scaleSet, float scale, float defaultValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildDataTypeDescriptionString(String name, boolean minSet, long min, boolean maxSet, long max, boolean regExSet, String regEx, String defaultValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildInputOperator(String name, SCAISensorReference sensor, Map<String, String> properties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildLinkOperatorsInputToOutput(SCAIReference source, SCAIReference destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildLinkOperatorsInputToService(SCAIReference source, SCAIReference destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildLinkOperatorsServiceToOutput(SCAIReference source, SCAIReference destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildLinkOperatorsServiceToService(SCAIReference source, SCAIReference destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildOperatorGroup(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildOperatorType(String name, String metaType, Map<String, String> properties, Map<String, Boolean> readOnly, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildOutputOperator(String name, SCAISensorReference sensor, Map<String, String> properties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildProcessingUnit(String address) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildReplyDataConfigurationValue(String name, Integer index, String value, ArrayList<SCAIConfigurationValue> configurationValues) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildReplyDataSecurityToken(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildReplyDataStream(Calendar timestamp, String name, String domainName, Integer oldValues, SCAIDataStreamElement[] dataStreamElements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType, boolean virtual) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildSensorCategory(String name, SCAIReference parent, ArrayList<SCAIReference> sensorDomains) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildSensorDomain(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildSensorType(String name, String adapter, SCAIReference dataStreamType, ArrayList<SCAIReference> configurationParameters, ArrayList<SCAIReference> sensorCategories, ArrayList<SCAIReference> sensorDomains) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildServiceOperator(String name, SCAIReference type, Map<String, String> properties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildUser(String name, String password, SCAIPermission[] permissions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearDocument() {
        this.lines = new ArrayList<String>();
    }

    @Override
    public Object getDocument() {
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(line);
        }
        return result.toString();
    }

    @SuppressWarnings("CallToThreadDumpStack")
    public static void main (String[] args) {
        TranslatorText testtrans = new TranslatorText();
        try {
//            SCAIDocument testdoc = SCAIDocument.Factory.parse("<scai:SCAI xmlns:scai=\"http://xml.offis.de/schema/SCAI-2.0\"><scai:Payload><scai:Acknowledgment><scai:Exception><scai:Text>DataType could not be created.</scai:Text><scai:OperationID>45</scai:OperationID></scai:Exception></scai:Acknowledgment></scai:Payload></scai:SCAI>");
//            SCAIDocument testdoc = SCAIDocument.Factory.parse("<scai:SCAI xmlns:scai=\"http://xml.offis.de/schema/SCAI-2.0\"><scai:Payload><scai:Acknowledgment><scai:Reply><scai:Data xsi:type=\"scai:ConfigurationParameterDescription\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><scai:name>test1</scai:name><scai:optional>false</scai:optional><scai:AtomicParameter><scai:uom>a1</scai:uom><scai:identifier>true</scai:identifier><scai:DataType><scai:name>ref1</scai:name></scai:DataType></scai:AtomicParameter></scai:Data><scai:Data xsi:type=\"scai:ConfigurationParameterDescription\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><scai:name>test2</scai:name><scai:optional>false</scai:optional><scai:ComplexParameter><scai:ConfigurationParameter><scai:name>ref3</scai:name></scai:ConfigurationParameter><scai:ConfigurationParameter><scai:id>ref2</scai:id></scai:ConfigurationParameter><scai:ConfigurationParameter><scai:name>ref1</scai:name></scai:ConfigurationParameter></scai:ComplexParameter></scai:Data><scai:OperationID>45</scai:OperationID></scai:Reply></scai:Acknowledgment></scai:Payload></scai:SCAI>");
            SCAIDocument testdoc = SCAIDocument.Factory.parse(new File("/media/Data/Development/Scampi/SCAITests/response.tmp"));
            System.out.println((String)testtrans.translate(testdoc));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCreateDataTypeBinaryType(String name, Long min, Long max, byte[] defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[0]);
        appendVariable("n", name);
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        appendBinary("d", defaultvalue);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataTypeListType(String name, Long min, Long max, String[] allowedValues, String[] defaultValues, String operationID) {
        sb.append(Constants.REGISTRYCMDS[0]);
        appendVariable("n", name);
        appendVariable("t", "l");
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        for (String allowedValue : allowedValues) {
            appendString("a", allowedValue);
        }
        for (String defaultValue : defaultValues) {
            appendString("d", defaultValue);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateDataTypeEnumType(String name, Map<Long, String> allowedValues, String defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[0]);
        appendVariable("n", name);
        appendVariable("t", "e");
        for (Map.Entry<Long, String> allowedValue : allowedValues.entrySet()) {
            sb.append(" a=(");
            appendVariable("o", allowedValue.getKey().toString());
            appendString("n", allowedValue.getValue());
            sb.append(")");
        }
        appendString("d", defaultvalue);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateOperatorInputOperator(SCAIReference group, String name, Map<String, String> properties, SCAISensorReference sensor, String operationID) {
        sb.append(Constants.REGISTRYCMDS[40]);
        appendReference(group);
        appendVariable("n", name);
        appendVariable("t", "i");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey().toString());
            appendString("v", property.getValue());
            sb.append(")");
        }
        appendSensorReference(sensor);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateOperatorOutputOperator(SCAIReference group, String name, Map<String, String> properties, SCAISensorReference sensor, String operationID) {
        sb.append(Constants.REGISTRYCMDS[40]);
        appendReference(group);
        appendVariable("n", name);
        appendVariable("t", "o");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey().toString());
            appendString("v", property.getValue());
            sb.append(")");
        }
        appendSensorReference(sensor);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateOperatorServiceOperator(SCAIReference group, String name, Map<String, String> properties, SCAIReference operatorType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[40]);
        appendReference(group);
        appendVariable("n", name);
        appendVariable("t", "s");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey().toString());
            appendString("v", property.getValue());
            sb.append(")");
        }
        appendReference(operatorType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addCreateUser(String username, byte[] password, SCAIPermission[] permissions, String[] roles, String operationID) {
        sb.append(Constants.ACCESSCMDS[0]);
        appendString("un", username);
        appendBinary("pw", password);

        for (SCAIPermission permission : permissions) {
            sb.append(" pm=(");
            switch (permission.getPrivilegeAction()) {
                case GRANT:
                    appendString("g", permission.getPrivilege());
                    break;
                case WITHDRAW:
                    appendString("w", permission.getPrivilege());
                    break;
            }
            for (Map.Entry<String, String> property : permission.getProperties().entrySet()) {
                sb.append(" p=(");
                appendVariable("k", property.getKey());
                appendVariable("v", property.getValue());
                sb.append(")");
            }
            if (permission.isInheritable() != null) {
                appendVariable("ih", permission.isInheritable().toString());
            }
            sb.append(")");
        }

        if (operationID != null) {
            appendVariable("id", operationID);
        }
        
        applyChanges();
    }

    @Override
    public void addGetOperatorGroupStatus(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[74]);
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorTypesBySensorCategory(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[47]);
        appendVariable("t", "c");
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addListAllSensorTypesBySensorDomain(SCAIReference reference, String operationID) {
        sb.append(Constants.REGISTRYCMDS[47]);
        appendVariable("t", "d");
        appendReference(reference);
        appendVariable("id", operationID);
        applyChanges();
    }

    @Override
    public void addUpdateDataTypeListType(SCAIReference reference, String name, Long min, Long max, String[] allowedValues, String[] defaultValues, String operationID) {
        sb.append(Constants.REGISTRYCMDS[2]);
        appendReference(reference);
        appendVariable("n", name);
        appendVariable("t", "l");
        if (min != null) {
            appendVariable("l", min.toString());
        }
        if (max != null) {
            appendVariable("h", max.toString());
        }
        for (String allowedValue : allowedValues) {
            appendString("a", allowedValue);
        }
        for (String defaultValue : defaultValues) {
            appendString("d", defaultValue);
        }
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateDataTypeEnumType(SCAIReference reference, String name, Map<Long, String> allowedValues, String defaultvalue, String operationID) {
        sb.append(Constants.REGISTRYCMDS[2]);
        appendReference(reference);
        appendVariable("n", name);
        appendVariable("t", "e");
        for (Map.Entry<Long, String> allowedValue : allowedValues.entrySet()) {
            sb.append(" a=(");
            appendVariable("o", allowedValue.getKey().toString());
            appendString("n", allowedValue.getValue());
            sb.append(")");
        }
        appendString("d", defaultvalue);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateOperatorInputOperator(SCAIReference reference, SCAIReference group, String name, Map<String, String> properties, SCAISensorReference sensor, String operationID) {
        sb.append(Constants.REGISTRYCMDS[42]);
        appendReference(reference);
        appendReference(group);
        appendVariable("n", name);
        appendVariable("t", "i");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey().toString());
            appendString("v", property.getValue());
            sb.append(")");
        }
        appendSensorReference(sensor);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateOperatorOutputOperator(SCAIReference reference, SCAIReference group, String name, Map<String, String> properties, SCAISensorReference sensor, String operationID) {
        sb.append(Constants.REGISTRYCMDS[42]);
        appendReference(reference);
        appendReference(group);
        appendVariable("n", name);
        appendVariable("t", "o");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey().toString());
            appendString("v", property.getValue());
            sb.append(")");
        }
        appendSensorReference(sensor);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateOperatorServiceOperator(SCAIReference reference, SCAIReference group, String name, Map<String, String> properties, SCAIReference operatorType, String operationID) {
        sb.append(Constants.REGISTRYCMDS[42]);
        appendReference(reference);
        appendReference(group);
        appendVariable("n", name);
        appendVariable("t", "s");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            sb.append(" p=(");
            appendVariable("k", property.getKey().toString());
            appendString("v", property.getValue());
            sb.append(")");
        }
        appendReference(operatorType);
        if (operationID != null) {
            appendVariable("id", operationID);
        }
        applyChanges();
    }

    @Override
    public void addUpdateUser(SCAIReference reference, String username, byte[] password, SCAIPermission[] permissions, String[] roles, String operationID) {
        sb.append(Constants.ACCESSCMDS[2]);
        appendReference(reference);
        appendString("un", username);
        appendBinary("pw", password);

        for (SCAIPermission permission : permissions) {
            sb.append(" pm=(");
            switch (permission.getPrivilegeAction()) {
                case GRANT:
                    appendString("g", permission.getPrivilege());
                    break;
                case WITHDRAW:
                    appendString("w", permission.getPrivilege());
                    break;
            }
            for (Map.Entry<String, String> property : permission.getProperties().entrySet()) {
                sb.append(" p=(");
                appendVariable("k", property.getKey());
                appendVariable("v", property.getValue());
                sb.append(")");
            }
            if (permission.isInheritable() != null) {
                appendVariable("ih", permission.isInheritable().toString());
            }
            sb.append(")");
        }

        if (operationID != null) {
            appendVariable("id", operationID);
        }

        applyChanges();
    }

    @Override
    public Object buildDataTypeDescriptionBinary(String name, boolean defaultValueSet, byte[] defaultValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildOperatorGroupStatus(Boolean data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildAccessor(SCAIReference user, SCAIReference sensorDomain) {
        appendReference(user);
        appendReference(sensorDomain);
        return null;
    }

    @Override
    public Object buildConfigurationValue(SCAIConfigurationValue configurationValue) {
        appendConfigurationValue(configurationValue);
        return null;
    }

    @Override
    public Object buildModules(Map<String, Boolean> supportedModules) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
