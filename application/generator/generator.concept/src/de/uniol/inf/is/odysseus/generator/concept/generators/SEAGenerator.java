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
 *         SEA concept drift generator based on
 *         W. Nick Street, YongSeog Kim, A streaming ensemble algorithm (SEA)
 *         for large-scale classification, Proceeding
 *         KDD '01 Proceedings of the seventh ACM SIGKDD international
 *         conference on Knowledge discovery and data mining
 *         Pages 377-382
 *         ACM New York, NY, USA ©2001
 *         ISBN:1-58113-391-X doi:10.1145/502512.502568
 */
public class SEAGenerator implements ITupleGenerator {
    private Random random;
    private final long seed;

    public SEAGenerator(long seed) {
        this.seed = seed;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public DataTuple generateTuple() {
        double feature1 = 10 * this.random.nextDouble();
        double feature2 = 10 * this.random.nextDouble();
        double feature3 = 10 * this.random.nextDouble();
        boolean concept1 = (feature1 + feature2 <= 7) ? false : true;
        boolean concept2 = (feature1 + feature2 <= 8.0) ? false : true;
        boolean concept3 = (feature1 + feature2 <= 9) ? false : true;
        boolean concept4 = (feature1 + feature2 <= 9.5) ? false : true;
        int nextInt = this.random.nextInt(100);
        concept1 = ((1 + (nextInt)) <= 10) ? !concept1 : concept1;
        concept2 = ((1 + (nextInt)) <= 10) ? !concept2 : concept2;
        concept3 = ((1 + (nextInt)) <= 10) ? !concept3 : concept3;
        concept4 = ((1 + (nextInt)) <= 10) ? !concept4 : concept4;

        DataTuple tuple = new DataTuple();

        tuple.addDouble(feature1);
        tuple.addDouble(feature2);
        tuple.addDouble(feature3);
        tuple.addBoolean(concept1);
        tuple.addBoolean(concept2);
        tuple.addBoolean(concept3);
        tuple.addBoolean(concept4);
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
