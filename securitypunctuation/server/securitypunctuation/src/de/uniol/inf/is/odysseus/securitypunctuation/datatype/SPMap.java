package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class SPMap<K extends ITimeInterval, T extends IStreamObject<K>> {

	List<Map<ISecurityPunctuation, List<T>>> spMap;


	public SPMap() {
		this.spMap = new ArrayList<Map<ISecurityPunctuation, List<T>>>();
		this.spMap.add(new HashMap<ISecurityPunctuation, List<T>>());
		this.spMap.add(new HashMap<ISecurityPunctuation, List<T>>());
	}

	public void addSP(ISecurityPunctuation sp, int port) {
		if (!this.spMap.isEmpty() && !this.spMap.get(port).containsKey(sp)) {
			this.spMap.get(port).put(sp, new ArrayList<T>());
		}
	}

	public void addValue(ISecurityPunctuation sp, T object, int port) {
		if (!this.spMap.isEmpty() && this.spMap.get(port).containsKey(sp)) {
				this.spMap.get(port).get(sp).add(object);
			
		}
	}

	
	public ISecurityPunctuation invalidate(ISecurityPunctuation sp, PointInTime ts, int port) {
		ISecurityPunctuation removedSPs = null;
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

	public List<Map<ISecurityPunctuation, List<T>>> getSpMap() {
		return spMap;
	}

	public void setSpMap(List<Map<ISecurityPunctuation, List<T>>> spMap) {
		this.spMap = spMap;
	}

	public List<ISecurityPunctuation> getMatchingSPs(T object, int port) {
		List<ISecurityPunctuation> matchingSPs = new ArrayList<>();
		for (Map.Entry<ISecurityPunctuation, List<T>> entry : this.spMap.get(port).entrySet()) {
			if (entry.getValue().contains(object)) {
				matchingSPs.add(entry.getKey());
			}
		
		}

		return matchingSPs;

	}

}
