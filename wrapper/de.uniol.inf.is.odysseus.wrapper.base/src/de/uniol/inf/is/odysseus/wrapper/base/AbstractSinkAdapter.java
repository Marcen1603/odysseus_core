/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.base;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public abstract class AbstractSinkAdapter implements SinkAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractSinkAdapter.class);
    private final Map<String, SinkSpec> sinks = new HashMap<String, SinkSpec>();

    public AbstractSinkAdapter() {
        // TODO Auto-generated constructor stub
    }

    abstract protected void destroy(SinkSpec sink);

    @Override
    public Collection<SinkSpec> getSinks() {
        return Collections.unmodifiableCollection(this.sinks.values());
    }

    abstract protected void init(SinkSpec sink);

    @Override
    public void registerSink(final SinkSpec sink) {
        this.sinks.put(sink.getName(), sink);
        this.init(sink);
        AbstractSinkAdapter.LOG.debug("New sink {} registered", sink.getName());
    }

    @Override
    public void unregisterSink(final SinkSpec sink) {
        this.destroy(sink);
        this.sinks.remove(sink.getName());
        AbstractSinkAdapter.LOG.debug("Sink {} unregistered", sink.getName());
    }

    @Override
    public String toString() {
        return "AbstractSinkAdapter " + this.getName();
    }
}
