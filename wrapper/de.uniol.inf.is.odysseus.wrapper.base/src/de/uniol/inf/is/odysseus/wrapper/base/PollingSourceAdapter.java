package de.uniol.inf.is.odysseus.wrapper.base;

import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public interface PollingSourceAdapter extends SourceAdapter {
    void poll(SourceSpec source);
}
