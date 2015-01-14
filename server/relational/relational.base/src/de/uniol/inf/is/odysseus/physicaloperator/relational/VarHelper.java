package de.uniol.inf.is.odysseus.physicaloperator.relational;

public class VarHelper {
	public int pos;
	public int objectPosToUse;

	public VarHelper(int pos, int objectPosToUse) {
		super();
		this.pos = pos;
		this.objectPosToUse = objectPosToUse;
	}

	@Override
	public String toString() {
		return pos + " " + objectPosToUse;
	}
}