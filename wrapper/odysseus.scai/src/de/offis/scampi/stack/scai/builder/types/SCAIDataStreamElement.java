/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.offis.scampi.stack.scai.builder.types;

/**
 *
 * @author sbehrensen
 */
public class SCAIDataStreamElement {

    public enum Errortype {

        SENSORDEAD, NAN, BATTERYDEAD, UNKNOWN
    };
    private String data;
    private Errortype errorType;
    private String errorMessage;
    private String path;
    private Float quality;

    /**
     * Get the value of quality
     *
     * @return the value of quality
     */
    public Float getQuality() {
        return quality;
    }

    /**
     * Set the value of quality
     *
     * @param quality new value of quality
     */
    public void setQuality(Float quality) {
        this.quality = quality;
    }


    /**
     * Get the value of path
     *
     * @return the value of path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the value of path
     *
     * @param path new value of path
     */
    public void setPath(String path) {
        this.path = path;
    }


    public Errortype getErrortype() {
        return errorType;
    }

    public void setErrortype(Errortype errorType) {
        this.errorType = errorType;
    }

    /**
     * Get the value of Data
     *
     * @return the value of Data
     */
    public String getData() {
        return data;
    }

    /**
     * Set the value of Data
     *
     * @param data new value of Data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get the value of errorMessage
     *
     * @return the value of errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the value of errorMessage
     *
     * @param errorMessage new value of errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
