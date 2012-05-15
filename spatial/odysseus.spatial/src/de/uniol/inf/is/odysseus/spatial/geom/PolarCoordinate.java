package de.uniol.inf.is.odysseus.spatial.geom;

import java.io.Serializable;

public class PolarCoordinate implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2570425136506579259L;
	public Double r;
	public Double a;

	public PolarCoordinate(Double r, Double a) {
		this.r = r;
		this.a = a;
	}

	@Override
	public PolarCoordinate clone() {
		return new PolarCoordinate(r, a);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PolarCoordinate other = (PolarCoordinate) obj;
		if (a == null) {
			if (other.a != null) {
				return false;
			}
		} else if (!a.equals(other.a)) {
			return false;
		}
		if (r == null) {
			if (other.r != null) {
				return false;
			}
		} else if (!r.equals(other.r)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "(" + r + ", " + a + "°)";
	}
	
	
}
