/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack.scai.builder;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.scai.builder.types.SCAIConfigurationValue;
import de.offis.scampi.stack.scai.builder.types.SCAIDataStreamElement;
import de.offis.scampi.stack.scai.builder.types.SCAIPermission;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;

import de.offis.xml.schema.scai20.ConfigurationParameterDescription;
import de.offis.xml.schema.scai20.ConfigurationParameterDescription.*;
import de.offis.xml.schema.scai20.ConfigurationValueDescription;
import de.offis.xml.schema.scai20.DataElementDescription;
import de.offis.xml.schema.scai20.DataElementDescription.*;
import de.offis.xml.schema.scai20.DataElementValueDescription;
import de.offis.xml.schema.scai20.DataStreamTypeDescription;
import de.offis.xml.schema.scai20.DataTypeDescription;
import de.offis.xml.schema.scai20.DataTypeDescription.*;
import de.offis.xml.schema.scai20.ErrorDescription;
import de.offis.xml.schema.scai20.ModulesDescription;
import de.offis.xml.schema.scai20.OperatorDescription;
import de.offis.xml.schema.scai20.OperatorDescription.*;
import de.offis.xml.schema.scai20.OperatorGroupDescription;
import de.offis.xml.schema.scai20.OperatorGroupStatus;
import de.offis.xml.schema.scai20.OperatorLinkDescription;
import de.offis.xml.schema.scai20.OperatorLinkDescription.*;
import de.offis.xml.schema.scai20.OperatorTypeDescription;
import de.offis.xml.schema.scai20.PermissionDescription;
import de.offis.xml.schema.scai20.ProcessingUnitDescription;
import de.offis.xml.schema.scai20.Reference;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Identification;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Identification.LoginIdentification;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Acknowledgment.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.AccessControl.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorControl.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.*;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.ControlData.SensorRegistryControl.UpdateOperator.Operator;
import de.offis.xml.schema.scai20.SCAIDocument.SCAI.Payload.Measurements;
import de.offis.xml.schema.scai20.SensorCategoryDescription;
import de.offis.xml.schema.scai20.SensorDataDescription;
import de.offis.xml.schema.scai20.SensorDescription;
import de.offis.xml.schema.scai20.SensorDomainDescription;
import de.offis.xml.schema.scai20.SensorTypeDescription;
import de.offis.xml.schema.scai20.UserDescription;
import java.math.BigDecimal;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.util.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



/**
 *
 * @author Claas
 */
public class BuilderSCAI20 implements IBuilder {
    private SCAIDocument document;
    private SCAIDocument docTemp;

    public BuilderSCAI20() {
        clearDocument();
    }

    @Override
    public void addAcknowledgmentData(Object contents, String operationID) {
        Acknowledgment.Reply data = buildAcknowledgmentTree().addNewReply();

        if (operationID != null) {
            data.setOperationID(operationID);
        }

        if (contents instanceof XmlObject[]) {
            data.setDataArray((XmlObject[]) contents);
        }

        if (contents instanceof XmlObject) {
            data.addNewData();
            data.setDataArray(0, (XmlObject) contents);
        }

        applyChanges();
    }

    @Override
    public void addAcknowledgmentException(String text, Integer errorCode, String operationID) {
        Acknowledgment.Exception exception = buildAcknowledgmentTree().addNewException();

        if (operationID != null) {
            exception.setOperationID(operationID);
        }

        if (text != null) {
            exception.setText(text);
        }

        if (errorCode != null) {
            exception.setErrorCode(errorCode);
        }

        applyChanges();
    }

    @Override
    public void addAcknowledgmentSuccess(String text, String operationID) {
        Success success = buildAcknowledgmentTree().addNewSuccess();

        if (operationID != null) {
            success.setOperationID(operationID);
        }

        if (text != null) {
            success.setText(text);
        }

        applyChanges();
    }

    @Override
    public void addAddChildCategoryToSensorCategory(SCAIReference category,
        SCAIReference parentCategory, String operationID) {
        AddChildCategoryToSensorCategory addChildCategoryToSensorCategory =
            buildSensorRegistryControlTree().addNewAddChildCategoryToSensorCategory();

        if (operationID != null) {
            addChildCategoryToSensorCategory.setOperationID(operationID);
        }

        addChildCategoryToSensorCategory.setSensorCategory(convertReference(category));
        addChildCategoryToSensorCategory.setParentSensorCategory(convertReference(parentCategory));
        applyChanges();
    }

    @Override
    public void addAddDataElementToDataStreamType(SCAIReference element, SCAIReference streamType,
        String operationID) {
        AddDataElementToDataStreamType addDataElementToDataStreamType =
            buildSensorRegistryControlTree().addNewAddDataElementToDataStreamType();

        if (operationID != null) {
            addDataElementToDataStreamType.setOperationID(operationID);
        }

        addDataElementToDataStreamType.setDataElement(convertReference(element));
        addDataElementToDataStreamType.setDataStreamType(convertReference(streamType));
        applyChanges();
    }

    @Override
    public void addAddSensorCategoryToSensorDomain(SCAIReference category, SCAIReference domain,
        String operationID) {
        AddSensorCategoryToSensorDomain addSensorCategoryToSensorDomain =
            buildSensorRegistryControlTree().addNewAddSensorCategoryToSensorDomain();

        if (operationID != null) {
            addSensorCategoryToSensorDomain.setOperationID(operationID);
        }

        addSensorCategoryToSensorDomain.setSensorCategory(convertReference(category));
        addSensorCategoryToSensorDomain.setSensorDomain(convertReference(domain));
        applyChanges();
    }

    @Override
    public void addAddSensorTypeToSensorCategory(SCAIReference type, SCAIReference category,
        String operationID) {
        AddSensorTypeToSensorCategory addSensorTypeToSensorCategory =
            buildSensorRegistryControlTree().addNewAddSensorTypeToSensorCategory();

        if (operationID != null) {
            addSensorTypeToSensorCategory.setOperationID(operationID);
        }

        addSensorTypeToSensorCategory.setSensorType(convertReference(type));
        addSensorTypeToSensorCategory.setSensorCategory(convertReference(category));
        applyChanges();
    }

    @Override
    public void addAddSensorTypeToSensorDomain(SCAIReference type, SCAIReference domain,
        String operationID) {
        AddSensorTypeToSensorDomain addSensorTypeToSensorDomain =
            buildSensorRegistryControlTree().addNewAddSensorTypeToSensorDomain();

        if (operationID != null) {
            addSensorTypeToSensorDomain.setOperationID(operationID);
        }

        addSensorTypeToSensorDomain.setSensorType(convertReference(type));
        addSensorTypeToSensorDomain.setSensorDomain(convertReference(domain));
        applyChanges();
    }

    @Override
    public void addAuthenticateUser(String username, byte[] password, String operationID) {
        AuthenticateUser authenticateUser = buildAccessControlTree().addNewAuthenticateUser();

        if (operationID != null) {
            authenticateUser.setOperationID(operationID);
        }

        authenticateUser.setUsername(username);
//        authenticateUser.setPassword(password.getBytes());
        authenticateUser.setPassword(Base64.decode(password));

        applyChanges();
    }

    private ConfigurationValueDescription[] addConfigurationValuesRecursive(
        List<SCAIConfigurationValue> configurationValues) {
        ConfigurationValueDescription[] result =
            new ConfigurationValueDescription[configurationValues.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = ConfigurationValueDescription.Factory.newInstance();
            result[i].setName(configurationValues.get(i).getName());

            if (configurationValues.get(i).getIndex() != null) {
                result[i].setIndex(configurationValues.get(i).getIndex());
            }

            if (configurationValues.get(i).getValue() != null) {
                result[i].setValue(configurationValues.get(i).getValue());
            } else if (configurationValues.get(i).getConfigurationValues() != null) {
                result[i].setConfigurationValueArray(
                        addConfigurationValuesRecursive(
                                configurationValues.get(i).getConfigurationValues()));
            }
        }

        return result;
    }

    @Override
    public void addConfigureSensor(SCAISensorReference reference,
        SCAIConfigurationValue[] configurationValues, String operationID) {
        ConfigureSensor configureSensor = buildSensorControlTree().addNewConfigureSensor();

        if (operationID != null) {
            configureSensor.setOperationID(operationID);
        }

        configureSensor.setSensor(convertSensorReference(reference));
        configureSensor.setConfigurationValue(
                addConfigurationValuesRecursive(Arrays.asList(configurationValues))[0]);

        applyChanges();
    }

    @Override
    public void addCreateConfigurationParameterAtomicParameter(String name, Boolean optional,
        String uom, Boolean identifier, SCAIReference dataType, String operationID) {
        CreateConfigurationParameter createConfigurationParameter =
            buildSensorRegistryControlTree().addNewCreateConfigurationParameter();

        if (operationID != null) {
            createConfigurationParameter.setOperationID(operationID);
        }

        ConfigurationParameterDescription configurationParameterDescription =
            createConfigurationParameter.addNewConfigurationParameter();
        configurationParameterDescription.setName(name);
        configurationParameterDescription.setOptional(optional);

        AtomicParameter atomicParameter = configurationParameterDescription.addNewAtomicParameter();
        atomicParameter.setUom(uom);
        atomicParameter.setIdentifier(identifier);

        Reference newDataType = atomicParameter.addNewDataType();

        if (dataType.getId() != null) {
            newDataType.setId(dataType.getId());
        } else {
            newDataType.setName(dataType.getName());
        }

        applyChanges();
    }

    @Override
    public void addCreateConfigurationParameterComplexParameter(String name, Boolean optional,
        SCAIReference[] configurationParameters, String operationID) {
        CreateConfigurationParameter createConfigurationParameter =
            buildSensorRegistryControlTree().addNewCreateConfigurationParameter();

        if (operationID != null) {
            createConfigurationParameter.setOperationID(operationID);
        }

        ConfigurationParameterDescription configurationParameterDescription =
            createConfigurationParameter.addNewConfigurationParameter();
        configurationParameterDescription.setName(name);
        configurationParameterDescription.setOptional(optional);

        ComplexParameter complexParameter =
            configurationParameterDescription.addNewComplexParameter();

        for (SCAIReference configurationParameter : configurationParameters) {
            Reference newConfigurationParameter = complexParameter.addNewConfigurationParameter();

            if (configurationParameter.getId() != null) {
                newConfigurationParameter.setId(configurationParameter.getId());
            } else {
                newConfigurationParameter.setName(configurationParameter.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addCreateConfigurationParameterSequenceParameter(String name, Boolean optional,
        Integer min, Integer max, SCAIReference configurationParameter, String operationID) {
        CreateConfigurationParameter createConfigurationParameter =
            buildSensorRegistryControlTree().addNewCreateConfigurationParameter();

        if (operationID != null) {
            createConfigurationParameter.setOperationID(operationID);
        }

        ConfigurationParameterDescription configurationParameterDescription =
            createConfigurationParameter.addNewConfigurationParameter();
        configurationParameterDescription.setName(name);
        configurationParameterDescription.setOptional(optional);

        SequenceParameter sequenceParameter =
            configurationParameterDescription.addNewSequenceParameter();
        sequenceParameter.setMinlength(min);
        sequenceParameter.setMaxlength(max);

        Reference newConfigurationParameter = sequenceParameter.addNewConfigurationParameter();

        if (configurationParameter.getId() != null) {
            newConfigurationParameter.setId(configurationParameter.getId());
        } else {
            newConfigurationParameter.setName(configurationParameter.getName());
        }

        applyChanges();
    }

    @Override
    public void addCreateDataElementAtomicElement(String name, SCAIReference dataType,
        String operationID) {
        CreateDataElement createDataElement =
            buildSensorRegistryControlTree().addNewCreateDataElement();

        if (operationID != null) {
            createDataElement.setOperationID(operationID);
        }

        DataElementDescription dataElementDescription = createDataElement.addNewDataElement();

        AtomicElement atomicElement = dataElementDescription.addNewAtomicElement();
        atomicElement.setName(name);

        atomicElement.setDataType(convertReference(dataType));

        applyChanges();
    }

    @Override
    public void addCreateDataElementComplexElement(String name, SCAIReference[] dataElements,
        String operationID) {
        CreateDataElement createDataElement =
            buildSensorRegistryControlTree().addNewCreateDataElement();

        if (operationID != null) {
            createDataElement.setOperationID(operationID);
        }

        DataElementDescription dataElementDescription = createDataElement.addNewDataElement();

        ComplexElement complexElement = dataElementDescription.addNewComplexElement();
        complexElement.setName(name);

        for (SCAIReference dataElement : dataElements) {
            Reference newDataElement = complexElement.addNewDataElement();

            if (dataElement.getId() != null) {
                newDataElement.setId(dataElement.getId());
            } else {
                newDataElement.setName(dataElement.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addCreateDataStreamType(String name, SCAIReference[] dataElements,
        String operationID) {
        CreateDataStreamType createDataStreamType =
            buildSensorRegistryControlTree().addNewCreateDataStreamType();

        if (operationID != null) {
            createDataStreamType.setOperationID(operationID);
        }

        DataStreamTypeDescription dataElementDescription =
            createDataStreamType.addNewDataStreamType();
        dataElementDescription.setName(name);

        for (SCAIReference dataElement : dataElements) {
            Reference newDataElement = dataElementDescription.addNewDataElement();

            if (dataElement.getId() != null) {
                newDataElement.setId(dataElement.getId());
            } else {
                newDataElement.setName(dataElement.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addCreateDataTypeBinaryType(String name, Long min, Long max,
            byte[] defaultvalue, String operationID) {
        CreateDataType createDataType = buildSensorRegistryControlTree().addNewCreateDataType();

        if (operationID != null) {
            createDataType.setOperationID(operationID);
        }

        DataTypeDescription dataTypeDescription = createDataType.addNewDataType();
        dataTypeDescription.setName(name);

        BinaryType binaryType = dataTypeDescription.addNewBinaryType();
        binaryType.setDefaultvalue(Base64.decode(defaultvalue));
        
        if (min != null) binaryType.setMin(min);
        if (max != null) binaryType.setMax(max);

        applyChanges();
    }

    @Override
    public void addCreateDataTypeDecimalType(String name, Float min, Float max, Float scale,
        Float defaultvalue, String operationID) {
        CreateDataType createDataType = buildSensorRegistryControlTree().addNewCreateDataType();

        if (operationID != null) {
            createDataType.setOperationID(operationID);
        }

        DataTypeDescription dataTypeDescription = createDataType.addNewDataType();
        dataTypeDescription.setName(name);

        DecimalType decimalType = dataTypeDescription.addNewDecimalType();

        if (min != null) {
            decimalType.setMin(min);
        }

        if (max != null) {
            decimalType.setMax(max);
        }

        if (scale != null) {
            decimalType.setScale(scale);
        }

        decimalType.setDefaultvalue(defaultvalue);

        applyChanges();
    }

    @Override
    public void addCreateDataTypeStringType(String name, Long min, Long max, String regex,
        String defaultvalue, String operationID) {
        CreateDataType createDataType = buildSensorRegistryControlTree().addNewCreateDataType();

        if (operationID != null) {
            createDataType.setOperationID(operationID);
        }

        DataTypeDescription dataTypeDescription = createDataType.addNewDataType();
        dataTypeDescription.setName(name);

        StringType stringType = dataTypeDescription.addNewStringType();

        if (min != null) {
            stringType.setMin(min);
        }

        if (max != null) {
            stringType.setMax(max);
        }

        if (regex != null) {
            stringType.setRegex(regex);
        }

        stringType.setDefaultvalue(defaultvalue);

        applyChanges();
    }

    @Override
    public void addCreateDataTypeListType(String name, Long min, Long max,
            String[] allowedValues, String[] defaultValues, String operationID) {
        CreateDataType createDataType = buildSensorRegistryControlTree().addNewCreateDataType();

        if (operationID != null) {
            createDataType.setOperationID(operationID);
        }

        DataTypeDescription dataTypeDescription = createDataType.addNewDataType();
        dataTypeDescription.setName(name);

        ListType listType = dataTypeDescription.addNewListType();

        if (min != null) {
            listType.setMin(min);
        }

        if (max != null) {
            listType.setMax(max);
        }

        for (String allowedValue : allowedValues) {
            listType.addAllowedvalue(allowedValue);
        }

        for (String defaultValue : defaultValues) {
            listType.addDefaultvalue(defaultValue);
        }

        applyChanges();
    }

    @Override
    public void addCreateDataTypeEnumType(String name, Map<Long, String> allowedValues,
        String defaultvalue, String operationID) {
        CreateDataType createDataType = buildSensorRegistryControlTree().addNewCreateDataType();

        if (operationID != null) {
            createDataType.setOperationID(operationID);
        }

        DataTypeDescription dataTypeDescription = createDataType.addNewDataType();
        dataTypeDescription.setName(name);

        EnumType enumType = dataTypeDescription.addNewEnumType();

        for (Entry<Long, String> allowedValue : allowedValues.entrySet()) {
            EnumType.Allowedvalue newAllowedValue = enumType.addNewAllowedvalue();
            newAllowedValue.setOrdinal(allowedValue.getKey());
            newAllowedValue.setName(allowedValue.getValue());
        }

        enumType.setDefaultvalue(defaultvalue);

        applyChanges();
    }

    @Override
    public void addCreateOperatorGroup(String name, String operationID) {
        CreateOperatorGroup createOperatorGroup =
            buildSensorRegistryControlTree().addNewCreateOperatorGroup();

        if (operationID != null) {
            createOperatorGroup.setOperationID(operationID);
        }

        OperatorGroupDescription operatorGroup = createOperatorGroup.addNewOperatorGroup();
        operatorGroup.setName(name);

        applyChanges();
    }

    @Override
    public void addCreateOperatorInputOperator(SCAIReference group, String name, 
        Map<String, String> properties, SCAISensorReference sensor, String operationID) {
        CreateOperator createOperator = buildSensorRegistryControlTree().addNewCreateOperator();

        if (operationID != null) {
            createOperator.setOperationID(operationID);
        }

        createOperator.setOperatorGroup(convertReference(group));

        OperatorDescription operator = createOperator.addNewOperator();
        operator.setName(name);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                Property property = operator.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        InputOperator inputOperator = operator.addNewInputOperator();
        inputOperator.setSensor(convertSensorReference(sensor));

        applyChanges();
    }

    @Override
    public void addCreateOperatorOutputOperator(SCAIReference group, String name,
        Map<String, String> properties, SCAISensorReference sensor, String operationID) {
        CreateOperator createOperator = buildSensorRegistryControlTree().addNewCreateOperator();

        if (operationID != null) {
            createOperator.setOperationID(operationID);
        }

        createOperator.setOperatorGroup(convertReference(group));

        OperatorDescription operator = createOperator.addNewOperator();
        operator.setName(name);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                Property property = operator.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        OutputOperator outputOperator = operator.addNewOutputOperator();
        outputOperator.setSensor(convertSensorReference(sensor));

        applyChanges();
    }

    @Override
    public void addCreateOperatorServiceOperator(SCAIReference group, String name,
        Map<String, String> properties, SCAIReference operatorType, String operationID) {
        CreateOperator createOperator = buildSensorRegistryControlTree().addNewCreateOperator();

        if (operationID != null) {
            createOperator.setOperationID(operationID);
        }

        createOperator.setOperatorGroup(convertReference(group));

        OperatorDescription operator = createOperator.addNewOperator();
        operator.setName(name);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                Property property = operator.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        ServiceOperator serviceOperator = operator.addNewServiceOperator();
        serviceOperator.setOperatorType(convertReference(operatorType));

        applyChanges();
    }

    @Override
    public void addCreateOperatorType(String name, String metaType, Map<String, String> properties,
        Map<String, Boolean> readOnly, String description, String operationID) {
        CreateOperatorType createOperatorType =
            buildSensorRegistryControlTree().addNewCreateOperatorType();

        if (operationID != null) {
            createOperatorType.setOperationID(operationID);
        }

        OperatorTypeDescription operatorType = createOperatorType.addNewOperatorType();
        operatorType.setName(name);
        operatorType.setMetaType(metaType);

        if (description != null) {
            operatorType.setDescription(description);
        }

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                OperatorTypeDescription.Property property = operatorType.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
                property.setReadOnly(readOnly.get(entry.getKey()));
            }
        }

        applyChanges();
    }

    @Override
    public void addCreateSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType,
        boolean virtual, String operationID) {
        CreateSensor createSensor = buildSensorRegistryControlTree().addNewCreateSensor();

        if (operationID != null) {
            createSensor.setOperationID(operationID);
        }

        SensorDescription sensor = createSensor.addNewSensor();
        sensor.setName(name);
        sensor.setSensorType(convertReference(sensorType));
        sensor.setSensorDomain(convertReference(sensorDomain));
        sensor.setVirtual(virtual);

        applyChanges();
    }

    @Override
    public void addCreateSensorCategory(String name, SCAIReference parentSensorCategory,
        SCAIReference[] sensorDomains, String operationID) {
        CreateSensorCategory createSensorCategory =
            buildSensorRegistryControlTree().addNewCreateSensorCategory();

        if (operationID != null) {
            createSensorCategory.setOperationID(operationID);
        }

        SensorCategoryDescription sensorCategory = createSensorCategory.addNewSensorCategory();
        sensorCategory.setName(name);

        if (parentSensorCategory != null) {
            if ((parentSensorCategory.getId() != null) || (parentSensorCategory.getName() != null)) {
                Reference newParentSensorCategory = sensorCategory.addNewParentSensorCategory();

                if (parentSensorCategory.getId() != null) {
                    newParentSensorCategory.setId(parentSensorCategory.getId());
                } else {
                    newParentSensorCategory.setName(parentSensorCategory.getName());
                }
            }
        }

        for (SCAIReference sensorDomain : sensorDomains) {
            Reference newSensorDomain = sensorCategory.addNewSensorDomain();

            if (sensorDomain.getId() != null) {
                newSensorDomain.setId(sensorDomain.getId());
            } else {
                newSensorDomain.setName(sensorDomain.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addCreateSensorDomain(String name, String operationID) {
        CreateSensorDomain createSensorDomain =
            buildSensorRegistryControlTree().addNewCreateSensorDomain();

        if (operationID != null) {
            createSensorDomain.setOperationID(operationID);
        }

        SensorDomainDescription sensorDomain = createSensorDomain.addNewSensorDomain();
        sensorDomain.setName(name);

        applyChanges();
    }

    @Override
    public void addCreateSensorType(String name, String adapter, SCAIReference dataStreamType,
        SCAIReference[] configurationParameters, SCAIReference[] sensorCategories,
        SCAIReference[] sensorDomains, String operationID) {
        CreateSensorType createSensorType =
            buildSensorRegistryControlTree().addNewCreateSensorType();

        if (operationID != null) {
            createSensorType.setOperationID(operationID);
        }

        SensorTypeDescription sensorType = createSensorType.addNewSensorType();
        sensorType.setName(name);
        sensorType.setAdapter(adapter);

        sensorType.setDataStreamType(convertReference(dataStreamType));

        for (SCAIReference configurationParameter : configurationParameters) {
            Reference newConfigurationParameter = sensorType.addNewConfigurationParameter();

            if (configurationParameter.getId() != null) {
                newConfigurationParameter.setId(configurationParameter.getId());
            } else {
                newConfigurationParameter.setName(configurationParameter.getName());
            }
        }

        for (SCAIReference sensorCategory : sensorCategories) {
            Reference newSensorCategory = sensorType.addNewSensorCategory();

            if (sensorCategory.getId() != null) {
                newSensorCategory.setId(sensorCategory.getId());
            } else {
                newSensorCategory.setName(sensorCategory.getName());
            }
        }

        for (SCAIReference sensorDomain : sensorDomains) {
            Reference newSensorDomain = sensorType.addNewSensorDomain();

            if (sensorDomain.getId() != null) {
                newSensorDomain.setId(sensorDomain.getId());
            } else {
                newSensorDomain.setName(sensorDomain.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addCreateUser(String username, byte[] password, SCAIPermission[] permissions,
        String[] roles, String operationID) {
        CreateUser createUser = buildAccessControlTree().addNewCreateUser();

        if (operationID != null) {
            createUser.setOperationID(operationID);
        }

        UserDescription user = createUser.addNewUser();
        user.setUsername(username);
        user.setPassword(Base64.decode(password));

        for (SCAIPermission scaiPermission : permissions) {
            PermissionDescription permission = user.addNewPermission();
            switch (scaiPermission.getPrivilegeAction()) {
                case GRANT:
                    permission.addNewPermission().setGrant(scaiPermission.getPrivilege());
                    break;
                case WITHDRAW:
                    permission.addNewPermission().setWithdraw(scaiPermission.getPrivilege());
                    break;
                default:
                    continue;
            }
            for (Map.Entry<String, String> scaiProperty : scaiPermission.getProperties().entrySet()) {
                PermissionDescription.Property property = permission.addNewProperty();
                property.setKey(scaiProperty.getKey());
                property.setValue(scaiProperty.getValue());
            }
            if (scaiPermission.isInheritable() != null) {
                permission.setInheritable(scaiPermission.isInheritable());
            }
        }

        if (roles != null) {
            for (String role : roles) {
                user.addRole(role);
            }
        }

        applyChanges();
    }

    @Override
    public void addDataStream(Calendar timeStamp, String sensorName,
        String sensorDomainName, Integer olderValues, SCAIDataStreamElement[] dataStreamElements) {
        SensorDataDescription dataStream = buildMeasurementsTree().addNewDataStream();

        dataStream.setTimeStamp(timeStamp);
        dataStream.setSensorName(sensorName);
        dataStream.setSensorDomainName(sensorDomainName);

        if (olderValues != null) {
            dataStream.setOlderValues(olderValues);
        }

        for (SCAIDataStreamElement scaiDataStreamElement : dataStreamElements) {
            DataElementValueDescription dataElement = dataStream.addNewDataStreamElement();
            dataElement.setData(scaiDataStreamElement.getData());
            dataElement.setPath(scaiDataStreamElement.getPath());

            if (scaiDataStreamElement.getQuality() != null) {
                dataElement.setQuality(
                    java.math.BigDecimal.valueOf(scaiDataStreamElement.getQuality()));
            }

            if (scaiDataStreamElement.getErrorMessage() != null) {
                ErrorDescription error = dataElement.addNewError();

                switch (scaiDataStreamElement.getErrortype()) {
                case SENSORDEAD:
                    error.setSensorDead(scaiDataStreamElement.getErrorMessage());

                    break;

                case NAN:
                    error.setNAN(scaiDataStreamElement.getErrorMessage());

                    break;

                case BATTERYDEAD:
                    error.setBatteryDead(scaiDataStreamElement.getErrorMessage());

                    break;

                case UNKNOWN:
                    error.setUnknown(scaiDataStreamElement.getErrorMessage());

                    break;

                default:
                    break;
                }
            }
        }

        applyChanges();
    }

    @Override
    public void addDeployOperatorGroup(SCAIReference operatorGroup, SCAIReference processingUnit,
        String operationID) {
        DeployOperatorGroup deployOperatorGroup =
            buildSensorRegistryControlTree().addNewDeployOperatorGroup();

        if (operationID != null) {
            deployOperatorGroup.setOperationID(operationID);
        }

        deployOperatorGroup.setOperatorGroup(convertReference(operatorGroup));

        if (processingUnit != null) {
            deployOperatorGroup.setProcessingUnit(convertReference(processingUnit));
        }

        applyChanges();
    }

    @Override
    public void addDestroySession(String sessionID, String operationID) {
        DestroySession destroySession = buildAccessControlTree().addNewDestroySession();

        if (operationID != null) {
            destroySession.setOperationID(operationID);
        }

        if (operationID != null) {
            destroySession.setSessionID(sessionID);
        }

        applyChanges();
    }

    @Override
    public void addGetConfiguration(SCAISensorReference reference, String operationID) {
        GetConfiguration getConfiguration = buildSensorControlTree().addNewGetConfiguration();

        if (operationID != null) {
            getConfiguration.setOperationID(operationID);
        }

        getConfiguration.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addGetConfigurationParameter(SCAIReference reference, String operationID) {
        GetConfigurationParameter getConfigurationParameter =
            buildSensorRegistryControlTree().addNewGetConfigurationParameter();
        getConfigurationParameter.setOperationID(operationID);
        getConfigurationParameter.setConfigurationParameter(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetDataElement(SCAIReference reference, String operationID) {
        GetDataElement getDataElement = buildSensorRegistryControlTree().addNewGetDataElement();
        getDataElement.setOperationID(operationID);
        getDataElement.setDataElement(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetDataStreamType(SCAIReference reference, String operationID) {
        GetDataStreamType getDataStreamType =
            buildSensorRegistryControlTree().addNewGetDataStreamType();
        getDataStreamType.setOperationID(operationID);
        getDataStreamType.setDataStreamType(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetDataType(SCAIReference reference, String operationID) {
        GetDataType getDataType = buildSensorRegistryControlTree().addNewGetDataType();
        getDataType.setOperationID(operationID);
        getDataType.setDataType(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetOperator(SCAIReference reference, SCAIReference group, String operationID) {
        GetOperator getOperator = buildSensorRegistryControlTree().addNewGetOperator();
        getOperator.setOperationID(operationID);
        getOperator.setOperator(convertReference(reference));
        getOperator.setOperatorGroup(convertReference(group));

        applyChanges();
    }

    @Override
    public void addGetOperatorGroup(SCAIReference reference, String operationID) {
        GetOperatorGroup getOperatorGroup =
            buildSensorRegistryControlTree().addNewGetOperatorGroup();
        getOperatorGroup.setOperationID(operationID);
        getOperatorGroup.setOperatorGroup(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetOperatorGroupStatus(SCAIReference reference, String operationID) {
        GetOperatorGroupStatus getOperatorGroupStatus =
            buildSensorRegistryControlTree().addNewGetOperatorGroupStatus();
        getOperatorGroupStatus.setOperationID(operationID);
        getOperatorGroupStatus.setOperatorGroup(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetOperatorType(SCAIReference reference, String operationID) {
        GetOperatorType getOperatorType = buildSensorRegistryControlTree().addNewGetOperatorType();
        getOperatorType.setOperationID(operationID);
        getOperatorType.setOperatorType(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetProcessingUnit(SCAIReference reference, String operationID) {
        GetProcessingUnit getProcessingUnit = buildSensorRegistryControlTree().addNewGetProcessingUnit();
        getProcessingUnit.setOperationID(operationID);
        getProcessingUnit.setOperatorGroup(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetSensor(SCAISensorReference reference, String operationID) {
        GetSensor getSensor = buildSensorRegistryControlTree().addNewGetSensor();
        getSensor.setOperationID(operationID);
        getSensor.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addGetSensorCategory(SCAIReference reference, String operationID) {
        GetSensorCategory getSensorCategory =
            buildSensorRegistryControlTree().addNewGetSensorCategory();
        getSensorCategory.setOperationID(operationID);
        getSensorCategory.setSensorCategory(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetSensorDomain(SCAIReference reference, String operationID) {
        GetSensorDomain getSensorDomain = buildSensorRegistryControlTree().addNewGetSensorDomain();
        getSensorDomain.setOperationID(operationID);
        getSensorDomain.setSensorDomain(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetSensorType(SCAIReference reference, String operationID) {
        GetSensorType getSensorType = buildSensorRegistryControlTree().addNewGetSensorType();
        getSensorType.setOperationID(operationID);
        getSensorType.setSensorType(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addGetSupportedModules(SCAISensorReference reference, String operationID) {
        GetSupportedModules getSupportedModules =
            buildSensorControlTree().addNewGetSupportedModules();

        if (operationID != null) {
            getSupportedModules.setOperationID(operationID);
        }

        getSupportedModules.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addGetUserData(SCAIReference reference, String operationID) {
        GetUserData getUserData = buildAccessControlTree().addNewGetUserData();

        getUserData.setOperationID(operationID);

        getUserData.setUser(convertReference(reference));
    }

    @Override
    public void addGetValue(SCAISensorReference reference, String operationID) {
        GetValue getValue = buildSensorControlTree().addNewGetValue();

        if (operationID != null) {
            getValue.setOperationID(operationID);
        }

        getValue.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addGrantSensorAccess(SCAISensorReference sensorReference, SCAIReference[] users,
            SCAIReference[] domains, String operationID) {
        GrantSensorAccess grantSensorAccess = buildAccessControlTree().addNewGrantSensorAccess();

        if (operationID != null) {
            grantSensorAccess.setOperationID(operationID);
        }
        grantSensorAccess.setSensor(convertSensorReference(sensorReference));
        if (users != null && users.length > 0) {
            for (SCAIReference user : users) {
                grantSensorAccess.addNewAccessor().setUser(convertReference(user));
            }
        }
        if (domains != null && domains.length > 0) {
            for (SCAIReference domain : domains) {
                grantSensorAccess.addNewAccessor().setSensorDomain(convertReference(domain));
            }
        }
        
        applyChanges();
    }

    @Override
    public void addIdentification(String securityToken, String username, byte[] password) {
        Identification id = buildIdentificationTree();

        if ((securityToken != null) && !securityToken.isEmpty()) {
            id.setSecurityToken(securityToken);
        } else {
            LoginIdentification loginid = id.addNewLoginIdentification();
            loginid.setUsername(username);
            loginid.setPassword(Base64.decode(password));
        }

        document = generateSCAIDocument(docTemp);
//	applyChanges();
    }

    @Override
    public void addLinkOperatorsInputToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        LinkOperators linkOperators = buildSensorRegistryControlTree().addNewLinkOperators();

        if (operationID != null) {
            linkOperators.setOperationID(operationID);
        }

        linkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = linkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setInputOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setOutputOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addLinkOperatorsInputToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        LinkOperators linkOperators = buildSensorRegistryControlTree().addNewLinkOperators();

        if (operationID != null) {
            linkOperators.setOperationID(operationID);
        }

        linkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = linkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setInputOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setServiceOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addLinkOperatorsServiceToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        LinkOperators linkOperators = buildSensorRegistryControlTree().addNewLinkOperators();

        if (operationID != null) {
            linkOperators.setOperationID(operationID);
        }

        linkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = linkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setServiceOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setOutputOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addLinkOperatorsServiceToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        LinkOperators linkOperators = buildSensorRegistryControlTree().addNewLinkOperators();

        if (operationID != null) {
            linkOperators.setOperationID(operationID);
        }

        linkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = linkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setServiceOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setServiceOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addListAllConfigurationParameters(String operationID) {
        ListAllConfigurationParameters listAllConfigurationParameters =
            buildSensorRegistryControlTree().addNewListAllConfigurationParameters();
        listAllConfigurationParameters.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllDataElements(String operationID) {
        ListAllDataElements listAllDataElements =
            buildSensorRegistryControlTree().addNewListAllDataElements();
        listAllDataElements.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllDataStreamTypes(String operationID) {
        ListAllDataStreamTypes listAllDataStreamTypes =
            buildSensorRegistryControlTree().addNewListAllDataStreamTypes();
        listAllDataStreamTypes.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllDataTypes(String operationID) {
        ListAllDataTypes listAllDataTypes =
            buildSensorRegistryControlTree().addNewListAllDataTypes();
        listAllDataTypes.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllLinks(SCAIReference reference, String operationID) {
        ListAllLinks listAllLinks = buildSensorRegistryControlTree().addNewListAllLinks();
        listAllLinks.setOperationID(operationID);
        listAllLinks.setOperatorGroup(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addListAllOperatorGroups(String operationID) {
        ListAllOperatorGroups listAllOperatorGroups =
            buildSensorRegistryControlTree().addNewListAllOperatorGroups();
        listAllOperatorGroups.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllOperatorTypes(String operationID) {
        ListAllOperatorTypes listAllOperatorTypes =
            buildSensorRegistryControlTree().addNewListAllOperatorTypes();
        listAllOperatorTypes.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllOperators(SCAIReference reference, String operationID) {
        ListAllOperators listAllOperators =
            buildSensorRegistryControlTree().addNewListAllOperators();
        listAllOperators.setOperationID(operationID);
        listAllOperators.setOperatorGroup(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addListAllProcessingUnits(String operationID) {
        ListAllProcessingUnits listAllProcessingUnits =
            buildSensorRegistryControlTree().addNewListAllProcessingUnits();
        listAllProcessingUnits.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllSensorAccessors(SCAISensorReference sensorReference, String operationID) {
        ListAllSensorAccessors listAllSensorAccessors =
            buildAccessControlTree().addNewListAllSensorAccessors();
        listAllSensorAccessors.setOperationID(operationID);
        listAllSensorAccessors.setSensor(convertSensorReference(sensorReference));
        
        applyChanges();
    }

    @Override
    public void addListAllSensorCategories(String operationID) {
        ListAllSensorCategories listAllSensorCategories =
            buildSensorRegistryControlTree().addNewListAllSensorCategories();
        listAllSensorCategories.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllSensorCategoriesBySensorDomain(SCAIReference reference, String operationID) {
        ListAllSensorCategoriesBySensorDomain listAllSensorCategoriesByDomain =
            buildSensorRegistryControlTree().addNewListAllSensorCategoriesBySensorDomain();
        listAllSensorCategoriesByDomain.setOperationID(operationID);
        listAllSensorCategoriesByDomain.setSensorDomain(convertReference(reference));
        applyChanges();
    }

    @Override
    public void addListAllSensorDomains(String operationID) {
        ListAllSensorDomains listAllSensorDomains =
            buildSensorRegistryControlTree().addNewListAllSensorDomains();
        listAllSensorDomains.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllSensorTypes(String operationID) {
        ListAllSensorTypes listAllSensorTypes =
            buildSensorRegistryControlTree().addNewListAllSensorTypes();
        listAllSensorTypes.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addListAllSensorTypesBySensorCategory(SCAIReference reference, String operationID) {
        ListAllSensorTypesBySensorCategory listAllSensorTypesBySensorCategory =
            buildSensorRegistryControlTree().addNewListAllSensorTypesBySensorCategory();
        listAllSensorTypesBySensorCategory.setOperationID(operationID);
        listAllSensorTypesBySensorCategory.setSensorCategory(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addListAllSensorTypesBySensorDomain(SCAIReference reference, String operationID) {
        ListAllSensorTypesBySensorDomain listAllSensorTypesBySensorDomain =
            buildSensorRegistryControlTree().addNewListAllSensorTypesBySensorDomain();
        listAllSensorTypesBySensorDomain.setOperationID(operationID);
        listAllSensorTypesBySensorDomain.setSensorDomain(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addListAllSensors(String operationID) {
        ListAllSensors listAllSensors = buildSensorRegistryControlTree().addNewListAllSensors();
        listAllSensors.setOperationID(operationID);

        applyChanges();
    }

    @Override
    public void addRegisterProcessingUnit(String name, String address, Double version,
        String operationID) {
        RegisterProcessingUnit registerProcessingUnit = buildSensorRegistryControlTree().addNewRegisterProcessingUnit();

        if (operationID != null) {
            registerProcessingUnit.setOperationID(operationID);
        }

        ProcessingUnitDescription processingUnit = registerProcessingUnit.addNewProcessingUnit();
        processingUnit.setName(name);
        processingUnit.setAddress(address);
        processingUnit.setVersion(version);
        applyChanges();
    }

    @Override
    public void addRemoveChildCategoryFromSensorCategory(SCAIReference category,
        SCAIReference parentCategory, String operationID) {
        RemoveChildCategoryFromSensorCategory removeChildCategoryFromSensorCategory =
            buildSensorRegistryControlTree().addNewRemoveChildCategoryFromSensorCategory();

        if (operationID != null) {
            removeChildCategoryFromSensorCategory.setOperationID(operationID);
        }

        removeChildCategoryFromSensorCategory.setSensorCategory(convertReference(category));
        removeChildCategoryFromSensorCategory.setParentSensorCategory(
            convertReference(parentCategory));
        applyChanges();
    }

    @Override
    public void addRemoveConfigurationParameter(SCAIReference reference, String operationID) {
        RemoveConfigurationParameter removeConfigurationParameter =
            buildSensorRegistryControlTree().addNewRemoveConfigurationParameter();

        if (operationID != null) {
            removeConfigurationParameter.setOperationID(operationID);
        }

        Reference dataStreamType = removeConfigurationParameter.addNewConfigurationParameter();

        if (reference.getId() != null) {
            dataStreamType.setId(reference.getId());
        } else {
            dataStreamType.setName(reference.getName());
        }

        applyChanges();
    }

    @Override
    public void addRemoveDataElement(SCAIReference reference, String operationID) {
        RemoveDataElement removeDataElement =
            buildSensorRegistryControlTree().addNewRemoveDataElement();

        if (operationID != null) {
            removeDataElement.setOperationID(operationID);
        }

        Reference dataElement = removeDataElement.addNewDataElement();

        if (reference.getId() != null) {
            dataElement.setId(reference.getId());
        } else {
            dataElement.setName(reference.getName());
        }

        applyChanges();
    }

    @Override
    public void addRemoveDataElementFromDataStreamType(SCAIReference element,
        SCAIReference streamType, String operationID) {
        RemoveDataElementFromDataStreamType removeDataElementFromDataStreamType =
            buildSensorRegistryControlTree().addNewRemoveDataElementFromDataStreamType();

        if (operationID != null) {
            removeDataElementFromDataStreamType.setOperationID(operationID);
        }

        removeDataElementFromDataStreamType.setDataElement(convertReference(element));
        removeDataElementFromDataStreamType.setDataStreamType(convertReference(streamType));
        applyChanges();
    }

    @Override
    public void addRemoveDataStreamType(SCAIReference reference, String operationID) {
        RemoveDataStreamType removeDataStreamType =
            buildSensorRegistryControlTree().addNewRemoveDataStreamType();

        if (operationID != null) {
            removeDataStreamType.setOperationID(operationID);
        }

        Reference dataStreamType = removeDataStreamType.addNewDataStreamType();

        if (reference.getId() != null) {
            dataStreamType.setId(reference.getId());
        } else {
            dataStreamType.setName(reference.getName());
        }

        applyChanges();
    }

    @Override
    public void addRemoveDataType(SCAIReference reference, String operationID) {
        RemoveDataType removeDataType = buildSensorRegistryControlTree().addNewRemoveDataType();

        if (operationID != null) {
            removeDataType.setOperationID(operationID);
        }

        Reference dataType = removeDataType.addNewDataType();

        if (reference.getId() != null) {
            dataType.setId(reference.getId());
        } else {
            dataType.setName(reference.getName());
        }

        applyChanges();
    }

    @Override
    public void addRemoveOperator(SCAIReference reference, SCAIReference group, String operationID) {
        RemoveOperator removeOperator = buildSensorRegistryControlTree().addNewRemoveOperator();

        if (operationID != null) {
            removeOperator.setOperationID(operationID);
        }

        removeOperator.setOperator(convertReference(reference));
        removeOperator.setOperatorGroup(convertReference(group));

        applyChanges();
    }

    @Override
    public void addRemoveOperatorGroup(SCAIReference reference, String operationID) {
        RemoveOperatorGroup removeOperatorGroup =
            buildSensorRegistryControlTree().addNewRemoveOperatorGroup();

        if (operationID != null) {
            removeOperatorGroup.setOperationID(operationID);
        }

        removeOperatorGroup.setOperatorGroup(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addRemoveOperatorType(SCAIReference reference, String operationID) {
        RemoveOperatorType removeOperatorType =
            buildSensorRegistryControlTree().addNewRemoveOperatorType();

        if (operationID != null) {
            removeOperatorType.setOperationID(operationID);
        }

        removeOperatorType.setOperatorType(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addRemoveSensor(SCAISensorReference reference, String operationID) {
        RemoveSensor removeSensor = buildSensorRegistryControlTree().addNewRemoveSensor();

        if (operationID != null) {
            removeSensor.setOperationID(operationID);
        }

        removeSensor.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addRemoveSensorCategory(SCAIReference reference, String operationID) {
        RemoveSensorCategory removeSensorCategory =
            buildSensorRegistryControlTree().addNewRemoveSensorCategory();

        if (operationID != null) {
            removeSensorCategory.setOperationID(operationID);
        }

        removeSensorCategory.setSensorCategory(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addRemoveSensorCategoryFromSensorDomain(SCAIReference category,
        SCAIReference domain, String operationID) {
        RemoveSensorCategoryFromSensorDomain removeSensorCategoryFromSensorDomain =
            buildSensorRegistryControlTree().addNewRemoveSensorCategoryFromSensorDomain();

        if (operationID != null) {
            removeSensorCategoryFromSensorDomain.setOperationID(operationID);
        }

        removeSensorCategoryFromSensorDomain.setSensorCategory(convertReference(category));
        removeSensorCategoryFromSensorDomain.setSensorDomain(convertReference(domain));
        applyChanges();
    }

    @Override
    public void addRemoveSensorDomain(SCAIReference reference, String operationID) {
        RemoveSensorDomain removeSensorDomain =
            buildSensorRegistryControlTree().addNewRemoveSensorDomain();

        if (operationID != null) {
            removeSensorDomain.setOperationID(operationID);
        }

        removeSensorDomain.setSensorDomain(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addRemoveSensorType(SCAIReference reference, String operationID) {
        RemoveSensorType removeSensorType =
            buildSensorRegistryControlTree().addNewRemoveSensorType();

        if (operationID != null) {
            removeSensorType.setOperationID(operationID);
        }

        removeSensorType.setSensorType(convertReference(reference));

        applyChanges();
    }

    @Override
    public void addRemoveSensorTypeFromSensorCategory(SCAIReference type, SCAIReference category,
        String operationID) {
        RemoveSensorTypeFromSensorCategory removeSensorTypeFromSensorCategory =
            buildSensorRegistryControlTree().addNewRemoveSensorTypeFromSensorCategory();

        if (operationID != null) {
            removeSensorTypeFromSensorCategory.setOperationID(operationID);
        }

        removeSensorTypeFromSensorCategory.setSensorType(convertReference(type));
        removeSensorTypeFromSensorCategory.setSensorCategory(convertReference(category));
        applyChanges();
    }

    @Override
    public void addRemoveSensorTypeFromSensorDomain(SCAIReference type, SCAIReference domain,
        String operationID) {
        RemoveSensorTypeFromSensorDomain removeSensorTypeFromSensorDomain =
            buildSensorRegistryControlTree().addNewRemoveSensorTypeFromSensorDomain();

        if (operationID != null) {
            removeSensorTypeFromSensorDomain.setOperationID(operationID);
        }

        removeSensorTypeFromSensorDomain.setSensorType(convertReference(type));
        removeSensorTypeFromSensorDomain.setSensorDomain(convertReference(domain));
        applyChanges();
    }

    @Override
    public void addRemoveUser(SCAIReference reference, String operationID) {
        RemoveUser removeUser = buildAccessControlTree().addNewRemoveUser();

        if (operationID != null) {
            removeUser.setOperationID(operationID);
        }

        removeUser.setUser(convertReference(reference));
    }

    @Override
    public void addStartSensor(SCAISensorReference reference, String operationID) {
        StartSensor startSensor = buildSensorControlTree().addNewStartSensor();

        if (operationID != null) {
            startSensor.setOperationID(operationID);
        }

        startSensor.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addStopSensor(SCAISensorReference reference, String operationID) {
        StopSensor stopSensor = buildSensorControlTree().addNewStopSensor();

        if (operationID != null) {
            stopSensor.setOperationID(operationID);
        }

        stopSensor.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addSubscribeDatastream(SCAISensorReference reference, String operationID) {
        SubscribeDatastream subscribeDatastream =
            buildSensorControlTree().addNewSubscribeDatastream();

        if (operationID != null) {
            subscribeDatastream.setOperationID(operationID);
        }

        subscribeDatastream.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addUndeployOperatorGroup(SCAIReference operatorGroup, String operationID) {
        UndeployOperatorGroup undeployOperatorGroup =
            buildSensorRegistryControlTree().addNewUndeployOperatorGroup();

        if (operationID != null) {
            undeployOperatorGroup.setOperationID(operationID);
        }

        undeployOperatorGroup.setOperatorGroup(convertReference(operatorGroup));
        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsInputToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        UnlinkOperators unlinkOperators = buildSensorRegistryControlTree().addNewUnlinkOperators();

        if (operationID != null) {
            unlinkOperators.setOperationID(operationID);
        }

        unlinkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = unlinkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setInputOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setOutputOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsInputToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        UnlinkOperators unlinkOperators = buildSensorRegistryControlTree().addNewUnlinkOperators();

        if (operationID != null) {
            unlinkOperators.setOperationID(operationID);
        }

        unlinkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = unlinkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setInputOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setServiceOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsServiceToOutput(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        UnlinkOperators unlinkOperators = buildSensorRegistryControlTree().addNewUnlinkOperators();

        if (operationID != null) {
            unlinkOperators.setOperationID(operationID);
        }

        unlinkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = unlinkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setServiceOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setOutputOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addUnlinkOperatorsServiceToService(SCAIReference group, SCAIReference source,
        SCAIReference destination, String operationID) {
        UnlinkOperators unlinkOperators = buildSensorRegistryControlTree().addNewUnlinkOperators();

        if (operationID != null) {
            unlinkOperators.setOperationID(operationID);
        }

        unlinkOperators.setOperatorGroup(convertReference(group));

        OperatorLinkDescription operatorLink = unlinkOperators.addNewOperatorLink();
        Source newSource = operatorLink.addNewSource();
        newSource.setServiceOperator(convertReference(source));

        Destination newDestination = operatorLink.addNewDestination();
        newDestination.setServiceOperator(convertReference(destination));

        applyChanges();
    }

    @Override
    public void addUnregisterProcessingUnit(SCAIReference processingUnit, String operationID) {
        UnregisterProcessingUnit unregisterProcessingUnit =
            buildSensorRegistryControlTree().addNewUnregisterProcessingUnit();

        if (operationID != null) {
            unregisterProcessingUnit.setOperationID(operationID);
        }

        unregisterProcessingUnit.setProcessingUnit(convertReference(processingUnit));
        applyChanges();
    }

    @Override
    public void addUnsubscribeDatastream(SCAISensorReference reference, String operationID) {
        UnsubscribeDatastream unsubscribeDatastream =
            buildSensorControlTree().addNewUnsubscribeDatastream();

        if (operationID != null) {
            unsubscribeDatastream.setOperationID(operationID);
        }

        unsubscribeDatastream.setSensor(convertSensorReference(reference));

        applyChanges();
    }

    @Override
    public void addUpdateConfigurationParameterAtomicParameter(SCAIReference reference,
        String name, Boolean optional, String uom, Boolean identifier, SCAIReference dataType,
        String operationID) {
        UpdateConfigurationParameter updateConfigurationParameter =
            buildSensorRegistryControlTree().addNewUpdateConfigurationParameter();

        if (operationID != null) {
            updateConfigurationParameter.setOperationID(operationID);
        }

        Reference oldConfigurationParameter =
            updateConfigurationParameter.addNewConfigurationParameter();

        if (reference.getId() != null) {
            oldConfigurationParameter.setId(reference.getId());
        } else {
            oldConfigurationParameter.setName(reference.getName());
        }

        ConfigurationParameterDescription configurationParameterDescription =
            updateConfigurationParameter.addNewNewConfigurationParameter();
        configurationParameterDescription.setName(name);
        configurationParameterDescription.setOptional(optional);

        AtomicParameter atomicParameter = configurationParameterDescription.addNewAtomicParameter();
        atomicParameter.setUom(uom);
        atomicParameter.setIdentifier(identifier);

        Reference newDataType = atomicParameter.addNewDataType();

        if (dataType.getId() != null) {
            newDataType.setId(dataType.getId());
        } else {
            newDataType.setName(dataType.getName());
        }

        applyChanges();
    }

    @Override
    public void addUpdateConfigurationParameterComplexParameter(SCAIReference reference,
        String name, Boolean optional, SCAIReference[] configurationParameters, String operationID) {
        UpdateConfigurationParameter updateConfigurationParameter =
            buildSensorRegistryControlTree().addNewUpdateConfigurationParameter();

        if (operationID != null) {
            updateConfigurationParameter.setOperationID(operationID);
        }

        Reference oldConfigurationParameter =
            updateConfigurationParameter.addNewConfigurationParameter();

        if (reference.getId() != null) {
            oldConfigurationParameter.setId(reference.getId());
        } else {
            oldConfigurationParameter.setName(reference.getName());
        }

        ConfigurationParameterDescription configurationParameterDescription =
            updateConfigurationParameter.addNewNewConfigurationParameter();
        configurationParameterDescription.setName(name);
        configurationParameterDescription.setOptional(optional);

        ComplexParameter complexParameter =
            configurationParameterDescription.addNewComplexParameter();

        for (SCAIReference configurationParameter : configurationParameters) {
            Reference newConfigurationParameter = complexParameter.addNewConfigurationParameter();

            if (configurationParameter.getId() != null) {
                newConfigurationParameter.setId(configurationParameter.getId());
            } else {
                newConfigurationParameter.setName(configurationParameter.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addUpdateConfigurationParameterSequenceParameter(SCAIReference reference,
        String name, Boolean optional, Integer min, Integer max,
        SCAIReference configurationParameter, String operationID) {
        UpdateConfigurationParameter updateConfigurationParameter =
            buildSensorRegistryControlTree().addNewUpdateConfigurationParameter();

        if (operationID != null) {
            updateConfigurationParameter.setOperationID(operationID);
        }

        Reference oldConfigurationParameter =
            updateConfigurationParameter.addNewConfigurationParameter();

        if (reference.getId() != null) {
            oldConfigurationParameter.setId(reference.getId());
        } else {
            oldConfigurationParameter.setName(reference.getName());
        }

        ConfigurationParameterDescription configurationParameterDescription =
            updateConfigurationParameter.addNewNewConfigurationParameter();
        configurationParameterDescription.setName(name);
        configurationParameterDescription.setOptional(optional);

        SequenceParameter sequenceParameter =
            configurationParameterDescription.addNewSequenceParameter();
        sequenceParameter.setMinlength(min);
        sequenceParameter.setMaxlength(max);

        Reference newConfigurationParameter = sequenceParameter.addNewConfigurationParameter();

        if (configurationParameter.getId() != null) {
            newConfigurationParameter.setId(configurationParameter.getId());
        } else {
            newConfigurationParameter.setName(configurationParameter.getName());
        }

        applyChanges();
    }

    @Override
    public void addUpdateDataElementAtomicElement(SCAIReference reference, String name,
        SCAIReference dataType, String operationID) {
        UpdateDataElement updateDataElement =
            buildSensorRegistryControlTree().addNewUpdateDataElement();

        if (operationID != null) {
            updateDataElement.setOperationID(operationID);
        }

        Reference oldDataElement = updateDataElement.addNewDataElement();

        if (reference.getId() != null) {
            oldDataElement.setId(reference.getId());
        } else {
            oldDataElement.setName(reference.getName());
        }

        DataElementDescription dataElementDescription = updateDataElement.addNewNewDataElement();

        AtomicElement atomicElement = dataElementDescription.addNewAtomicElement();
        atomicElement.setName(name);

        Reference newDataType = atomicElement.addNewDataType();

        if (dataType.getId() != null) {
            newDataType.setId(dataType.getId());
        } else {
            newDataType.setName(dataType.getName());
        }

        applyChanges();
    }

    @Override
    public void addUpdateDataElementComplexElement(SCAIReference reference, String name,
        SCAIReference[] dataElements, String operationID) {
        UpdateDataElement updateDataElement =
            buildSensorRegistryControlTree().addNewUpdateDataElement();

        if (operationID != null) {
            updateDataElement.setOperationID(operationID);
        }

        Reference oldDataElement = updateDataElement.addNewDataElement();

        if (reference.getId() != null) {
            oldDataElement.setId(reference.getId());
        } else {
            oldDataElement.setName(reference.getName());
        }

        DataElementDescription dataElementDescription = updateDataElement.addNewNewDataElement();

        ComplexElement complexElement = dataElementDescription.addNewComplexElement();
        complexElement.setName(name);

        for (SCAIReference dataElement : dataElements) {
            Reference newDataElement = complexElement.addNewDataElement();

            if (dataElement.getId() != null) {
                newDataElement.setId(dataElement.getId());
            } else {
                newDataElement.setName(dataElement.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addUpdateDataStreamType(SCAIReference reference, String name,
        SCAIReference[] dataElements, String operationID) {
        UpdateDataStreamType updateDataStreamType =
            buildSensorRegistryControlTree().addNewUpdateDataStreamType();

        if (operationID != null) {
            updateDataStreamType.setOperationID(operationID);
        }

        Reference oldDataStreamType = updateDataStreamType.addNewDataStreamType();

        if (reference.getId() != null) {
            oldDataStreamType.setId(reference.getId());
        } else {
            oldDataStreamType.setName(reference.getName());
        }

        DataStreamTypeDescription dataElementDescription =
            updateDataStreamType.addNewNewDataStreamType();
        dataElementDescription.setName(name);

        for (SCAIReference dataElement : dataElements) {
            Reference newDataElement = dataElementDescription.addNewDataElement();

            if (dataElement.getId() != null) {
                newDataElement.setId(dataElement.getId());
            } else {
                newDataElement.setName(dataElement.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addUpdateDataTypeBinaryType(SCAIReference reference, String name, Long min, Long max,
        byte[] defaultvalue, String operationID) {
        UpdateDataType updateDataType = buildSensorRegistryControlTree().addNewUpdateDataType();

        if (operationID != null) {
            updateDataType.setOperationID(operationID);
        }

        Reference oldDataType = updateDataType.addNewDataType();

        if (reference.getId() != null) {
            oldDataType.setId(reference.getId());
        } else {
            oldDataType.setName(reference.getName());
        }

        DataTypeDescription dataTypeDescription = updateDataType.addNewNewDataType();
        dataTypeDescription.setName(name);

        BinaryType binaryType = dataTypeDescription.addNewBinaryType();
        if (min != null) {
            binaryType.setMin(min);
        }
        if (max != null) {
            binaryType.setMax(max);
        }
        binaryType.setDefaultvalue(Base64.decode(defaultvalue));


        applyChanges();
    }

    @Override
    public void addUpdateDataTypeDecimalType(SCAIReference reference, String name, Float min,
        Float max, Float scale, Float defaultvalue, String operationID) {
        UpdateDataType updateDataType = buildSensorRegistryControlTree().addNewUpdateDataType();

        if (operationID != null) {
            updateDataType.setOperationID(operationID);
        }

        Reference oldDataType = updateDataType.addNewDataType();

        if (reference.getId() != null) {
            oldDataType.setId(reference.getId());
        } else {
            oldDataType.setName(reference.getName());
        }

        DataTypeDescription dataTypeDescription = updateDataType.addNewNewDataType();
        dataTypeDescription.setName(name);

        DecimalType decimalType = dataTypeDescription.addNewDecimalType();

        if (min != null) {
            decimalType.setMin(min);
        }

        if (max != null) {
            decimalType.setMax(max);
        }

        if (scale != null) {
            decimalType.setScale(scale);
        }

        decimalType.setDefaultvalue(defaultvalue);

        applyChanges();
    }

    @Override
    public void addUpdateDataTypeStringType(SCAIReference reference, String name, Long min,
        Long max, String regex, String defaultvalue, String operationID) {
        UpdateDataType updateDataType = buildSensorRegistryControlTree().addNewUpdateDataType();

        if (operationID != null) {
            updateDataType.setOperationID(operationID);
        }

        Reference oldDataType = updateDataType.addNewDataType();

        if (reference.getId() != null) {
            oldDataType.setId(reference.getId());
        } else {
            oldDataType.setName(reference.getName());
        }

        DataTypeDescription dataTypeDescription = updateDataType.addNewNewDataType();
        dataTypeDescription.setName(name);

        StringType stringType = dataTypeDescription.addNewStringType();

        if (min != null) {
            stringType.setMin(min);
        }

        if (max != null) {
            stringType.setMax(max);
        }

        if (regex != null) {
            stringType.setRegex(regex);
        }

        stringType.setDefaultvalue(defaultvalue);

        applyChanges();
    }

      @Override
    public void addUpdateDataTypeListType(SCAIReference reference, String name, Long min,
              Long max, String[] allowedValues, String[] defaultValues, String operationID) {
        UpdateDataType updateDataType = buildSensorRegistryControlTree().addNewUpdateDataType();

        if (operationID != null) {
            updateDataType.setOperationID(operationID);
        }
        
        updateDataType.setDataType(convertReference(reference));

        DataTypeDescription dataTypeDescription = updateDataType.addNewNewDataType();
        dataTypeDescription.setName(name);

        ListType listType = dataTypeDescription.addNewListType();

        if (min != null) {
            listType.setMin(min);
        }

        if (max != null) {
            listType.setMax(max);
        }

        for (String allowedValue : allowedValues) {
            listType.addAllowedvalue(allowedValue);
        }

        for (String defaultValue : defaultValues) {
            listType.addDefaultvalue(defaultValue);
        }

        applyChanges();
    }

    @Override
    public void addUpdateDataTypeEnumType(SCAIReference reference, String name,
            Map<Long, String> allowedValues, String defaultvalue, String operationID) {
        UpdateDataType updateDataType = buildSensorRegistryControlTree().addNewUpdateDataType();

        if (operationID != null) {
            updateDataType.setOperationID(operationID);
        }

        updateDataType.setDataType(convertReference(reference));

        DataTypeDescription dataTypeDescription = updateDataType.addNewNewDataType();
        dataTypeDescription.setName(name);

        EnumType enumType = dataTypeDescription.addNewEnumType();

        for (Entry<Long, String> allowedValue : allowedValues.entrySet()) {
            EnumType.Allowedvalue newAllowedValue = enumType.addNewAllowedvalue();
            newAllowedValue.setOrdinal(allowedValue.getKey());
            newAllowedValue.setName(allowedValue.getValue());
        }

        enumType.setDefaultvalue(defaultvalue);

        applyChanges();
    }

    @Override
    public void addUpdateOperatorGroup(SCAIReference reference, String name, String operationID) {
        UpdateOperatorGroup updateOperatorGroup =
            buildSensorRegistryControlTree().addNewUpdateOperatorGroup();

        if (operationID != null) {
            updateOperatorGroup.setOperationID(operationID);
        }

        updateOperatorGroup.setOperatorGroup(convertReference(reference));

        OperatorGroupDescription operatorGroup = updateOperatorGroup.addNewNewOperatorGroup();
        operatorGroup.setName(name);

        applyChanges();
    }

    @Override
    public void addUpdateOperatorInputOperator(SCAIReference reference, SCAIReference group,
        String name, Map<String, String> properties, SCAISensorReference Sensor,
        String operationID) {
        UpdateOperator updateOperator = buildSensorRegistryControlTree().addNewUpdateOperator();

        if (operationID != null) {
            updateOperator.setOperationID(operationID);
        }

        updateOperator.setOperatrorGroup(convertReference(group));

        Operator newOperator = updateOperator.addNewOperator();
        newOperator.setOldOperator(convertReference(reference));

        OperatorDescription operator = newOperator.addNewNewOperator();

        operator.setName(name);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                Property property = operator.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        InputOperator inputOperator = operator.addNewInputOperator();
        inputOperator.setSensor(convertSensorReference(Sensor));

        applyChanges();
    }

    @Override
    public void addUpdateOperatorOutputOperator(SCAIReference reference, SCAIReference group,
        String name, Map<String, String> properties, SCAISensorReference sensor,
        String operationID) {
        UpdateOperator updateOperator = buildSensorRegistryControlTree().addNewUpdateOperator();

        if (operationID != null) {
            updateOperator.setOperationID(operationID);
        }

        updateOperator.setOperatrorGroup(convertReference(group));

        Operator newOperator = updateOperator.addNewOperator();
        newOperator.setOldOperator(convertReference(reference));

        OperatorDescription operator = newOperator.addNewNewOperator();

        operator.setName(name);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                Property property = operator.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        OutputOperator outputOperator = operator.addNewOutputOperator();
        outputOperator.setSensor(convertSensorReference(sensor));

        applyChanges();
    }

    @Override
    public void addUpdateOperatorServiceOperator(SCAIReference reference, SCAIReference group,
        String name, Map<String, String> properties, SCAIReference operatorType,
        String operationID) {
        UpdateOperator updateOperator = buildSensorRegistryControlTree().addNewUpdateOperator();

        if (operationID != null) {
            updateOperator.setOperationID(operationID);
        }

        updateOperator.setOperatrorGroup(convertReference(group));

        Operator newOperator = updateOperator.addNewOperator();
        newOperator.setOldOperator(convertReference(reference));

        OperatorDescription operator = newOperator.addNewNewOperator();

        operator.setName(name);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                Property property = operator.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        ServiceOperator serviceOperator = operator.addNewServiceOperator();
        serviceOperator.setOperatorType(convertReference(operatorType));

        applyChanges();
    }

    @Override
    public void addUpdateOperatorType(SCAIReference reference, String name, String metaType,
        Map<String, String> properties, Map<String, Boolean> readOnly, String description, String operationID) {
        UpdateOperatorType updateOperatorType =
            buildSensorRegistryControlTree().addNewUpdateOperatorType();

        if (operationID != null) {
            updateOperatorType.setOperationID(operationID);
        }

        updateOperatorType.setOperatorType(convertReference(reference));

        OperatorTypeDescription operatorType = updateOperatorType.addNewNewOperatorType();
        operatorType.setName(name);
        operatorType.setMetaType(metaType);

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                OperatorTypeDescription.Property property = operatorType.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
                property.setReadOnly(readOnly.get(entry.getKey()));
            }
        }

        if (description != null) {
            operatorType.setDescription(description);
        }

        applyChanges();
    }

    @Override
    public void addUpdateSensor(SCAISensorReference reference, String name, SCAIReference sensorDomain,
        SCAIReference sensorType, boolean virtual, String operationID) {
        UpdateSensor updateSensor = buildSensorRegistryControlTree().addNewUpdateSensor();

        if (operationID != null) {
            updateSensor.setOperationID(operationID);
        }

        updateSensor.setSensor(convertSensorReference(reference));

        SensorDescription sensor = updateSensor.addNewNewSensor();
        sensor.setName(name);
        sensor.setSensorType(convertReference(sensorType));
        sensor.setSensorDomain(convertReference(sensorDomain));
        sensor.setVirtual(virtual);

        applyChanges();
    }

    @Override
    public void addUpdateSensorCategory(SCAIReference reference, String name,
        SCAIReference parentSensorCategory, SCAIReference[] sensorDomains, String operationID) {
        UpdateSensorCategory updateSensorCategory =
            buildSensorRegistryControlTree().addNewUpdateSensorCategory();

        if (operationID != null) {
            updateSensorCategory.setOperationID(operationID);
        }

        updateSensorCategory.setSensorCategory(convertReference(reference));

        SensorCategoryDescription sensorCategory = updateSensorCategory.addNewNewSensorCategory();
        sensorCategory.setName(name);

        if ((parentSensorCategory.getId() != null) || (parentSensorCategory.getName() != null)) {
            Reference newParentSensorCategory = sensorCategory.addNewParentSensorCategory();

            if (parentSensorCategory.getId() != null) {
                newParentSensorCategory.setId(parentSensorCategory.getId());
            } else {
                newParentSensorCategory.setName(parentSensorCategory.getName());
            }
        }

        for (SCAIReference sensorDomain : sensorDomains) {
            Reference newSensorType = sensorCategory.addNewSensorDomain();

            if (sensorDomain.getId() != null) {
                newSensorType.setId(sensorDomain.getId());
            } else {
                newSensorType.setName(sensorDomain.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addUpdateSensorDomain(SCAIReference reference, String name, String operationID) {
        UpdateSensorDomain updateSensorDomain =
            buildSensorRegistryControlTree().addNewUpdateSensorDomain();

        if (operationID != null) {
            updateSensorDomain.setOperationID(operationID);
        }

        updateSensorDomain.setSensorDomain(convertReference(reference));

        SensorDomainDescription sensorDomain = updateSensorDomain.addNewNewSensorDomain();
        sensorDomain.setName(name);

        applyChanges();
    }

    @Override
    public void addUpdateSensorType(SCAIReference reference, String name, String adapter,
        SCAIReference dataStreamType, SCAIReference[] configurationParameters,
        SCAIReference[] sensorCategories, SCAIReference[] sensorDomains, String operationID) {
        UpdateSensorType updateSensorType =
            buildSensorRegistryControlTree().addNewUpdateSensorType();

        if (operationID != null) {
            updateSensorType.setOperationID(operationID);
        }

        updateSensorType.setSensorType(convertReference(reference));

        SensorTypeDescription sensorType = updateSensorType.addNewNewSensorType();
        sensorType.setName(name);
        sensorType.setAdapter(adapter);

        sensorType.setDataStreamType(convertReference(dataStreamType));

        for (SCAIReference configurationParameter : configurationParameters) {
            Reference newConfigurationParameter = sensorType.addNewConfigurationParameter();

            if (configurationParameter.getId() != null) {
                newConfigurationParameter.setId(configurationParameter.getId());
            } else {
                newConfigurationParameter.setName(configurationParameter.getName());
            }
        }

        for (SCAIReference sensorCategory : sensorCategories) {
            Reference newSensorCategory = sensorType.addNewSensorCategory();

            if (sensorCategory.getId() != null) {
                newSensorCategory.setId(sensorCategory.getId());
            } else {
                newSensorCategory.setName(sensorCategory.getName());
            }
        }

        for (SCAIReference sensorDomain : sensorDomains) {
            Reference newSensorDomain = sensorType.addNewSensorDomain();

            if (sensorDomain.getId() != null) {
                newSensorDomain.setId(sensorDomain.getId());
            } else {
                newSensorDomain.setName(sensorDomain.getName());
            }
        }

        applyChanges();
    }

    @Override
    public void addUpdateUser(SCAIReference reference, String username, byte[] password,
            SCAIPermission[] permissions, String[] roles, String operationID) {
        UpdateUser updateUser = buildAccessControlTree().addNewUpdateUser();

        if (operationID != null) {
            updateUser.setOperationID(operationID);
        }

        updateUser.setUser(convertReference(reference));

        UserDescription user = updateUser.addNewNewUser();
        user.setUsername(username);
        user.setPassword(Base64.decode(password));
        
        for (SCAIPermission scaiPermission : permissions) {
            PermissionDescription permission = user.addNewPermission();
            switch (scaiPermission.getPrivilegeAction()) {
                case GRANT:
                    permission.addNewPermission().setGrant(scaiPermission.getPrivilege());
                    break;
                case WITHDRAW:
                    permission.addNewPermission().setWithdraw(scaiPermission.getPrivilege());
                    break;
                default:
                    continue;
            }
            for (Map.Entry<String, String> scaiProperty : scaiPermission.getProperties().entrySet()) {
                PermissionDescription.Property property = permission.addNewProperty();
                property.setKey(scaiProperty.getKey());
                property.setValue(scaiProperty.getValue());
            }
            if (scaiPermission.isInheritable() != null) {
                permission.setInheritable(scaiPermission.isInheritable());
            }
        }

        for (String role : roles) {
            user.addRole(role);
        }

        applyChanges();
    }

    @Override
    public void addWithdrawSensorAccess(SCAISensorReference sensorReference, SCAIReference[] users,
            SCAIReference[] domains, String operationID) {
        WithdrawSensorAccess withdrawSensorAccess = buildAccessControlTree().addNewWithdrawSensorAccess();

        if (operationID != null) {
            withdrawSensorAccess.setOperationID(operationID);
        }
        withdrawSensorAccess.setSensor(convertSensorReference(sensorReference));
        if (users != null && users.length > 0) {
            for (SCAIReference user : users) {
                withdrawSensorAccess.addNewAccessor().setUser(convertReference(user));
            }
        }
        if (domains != null && domains.length > 0) {
            for (SCAIReference domain : domains) {
                withdrawSensorAccess.addNewAccessor().setSensorDomain(convertReference(domain));
            }
        }

        applyChanges();
    }

    /**
     * Check if the generated SCAIDocument is valid
     */
    private void applyChanges() {
        XmlOptions validateOptions = new XmlOptions();
        ArrayList<XmlError> errorList = new ArrayList<XmlError>();
        validateOptions.setErrorListener(errorList);

        if (docTemp.validate(validateOptions)) {
            document = generateSCAIDocument(docTemp);
        } else {
            for (XmlError error : errorList) {
                System.out.println("\n");
                System.out.println("Message: " + error.getMessage() + "\n");
                System.out.println(
                        "Location of invalid XML: " + error.getCursorLocation().xmlText() + "\n");

                //                System.out.println(docTemp.xmlText(new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(4)));
            }

            // revert changes if validation failed
            docTemp = generateSCAIDocument(document);
        }

        //        System.out.println(document.xmlText());
    }

    private AccessControl buildAccessControlTree() {
        if (docTemp == null) {
            docTemp = generateSCAIDocument(document);
        }

        if (docTemp.getSCAI() == null) {
            docTemp.addNewSCAI();
        }

        if (docTemp.getSCAI().getPayload() == null) {
            docTemp.getSCAI().addNewPayload();
        }

        if (docTemp.getSCAI().getPayload().getControlData() == null) {
            docTemp.getSCAI().getPayload().addNewControlData();
        }

        return docTemp.getSCAI().getPayload().getControlData().addNewAccessControl();
    }

    /**
     * Check if the SCAIDocument tree has already been created to the depth necessary for Acknowledgment
     */
    private Acknowledgment buildAcknowledgmentTree() {
        if (docTemp == null) {
            docTemp = generateSCAIDocument(document);
        }

        if (docTemp.getSCAI() == null) {
            docTemp.addNewSCAI();
        }

        if (docTemp.getSCAI().getPayload() == null) {
            docTemp.getSCAI().addNewPayload();
        }

        if (docTemp.getSCAI().getPayload().getAcknowledgment() == null) {
            return docTemp.getSCAI().getPayload().addNewAcknowledgment();
        } else {
            return docTemp.getSCAI().getPayload().getAcknowledgment();
        }
    }

    @Override
    public Object buildConfigurationParameterAtomicParameter(String name, boolean optional,
        String uom, boolean identifier, SCAIReference dataType) {
        ConfigurationParameterDescription ret =
            ConfigurationParameterDescription.Factory.newInstance();
        ret.setName(name);
        ret.setOptional(optional);
        ret.addNewAtomicParameter();
        ret.getAtomicParameter().setUom(uom);
        ret.getAtomicParameter().setIdentifier(identifier);
        ret.getAtomicParameter().setDataType(convertReference(dataType));

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildConfigurationParameterComplexParameter(String name, boolean optional,
        ArrayList<SCAIReference> configurationParameters) {
        ConfigurationParameterDescription ret =
            ConfigurationParameterDescription.Factory.newInstance();
        ret.setName(name);
        ret.setOptional(optional);
        ret.addNewComplexParameter();

        for (int i = 0; i < configurationParameters.size(); i++) {
            ret.getComplexParameter().addNewConfigurationParameter();

            if (configurationParameters.get(i).isReferenceByID()) {
                ret.getComplexParameter().getConfigurationParameterArray(i).setId(
                        configurationParameters.get(i).getId());
            } else {
                ret.getComplexParameter().getConfigurationParameterArray(i).setName(
                        configurationParameters.get(i).getName());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildConfigurationParameterSequenceParameter(String name, boolean optional,
        int minLength, int maxLenght, SCAIReference configurationParameter) {
        ConfigurationParameterDescription ret =
            ConfigurationParameterDescription.Factory.newInstance();
        ret.setName(name);
        ret.setOptional(optional);
        ret.addNewSequenceParameter();
        ret.getSequenceParameter().setMinlength(minLength);
        ret.getSequenceParameter().setMaxlength(maxLenght);
        ret.getSequenceParameter()
           .setConfigurationParameter(convertReference(configurationParameter));

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildDataElementAtomic(String name, SCAIReference dataType) {
        DataElementDescription ret = DataElementDescription.Factory.newInstance();
        ret.addNewAtomicElement();
        ret.getAtomicElement().setName(name);
        ret.getAtomicElement().setDataType(convertReference(dataType));

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildDataElementComplex(String name, ArrayList<SCAIReference> dataElements) {
        DataElementDescription ret = DataElementDescription.Factory.newInstance();
        ret.addNewComplexElement();
        ret.getComplexElement().setName(name);

        for (int i = 0; i < dataElements.size(); i++) {
            ret.getComplexElement().addNewDataElement();

            if (!dataElements.get(i).isReferenceByID()) {
                ret.getComplexElement().getDataElementArray(i).setName(
                    dataElements.get(i).getName());
            } else {
                ret.getComplexElement().getDataElementArray(i).setId(dataElements.get(i).getId());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildDataStreamType(String name, ArrayList<SCAIReference> dataElements) {
        DataStreamTypeDescription ret = DataStreamTypeDescription.Factory.newInstance();
        ret.setName(name);

        for (int i = 0; i < dataElements.size(); i++) {
            ret.addNewDataElement();

            if (!dataElements.get(i).isReferenceByID()) {
                ret.getDataElementArray(i).setName(dataElements.get(i).getName());
            } else {
                ret.getDataElementArray(i).setId(dataElements.get(i).getId());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildDataTypeDescriptionDecimal(String name, boolean minSet, float min,
        boolean maxSet, float max, boolean scaleSet, float scale, float defaultValue) {
        DataTypeDescription ret = DataTypeDescription.Factory.newInstance();
        ret.setName(name);
        ret.addNewDecimalType();

        if (minSet) {
            ret.getDecimalType().setMin(min);
        }

        if (maxSet) {
            ret.getDecimalType().setMax(max);
        }

        if (scaleSet) {
            ret.getDecimalType().setScale(scale);
        }

        ret.getDecimalType().setDefaultvalue(defaultValue);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildDataTypeDescriptionString(String name, boolean minSet, long min,
        boolean maxSet, long max, boolean regExSet, String regEx, String defaultValue) {
        DataTypeDescription ret = DataTypeDescription.Factory.newInstance();
        ret.setName(name);
        ret.addNewStringType();

        if (minSet) {
            ret.getStringType().setMin(min);
        }

        if (maxSet) {
            ret.getStringType().setMax(max);
        }

        if (regExSet) {
            ret.getStringType().setRegex(regEx);
        }

        ret.getStringType().setDefaultvalue(defaultValue);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildDataTypeDescriptionBinary(String name, boolean defaultValueSet, byte[] defaultValue) {
        DataTypeDescription ret = DataTypeDescription.Factory.newInstance();
        ret.setName(name);
        ret.addNewBinaryType();

        if (defaultValueSet) {
            ret.getBinaryType().setDefaultvalue(defaultValue);
        }

//        if (maxSet) {
//            ret.getStringType().setMax(max);
//        }
//
//        if (regExSet) {
//            ret.getStringType().setRegex(regEx);
//        }
//
//        ret.getStringType().setDefaultvalue(defaultValue);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    private Identification buildIdentificationTree() {
        if (docTemp == null) {
            docTemp = generateSCAIDocument(document);
        }

        if (docTemp.getSCAI() == null) {
            docTemp.addNewSCAI();
        }

        if (docTemp.getSCAI().isSetIdentification()) {
            docTemp.getSCAI().unsetIdentification();
        }

        return docTemp.getSCAI().addNewIdentification();
    }

    @Override
    public Object buildInputOperator(String name, SCAISensorReference sensor,
        Map<String, String> properties) {
        OperatorDescription ret = OperatorDescription.Factory.newInstance();
        ret.setName(name);
        ret.addNewInputOperator();
        ret.getInputOperator().setSensor(convertSensorReference(sensor));

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                OperatorDescription.Property property = ret.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildLinkOperatorsInputToOutput(SCAIReference source, SCAIReference destination) {
        OperatorLinkDescription operatorLinkDescription =
            OperatorLinkDescription.Factory.newInstance();
        Source linkSource = operatorLinkDescription.addNewSource();
        Destination linkDestination = operatorLinkDescription.addNewDestination();
        linkSource.setInputOperator(convertReference(source));
        linkDestination.setOutputOperator(convertReference(destination));

        return operatorLinkDescription;
    }

    @Override
    public Object buildLinkOperatorsInputToService(SCAIReference source, SCAIReference destination) {
        OperatorLinkDescription operatorLinkDescription =
            OperatorLinkDescription.Factory.newInstance();
        Source linkSource = operatorLinkDescription.addNewSource();
        Destination linkDestination = operatorLinkDescription.addNewDestination();
        linkSource.setInputOperator(convertReference(source));
        linkDestination.setServiceOperator(convertReference(destination));

        return operatorLinkDescription;
    }

    @Override
    public Object buildLinkOperatorsServiceToOutput(SCAIReference source, SCAIReference destination) {
        OperatorLinkDescription operatorLinkDescription =
            OperatorLinkDescription.Factory.newInstance();
        Source linkSource = operatorLinkDescription.addNewSource();
        Destination linkDestination = operatorLinkDescription.addNewDestination();
        linkSource.setServiceOperator(convertReference(source));
        linkDestination.setOutputOperator(convertReference(destination));

        return operatorLinkDescription;
    }

    @Override
    public Object buildLinkOperatorsServiceToService(SCAIReference source, SCAIReference destination) {
        OperatorLinkDescription operatorLinkDescription =
            OperatorLinkDescription.Factory.newInstance();
        Source linkSource = operatorLinkDescription.addNewSource();
        Destination linkDestination = operatorLinkDescription.addNewDestination();
        linkSource.setServiceOperator(convertReference(source));
        linkDestination.setServiceOperator(convertReference(destination));

        return operatorLinkDescription;
    }

    private Measurements buildMeasurementsTree() {
        if (docTemp == null) {
            docTemp = generateSCAIDocument(document);
        }

        if (docTemp.getSCAI() == null) {
            docTemp.addNewSCAI();
        }

        if (docTemp.getSCAI().getPayload() == null) {
            docTemp.getSCAI().addNewPayload();
        }

        if (docTemp.getSCAI().getPayload().getMeasurements() == null) {
            return docTemp.getSCAI().getPayload().addNewMeasurements();
        } else {
            return docTemp.getSCAI().getPayload().getMeasurements();
        }
    }

    @Override
    public Object buildOperatorGroup(String name) {
        OperatorGroupDescription ret = OperatorGroupDescription.Factory.newInstance();
        ret.setName(name);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildOperatorGroupStatus(Boolean data) {
        OperatorGroupStatus ret = OperatorGroupStatus.Factory.newInstance();
        ret.setDeployed(data);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildOperatorType(String name, String metaType, Map<String, String> properties,
        Map<String, Boolean> readOnly, String description) {
        OperatorTypeDescription ret = OperatorTypeDescription.Factory.newInstance();
        ret.setName(name);
        ret.setMetaType(metaType);

        if (description != null) {
            ret.setDescription(description);
        }

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                OperatorTypeDescription.Property property = ret.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());

                if (readOnly.get(entry.getKey())) {
                    property.setReadOnly(true);
                } else {
                    property.setReadOnly(false);
                }
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildOutputOperator(String name, SCAISensorReference sensor,
        Map<String, String> properties) {
        OperatorDescription ret = OperatorDescription.Factory.newInstance();
        ret.setName(name);

        ret.addNewOutputOperator();
        ret.getOutputOperator().setSensor(convertSensorReference(sensor));

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                OperatorDescription.Property property = ret.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildProcessingUnit(String address) {
        ProcessingUnitDescription ret = ProcessingUnitDescription.Factory.newInstance();
        ret.setAddress(address);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildReplyDataConfigurationValue(String name, Integer index, String value,
        ArrayList<SCAIConfigurationValue> configurationValues) {
        ConfigurationValueDescription content = ConfigurationValueDescription.Factory.newInstance();
        content.setName(name);

        if (index != null) {
            content.setIndex(index);
        }

        if (value != null) {
            content.setValue(value);
        } else if (configurationValues != null) {
            content = convertConfigurationValues(content, configurationValues);
        }

        if (validateXml(content)) {
            return content;
        } else {
            return null;
        }
    }

    @Override
    public Object buildReplyDataSecurityToken(Object data) {
        Identification id = this.buildIdentificationTree();
        id.setSecurityToken((String) data);

        return id;
    }

    @Override
    public Object buildReplyDataStream(Calendar timestamp, String sensorName,
        String domainName, Integer oldValues, SCAIDataStreamElement[] dataStreamElements) {
        SensorDataDescription content = SensorDataDescription.Factory.newInstance();
        content.setTimeStamp(timestamp);
        content.setSensorName(sensorName);
        content.setSensorDomainName(domainName);

        if (oldValues != null) {
            content.setOlderValues(oldValues);
        }

        if (dataStreamElements != null) {
            for (SCAIDataStreamElement scaiDataStreamElement : dataStreamElements) {
                DataElementValueDescription dataStreamElement = content.addNewDataStreamElement();
                dataStreamElement.setData(scaiDataStreamElement.getData());
                dataStreamElement.setPath(scaiDataStreamElement.getPath());

                if (scaiDataStreamElement.getQuality() != null) {
                    dataStreamElement.setQuality(
                        BigDecimal.valueOf(scaiDataStreamElement.getQuality()));
                }

                if (scaiDataStreamElement.getErrortype() != null) {
                    if (scaiDataStreamElement.getErrortype().equals(
                                    SCAIDataStreamElement.Errortype.SENSORDEAD)) {
                        ErrorDescription error = dataStreamElement.addNewError();
                        error.setSensorDead(scaiDataStreamElement.getErrorMessage());
                    } else if (scaiDataStreamElement.getErrortype().equals(
                                    SCAIDataStreamElement.Errortype.BATTERYDEAD)) {
                        ErrorDescription error = dataStreamElement.addNewError();
                        error.setBatteryDead(scaiDataStreamElement.getErrorMessage());
                    } else if (scaiDataStreamElement.getErrortype().equals(
                                    SCAIDataStreamElement.Errortype.NAN)) {
                        ErrorDescription error = dataStreamElement.addNewError();
                        error.setNAN(scaiDataStreamElement.getErrorMessage());
                    } else if (scaiDataStreamElement.getErrortype().equals(
                                    SCAIDataStreamElement.Errortype.UNKNOWN)) {
                        ErrorDescription error = dataStreamElement.addNewError();
                        error.setUnknown(scaiDataStreamElement.getErrorMessage());
                    }
                }
            }
        }

        if (validateXml(content)) {
            return content;
        } else {
            return null;
        }
    }

    @Override
    public Object buildSensor(String name, SCAIReference sensorDomain, SCAIReference sensorType,
        boolean virtual) {
        SensorDescription ret = SensorDescription.Factory.newInstance();
        ret.setName(name);
        ret.setSensorDomain(convertReference(sensorDomain));
        ret.setSensorType(convertReference(sensorType));
        ret.setVirtual(virtual);

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildSensorCategory(String name, SCAIReference parent,
        ArrayList<SCAIReference> sensorDomains) {
        SensorCategoryDescription ret = SensorCategoryDescription.Factory.newInstance();
        ret.setName(name);

        if (parent != null) {
            ret.addNewParentSensorCategory();

            if (!parent.isReferenceByID()) {
                ret.getParentSensorCategory().setName(parent.getName());
            } else {
                ret.getParentSensorCategory().setId(parent.getId());
            }
        }

        /*for (int i = 0; i < sensorTypes.size(); i++) {
        ret.addNewSensorType();
        if (!sensorTypes.get(i).isReferenceByID()) {
        ret.getSensorTypeArray(i).setName(sensorTypes.get(i).getName());
        } else {
        ret.getSensorTypeArray(i).setId(sensorTypes.get(i).getId());
        }
        }*/
        for (int i = 0; i < sensorDomains.size(); i++) {
            ret.addNewSensorDomain();

            if (!sensorDomains.get(i).isReferenceByID()) {
                ret.getSensorDomainArray(i).setName(sensorDomains.get(i).getName());
            } else {
                ret.getSensorDomainArray(i).setId(sensorDomains.get(i).getId());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    private SensorControl buildSensorControlTree() {
        if (docTemp == null) {
            docTemp = generateSCAIDocument(document);
        }

        if (docTemp.getSCAI() == null) {
            docTemp.addNewSCAI();
        }

        if (docTemp.getSCAI().getPayload() == null) {
            docTemp.getSCAI().addNewPayload();
        }

        if (docTemp.getSCAI().getPayload().getControlData() == null) {
            docTemp.getSCAI().getPayload().addNewControlData();
        }

        return docTemp.getSCAI().getPayload().getControlData().addNewSensorControl();
    }

    @Override
    public Object buildSensorDomain(String name) {
        SensorDomainDescription ret = SensorDomainDescription.Factory.newInstance();
        ret.setName(name);

        /*for (int i = 0; i < types.size(); i++) {
        ret.addNewSensorType();
        if (!types.get(i).isReferenceByID()) {
        ret.getSensorTypeArray(i).setName(types.get(i).getName());
        } else {
        ret.getSensorTypeArray(i).setId(types.get(i).getId());
        }
        }

        for (int i = 0; i < categories.size(); i++) {
        ret.addNewSensorCategory();
        if (!types.get(i).isReferenceByID()) {
        ret.getSensorCategoryArray(i).setName(categories.get(i).getName());
        } else {
        ret.getSensorCategoryArray(i).setId(categories.get(i).getId());
        }
        }

        for (int i = 0; i < sensors.size(); i++) {
        ret.addNewSensor();
        if (!sensors.get(i).isReferenceByID()) {
        ret.getSensorArray(i).setName(sensors.get(i).getName());
        } else {
        ret.getSensorArray(i).setId(sensors.get(i).getId());
        }
        }*/
        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    /**
     * Check if the SCAIDocument tree has already been created to the depth necessary for SensorRegistryControl
     */
    private SensorRegistryControl buildSensorRegistryControlTree() {
        if (docTemp == null) {
            docTemp = generateSCAIDocument(document);
        }

        if (docTemp.getSCAI() == null) {
            docTemp.addNewSCAI();
        }

        if (docTemp.getSCAI().getPayload() == null) {
            docTemp.getSCAI().addNewPayload();
        }

        if (docTemp.getSCAI().getPayload().getControlData() == null) {
            docTemp.getSCAI().getPayload().addNewControlData();
        }

        return docTemp.getSCAI().getPayload().getControlData().addNewSensorRegistryControl();

        /*if (docTemp.getSCAI().getPayload().getControlData().getSensorRegistryControl() == null) {
        return docTemp.getSCAI().getPayload().getControlData().addNewSensorRegistryControl();
        }
        else return docTemp.getSCAI().getPayload().getControlData().getSensorRegistryControl();*/
    }

    @Override
    public Object buildSensorType(String name, String adapter, SCAIReference dataStreamType,
        ArrayList<SCAIReference> configurationParameters,
        ArrayList<SCAIReference> sensorCategories, ArrayList<SCAIReference> sensorDomains) {
        SensorTypeDescription ret = SensorTypeDescription.Factory.newInstance();
        ret.setName(name);
        ret.setAdapter(adapter);
        ret.setDataStreamType(convertReference(dataStreamType));

        for (int i = 0; i < configurationParameters.size(); i++) {
            ret.addNewConfigurationParameter();

            if (!configurationParameters.get(i).isReferenceByID()) {
                ret.getConfigurationParameterArray(i)
                   .setName(configurationParameters.get(i).getName());
            } else {
                ret.getConfigurationParameterArray(i).setId(configurationParameters.get(i).getId());
            }
        }

        for (int i = 0; i < sensorCategories.size(); i++) {
            ret.addNewSensorCategory();

            if (!sensorCategories.get(i).isReferenceByID()) {
                ret.getSensorCategoryArray(i).setName(sensorCategories.get(i).getName());
            } else {
                ret.getSensorCategoryArray(i).setId(sensorCategories.get(i).getId());
            }
        }

        for (int i = 0; i < sensorDomains.size(); i++) {
            ret.addNewSensorDomain();

            if (!sensorDomains.get(i).isReferenceByID()) {
                ret.getSensorDomainArray(i).setName(sensorDomains.get(i).getName());
            } else {
                ret.getSensorDomainArray(i).setId(sensorDomains.get(i).getId());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildServiceOperator(String name, SCAIReference type,
        Map<String, String> properties) {
        OperatorDescription ret = OperatorDescription.Factory.newInstance();
        ret.setName(name);

        ret.addNewServiceOperator();
        ret.getServiceOperator().setOperatorType(convertReference(type));

        if (properties != null) {
            for (Entry<String, String> entry : properties.entrySet()) {
                OperatorDescription.Property property = ret.addNewProperty();
                property.setKey(entry.getKey());
                property.setValue(entry.getValue());
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildUser(String name, String password, SCAIPermission[] permissions) {
        UserDescription ret = UserDescription.Factory.newInstance();
        ret.setUsername(name);
        ret.setPassword(password.getBytes());

        // TODO: implement user roles
        if (permissions != null) {
            for (SCAIPermission scaiPermission : permissions) {
                PermissionDescription pd = ret.addNewPermission();
                PermissionDescription.Permission permission = pd.addNewPermission();
                if (scaiPermission.getPrivilegeAction().equals(SCAIPermission.PrivilegeActions.GRANT)) {
                    permission.setGrant(scaiPermission.getPrivilege());
                }
                else if(scaiPermission.getPrivilegeAction().equals(SCAIPermission.PrivilegeActions.WITHDRAW)) {
                    permission.setWithdraw(scaiPermission.getPrivilege());
                }
                for (Map.Entry<String, String> scaiProperty : scaiPermission.getProperties().entrySet()) {
                    PermissionDescription.Property property = pd.addNewProperty();
                    property.setKey(scaiProperty.getKey());
                    property.setValue(scaiProperty.getValue());
                }
            }
        }
        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public final void clearDocument() {
        document = SCAIDocument.Factory.newInstance();
    }

    private ConfigurationValueDescription convertConfigurationValues(
        ConfigurationValueDescription cv, ArrayList<SCAIConfigurationValue> configurationValues) {
        for (SCAIConfigurationValue scaiConfigurationValue : configurationValues) {
            ConfigurationValueDescription newcv = cv.addNewConfigurationValue();
            newcv.setName(scaiConfigurationValue.getName());

            if (scaiConfigurationValue.getIndex() != null) {
                newcv.setIndex(scaiConfigurationValue.getIndex());
            }

            if (scaiConfigurationValue.getValue() != null) {
                newcv.setValue(scaiConfigurationValue.getValue());
            } else if (scaiConfigurationValue.getConfigurationValues() != null) {
                newcv = convertConfigurationValues(
                        newcv,
                        scaiConfigurationValue.getConfigurationValues());
            }
        }

        return cv;
    }

    /**
     * Helper to convert SCAIReference to de.offis.xml.schema.scai20.Reference
     * @param reference
     * @return
     */
    private Reference convertReference(SCAIReference reference) {
        Reference result = Reference.Factory.newInstance();

        if (reference.getId() != null) {
            result.setId(reference.getId());
        } else {
            result.setName(reference.getName());
        }

        return result;
    }

    /**
     * Helper to convert SensorReference to de.offis.xml.schema.scai20.SensorReference
     * @param reference
     * @return
     */
    private de.offis.xml.schema.scai20.SensorReference convertSensorReference(
        SCAISensorReference reference) {
        de.offis.xml.schema.scai20.SensorReference result =
            de.offis.xml.schema.scai20.SensorReference.Factory.newInstance();

        if (reference.getName() != null && reference.getDomainName() != null) {
            de.offis.xml.schema.scai20.SensorReference.NameReference nameReference =
                result.addNewNameReference();
            nameReference.setSensorName(reference.getName());
            nameReference.setSensorDomainName(reference.getDomainName());
            result.setNameReference(nameReference);
        } else if (reference.getId() != null) {
            result.setSensorID(reference.getId());
        }

        return result;
    }

    // TODO: this generates an XML String out of a SCAIDocument and puts it back into a new SCAIDocument to clone it: find a better way
    SCAIDocument generateSCAIDocument(SCAIDocument doc) {
        try {
            if ((doc == null) || !doc.validate()) {
                return SCAIDocument.Factory.newInstance();
            } else {
                return SCAIDocument.Factory.parse(doc.xmlText());

                //                return (SCAIDocument)doc.copy();
            }
        } catch (XmlException ex) {
            return null;
        }
    }

    @Override
    public Object getDocument() {
        if (validateXml(document)) {
            return document;
        } else {
            return null;
        }
    }

    private boolean validateXml(XmlObject testObject) {
        XmlOptions validateOptions = new XmlOptions();
        ArrayList<XmlError> errorList = new ArrayList<XmlError>();
        validateOptions.setErrorListener(errorList);

        if (testObject.validate(validateOptions)) {
            //            System.out.println(testObject.xmlText(new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(4)));
            return true;
        } else {
            for (XmlError error : errorList) {
                System.out.println("\n");
                System.out.println("Message: " + error.getMessage() + "\n");
                System.out.println(
                        "Location of invalid XML: " + error.getCursorLocation().xmlText() + "\n");

                //                System.out.println(testObject.xmlText(new XmlOptions().setSavePrettyPrint().setSavePrettyPrintIndent(4)));
            }

            return false;
        }
    }

    @Override
    public Object buildAccessor(SCAIReference user, SCAIReference SensorDomain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object buildConfigurationValue(SCAIConfigurationValue configurationValue) {
        ConfigurationValueDescription ret = ConfigurationValueDescription.Factory.newInstance();
        ret.setName(configurationValue.getName());
        if (configurationValue.getIndex() != null) {
            ret.setIndex(configurationValue.getIndex());
        }

        if (configurationValue.getValue() != null) {
            ret.setValue(configurationValue.getValue());
        }
        else if (configurationValue.getConfigurationValues() != null) {
            for (SCAIConfigurationValue scaiConfigurationValue : configurationValue.getConfigurationValues()) {
                ConfigurationValueDescription newCV = ret.addNewConfigurationValue();
                newCV = (ConfigurationValueDescription)buildConfigurationValue(scaiConfigurationValue);
            }
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Object buildModules(Map<String, Boolean> supportedModules) {
        ModulesDescription ret = ModulesDescription.Factory.newInstance();
        for (Map.Entry<String, Boolean> item : supportedModules.entrySet()) {
            if (item.getKey().equals("AccessControl")) ret.setAccessControl(item.getValue());
            else if (item.getKey().equals("Identification")) ret.setIdentification(item.getValue());
            else if (item.getKey().equals("Measurements")) ret.setMeasurements(item.getValue());
            else if (item.getKey().equals("SensorControl")) ret.setSensorControl(item.getValue());
            else if (item.getKey().equals("SensorRegistryControl")) ret.setSensorRegistryControl(item.getValue());
        }

        if (ret.validate()) {
            return ret;
        } else {
            return null;
        }
    }
}
