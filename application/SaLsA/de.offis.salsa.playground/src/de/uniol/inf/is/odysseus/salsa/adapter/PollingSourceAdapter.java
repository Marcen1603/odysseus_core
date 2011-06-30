package de.uniol.inf.is.odysseus.salsa.adapter;
/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public interface PollingSourceAdapter extends SourceAdapter {
    void poll(Source source);
}
