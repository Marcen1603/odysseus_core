package de.uniol.inf.is.odysseus.salsa.sensor.model;

import java.util.Arrays;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class Measurement {
    public enum Error {
        DEVICE_OK, DEVICE_ERROR, CONTAMINATION_WARNING, CONTAMINATION_ERROR
    }

    private String version;
    private String device;
    private String serial;
    private Error status;

    private int messageCount;
    private int scanCount;

    private long powerUpDuration;
    private long transmissionDuration;

    private boolean inputStatus;
    private boolean outputStatus;

    private long scanningFrequency;
    private long measurementFrequency;

    private int encoders;
    private long encoderPosition;
    private int encoderSpeed;

    private Sample[] samples;

    public String getDevice() {
        return this.device;
    }

    public long getEncoderPosition() {
        return this.encoderPosition;
    }

    public int getEncoders() {
        return this.encoders;
    }

    public int getEncoderSpeed() {
        return this.encoderSpeed;
    }

    public long getMeasurementFrequency() {
        return this.measurementFrequency;
    }

    public int getMessageCount() {
        return this.messageCount;
    }

    public long getPowerUpDuration() {
        return this.powerUpDuration;
    }

    public Sample[] getSamples() {
        return this.samples;
    }

    public int getScanCount() {
        return this.scanCount;
    }

    public long getScanningFrequency() {
        return this.scanningFrequency;
    }

    public String getSerial() {
        return this.serial;
    }

    public Error getStatus() {
        return this.status;
    }

    public long getTransmissionDuration() {
        return this.transmissionDuration;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isDirty() {
        return (this.status == Error.CONTAMINATION_ERROR)
                || (this.status == Error.CONTAMINATION_WARNING);
    }

    public boolean isInputStatus() {
        return this.inputStatus;
    }

    public boolean isOutputStatus() {
        return this.outputStatus;
    }

    public void setDevice(final String device) {
        this.device = device;
    }

    public void setEncoderPosition(final long encoderPosition) {
        this.encoderPosition = encoderPosition;
    }

    public void setEncoders(final int encoders) {
        this.encoders = encoders;
    }

    public void setEncoderSpeed(final int encoderSpeed) {
        this.encoderSpeed = encoderSpeed;
    }

    public void setInputStatus(final boolean inputStatus) {
        this.inputStatus = inputStatus;
    }

    public void setMeasurementFrequency(final long measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
    }

    public void setMessageCount(final int messageCount) {
        this.messageCount = messageCount;

    }

    public void setOutputStatus(final boolean outputStatus) {
        this.outputStatus = outputStatus;
    }

    public void setPowerUpDuration(final long powerUpDuration) {
        this.powerUpDuration = powerUpDuration;
    }

    public void setSamples(final Sample[] samples) {
        this.samples = samples;
    }

    public void setScanCount(final int scanCount) {
        this.scanCount = scanCount;
    }

    public void setScanningFrequency(final long scanningFrequency) {
        this.scanningFrequency = scanningFrequency;
    }

    public void setSerial(final String serial) {
        this.serial = serial;
    }

    public void setStatus(final Error status) {
        this.status = status;
    }

    public void setStatus(final int status1, final int status2) {
        if ((status1 == 1) || (status2 == 1)) {
            this.status = Error.DEVICE_ERROR;
        }
        else if ((status1 == 2) || (status2 == 2)) {
            this.status = Error.CONTAMINATION_WARNING;
        }
        else if ((status1 == 4) || (status2 == 4)) {
            this.status = Error.CONTAMINATION_ERROR;
        }
        else {
            this.status = Error.DEVICE_OK;
        }
    }

    public void setTransmissionDuration(final long transmissionDuration) {
        this.transmissionDuration = transmissionDuration;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Measurement [version=" + this.version + ", device=" + this.device + ", serial="
                + this.serial + ", status=" + this.status + ", messageCount=" + this.messageCount
                + ", scanCount=" + this.scanCount + ", powerUpDuration=" + this.powerUpDuration
                + ", transmissionDuration=" + this.transmissionDuration + ", inputStatus="
                + this.inputStatus + ", outputStatus=" + this.outputStatus + ", scanningFrequency="
                + this.scanningFrequency + ", measurementFrequency=" + this.measurementFrequency
                + ", encoders=" + this.encoders + ", encoderPosition=" + this.encoderPosition
                + ", encoderSpeed=" + this.encoderSpeed + ", samples="
                + Arrays.toString(this.samples) + "]";
    }

}
