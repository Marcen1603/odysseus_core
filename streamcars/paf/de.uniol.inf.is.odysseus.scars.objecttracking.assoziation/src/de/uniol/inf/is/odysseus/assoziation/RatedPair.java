package de.uniol.inf.is.odysseus.assoziation;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

public class RatedPair {
	
	private MVRelationalTuple oldObject;
	private MVRelationalTuple newObject;
	private int rating;
	
	public RatedPair(MVRelationalTuple oldObject, MVRelationalTuple newObject) {
		this.oldObject = oldObject;
		this.newObject = newObject;
		this.rating = 0;
	}

	public MVRelationalTuple getOldObject() {
		return oldObject;
	}

	public void setOldObject(MVRelationalTuple oldObject) {
		this.oldObject = oldObject;
	}

	public MVRelationalTuple getNewObject() {
		return newObject;
	}

	public void setNewObject(MVRelationalTuple newObject) {
		this.newObject = newObject;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
