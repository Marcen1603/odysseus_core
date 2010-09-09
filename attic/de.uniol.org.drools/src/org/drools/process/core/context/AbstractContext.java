package org.drools.process.core.context;

import java.io.Serializable;

import org.drools.process.core.Context;
import org.drools.process.core.ContextContainer;

public abstract class AbstractContext implements Context, Serializable {

   	private static final long serialVersionUID = 1559137879971470787L;
	private long id;
    private ContextContainer contextContainer;

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public ContextContainer getContextContainer() {
		return contextContainer;
	}

	public void setContextContainer(ContextContainer contextContainer) {
		this.contextContainer = contextContainer;
	}

}
