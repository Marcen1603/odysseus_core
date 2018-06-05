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
package de.uniol.inf.is.odysseus.generator.valuegenerator.spatial;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractMultiValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MovingCircleGenerator extends AbstractMultiValueGenerator {
    private final double x;
    private final double y;
    private final double diameter;
    private final double increase;
    private double alpha;

    /**
     * @param errorModel
     */
    public MovingCircleGenerator(final IErrorModel errorModel, final double diameter) {
        this(errorModel, diameter, 0.0, 0.0);
    }

    /**
     * @param errorModel
     */
    public MovingCircleGenerator(final IErrorModel errorModel, final double diameter, final double increase) {
        this(errorModel, diameter, increase, 0.0, 0.0);
    }

    /**
     * @param errorModel
     */
    public MovingCircleGenerator(final IErrorModel errorModel, final double diameter, final double x, final double y) {
        this(errorModel, diameter, 1.0, x, y);
    }

    /**
     * @param errorModel
     */
    public MovingCircleGenerator(final IErrorModel errorModel, final double diameter, final double increase, final double x, final double y) {
        super(errorModel);
        this.diameter = diameter;
        this.increase = increase;
        this.x = x;
        this.y = y;
        this.alpha = 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int dimension() {
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] generateValue() {
        final double[] value = new double[] { ((this.diameter / 2.0) * FastMath.cos(this.alpha)) + this.x, ((this.diameter / 2.0) * FastMath.sin(this.alpha)) + this.y };
        this.alpha += this.increase;
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initGenerator() {
        this.alpha = 0.0;
    }
}
