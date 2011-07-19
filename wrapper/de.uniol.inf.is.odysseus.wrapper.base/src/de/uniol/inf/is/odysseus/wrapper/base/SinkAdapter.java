package de.uniol.inf.is.odysseus.wrapper.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public interface SinkAdapter {
    String getName();

    Collection<SinkSpec> getSinks();

    void registerSink(SinkSpec sink);

    void unregisterSink(SinkSpec sink);

    void transfer(SinkSpec sink, long timestamp, Object[] data);
}
