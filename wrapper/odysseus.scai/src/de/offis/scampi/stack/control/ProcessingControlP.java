/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.control;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.scai.builder.types.SCAIOperator;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import java.net.URI;
import java.util.HashMap;

/**
 *
 * @author Claas
 */
public class ProcessingControlP extends ControlModuleP {

    public boolean createOperator(SCAIOperator operator, SCAIReference operatorGroup)
    {
        return false;
    }

     public boolean createOperatorGroup(String name)
    {
        return false;
    }

    public boolean createOperatorType(String name, String metaType, HashMap<String, String> properties, HashMap<String, Boolean> readOnly, String description)
    {
        return false;
    }

    public boolean deployOperatorGroup(SCAIReference operatorGroup, SCAIReference processingUnit)
    {
        return false;
    }

    public Object getOperator(SCAIReference operator, SCAIReference operatorGroup, IBuilder builder)
    {
        return null;
    }

    public Object getOperatorGroup(SCAIReference operatorGroup, IBuilder builder)
    {
        return null;
    }
    
    public Object getOperatorGroupStatus(SCAIReference operatorGroup, IBuilder builder)
    {
        return null;
    }


    public Object getOperatorType(SCAIReference operatorType, IBuilder builder)
    {
        return null;
    }

    public boolean linkInputToOperator(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean linkInputToOutput(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean linkOperatorToOperator(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean linkOperatorToOutput(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean registerProcessingUnit(String name, URI address, double version)
    {
        return false;
    }

    public Object listAllLinks(IBuilder builder, SCAIReference group)
    {
        return null;
    }
    
    public Object listAllOperatorsByGroup(IBuilder builder, SCAIReference group)
    {
        return null;
    }

    public Object listAllOperatorGroups(IBuilder builder)
    {
        return null;
    }

    public Object listAllOperatorTypes(IBuilder builder)
    {
        return null;
    }

    public Object listAllProcessingUnits(IBuilder builder)
    {
        return null;
    }

    public boolean undeployOperatorGroup(SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean unlinkInputToOperator(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean unlinkInputToOutput(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean unlinkOperatorToOperator(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean unlinkOperatorToOutput(SCAIReference sourceOperator, SCAIReference destinationOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean unregisterProcessingUnit(SCAIReference processingUnit)
    {
        return false;
    }

    public boolean updateOperator(SCAIReference oldOperator, SCAIOperator newOperator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean updateOperatorGroup(SCAIReference oldOperatorGroup, String operatorGroup)
    {
        return false;
    }

    public boolean updateOperatorType(SCAIReference oldOperatorType, String name, String metaType, HashMap<String, String> properties, HashMap<String, Boolean> readOnly, String description)
    {
        return false;
    }

    public boolean removeOperator(SCAIReference operator, SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean removeOperatorGroup(SCAIReference operatorGroup)
    {
        return false;
    }

    public boolean removeOperatorType(SCAIReference operatorType)
    {
        return false;
    }

    public void processSensorData(Object sensorData)
    {
        
    }

}
