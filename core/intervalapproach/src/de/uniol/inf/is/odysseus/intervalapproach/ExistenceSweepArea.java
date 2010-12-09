package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Collections;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetadataComparator;

public class ExistenceSweepArea<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends JoinTISweepArea<T>{

	@Override
	public void insert(T object){
		synchronized(this.elements){
			this.elements.add(object);
			MetadataComparator<ITimeInterval> comp = new MetadataComparator<ITimeInterval>();
			Collections.sort(this.elements, comp);
		}
	}
}
