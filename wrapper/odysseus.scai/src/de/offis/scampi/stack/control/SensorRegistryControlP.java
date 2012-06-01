/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.control;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;
import java.util.Map;

/**
 *
 * @author Claas
 */
public class SensorRegistryControlP extends ControlModuleP {
    public boolean addChildCategoryToSensorCategory(SCAIReference category, SCAIReference parentCategory){
        return false;
    }
    
    public boolean addDataElementToDataStreamType(SCAIReference element, SCAIReference streamType){
        return false;
    }

    public boolean addSensorCategoryToSensorDomain(SCAIReference category, SCAIReference domain){
        return false;
    }

    public boolean addSensorTypeToSensorCategory(SCAIReference type, SCAIReference category)
    {
        return false;
    }

    public boolean addSensorTypeToSensorDomain(SCAIReference type, SCAIReference domain){
        return false;
    }

    public boolean createConfigurationParameterAtomic(String name, boolean optional, String uom, boolean identifier, SCAIReference dataType)
    {
        return false;
    }

    public boolean createConfigurationParameterComplex(String name, boolean optional, SCAIReference[] configurationParameters)
    {
        return false;
    }

    public boolean createConfigurationParameterSequence(String name, boolean optional, int minlength, int maxlength, SCAIReference configurationParameter)
    {
        return false;
    }

    public boolean createDataElementAtomic(String name, SCAIReference dataType)
    {
        return false;
    }

    public boolean createDataElementComplex(String name, SCAIReference[] dataElements)
    {
        return false;
    }

    public boolean createDataStreamType(String name, SCAIReference[] dataElements)
    {
        return false;
    }

    public boolean createDataTypeDecimal(String name, boolean minSet, float min, boolean maxSet, float max, boolean scaleSet, float scale, float defaultValue)
    {
        return false;
    }

    public boolean createDataTypeString(String name, boolean minSet, long min, boolean maxSet, long max, boolean regExSet, String regEx, String defaultValue)
    {
        return false;
    }

    public boolean createDataTypeBinary(String name, boolean minSet, long min, boolean maxSet, long max, byte[] defaultValue)
    {
        return false;
    }

    public boolean createDataTypeList(String name, boolean minSet, long min, boolean maxSet, long max, boolean allowedValuesSet, String[] allowedValues, String[] defaultValues)
    {
        return false;
    }

    public boolean createDataTypeEnum(String name, boolean allowedValuesSet, Map<Long, String> allowedValues, String defaultValue)
    {
        return false;
    }

    public boolean createSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType, boolean virtual)
    {
        return false;
    }

    public boolean createSensorCategory(String name, SCAIReference parentCategory, SCAIReference[] sensorDomains)
    {
        return false;
    }

    public boolean createSensorDomain(String name)
    {
        return false;
    }

    public boolean createSensorType(String name, String adapter, SCAIReference dataStreamType, SCAIReference[] configurationParameters, SCAIReference[] sensorTypes, SCAIReference[] sensorDomains)
    {
        return false;
    }

    public Object getConfigurationParameter(SCAIReference configurationParameter, IBuilder builder)
    {
        return null;
    }

    public Object getDataElement(SCAIReference dataElement, IBuilder builder)
    {
        return null;
    }

    public Object getDataStreamType(SCAIReference dataStreamType, IBuilder builder)
    {
        return null;
    }

    public Object getDataType(SCAIReference dataType, IBuilder builder)
    {
        return null;
    }

    public Object getSensor(SCAISensorReference sensor, IBuilder builder)
    {
        return null;
    }

    public Object getSensorCategory(SCAIReference sensorCategory, IBuilder builder)
    {
        return null;
    }

    public Object getSensorDomain(SCAIReference sensorDomain, IBuilder builder)
    {
        return null;
    }

    public Object getSensorType(SCAIReference sensorType, IBuilder builder)
    {
        return null;
    }

    public Object listAllConfigurationParameters(IBuilder builder)
    {
        return null;
    }

    public Object listAllDataElements(IBuilder builder)
    {
        return null;
    }

    public Object listAllDataStreamTypes(IBuilder builder)
    {
        return null;
    }

    public Object listAllDataTypes(IBuilder builder)
    {
        return null;
    }

    public Object listAllSensors(IBuilder builder)
    {
        return null;
    }

    public Object listAllSensorCategories(IBuilder builder)
    {
        return null;
    }
    public Object listAllSensorCategoriesBySensorDomain(SCAIReference domain, IBuilder builder)
    {
        return null;
    }
    public Object listAllSensorDomains(IBuilder builder)
    {
        return null;
    }
    public Object listAllSensorTypes(IBuilder builder)
    {
        return null;
    }
    public Object listAllSensorTypesBySensorCategory(SCAIReference category, IBuilder builder)
    {
        return null;
    }
    public Object listAllSensorTypesBySensorDomain(SCAIReference domain, IBuilder builder)
    {
        return null;
    }


    public boolean removeChildCategoryFromSensorCategory(SCAIReference category, SCAIReference parentCategory)
    {
        return false;
    }

    public boolean removeConfigurationParameter(SCAIReference configurationParameter)
    {
        return false;
    }

    public boolean removeDataElement(SCAIReference dataElement)
    {
        return false;
    }

    public boolean removeDataElementFromDataStreamType(SCAIReference element, SCAIReference streamType)
    {
        return false;
    }

    public boolean removeDataStreamType(SCAIReference dataStreamType)
    {
        return false;
    }

    public boolean removeDataType(SCAIReference dataType)
    {
        return false;
    }
    
    public boolean removeSensor(SCAISensorReference sensor)
    {
        return false;
    }

    public boolean removeSensorCategory(SCAIReference sensorCategory)
    {
        return false;
    }

    public boolean removeSensorCategoryFromSensorDomain(SCAIReference category, SCAIReference domain)
    {
        return false;
    }

    public boolean removeSensorDomain(SCAIReference sensorDomain)
    {
        return false;
    }

    public boolean removeSensorType(SCAIReference sensorType)
    {
        return false;
    }

    public boolean removeSensorTypeFromSensorCategory(SCAIReference type, SCAIReference category)
    {
        return false;
    }

    public boolean removeSensorTypeFromSensorDomain(SCAIReference type, SCAIReference domain)
    {
        return false;
    }

    public boolean updateConfigurationParameterAtomic(SCAIReference oldConfigurationParameter, String name, boolean optional, String uom, boolean identifier, SCAIReference dataType) {
            return false;
    }
    
    public boolean updateConfigurationParameterComplex(SCAIReference oldConfigurationParameter, String name, boolean optional, SCAIReference[] configurationParameters) {
            return false;
    }
    
    public boolean updateConfigurationParameterSequence(SCAIReference oldConfigurationParameter, String name, boolean optional, int minlength, int maxlength, SCAIReference configurationParameter) {
            return false;
    }

    public boolean updateDataElementAtomic(SCAIReference oldDataElement, String name, SCAIReference dataType)
    {
        return false;
    }

    public boolean updateDataElementComplex(SCAIReference oldDataElement, String name, SCAIReference[] dataElements)
    {
        return false;
    }

    public boolean updateDataStreamType(SCAIReference oldDataStreamType, String name, SCAIReference[] dataElements) {
        return false;
    }

    public boolean updateDataTypeBinary(SCAIReference oldDataType, String name, boolean minSet, long min, boolean maxSet, long max, byte[] defaultValue) {
        return false;
    }

    public boolean updateDataTypeDecimal(SCAIReference oldDataType, String name, boolean minSet, float min, boolean maxSet, float max, boolean scaleSet, float scale, float defaultValue)
    {
        return false;
    }

    public boolean updateDataTypeEnum(SCAIReference oldDataType, String name, boolean allowedValuesSet, Map<Long, String> allowedValues, String defaultValue) {
        return false;
    }

    public boolean updateDataTypeList(SCAIReference oldDataType, String name, boolean minSet, long min, boolean maxSet, long max, boolean allowedValuesSet, String[] allowedValues, String[] defaultValues)
    {
        return false;
    }

    public boolean updateDataTypeString(SCAIReference oldDataType, String name, boolean minSet, long min, boolean maxSet, long max, boolean regExSet, String regEx, String defaultValue)
    {
        return false;
    }

    public boolean updateSensor(SCAISensorReference oldSensor, String name, SCAIReference sensorDomain, SCAIReference sensorType, boolean virtual)
    {
        return false;
    }

    public boolean updateSensorCategory(SCAIReference oldSensorCategory, String name, SCAIReference parentCategory, SCAIReference[] sensorDomains)
    {
        return false;
    }

    public boolean updateSensorDomain(SCAIReference oldSensorDomain, String name)
    {
        return false;
    }

    public boolean updateSensorType(SCAIReference oldSensorType, String name, String adapter, SCAIReference dataStreamType, SCAIReference[] configurationParameters, SCAIReference[] sensorTypes, SCAIReference[] sensorDomains)
    {
        return false;
    }
    
}
