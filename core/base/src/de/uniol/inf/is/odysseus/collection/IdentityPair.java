package de.uniol.inf.is.odysseus.collection;

public class IdentityPair<E1, E2> {

	protected E1 e1;
	protected E2 e2;
	
	public IdentityPair(E1 e1, E2 e2) {
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
	public String toString() {
		StringBuffer ret = new StringBuffer("(");		
		ret.append(e1).append(",");
		ret.append(e2).append(")");
		return ret.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
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
		IdentityPair other = (IdentityPair) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!(e1 == other.e1)) // Identity!
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		} else if (!(e2 == e2)) // Identity!
			return false;
		return true;
	}
	
	
}
