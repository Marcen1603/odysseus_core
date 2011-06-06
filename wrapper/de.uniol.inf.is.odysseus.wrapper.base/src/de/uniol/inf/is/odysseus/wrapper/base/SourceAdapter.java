package de.uniol.inf.is.odysseus.wrapper.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public interface SourceAdapter {
    String getName();

    Collection<Source> getSources();

    void registerSource(Source source);

    void unregisterSource(Source source);
}
