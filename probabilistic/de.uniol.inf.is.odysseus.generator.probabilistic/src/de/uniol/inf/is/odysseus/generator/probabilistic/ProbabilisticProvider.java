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
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.NormalDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticProvider extends AbstractDataGenerator {

    enum Attribute {
        Time, Value
    }

    private final Map<Attribute, IValueGenerator> generators = new HashMap<Attribute, IValueGenerator>();

    public ProbabilisticProvider() {

    }

    @Override
    public synchronized List<DataTuple> next() {
        DataTuple tuple = new DataTuple();
        tuple.addLong(this.generators.get(Attribute.Time).nextValue());
        tuple.addDouble(this.generators.get(Attribute.Value).nextValue());

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
        IValueGenerator timeGenerator = new IncreaseGenerator(new NoError(), 0, 1);
        timeGenerator.init();
        this.generators.put(Attribute.Time, timeGenerator);

        IValueGenerator normalDistributionGenerator = new NormalDistributionGenerator(new NoError());
        normalDistributionGenerator.init();
        this.generators.put(Attribute.Value, normalDistributionGenerator);
    }

    @Override
    public void close() {

    }

    @Override
    public ProbabilisticProvider newCleanInstance() {
        return new ProbabilisticProvider();
    }

    public static void main(final String[] args) {
        final ProbabilisticProvider provider = new ProbabilisticProvider();
        provider.process_init();
        System.out.println(provider.next());
        System.out.println(provider.next());

        System.out.println(provider.next());
    }
}
