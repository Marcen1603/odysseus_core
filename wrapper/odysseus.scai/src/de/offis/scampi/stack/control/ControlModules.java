/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.control;


/**
 *
 * @author Claas
 */
public class ControlModules {
    private AccessControlP accessControl = null;
    private SensorControlP sensorControl = null;
    private SensorRegistryControlP sensorRegistryControl = null;
    private ProcessingControlP processingControl = null;

    public AccessControlP getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControlP accessControl) {
        this.accessControl = accessControl;
    }

    public SensorControlP getSensorControl() {
        return sensorControl;
    }

    public void setSensorControl(SensorControlP sensorControl) {
        this.sensorControl = sensorControl;
    }

    public SensorRegistryControlP getSensorRegistryControl() {
        return sensorRegistryControl;
    }

    public void setSensorRegistryControl(SensorRegistryControlP sensorRegistryControl) {
        this.sensorRegistryControl = sensorRegistryControl;
    }

    public ProcessingControlP getProcessingControl() {
        return processingControl;
    }

    public void setProcessingControl(ProcessingControlP processingControl) {
        this.processingControl = processingControl;
    }

    public ControlModules() {
        accessControl = new AccessControlP();
        sensorControl = new SensorControlP();
        sensorRegistryControl = new SensorRegistryControlP();
        processingControl = new ProcessingControlP();
    }

}
