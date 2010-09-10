package org.drools.event.rule.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.common.InternalWorkingMemory;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.FactHandle;

public class ObjectRetractedEventImpl  extends WorkingMemoryEventImpl implements ObjectRetractedEvent {
    private FactHandle factHandle;
    private Object oldbOject;
    
    public ObjectRetractedEventImpl( org.drools.event.ObjectRetractedEvent event) {
        super( ((InternalWorkingMemory) event.getWorkingMemory() ).getKnowledgeRuntime(), event.getPropagationContext() );
        factHandle = event.getFactHandle();
        oldbOject = event.getOldObject();
    }
    
    @Override
	public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal( out );
        out.writeObject( factHandle );
        out.writeObject( oldbOject );
    }
    
    @Override
	public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        super.readExternal( in );
        this.factHandle = ( FactHandle ) in.readObject();
        this.oldbOject = in.readObject();
    }    
    
    public FactHandle getFactHandle() {
        return this.factHandle;
    }

    public Object getOldObject() {
        return this.oldbOject;
    }
}
