package de.uniol.inf.is.odysseus.core.server.ac;

/**
 * Schnittstelle, welche von Klassen implementiert werden muss, damit sie über
 * Überlastungen in der {@link IAdmissionControl} informiert werden. Sie müssen
 * sind mittels der Methode <code>registerListener</code> der Klasse
 * {@link IAdmissionControl} registrieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IAdmissionListener {

	/**
	 * Wird aufgerufen, wenn eine Überlastung erkannt wurde. Eine Überlastung
	 * tritt auf, wenn die Gesamtkosten des Ausführungsplans die Maximalkosten
	 * übersteigen. Diese Methode wird nur dann aufgerufen, wenn die
	 * AdmissionControl Kostenschätzungen vornimmt.
	 * 
	 * @param sender
	 *            Instanz von {@link IAdmissionControl}, welcher die Überlastung
	 *            entdeckt hat.
	 */
	public void overloadOccured(IAdmissionControl sender);
}
