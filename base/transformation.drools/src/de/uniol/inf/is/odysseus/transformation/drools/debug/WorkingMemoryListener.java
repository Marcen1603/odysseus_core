package de.uniol.inf.is.odysseus.transformation.drools.debug;

import org.drools.event.ObjectInsertedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.ObjectUpdatedEvent;
import org.drools.event.WorkingMemoryEventListener;

public class WorkingMemoryListener implements WorkingMemoryEventListener {

	private static String PREFIX_WM = "Working Memory: ";	
	private ILogger logger;
	
	public WorkingMemoryListener(ILogger logger){
		this.logger = logger;
	}
	
	
	@Override
	public void objectInserted(ObjectInsertedEvent e) {			
		logger.printlog(PREFIX_WM+"Object inserted: "+e.getObject());
	}

	@Override
	public void objectRetracted(ObjectRetractedEvent e) {
		logger.printlog(PREFIX_WM+"Object retracted: "+e.getOldObject());
		
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent e) {
		logger.printlog(PREFIX_WM+"Object updated: New: "+e.getObject()+" | Old: "+e.getOldObject());		
	}

}
