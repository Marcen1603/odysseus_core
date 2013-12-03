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

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public abstract class AbstractSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractSourceAdapter.class);

    private final Map<String, SourceSpec> sources = new HashMap<String, SourceSpec>();

    public AbstractSourceAdapter() {

    }

    abstract protected void destroy(SourceSpec source);

    @Override
    public Collection<SourceSpec> getSources() {
        return Collections.unmodifiableCollection(this.sources.values());
    }

    abstract protected void init(SourceSpec source);

    @Override
    public void registerSource(final SourceSpec source) {
        this.sources.put(source.getName(), source);
        this.init(source);
        AbstractSourceAdapter.LOG.debug("New source {} registered", source.getName());
    }

    @Override
    public String toString() {
        return "AbstractSourceAdapter " + this.getName() + " [sources=" + this.sources + "]";
    }

    protected void transfer(final SourceSpec sourceSpec, final long timestamp, final Object[] data) {
        SourcePool.transfer(sourceSpec, timestamp, data);
    }

    @Override
    public void unregisterSource(final SourceSpec source) {
        this.destroy(source);
        this.sources.remove(source.getName());
        AbstractSourceAdapter.LOG.debug("Source {} unregistered", source.getName());
    }

}
