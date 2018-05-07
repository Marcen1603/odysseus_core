/**
 * Copyright 2017 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.Optional;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class AbstractSelectPOTest extends AbstractOperatorTest {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void givenProbabilisticSelectPO() {
        this.operator = Optional
                .of(new ProbabilisticSelectPO<IProbabilistic>(this.schema.orElse(null), this.predicate.orElse(null)));
        this.operator.get().connectSink((ISink)new TestableSink(new ITestableSinkListener<IStreamObject<IProbabilistic>>() {

            @Override
            public void onPunctuation(final IPunctuation punctuation, final int port) {
                AbstractSelectPOTest.this.lastPunctuation = Optional.of(punctuation);
            }

            @Override
            public void onObject(final IStreamObject<?> object, final int port) {
                AbstractSelectPOTest.this.lastObject = Optional.of(object);
            }
        }), 0, 0, this.operator.get().getOutputSchema());

    }
}
