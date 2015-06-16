package cm.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by Tobi on 03.04.2015.
 */
public class Sensor {

    private Collection collection;
    private String name;
    private SimpleDoubleProperty currentValuePropery;
    private String status;

    public Sensor(Collection collection, String name, double currentValuePropery, String status) {
        this.collection = collection;
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

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
