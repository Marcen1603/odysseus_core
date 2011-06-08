package de.uniol.inf.is.odysseus.salsa.adapter;

import java.util.Collection;

public interface SourceAdapter {
    String getName();

    Collection<Source> getSources();

    void registerSource(Source source);

    void unregisterSource(Source source);
}
