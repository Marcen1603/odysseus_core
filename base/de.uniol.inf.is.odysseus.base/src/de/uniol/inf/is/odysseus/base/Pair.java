package de.uniol.inf.is.odysseus.base;

public class Pair<E1, E2> {

	private E1 e1;
	private E2 e2;
	
	public Pair(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public E1 getE1() {
		return e1;
	}

	public void setE1(E1 e1) {
		this.e1 = e1;
	}

	public E2 getE2() {
		return e2;
	}

	public void setE2(E2 e2) {
		this.e2 = e2;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((e1 == null) ? 0 : e1.hashCode());
		result = PRIME * result + ((e2 == null) ? 0 : e2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair<?,?> other = (Pair<?,?>) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!e1.equals(other.e1))
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		} else if (!e2.equals(other.e2))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("(");		
		ret.append(e1).append(",");
		ret.append(e2).append(")");
		return ret.toString();
	}

}
