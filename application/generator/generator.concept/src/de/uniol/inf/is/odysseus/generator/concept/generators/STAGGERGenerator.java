/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.concept.generators;

import java.util.Random;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.concept.ITupleGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *         STAGGER concept drift generator based on Schlimmer J.C., Granger
 *         R.H., Incremental learning from noisy data, Machine Learning,
 *         1(3), 1986, 317-354.
 */
public class STAGGERGenerator implements ITupleGenerator {

    private Random random;
    private final long seed;

    public STAGGERGenerator(long seed) {
        this.seed = seed;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public DataTuple generateTuple() {
        int size = this.random.nextInt(3);
        int color = this.random.nextInt(3);
        int shape = this.random.nextInt(3);

        boolean concept1 = ((size == 0) && (color == 0)) ? true : false;
        boolean concept2 = ((shape == 0) || (color == 2)) ? true : false;
        boolean concept3 = ((size == 1) || (size == 2)) ? true : false;

        DataTuple tuple = new DataTuple();
        tuple.addInteger(size);
        tuple.addInteger(color);
        tuple.addInteger(shape);
        tuple.addBoolean(concept1);
        tuple.addBoolean(concept2);
        tuple.addBoolean(concept3);
        return tuple;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void initGenerator() {
        this.random = new Random(seed);
    }

}
