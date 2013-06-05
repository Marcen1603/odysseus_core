package de.uniol.inf.is.odysseus.p2p_new.lb.physicaloperator;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractTimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.FastArrayList;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;

// TODO javaDoc M.B.
public class DistributionMergeSweepArea<T extends IStreamObject<? extends ITimeInterval>> 
		extends AbstractTimeIntervalSweepArea<T> 
			implements Comparable<DistributionMergeSweepArea<T>>, ITimeIntervalSweepArea<T> {
	

	public DistributionMergeSweepArea() {
		
		super(new FastArrayList<T>(), new MetadataComparator<ITimeInterval>());
		// TODO Need a new Comparator? M.B.
		
	}
	
	public DistributionMergeSweepArea(DistributionMergeSweepArea<T> area) 
			throws InstantiationException, IllegalAccessException {
		
		super(area);
		
	}
	
	@Override
	public AbstractTimeIntervalSweepArea<T> clone() {
		
		try {
			
			return new DistributionMergeSweepArea<T>(this);
			
		} catch(InstantiationException | IllegalAccessException e) {
			
			e.printStackTrace();
			throw new RuntimeException("Clone error");
			
		}
		
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		
		synchronized (getElements()) {
			
			Iterator<T> li = getElements().iterator();
			while(li.hasNext()) {
				
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// können (also davor liegen)
				if(s_hat.getMetadata().getEnd().beforeOrEquals(time))
					li.remove();
				else break;
				
			}
		}		
		
	}

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointInTime getMaxTs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointInTime getMinTs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(DistributionMergeSweepArea<T> o) {
		// TODO Auto-generated method stub
		return 0;
	}

}