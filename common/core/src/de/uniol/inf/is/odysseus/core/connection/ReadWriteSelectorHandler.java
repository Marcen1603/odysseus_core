package de.uniol.inf.is.odysseus.core.connection;

public interface ReadWriteSelectorHandler extends SelectorHandler {

    void onRead();

    void onWrite();
}
