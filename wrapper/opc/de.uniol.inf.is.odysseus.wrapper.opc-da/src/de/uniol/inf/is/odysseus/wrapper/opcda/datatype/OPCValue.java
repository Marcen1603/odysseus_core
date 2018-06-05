/*******************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.opcda.datatype;


/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class OPCValue<T> {
    private long timestamp;
    private T value;
    private short quality;
    private int error;

    /**
     * Class constructor.
     *
     * @param timestamp
     * @param value
     * @param quality
     * @param error
     */
    public OPCValue(long timestamp, T value, short quality, int error) {
        super();
        this.timestamp = timestamp;
        this.value = value;
        this.quality = quality;
        this.error = error;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return the quality
     */
    public short getQuality() {
        return this.quality;
    }

    /**
     * @param quality
     *            the quality to set
     */
    public void setQuality(short quality) {
        this.quality = quality;
    }

    /**
     * @return the error
     */
    public int getError() {
        return this.error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(int error) {
        this.error = error;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.value + "[timestamp=" + this.timestamp + ", quality=" + this.quality + ", error=" + this.error + "]";
    }

}
