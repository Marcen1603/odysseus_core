package org.drools.workflow.instance.node;

import org.drools.runtime.process.NodeInstance;
import org.drools.workflow.core.impl.NodeImpl;

public class StateNodeInstance extends EventBasedNodeInstance {

	private static final long serialVersionUID = 4L;

	@Override
	public void internalTrigger(NodeInstance from, String type) {
		addTriggerListener();
	}
	
	@Override
	public void signalEvent(String type, Object event) {
		if ("signal".equals(type)) {
			String connectionType = NodeImpl.CONNECTION_DEFAULT_TYPE;
			if (event instanceof String) {
				connectionType = (String) event;
			}
			removeEventListeners();
			triggerCompleted(connectionType, true);
		} else {
			super.signalEvent(type, event);
		}
	}
	
	private void addTriggerListener() {
		getProcessInstance().addEventListener("signal", this, false);
	}

    @Override
	public void addEventListeners() {
        super.addEventListeners();
        addTriggerListener();
    }
    
    @Override
	public void removeEventListeners() {
        super.removeEventListeners();
        getProcessInstance().removeEventListener("signal", this, false);
    }

}
