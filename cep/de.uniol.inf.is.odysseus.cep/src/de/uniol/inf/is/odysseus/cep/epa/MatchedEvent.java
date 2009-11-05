package de.uniol.inf.is.odysseus.cep.epa;

/**
 * Einfache Datenstruktur, um eine verkettete Liste aller konsumierter Events zu
 * realisieren
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class MatchedEvent {

	/**
	 * Referenz auf das zuvor konsumierte Event. Ist null für das erste
	 * konsumierte Event und nicht null für alle anderen.
	 */
	private MatchedEvent previous;
	/**
	 * Das eigentliche Event, das zwischengespeichert werden soll
	 */
	private Object event;

	/**
	 * Erzegt ein neues Listenelement.
	 * 
	 * @param previous
	 *            Referenz auf das vorherige Listenelement oder null, wenn das
	 *            neue Element das erste in der Liste sein soll.
	 * @param event
	 *            Das Event, das konsumiert und in die Liste eingehängt wird.
	 */
	public MatchedEvent(MatchedEvent previous, Object event) {
		this.event = event;
		this.previous = previous;
	}

	/**
	 * Liefert das vorherige Listenelement
	 * 
	 * @return Das vorherige Lsitenelement oder null, wenn es sich um das erste
	 *         Element der Liste handelt.
	 */
	public MatchedEvent getPrevious() {
		return previous;
	}

	/**
	 * Liefert das im Listenelement gespeicherte Event.
	 * 
	 * @return Das im Listenelement gespeicherte Event.
	 */
	public Object getEvent() {
		return event;
	}
	
	/**
	 * Gibt eine tiefe Kopie des MatchedEvent-Objekts zurück.
	 */
	@Override
	public MatchedEvent clone() {
		MatchedEvent newPrevious = null;
		if (this.previous != null) {
			newPrevious = this.previous.clone();
		}
		return new MatchedEvent(newPrevious, this.event);
	}

}
