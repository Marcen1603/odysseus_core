package de.uniol.inf.is.odysseus.cep.metamodel;


public interface ICondition extends IExpression{

	/**
	 * Setzt die Transitionsbedingung.
	 * 
	 * @param label
	 *            Die neue Transitionsbedingung als String. Darf nur (nach dem
	 *            internen Namensschema) gültige Variablennamen beinhalten.
	 *         	  Wird null
	 *            übergeben, so wird der Ausdruck auf 1 (true) gesetzt. Die
	 *            Transition wird somit zu einem Epsilon-Übergang.
	 */

	public void setLabel(String label);
	public String getLabel();
	public String toString(String indent);
	public boolean evaluate();
	public void append(String fullExpression);
	public void negate();
	public boolean isNegate();
	/**
	 * Check if eventType is applicable for Transition 
	 * @param type
	 * @return !isNegate() && eventType == type;
	 */
	public boolean checkEventType(String eventType);
	/**
	 * Same as checkEventType but using port
	 * @param port
	 * @return !isNegate() && eventTypePort == port;
	 */
	public boolean checkEventTypeWithPort(int port);
	/**
	 * Set Type of Events that can be processed by this Transition (only one possible?)
	 * @param type
	 * @return
	 */
	public void setEventType(String type);
	
	/**
	 * Set InputPort of Events that can be processed by this Transition 
	 * @param port
	 * @return
	 */
	void setEventPort(int eventPort);
	
	/**
	 * 
	 * @param start
	 * @param current
	 * @param windowsize
	 * @return !isNegate() && start + windowsize < current;
	 */
	public boolean checkTime(long start, long current, long windowsize);
	

}
