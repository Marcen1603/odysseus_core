package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class IntervalCanvasDataContainer {

	private final List<IStreamObject<? extends ITimeInterval>> storedElements = Lists.newArrayList();
	
	public IntervalCanvasDataContainer() {
		
	}
	
	public void add( IStreamObject<? extends ITimeInterval> newElement ) {
		synchronized( storedElements ) {
			storedElements.add(newElement);
		}
	}

	public void clear() {
		synchronized(storedElements ) {
			storedElements.clear();
		}
	}
	
	public List<IStreamObject<? extends ITimeInterval>> getElements() {
		// TODO: zu teuer!
		return Lists.newArrayList(storedElements); 
	}
}
