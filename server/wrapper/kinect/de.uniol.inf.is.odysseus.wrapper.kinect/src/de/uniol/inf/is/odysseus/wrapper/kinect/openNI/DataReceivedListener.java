package de.uniol.inf.is.odysseus.wrapper.kinect.openNI;

/**
 * Interface to listen for notification of an update.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public interface DataReceivedListener {
    /**
     * Notify of an update.
     */
    void onDataReceived();
}
