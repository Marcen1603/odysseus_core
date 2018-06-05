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
package de.uniol.inf.is.odysseus.generator.valuegenerator.random;

import org.apache.commons.math3.random.Well1024a;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Well1024aGenerator extends AbstractSingleValueGenerator {

    private Well1024a random;
    private long seed;

    /**
     * Creates a Well1024a.
     * 
     * @param errorModel
     *            Error model.
     * 
     */
    public Well1024aGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);
    }

    /**
     * Creates a Well1024a.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     * 
     */
    public Well1024aGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        this.random = new Well1024a(seed);
        this.seed = seed;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public double generateValue() {
        return random.nextDouble();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void initGenerator() {
        this.random.setSeed(seed);
    }
}
