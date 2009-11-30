package de.uniol.inf.is.odysseus.cep.epa;

/**
 * Einfache Datenstruktur, um eine verkettete Liste aller konsumierter Events zu
 * realisieren
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class MatchedEvent<R> {

	/**
	 * Referenz auf das zuvor konsumierte Event. Ist null für das erste
	 * konsumierte Event und nicht null für alle anderen.
	 */
	private MatchedEvent<R> previous;
	/**
	 * Das eigentliche Event, das zwischengespeichert werden soll
	 */
	private R event;

	/**
	 * Erzegt ein neues Listenelement.
	 * 
	 * @param previous
	 *            Referenz auf das vorherige Listenelement oder null, wenn das
	 *            neue Element das erste in der Liste sein soll.
	 * @param event
	 *            Das Event, das konsumiert und in die Liste eingehängt wird.
	 */
	public MatchedEvent(MatchedEvent<R> previous, R event) {
		this.event = event;
		this.previous = previous;
	}

	/**
	 * Liefert das vorherige Listenelement
	 * 
	 * @return Das vorherige Lsitenelement oder null, wenn es sich um das erste
	 *         Element der Liste handelt.
	 */
	public MatchedEvent<R> getPrevious() {
		return previous;
	}

	/**
	 * Liefert das im Listenelement gespeicherte Event.
	 * 
	 * @return Das im Listenelement gespeicherte Event.
	 */
	public R getEvent() {
		return event;
	}
	
	/**
	 * Gibt eine tiefe Kopie des MatchedEvent-Objekts zurück.
	 */
	@Override
	public MatchedEvent<R> clone() {
		MatchedEvent<R> newPrevious = null;
		if (this.previous != null) {
			newPrevious = this.previous.clone();
		}
		return new MatchedEvent<R>(newPrevious, this.event);
	}

}
