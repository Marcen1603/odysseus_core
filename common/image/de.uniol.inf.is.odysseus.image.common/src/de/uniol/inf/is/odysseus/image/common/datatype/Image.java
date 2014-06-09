/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.image.common.datatype;

import java.nio.DoubleBuffer;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Image implements IClone, Cloneable {

    private final int width;
    private final int height;
    private final DoubleBuffer buffer;

    public Image(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.buffer = DoubleBuffer.allocate(this.width * this.height);
    }

    public Image(final int width, final int height, final DoubleBuffer buffer) {
        this(width, height);
        synchronized (buffer) {
            this.buffer.put(buffer.duplicate());
        }
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return this.height;
    }

    public double get(final int x, final int y) {
        return this.buffer.get((y * this.width) + x);
    }

    public DoubleBuffer getBuffer() {
        this.buffer.rewind();
        return this.buffer;
    }

    public void set(final int x, final int y, final double value) {
        this.buffer.put((y * this.width) + x, value);
    }

    public double[][] get(final int x1, final int y1, final int x2, final int y2) {
        double[][] value = new double[y2 - y1 + 1][x2 - x1 + 1];
       
        for (int i = 0; i < value.length; i++) {
            try{
            this.buffer.position((i+y1) * this.width + x1);
            this.buffer.get(value[i]);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(i+" "+((i+y1) * this.width + x1)+" "+buffer.capacity()+" "+ x1+" "+x2+" "+y1+" "+y2+" "+Arrays.toString(value)+" "+value.length+" "+value[0].length+" "+buffer.position()+" "+buffer.limit());
        }
    }
        return value;
    }

    public void set(final int x1, final int y1, final int x2, final int y2, final double value) {
        double[] valueArray = new double[x2 - x1 + 1];
        Arrays.fill(valueArray, value);
        for (int i = y1; i <= y2; i++) {
            this.buffer.position(i * this.width + x1);
            this.buffer.put(valueArray);
        }
    }

    public void setBuffer(final DoubleBuffer value) {
        value.rewind();
        this.buffer.clear();
        this.buffer.put(value);
    }

    public void fill(final double value) {
        Arrays.fill(this.buffer.array(), value);
    }

    @Override
    public Image clone() {
        final Image image = new Image(this.width, this.height, this.buffer);
        return image;
    }

    @Override
    public String toString() {
        return "{Width: " + this.width + " Height: " + this.height + "}";
    }

}
