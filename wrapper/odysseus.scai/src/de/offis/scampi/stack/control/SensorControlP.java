/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack.control;

import de.offis.scampi.stack.IBuilder;
import de.offis.scampi.stack.scai.builder.types.SCAIConfigurationValue;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;


/**
 *
 * @author Claas
 */
public class SensorControlP extends ControlModuleP {
    public boolean configureSensor(SCAISensorReference sensor,
        SCAIConfigurationValue configureationValue) {
        return false;
    }

    public Object getConfiguration(SCAISensorReference sensor, IBuilder builder) {
        return null;
    }

    public Object getSupportedModules(SCAISensorReference sensor, IBuilder builder) {
        return null;
    }

    public Object getValue(SCAISensorReference sensor, IBuilder builder) {
        return null;
    }

    public boolean startSensor(SCAISensorReference sensor) {
        return false;
    }

    public boolean stopSensor(SCAISensorReference sensor) {
        return false;
    }

    public boolean subscribeDatastream(SCAISensorReference sensor) {
        return false;
    }

    public boolean unsubscribeDatastream(SCAISensorReference sensor) {
        return false;
    }
}
