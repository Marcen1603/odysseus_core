package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

/**
 * Schnittstelle, welches ein Schlüsselwort für den Preparser darstellt. Wird
 * vom QueryTextParser verwendet. Dadurch kann der Nutzer eigene Befehle
 * definieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPreParserKeyword {

	/**
	 * Diese Methode gibt zurück, ob der Befehl mit den gegebenen Parametern,
	 * welche vom PreParser gegeben wurde, ausgeführt werden <b>könnte</b>. Wenn
	 * alles ok ist, sollte die Funktion ohne <code>Exception</code> verlassen
	 * werden. Im Fehlerfall sollte eine <code>QueryTextParseException</code>
	 * geworfen werden. Damit wird der Test des ganzen Querytextes gestoppt und
	 * der Fehler wird angezeigt.
	 * <p>
	 * Diese Funktion wird vom PreParser aufgerufen. Nutzer sollten einen
	 * expliziten Aufruf vermeiden.
	 * 
	 * @param parser
	 *            Der Parser, welcher diesen Befehl im Querytext gelesen und
	 *            diese Methode aufgerufen hat. Ist garantiert nicht null.
	 * @param value
	 *            Der zugehörige Parameter zu diesem Befehl. Die Verarbeitung
	 *            wird der Methode überlassen. Ist garantiert nicht null, kann
	 *            aber ein String der Länge 0 sein.
	 * 
	 * @throws QueryTextParseException
	 *             Wird geworfen, wenn der Befehl mit den gegebenen Parametern
	 *             nicht ausführbar wäre.
	 */
	public void validate(QueryTextParser parser, String parameter) throws QueryTextParseException;

	/**
	 * Methode, worin der Befehl ausgeführt wird. Diese Methode wird vom
	 * PreParser aufgerufen und sollte nicht vom Nutzer explizit aufgerufen
	 * werden. Es wird der verarbeitende Parser sowie der Parameter, welcher mit
	 * dem Befehl angegeben wurde, angegegeben. Beide Parameter werden
	 * garantiert nicht null sein. Die Verarbeitung dieser wird der Methode
	 * überlassen. Der Parser unternimmt keine ersten Verarbeitungsschritte.
	 * <p>
	 * Diese Methode muss erfolgreich durchgeführt werden können, wenn die
	 * Methode <code>validate()</code> für die gleichen Parameter erfolgreich
	 * durchgeführt wurde.
	 * 
	 * @param parser
	 *            Der Parser, welcher diesen Befehl im Querytext gelesen und
	 *            diese Methode aufgerufen hat. Ist garantiert nicht null.
	 * @param value
	 *            Der zugehörige Parameter zu diesem Befehl. Die Verarbeitung
	 *            wird der Methode überlassen. Ist garantiert nicht null, kann
	 *            aber ein String der Länge 0 sein.
	 * 
	 * @throws QueryTextParseException
	 *             Wird geworfen, wenn der Befehl mit den gegebenen Parametern
	 *             nicht ausführbar wäre.
	 */
	public void execute(QueryTextParser parser, String parameter) throws QueryTextParseException;
}
