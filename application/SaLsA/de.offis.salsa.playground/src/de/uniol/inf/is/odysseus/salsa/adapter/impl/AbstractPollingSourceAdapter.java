package de.uniol.inf.is.odysseus.salsa.adapter.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.salsa.adapter.PollingSourceAdapter;
import de.uniol.inf.is.odysseus.salsa.adapter.Source;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public abstract class AbstractPollingSourceAdapter extends AbstractSourceAdapter implements
        PollingSourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractPollingSourceAdapter.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final Map<Source, ScheduledFuture<?>> handler = new HashMap<Source, ScheduledFuture<?>>();

    @Override
    protected void destroy(final Source source) {
        this.handler.get(source).cancel(true);
        try {
            this.doDestroy(source);
        }
        catch (final Exception e) {
            AbstractPollingSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    abstract protected void doDestroy(Source source);

    abstract protected void doInit(Source source);

    abstract protected int getDelay();

    abstract protected int getInterval();

    @Override
    protected void init(final Source source) {
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
