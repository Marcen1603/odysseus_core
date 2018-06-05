/*
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

package de.uniol.inf.is.odysseus.generator.probabilistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ConstantValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.NormalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticProvider extends AbstractDataGenerator {

    enum Attribute {
        Time, Class, X, Y
    }

    private List<ISingleValueGenerator> discreteProbabilityGenerators = new ArrayList<ISingleValueGenerator>();
    private List<ISingleValueGenerator> continuousProbabilityGenerators = new ArrayList<ISingleValueGenerator>();
    private final Map<Attribute, ISingleValueGenerator> generators = new HashMap<Attribute, ISingleValueGenerator>();

    public ProbabilisticProvider() {

    }

    @Override
    public synchronized List<DataTuple> next() {
        DataTuple tuple = new DataTuple();
        tuple.addLong(this.generators.get(Attribute.Time).nextValue());

        // Add discrete distribution
        generateDiscreteAttribute(tuple, Attribute.Class);

        // Add index to distributions for X and Y
        tuple.addInteger(0);
        tuple.addInteger(0);
        generateContinuousAttribute(tuple, new Attribute[] { Attribute.X, Attribute.Y });
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataTuple> list = new ArrayList<DataTuple>();
        list.add(tuple);
        return list;
    }

    @Override
    public void process_init() {
        ISingleValueGenerator timeGenerator = new IncreaseGenerator(new NoError(), 0, 1);
        timeGenerator.init();
        this.generators.put(Attribute.Time, timeGenerator);

        discreteProbabilityGenerators.add(new ConstantValueGenerator(new NoError(), 0.75));
        discreteProbabilityGenerators.add(new ConstantValueGenerator(new NoError(), 0.25));
        for (int i = 0; i < discreteProbabilityGenerators.size(); i++) {
            discreteProbabilityGenerators.get(i).init();
        }
        ISingleValueGenerator classGenerator = new NormalDistributionGenerator(new NoError());
        classGenerator.init();
        this.generators.put(Attribute.Class, classGenerator);

        continuousProbabilityGenerators.add(new ConstantValueGenerator(new NoError(), 0.75));
        continuousProbabilityGenerators.add(new ConstantValueGenerator(new NoError(), 0.25));
        ISingleValueGenerator xGenerator = new NormalDistributionGenerator(new NoError());
        xGenerator.init();
        this.generators.put(Attribute.X, xGenerator);
        ISingleValueGenerator yGenerator = new NormalDistributionGenerator(new NoError());
        yGenerator.init();
        this.generators.put(Attribute.Y, yGenerator);

    }

    @Override
    public void close() {

    }

    @Override
    public ProbabilisticProvider newCleanInstance() {
        return new ProbabilisticProvider();
    }

    private void generateDiscreteAttribute(final DataTuple tuple, final Attribute attribute) {
        tuple.addInteger(discreteProbabilityGenerators.size());
        for (int i = 0; i < discreteProbabilityGenerators.size(); i++) {
            tuple.addDouble(this.generators.get(attribute).nextValue());
            tuple.addDouble(discreteProbabilityGenerators.get(i).nextValue());
        }
    }

    private void generateContinuousAttribute(final DataTuple tuple, final Attribute[] attributes) {
        int mixtures = continuousProbabilityGenerators.size();
        int dimension = attributes.length;
        // Number of mixtures
        tuple.addInteger(mixtures);
        // Dimension
        tuple.addInteger(dimension);
        this.generateContinuousAttributeMixture(tuple, attributes.length, attributes);
        // The scalling
        tuple.addDouble(1.0);
        // Support vector
        for (int i = 0; i < (dimension * 2); i++) {
            // The support on each dimension
            if ((i % 2) == 0) {
                tuple.addDouble(Double.NEGATIVE_INFINITY);
            }
            else {
                tuple.addDouble(Double.POSITIVE_INFINITY);
            }
        }
    }

    private void generateContinuousAttributeMixture(final DataTuple tuple, final int dimension, final Attribute[] attributes) {
        for (int c = 0; c < continuousProbabilityGenerators.size(); c++) {
            tuple.addDouble(continuousProbabilityGenerators.get(c).nextValue());
            for (Attribute attr : attributes) {
                tuple.addDouble(this.generators.get(attr).nextValue());
            }

            for (int i = 0; i < CovarianceMatrixUtils.getCovarianceTriangleSizeFromDimension(dimension); i++) {
                tuple.addDouble(1.0);
            }
        }
    }

    public static void main(final String[] args) {
        final ProbabilisticProvider provider = new ProbabilisticProvider();
        provider.process_init();
        System.out.println(provider.next());
        System.out.println(provider.next());

        System.out.println(provider.next());
    }
}
