package de.uniol.inf.is.odysseus.salsa.adapter;

public interface PollingSourceAdapter extends SourceAdapter {
    void poll(Source source);
}
