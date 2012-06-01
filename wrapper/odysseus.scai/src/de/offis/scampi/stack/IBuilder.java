/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack;

import de.offis.scampi.stack.scai.builder.types.SCAIConfigurationValue;
import de.offis.scampi.stack.scai.builder.types.SCAIDataStreamElement;
import de.offis.scampi.stack.scai.builder.types.SCAIPermission;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;



/**
 *
 * @author Claas
 */
public interface IBuilder {
    void addAcknowledgmentData(Object data, String operationID);

    void addAcknowledgmentException(String text, Integer errorCode, String operationID);

    void addAcknowledgmentSuccess(String text, String operationID);

    public void addAddChildCategoryToSensorCategory(SCAIReference category,
        SCAIReference parentCategory, String operationID);

    public void addAddDataElementToDataStreamType(SCAIReference element, SCAIReference streamType,
        String operationID);

    public void addAddSensorCategoryToSensorDomain(SCAIReference category, SCAIReference domain,
        String operationID);

    public void addAddSensorTypeToSensorCategory(SCAIReference type, SCAIReference category,
        String operationID);

    public void addAddSensorTypeToSensorDomain(SCAIReference type, SCAIReference domain,
        String operationID);

    public void addAuthenticateUser(String username, byte[] password, String operationID);

    public void addConfigureSensor(SCAISensorReference reference,
        SCAIConfigurationValue[] configurationValues, String operationID);

    public void addCreateConfigurationParameterAtomicParameter(String name, Boolean optional,
        String uom, Boolean identifier, SCAIReference dataType, String operationID);

    public void addCreateConfigurationParameterComplexParameter(String name, Boolean optional,
        SCAIReference[] configurationParameters, String operationID);

    public void addCreateConfigurationParameterSequenceParameter(String name, Boolean optional,
        Integer min, Integer max, SCAIReference configurationParameter, String operationID);

    public void addCreateDataElementAtomicElement(String name, SCAIReference dataType,
        String operationID);

    public void addCreateDataElementComplexElement(String name, SCAIReference[] dataElements,
        String operationID);

    public void addCreateDataStreamType(String name, SCAIReference[] dataElements,
        String operationID);

    public void addCreateDataTypeBinaryType(String name, Long min, Long max,
            byte[] defaultvalue, String operationID);

    public void addCreateDataTypeDecimalType(String name, Float min, Float max, Float scale,
        Float defaultvalue, String operationID);

    public void addCreateDataTypeStringType(String name, Long min, Long max, String regex,
        String defaultvalue, String operationID);

    public void addCreateDataTypeListType(String name, Long min, Long max,
            String[] allowedValues, String[] defaultValues, String operationID);

    public void addCreateDataTypeEnumType(String name, Map<Long, String> allowedValues,
            String defaultvalue, String operationID);

    public void addCreateOperatorGroup(String name, String operationID);

    public void addCreateOperatorInputOperator(SCAIReference group, String name, Map<String, String> properties,
            SCAISensorReference sensor, String operationID);

    public void addCreateOperatorOutputOperator(SCAIReference group, String name, Map<String, String> properties,
            SCAISensorReference sensor, String operationID);

    public void addCreateOperatorServiceOperator(SCAIReference group, String name, Map<String, String> properties,
            SCAIReference operatorType, String operationID);

    public void addCreateOperatorType(String name, String metaType, Map<String, String> properties,
        Map<String, Boolean> readOnly, String description, String operationID);

    public void addCreateSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType,
        boolean virtual, String operationID);

    public void addCreateSensorCategory(String name, SCAIReference parentSensorCategory,
        SCAIReference[] sensorDomains, String operationID);

    public void addCreateSensorDomain(String name, String operationID);

    public void addCreateSensorType(String name, String adapter, SCAIReference dataStreamType,
        SCAIReference[] configurationParameters, SCAIReference[] sensorCategories,
        SCAIReference[] sensorDomains, String operationID);

    public void addCreateUser(String username, byte[] password, SCAIPermission[] permissions,
        String[] roles, String operationID);

    public void addDataStream(Calendar timeStamp, String sensorName,
        String sensorDomainName, Integer olderValues, SCAIDataStreamElement[] dataStreamElements);

    public void addDeployOperatorGroup(SCAIReference operatorGroup, SCAIReference processingUnit,
        String operationID);

    public void addDestroySession(String sessionID, String operationID);

    public void addGetConfiguration(SCAISensorReference reference, String operationID);

    public void addGetConfigurationParameter(SCAIReference reference, String operationID);

    public void addGetDataElement(SCAIReference reference, String operationID);

    public void addGetDataStreamType(SCAIReference reference, String operationID);

    public void addGetDataType(SCAIReference reference, String operationID);

    public void addGetOperator(SCAIReference reference, SCAIReference group, String operationID);

    public void addGetOperatorGroup(SCAIReference reference, String operationID);

    public void addGetOperatorGroupStatus(SCAIReference reference, String operationID);

    public void addGetOperatorType(SCAIReference reference, String operationID);

    public void addGetProcessingUnit(SCAIReference reference, String operationID);

    public void addGetSensor(SCAISensorReference reference, String operationID);

    public void addGetSensorCategory(SCAIReference reference, String operationID);

    public void addGetSensorDomain(SCAIReference reference, String operationID);

    public void addGetSensorType(SCAIReference reference, String operationID);

    public void addGetSupportedModules(SCAISensorReference reference, String operationID);

    public void addGetUserData(SCAIReference reference, String operationID);

    public void addGetValue(SCAISensorReference reference, String operationID);

    public void addGrantSensorAccess(SCAISensorReference sensorReference, SCAIReference[] user,
        SCAIReference[] domain, String operationID);

    public void addIdentification(String securityToken, String username, byte[] password);

    public void addLinkOperatorsInputToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addLinkOperatorsInputToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addLinkOperatorsServiceToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addLinkOperatorsServiceToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addListAllConfigurationParameters(String operationID);

    public void addListAllDataElements(String operationID);

    public void addListAllDataStreamTypes(String operationID);

    public void addListAllDataTypes(String operationID);

    public void addListAllLinks(SCAIReference reference, String operationID);

    public void addListAllOperatorGroups(String operationID);

    public void addListAllOperatorTypes(String operationID);

    public void addListAllOperators(SCAIReference reference, String operationID);

    public void addListAllProcessingUnits(String operationID);

    public void addListAllSensorAccessors(SCAISensorReference sensorReference, String operationID);

    public void addListAllSensorCategories(String operationID);

    public void addListAllSensorCategoriesBySensorDomain(SCAIReference reference, String operationID);

    public void addListAllSensorDomains(String operationID);

    public void addListAllSensorTypes(String operationID);

    public void addListAllSensorTypesBySensorCategory(SCAIReference reference, String operationID);

    public void addListAllSensorTypesBySensorDomain(SCAIReference reference, String operationID);

    public void addListAllSensors(String operationID);

    public void addRegisterProcessingUnit(String name, String address, Double version,
        String operationID);

    public void addRemoveChildCategoryFromSensorCategory(SCAIReference category,
        SCAIReference parentCategory, String operationID);

    public void addRemoveConfigurationParameter(SCAIReference reference, String operationID);

    public void addRemoveDataElement(SCAIReference reference, String operationID);

    public void addRemoveDataElementFromDataStreamType(SCAIReference element,
        SCAIReference streamType, String operationID);

    public void addRemoveDataStreamType(SCAIReference reference, String operationID);

    public void addRemoveDataType(SCAIReference reference, String operationID);

    public void addRemoveOperator(SCAIReference reference, SCAIReference group, String operationID);

    public void addRemoveOperatorGroup(SCAIReference reference, String operationID);

    public void addRemoveOperatorType(SCAIReference reference, String operationID);

    public void addRemoveSensor(SCAISensorReference reference, String operationID);

    public void addRemoveSensorCategory(SCAIReference reference, String operationID);

    public void addRemoveSensorCategoryFromSensorDomain(SCAIReference category,
        SCAIReference domain, String operationID);

    public void addRemoveSensorDomain(SCAIReference reference, String operationID);

    public void addRemoveSensorType(SCAIReference reference, String operationID);

    public void addRemoveSensorTypeFromSensorCategory(SCAIReference type, SCAIReference category,
        String operationID);

    public void addRemoveSensorTypeFromSensorDomain(SCAIReference type, SCAIReference domain,
        String operationID);

    public void addRemoveUser(SCAIReference reference, String operationID);

    public void addStartSensor(SCAISensorReference reference, String operationID);

    public void addStopSensor(SCAISensorReference reference, String operationID);

    public void addSubscribeDatastream(SCAISensorReference reference, String operationID);

    public void addUndeployOperatorGroup(SCAIReference operatorGroup, String operationID);

    public void addUnlinkOperatorsInputToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addUnlinkOperatorsInputToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addUnlinkOperatorsServiceToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addUnlinkOperatorsServiceToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID);

    public void addUnregisterProcessingUnit(SCAIReference processingUnit, String operationID);

    public void addUnsubscribeDatastream(SCAISensorReference reference, String operationID);

    public void addUpdateConfigurationParameterAtomicParameter(SCAIReference reference,
        String name, Boolean optional, String uom, Boolean identifier, SCAIReference dataType,
        String operationID);

    public void addUpdateConfigurationParameterComplexParameter(SCAIReference reference,
        String name, Boolean optional, SCAIReference[] configurationParameters, String operationID);

    public void addUpdateConfigurationParameterSequenceParameter(SCAIReference reference,
        String name, Boolean optional, Integer min, Integer max,
        SCAIReference configurationParameter, String operationID);

    public void addUpdateDataElementAtomicElement(SCAIReference reference, String name,
        SCAIReference dataType, String operationID);

    public void addUpdateDataElementComplexElement(SCAIReference reference, String name,
        SCAIReference[] dataElements, String operationID);

    public void addUpdateDataStreamType(SCAIReference reference, String name,
        SCAIReference[] dataElements, String operationID);

    public void addUpdateDataTypeBinaryType(SCAIReference reference, String name, Long min,
        Long max, byte[] defaultvalue, String operationID);

    public void addUpdateDataTypeDecimalType(SCAIReference reference, String name, Float min,
        Float max, Float scale, Float defaultvalue, String operationID);

    public void addUpdateDataTypeStringType(SCAIReference reference, String name, Long min,
        Long max, String regex, String defaultvalue, String operationID);

    public void addUpdateDataTypeListType(SCAIReference reference, String name, Long min,
            Long max, String[] allowedValues, String[] defaultValues, String operationID);

    public void addUpdateDataTypeEnumType(SCAIReference reference, String name,
            Map<Long, String> allowedValues, String defaultvalue, String operationID);

    public void addUpdateOperatorGroup(SCAIReference reference, String name, String operationID);

    public void addUpdateOperatorInputOperator(SCAIReference reference, SCAIReference group,
        String name, Map<String, String> properties, SCAISensorReference sensor, String operationID);

    public void addUpdateOperatorOutputOperator(SCAIReference reference, SCAIReference group,
        String name, Map<String, String> properties, SCAISensorReference sensor, String operationID);

    public void addUpdateOperatorServiceOperator(SCAIReference reference, SCAIReference group,
        String name, Map<String, String> properties, SCAIReference operatorType, String operationID);

    public void addUpdateOperatorType(SCAIReference reference, String name, String metaType,
        Map<String, String> properties, Map<String, Boolean> readOnly, String description, String operationID);

    public void addUpdateSensor(SCAISensorReference reference, String name, SCAIReference sensorDomain,
        SCAIReference sensorType, boolean virtual, String operationID);

    public void addUpdateSensorCategory(SCAIReference reference, String name,
        SCAIReference parentSensorCategory, SCAIReference[] sensorDomains, String operationID);

    public void addUpdateSensorDomain(SCAIReference reference, String name, String operationID);

    public void addUpdateSensorType(SCAIReference reference, String name, String adapter,
        SCAIReference dataStreamType, SCAIReference[] configurationParameters,
        SCAIReference[] sensorCategories, SCAIReference[] sensorDomains, String operationID);

    public void addUpdateUser(SCAIReference reference, String username, byte[] password,
        SCAIPermission[] permissions, String[] roles, String operationID);

    public void addWithdrawSensorAccess(SCAISensorReference sensorReference, SCAIReference[] user,
        SCAIReference[] domain, String operationID);

    public Object buildAccessor(SCAIReference user, SCAIReference sensorDomain);

    public Object buildConfigurationParameterAtomicParameter(String name, boolean optional,
        String uom, boolean identifier, SCAIReference dataType);

    public Object buildConfigurationParameterComplexParameter(String name, boolean optional,
        ArrayList<SCAIReference> configurationParameters);

    public Object buildConfigurationParameterSequenceParameter(String name, boolean optional,
        int minLength, int maxLenght, SCAIReference configurationParameter);

    public Object buildConfigurationValue(SCAIConfigurationValue configurationValue);

    public Object buildDataElementAtomic(String name, SCAIReference dataType);

    public Object buildDataElementComplex(String name, ArrayList<SCAIReference> dataElements);

    public Object buildDataStreamType(String name, ArrayList<SCAIReference> dataElements);

    public Object buildDataTypeDescriptionDecimal(String name, boolean minSet, float min,
        boolean maxSet, float max, boolean scaleSet, float scale, float defaultValue);

    public Object buildDataTypeDescriptionString(String name, boolean minSet, long min,
        boolean maxSet, long max, boolean regExSet, String regEx, String defaultValue);

    public Object buildDataTypeDescriptionBinary(String name, boolean defaultValueSet, byte[] defaultValue);
    
    public Object buildInputOperator(String name, SCAISensorReference sensor,
        Map<String, String> properties);

    public Object buildLinkOperatorsInputToOutput(SCAIReference source, SCAIReference destination);

    public Object buildLinkOperatorsInputToService(SCAIReference source, SCAIReference destination);

    public Object buildLinkOperatorsServiceToOutput(SCAIReference source, SCAIReference destination);

    public Object buildLinkOperatorsServiceToService(SCAIReference source, SCAIReference destination);

    public Object buildOperatorGroup(String name);
    
    public Object buildOperatorGroupStatus(Boolean data);

    public Object buildOperatorType(String name, String metaType, Map<String, String> properties,
        Map<String, Boolean> readOnly, String description);

    public Object buildOutputOperator(String name, SCAISensorReference sensor,
        Map<String, String> properties);

    public Object buildProcessingUnit(String address);

    public Object buildReplyDataConfigurationValue(String name, Integer index, String value,
        ArrayList<SCAIConfigurationValue> configurationValues);

    public Object buildReplyDataSecurityToken(Object data);

    public Object buildReplyDataStream(Calendar timestamp, String name, String domainName,
        Integer oldValues, SCAIDataStreamElement[] dataStreamElements);

    public Object buildSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType,
        boolean virtual);

    public Object buildSensorCategory(String name, SCAIReference parent,
        ArrayList<SCAIReference> sensorDomains);

    public Object buildSensorDomain(String name);

    public Object buildSensorType(String name, String adapter, SCAIReference dataStreamType,
        ArrayList<SCAIReference> configurationParameters,
        ArrayList<SCAIReference> sensorCategories, ArrayList<SCAIReference> sensorDomains);

    public Object buildServiceOperator(String name, SCAIReference type,
        Map<String, String> properties);

    public Object buildUser(String name, String password, SCAIPermission[] permissions);

    public Object buildModules(Map<String, Boolean> supportedModules);

    void clearDocument();

    Object getDocument();
}
