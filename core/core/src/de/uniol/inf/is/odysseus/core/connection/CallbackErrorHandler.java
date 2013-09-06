package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallbackErrorHandler {

	static Logger LOG = LoggerFactory.getLogger(CallbackErrorHandler.class);
	
    public void handleError(IOException e) {
    	LOG.error(e.getMessage(),e);
    }

}
