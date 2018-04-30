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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class TestableSink extends AbstractSink<IStreamObject<IProbabilistic>> implements ISink<IStreamObject<IProbabilistic>>{
    private final ITestableSinkListener<IStreamObject<IProbabilistic>> listener;

    public TestableSink(final ITestableSinkListener<IStreamObject<IProbabilistic>> listener) {
        super();
        setInputPortCount(1);
        this.listener = listener;
    }

    @Override
    public void processPunctuation(final IPunctuation punctuation, final int port) {
        this.listener.onPunctuation(punctuation, port);
    }

    @Override
    protected void process_next(final IStreamObject<IProbabilistic> object, final int port) {
        this.listener.onObject(object, port);
    }

}
