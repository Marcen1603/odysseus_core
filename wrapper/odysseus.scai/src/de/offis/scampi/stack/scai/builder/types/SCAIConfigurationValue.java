/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack.scai.builder.types;

import java.util.ArrayList;

/**
 *
 * @author sbehrensen
 */
public class SCAIConfigurationValue {
    private String name;
    private Integer index;
    private String value;
    private ArrayList<SCAIConfigurationValue> configurationValues;

    public SCAIConfigurationValue(String name, Integer index, String value) {
        this.name = name;
        this.index = index;
        this.value = value;
    }

    public SCAIConfigurationValue(String name, Integer index, ArrayList<SCAIConfigurationValue> configurationValues) {
        this.name = name;
        this.index = index;
        this.configurationValues = configurationValues;
    }

    public SCAIConfigurationValue() {
    }

    public ArrayList<SCAIConfigurationValue> getConfigurationValues() {
        return configurationValues;
    }

    public void setConfigurationValues(ArrayList<SCAIConfigurationValue> configurationValues) {
        this.configurationValues = configurationValues;
    }

    /**
     * Get the value of value
     *
     * @return the value of value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value of value
     *
     * @param value new value of value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the value of index
     *
     * @return the value of index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Set the value of index
     *
     * @param index new value of index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

}
