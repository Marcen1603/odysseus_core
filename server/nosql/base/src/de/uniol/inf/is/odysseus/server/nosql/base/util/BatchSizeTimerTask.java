package de.uniol.inf.is.odysseus.server.nosql.base.util;

import java.util.List;

public interface BatchSizeTimerTask<T> {
	
    /**
     *
     * this Method will be called when the time in the associated BatchSizeTimer is expired or
     * the max count of elements in the associated BatchSizeTimer is reached
     *
     * @param elements List of queued elements
     */
    void onTimerRings(List<T> elements);
}
