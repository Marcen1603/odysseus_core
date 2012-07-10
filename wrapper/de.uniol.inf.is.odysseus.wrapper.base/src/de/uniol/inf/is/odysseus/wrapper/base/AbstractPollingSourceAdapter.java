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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public abstract class AbstractPollingSourceAdapter extends AbstractSourceAdapter implements
        PollingSourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractPollingSourceAdapter.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<SourceSpec, ScheduledFuture<?>> handler = new HashMap<SourceSpec, ScheduledFuture<?>>();

    @Override
    protected void destroy(final SourceSpec source) {
        this.handler.get(source).cancel(true);
        try {
            this.doDestroy(source);
        }
        catch (final Exception e) {
            AbstractPollingSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    abstract protected void doDestroy(SourceSpec source);

    abstract protected void doInit(SourceSpec source);

    abstract protected int getDelay();

    abstract protected int getInterval();

    @Override
    protected void init(final SourceSpec source) {
        final Runnable sourceThread = new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractPollingSourceAdapter.this.poll(source);
                }
                catch (final Exception e) {
                    AbstractPollingSourceAdapter.LOG.error(e.getMessage(), e);
                }
            }
        };
        int interval = this.getInterval();
        int delay = this.getDelay();
        try {
            if (source.getConfiguration().containsKey("interval")) {
                interval = Integer.parseInt(source.getConfiguration().get("interval").toString());
            }
            if (source.getConfiguration().containsKey("delay")) {
                delay = Integer.parseInt(source.getConfiguration().get("delay").toString());
            }
        }
        catch (final Exception e) {
            AbstractPollingSourceAdapter.LOG.error(e.getMessage(), e);
        }
        try {
            this.doInit(source);
        }
        catch (final Exception e) {
            AbstractPollingSourceAdapter.LOG.error(e.getMessage(), e);
        }
        this.handler.put(source, this.scheduler.scheduleAtFixedRate(sourceThread, delay, interval,
                TimeUnit.MILLISECONDS));
    }
}
