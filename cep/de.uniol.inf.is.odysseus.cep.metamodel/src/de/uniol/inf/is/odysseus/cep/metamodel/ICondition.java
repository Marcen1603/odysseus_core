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
	public boolean evaluate(String eventType);
	public boolean evaluate(int eventTypePort);
	public void appendAND(String fullExpression);
	public void appendOR(String fullExpression);
	public void addAssignment(String attribute, String fullexpression);
	public void negate();
	public boolean isNegate();

	public boolean doEventTypeChecking();
	public void setEventTypeChecking(boolean eventTypeChecking);
	/**
	 * Set Type of Events that can be processed by this Transition (only one)
	 * @param type
	 * @return
	 */
	public void setEventType(String type);
	public String getEventType();
	
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
	 * @return start + windowsize < current;
	 */
	public boolean checkTime(long start, long current, long windowsize);
	boolean checkEventType(String eventType);
	boolean checkEventTypeWithPort(int port);	

}
