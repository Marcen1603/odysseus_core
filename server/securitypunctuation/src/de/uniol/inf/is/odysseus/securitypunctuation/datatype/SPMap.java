package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class SPMap<K extends ITimeInterval, T extends IStreamObject<K>> {

	List<Map<AbstractSecurityPunctuation, List<T>>> spMap;
	private static final Logger LOG = LoggerFactory.getLogger(SPMap.class);

	public SPMap() {
		this.spMap = new ArrayList<Map<AbstractSecurityPunctuation, List<T>>>();
		this.spMap.add(new HashMap<AbstractSecurityPunctuation, List<T>>());
		this.spMap.add(new HashMap<AbstractSecurityPunctuation, List<T>>());
	}

	public void addSP(AbstractSecurityPunctuation sp, int port) {
		if (!this.spMap.isEmpty() && !this.spMap.get(port).containsKey(sp)) {
			this.spMap.get(port).put(sp, new ArrayList<T>());
		}
	}

	public void addValue(AbstractSecurityPunctuation sp, T object, int port) {
		if (!this.spMap.isEmpty() && this.spMap.get(port).containsKey(sp)) {
			if (this.spMap.get(port).containsKey(sp)) {
				this.spMap.get(port).get(sp).add(object);
			}
		}
	}

	public AbstractSecurityPunctuation invalidate(AbstractSecurityPunctuation sp, PointInTime ts, int port) {
		AbstractSecurityPunctuation removedSPs = null;
		List<T> objectsToRemove = new ArrayList<>();
		if (!this.spMap.isEmpty() && !this.spMap.get(port).isEmpty() && this.spMap.get(port).containsKey(sp)) {
			for (T obj : this.spMap.get(port).get(sp)) {
				if (obj.getMetadata().getEnd().before(ts)) {
					objectsToRemove.add(obj);

				}
			}
			this.spMap.get(port).get(sp).removeAll(objectsToRemove);
			if (this.spMap.get(port).get(sp).isEmpty()) {
				removedSPs = sp;
				this.spMap.get(port).remove(sp);
			}

		}
		return removedSPs;
	}

	public List<Map<AbstractSecurityPunctuation, List<T>>> getSpMap() {
		return spMap;
	}

	public void setSpMap(List<Map<AbstractSecurityPunctuation, List<T>>> spMap) {
		this.spMap = spMap;
	}

	public List<AbstractSecurityPunctuation> getMatchingSPs(T object, int port) {
		List<AbstractSecurityPunctuation> matchingSPs = new ArrayList<>();
		for (Map.Entry<AbstractSecurityPunctuation, List<T>> entry : this.spMap.get(port).entrySet()) {
			if (entry.getValue().contains(object)) {
				matchingSPs.add(entry.getKey());
			}
			// for(T value:entry.getValue()){
			// if(value.equals(object)){
			//
			// }
			// }
		}

		return matchingSPs;

	}

}
