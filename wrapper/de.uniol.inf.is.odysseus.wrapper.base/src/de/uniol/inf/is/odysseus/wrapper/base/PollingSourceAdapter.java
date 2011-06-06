package de.uniol.inf.is.odysseus.wrapper.base;

import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public interface PollingSourceAdapter extends SourceAdapter {
    void poll(Source source);
}
