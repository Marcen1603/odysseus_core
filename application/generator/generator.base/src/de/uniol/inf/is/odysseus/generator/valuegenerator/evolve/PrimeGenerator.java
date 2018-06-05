/** Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.valuegenerator.evolve;

import org.apache.commons.math3.primes.Primes;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class PrimeGenerator extends AbstractSingleValueGenerator {

    private int current;
    private final int start;

    public PrimeGenerator(final IErrorModel errorModel, final int start) {
        super(errorModel);
        this.start = start;
        this.current = start;
    }

    @Override
    public double generateValue() {
        final int prime = Primes.nextPrime(this.current);
        this.current++;
        return prime;
    }

    @Override
    public void initGenerator() {
        this.current = this.start;
    }

}
