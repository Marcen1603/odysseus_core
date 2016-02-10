package de.uniol.inf.is.odysseus.core.expression;

public class VarHelper {
	private final int pos;
	private final int schema;
	private final int objectPosToUse;
	private boolean isThis = false;

	public VarHelper(int pos, int objectPosToUse) {
		super();
		this.pos = pos;
		schema = -1;
		this.objectPosToUse = objectPosToUse;
	}
	
	public VarHelper(int pos, int schema, int objectPosToUse) {
		super();
		this.pos = pos;
		this.schema = schema;
		this.objectPosToUse = objectPosToUse;
	}
	
	public void setThis(boolean isThis) {
		this.isThis = isThis;
	}
	
	public boolean isThis() {
		return isThis;
	}

	@Override
	public String toString() {
		return pos + " " + getObjectPosToUse();
	}

	public int getPos() {
		return pos;
	}
	
	public int getSchema(){
		return schema;
	}

	/**
	 * @return the objectPosToUse
	 */
	public int getObjectPosToUse() {
		return objectPosToUse;
	}
}