package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Collections;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;

public class ExistenceSweepArea<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends JoinTISweepArea<T>{

	public void insert(T object){
		synchronized(this.elements){
			this.elements.add(object);
			MetadataComparator<ITimeInterval> comp = new MetadataComparator<ITimeInterval>();
			Collections.sort(this.elements, comp);
		}
	}
}
