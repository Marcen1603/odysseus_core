package de.uniol.inf.is.odysseus.sla;

/**
 * Defines the scope of the sla.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class Scope {

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Scope (").append(this.getClass().getSimpleName())
				.append(")");

		return sb.toString();
	}
	
	public abstract boolean thresholdIsMin();

}
