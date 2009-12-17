package de.uniol.inf.is.odysseus.cep.metamodel;

/**
 * Diese Klasse kapselt die Transitionsbedingung. 
 * 
 * @author Thomas Vogelgesang, Marco Grawunder
 * 
 */
abstract public class AbstractCondition implements ICondition{

	/**
	 * Textuelle Repraesentation der Transitionsbedingung. 
	 */
	private String label;
	private String eventType;
	
	public AbstractCondition() {
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Liefert die textuelle Repraesentation der Transitionsbedingung.
	 * 
	 * @return Transitionsbedingung als String.
	 */
	public String getLabel() {
		return label;
	}
	
	public String getEventType() {
		return eventType;
	}
	
	@Override
	public void setEventType(String type) {		
		eventType = type;
	}
	
	@Override
	public boolean checkEventType(String eventType) {
		boolean ret = true;
		if (this.eventType != null) {
			ret = isNegate()?!eventType.equals(this.eventType):eventType.equals(this.eventType);
		}

		//System.out.println("checkEventType "+eventType+" "+this.eventType+" --> "+ret);
		
		return ret;
	}
	
	@Override
	public boolean checkTime(long start, long current, long windowsize) {
		boolean ret = isNegate()?current >= (start+windowsize):current<(start+windowsize);
		//System.out.println("checkTime "+start+" "+current+" "+windowsize+" --> "+ret);
		return ret;
	}
	
	@Override
	abstract public boolean isNegate();
	
	@Override
	public String toString() {
		return this.getLabel();
	}

}
