package cm.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Tobi on 03.04.2015.
 */
public class Sensor {

    private Machine machine;
    private String name;
    private SimpleDoubleProperty currentValuePropery;
    private String status;

    public Sensor(Machine machine, String name, double currentValuePropery, String status) {
        this.machine = machine;
        this.name = name;
        this.currentValuePropery = new SimpleDoubleProperty(currentValuePropery);
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleDoubleProperty getCurrentValue() {
        return currentValuePropery;
    }

    public void setCurrentValue(double currentValue) {
        Platform.runLater(() -> currentValuePropery.set(currentValue));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}
