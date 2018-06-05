package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Objects;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.CachePO;

public class CacheTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends CachePO<T> {

	final private long timesize;

	public CacheTIPO(TimeValueItem timesize, TimeUnit baseTimeUnit) {
		super(-1);
		
		if (baseTimeUnit == null) {
			baseTimeUnit = TimeUnit.MILLISECONDS;
		} 
		
		this.timesize = baseTimeUnit.convert(timesize.getTime(), timesize.getUnit());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void cacheInvalidation(List<IStreamable> cache, T object) {
		IStreamable elem = cache.get(0);
		PointInTime trigger = object.getMetadata().getStart();
		PointInTime limit = trigger.minus(timesize);
		while (elem != null) {
			boolean remove = false;
			if (elem.isPunctuation()) {
				if (((IPunctuation)elem).getTime().before(limit)){
					remove = true;
				}
			}else {
				if (((T)elem).getMetadata().getStart().before(limit)){
					remove = true;
				}
			}
			if (remove) {
				cache.remove(0);
				elem = cache.get(0);
			}else {
				elem = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof CacheTIPO)) {
			return false;
		}
		if (!(Objects.equal(this.timesize, ((CacheTIPO<K,T>)ipo).timesize))){
			return false;
		}
		return super.isSemanticallyEqual(ipo);
	}
	

}
