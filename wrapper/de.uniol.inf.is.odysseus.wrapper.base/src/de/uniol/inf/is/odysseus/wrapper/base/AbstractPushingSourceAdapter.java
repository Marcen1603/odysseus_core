package de.uniol.inf.is.odysseus.wrapper.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public abstract class AbstractPushingSourceAdapter extends AbstractSourceAdapter implements
        PushingSourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(AbstractPushingSourceAdapter.class);

    @Override
    protected void destroy(final Source source) {
        try {
            this.doDestroy(source);
        }
        catch (final Exception e) {
            AbstractPushingSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    abstract protected void doDestroy(Source source);

    abstract protected void doInit(Source source);

    @Override
    protected void init(final Source source) {
        try {
            this.doInit(source);
        }
        catch (final Exception e) {
            AbstractPushingSourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

}
