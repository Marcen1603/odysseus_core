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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public abstract class AbstractPushingSourceAdapter extends AbstractSourceAdapter implements
        PushingSourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractPushingSourceAdapter.class);

    @Override
    protected void destroy(final SourceSpec source) {
        try {
            this.doDestroy(source);
        }
        catch (final Exception e) {
            AbstractPushingSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    abstract protected void doDestroy(SourceSpec source);

    abstract protected void doInit(SourceSpec source);

    @Override
    protected void init(final SourceSpec source) {
        try {
            this.doInit(source);
        }
        catch (final Exception e) {
            AbstractPushingSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

}
