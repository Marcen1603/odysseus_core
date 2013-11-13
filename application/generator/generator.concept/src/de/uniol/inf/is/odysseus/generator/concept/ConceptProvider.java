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
package de.uniol.inf.is.odysseus.generator.concept;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ConceptProvider extends AbstractDataGenerator {

    @SuppressWarnings("unused")
    private int transId = 1;

    private final ITupleGenerator generator;

    public ConceptProvider(ITupleGenerator generator) {
        this.generator = generator;
    }

    public ConceptProvider(ConceptProvider provider) {
        this.generator = provider.generator;
    }

    @Override
    public List<DataTuple> next() throws InterruptedException {

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<DataTuple> list = new ArrayList<DataTuple>();
        list.add(generator.generateTuple());
        transId++;
        return list;
    }

    @Override
    public void process_init() {
        generator.initGenerator();
    }

    @Override
    public void close() {

    }

    @Override
    public ConceptProvider newCleanInstance() {
        return new ConceptProvider(this);
    }

}
