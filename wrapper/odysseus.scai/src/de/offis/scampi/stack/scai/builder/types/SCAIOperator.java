/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.builder.types;

import java.util.HashMap;

/**
 *
 * @author Claas
 */
public class SCAIOperator {

    private String name;
    private SCAIReference type;
    private SCAISensorReference sensor;

    private boolean operator = false;
    private boolean input = false;
    private boolean output = false;
    private HashMap<String, String> properties = new HashMap<String, String>();

    public void setInput() {
        this.input = true;
        this.operator = false;
        this.output = false;
    }

    public void setOperator() {
        this.input = false;
        this.operator = true;
        this.output = false;
    }

    public void setOutput() {
        this.input = false;
        this.operator = false;
        this.output = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSensor(SCAISensorReference sensor) {
        this.sensor = sensor;
    }

    public void setType(SCAIReference type) {
        this.type = type;
    }

    public boolean isInput() {
        return input;
    }

    public String getName() {
        return name;
    }

    public boolean isOperator() {
        return operator;
    }

    public boolean isOutput() {
        return output;
    }

    public SCAISensorReference getSensor() {
        return sensor;
    }

    public SCAIReference getType() {
        return type;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

}
