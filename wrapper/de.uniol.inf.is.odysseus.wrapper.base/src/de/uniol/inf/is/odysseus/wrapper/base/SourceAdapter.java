package de.uniol.inf.is.odysseus.wrapper.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public interface SourceAdapter {
    String getName();

    Collection<SourceSpec> getSources();

    void registerSource(SourceSpec source);

    void unregisterSource(SourceSpec source);
}
