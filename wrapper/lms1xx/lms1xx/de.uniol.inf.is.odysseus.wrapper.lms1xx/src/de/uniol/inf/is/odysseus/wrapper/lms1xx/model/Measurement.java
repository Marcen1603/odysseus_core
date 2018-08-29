/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.lms1xx.model;

import java.util.Arrays;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class Measurement {
    public enum Error {
        DEVICE_OK, DEVICE_ERROR, CONTAMINATION_WARNING, CONTAMINATION_ERROR
    }

    private String name;
    private String version;
    private String device;
    private String serial;
    private Error status;
    private String comment;

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

    private Sample[] samples16Bit;
    private Sample[] samples8Bit;

    private double[][] position;
    private int positionRotationType;
    
    private long timeStamp;

    /**
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * @param comment
     *            the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * @return the device
     */
    public String getDevice() {
        return this.device;
    }

    /**
     * @param device
     *            the device to set
     */
    public void setDevice(final String device) {
        this.device = device;
    }

    /**
     * @return the encoderPosition
     */
    public long getEncoderPosition() {
        return this.encoderPosition;
    }

    /**
     * @param encoderPosition
     *            the encoderPosition to set
     */
    public void setEncoderPosition(final long encoderPosition) {
        this.encoderPosition = encoderPosition;
    }

    /**
     * @return the encoders
     */
    public int getEncoders() {
        return this.encoders;
    }

    /**
     * @param encoders
     *            the encoders to set
     */
    public void setEncoders(final int encoders) {
        this.encoders = encoders;
    }

    /**
     * @return the encoderSpeed
     */
    public int getEncoderSpeed() {
        return this.encoderSpeed;
    }

    /**
     * @param encoderSpeed
     *            the encoderSpeed to set
     */
    public void setEncoderSpeed(final int encoderSpeed) {
        this.encoderSpeed = encoderSpeed;
    }

    /**
     * @return the inputStatus
     */
    public boolean isInputStatus() {
        return this.inputStatus;
    }

    /**
     * @param inputStatus
     *            the inputStatus to set
     */
    public void setInputStatus(final boolean inputStatus) {
        this.inputStatus = inputStatus;
    }

    /**
     * @return the measurementFrequency
     */
    public long getMeasurementFrequency() {
        return this.measurementFrequency;
    }

    /**
     * @param measurementFrequency
     *            the measurementFrequency to set
     */
    public void setMeasurementFrequency(final long measurementFrequency) {
        this.measurementFrequency = measurementFrequency;
    }

    /**
     * @return the messageCount
     */
    public int getMessageCount() {
        return this.messageCount;
    }

    /**
     * @param messageCount
     *            the messageCount to set
     */
    public void setMessageCount(final int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the outputStatus
     */
    public boolean isOutputStatus() {
        return this.outputStatus;
    }

    /**
     * @param outputStatus
     *            the outputStatus to set
     */
    public void setOutputStatus(final boolean outputStatus) {
        this.outputStatus = outputStatus;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(double[][] position) {
        this.position = position;
    }

    /**
     * @return the position
     */
    public double[][] getPosition() {
        return this.position;
    }

    /**
     * @param positionRotationType
     *            the positionRotationType to set
     */
    public void setPositionRotationType(int positionRotationType) {
        this.positionRotationType = positionRotationType;
    }

    /**
     * @return the positionRotationType
     */
    public int getPositionRotationType() {
        return this.positionRotationType;
    }

    /**
     * @return the powerUpDuration
     */
    public long getPowerUpDuration() {
        return this.powerUpDuration;
    }

    /**
     * @param powerUpDuration
     *            the powerUpDuration to set
     */
    public void setPowerUpDuration(final long powerUpDuration) {
        this.powerUpDuration = powerUpDuration;
    }

    /**
     * @return the samples16Bit
     */
    public Sample[] get16BitSamples() {
        return this.samples16Bit;
    }

    /**
     * @param samples16Bit
     *            the samples16Bit to set
     */
    public void set16BitSamples(final Sample[] samples16Bit) {
        this.samples16Bit = samples16Bit;
    }

    /**
     * @return the samples8Bit
     */
    public Sample[] get8BitSamples() {
        return this.samples8Bit;
    }

    /**
     * @param samples8Bit
     *            the samples8Bit to set
     */
    public void set8BitSamples(final Sample[] samples8Bit) {
        this.samples8Bit = samples8Bit;
    }

    /**
     * @return the scanCount
     */
    public int getScanCount() {
        return this.scanCount;
    }

    /**
     * @param scanCount
     *            the scanCount to set
     */
    public void setScanCount(final int scanCount) {
        this.scanCount = scanCount;
    }

    /**
     * @return the scanningFrequency
     */
    public long getScanningFrequency() {
        return this.scanningFrequency;
    }

    /**
     * @param scanningFrequency
     *            the scanningFrequency to set
     */
    public void setScanningFrequency(final long scanningFrequency) {
        this.scanningFrequency = scanningFrequency;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return this.serial;
    }

    /**
     * @param serial
     *            the serial to set
     */
    public void setSerial(final String serial) {
        this.serial = serial;
    }

    /**
     * @return the status
     */
    public Error getStatus() {
        return this.status;
    }

    /**
     * 
     * @param status1
     *            the status1
     * @param status2
     *            the status2
     */
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

    /**
     * @return the transmissionDuration
     */
    public long getTransmissionDuration() {
        return this.transmissionDuration;
    }

    /**
     * @param transmissionDuration
     *            the transmissionDuration to set
     */
    public void setTransmissionDuration(final long transmissionDuration) {
        this.transmissionDuration = transmissionDuration;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * 
     * @return the dirty status
     */
    public boolean isDirty() {
        return (this.status == Error.CONTAMINATION_ERROR) || (this.status == Error.CONTAMINATION_WARNING);
    }

    @Override
    public String toString() {
        return "Measurement [version=" + this.version + ", device=" + this.device + ", serial=" + this.serial + ", status=" + this.status + ", messageCount=" + this.messageCount + ", scanCount="
                + this.scanCount + ", powerUpDuration=" + this.powerUpDuration + ", transmissionDuration=" + this.transmissionDuration + ", inputStatus=" + this.inputStatus + ", outputStatus="
                + this.outputStatus + ", scanningFrequency=" + this.scanningFrequency + ", measurementFrequency=" + this.measurementFrequency + ", encoders=" + this.encoders + ", encoderPosition="
                + this.encoderPosition + ", encoderSpeed=" + this.encoderSpeed + ", samples=" + Arrays.toString(this.samples16Bit) + "]";
    }

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
