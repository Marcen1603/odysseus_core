package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

public interface ISymbolTableOperation<T> {
	/**
	 * Definiert die Schnittstelle zum Ausführen der Operation auf der
	 * Symboltabelle. Da eine Deep-Copy der Symboltabellenwerte beim ertsellen
	 * einer Verzweigung nicht möglich ist (für {@link java.lang.Object} ist die
	 * Methode clone() als protected deklariert), muss jede Implementierung
	 * dieser Methode sicherstellen, dass das übergebene Objekt oldValue nicht
	 * verändert wird. Implementierungen dieser Methode dürfen daher nur die
	 * unveränderte Referenz von oldValue oder ein neu erzeugtes Objekt
	 * zurückgeben!
	 * 
	 * TODO: Evtl. nachdenken, ob man T nicht entsprechend einschr�nkt
	 * 
	 * @param oldValue
	 *            Der alte Wert aus der Symboltabelle. Darf innerhalb der
	 *            Methode nicht verändert werden!
	 * @param eventValue
	 *            Der Attributwert aus dem Event.
	 * @return Ein neues Objekt, das den berechneten Wert darstellt. Unter
	 *         Umständen kann auch eine Referenz auf das unveränderte Objekt
	 *         oldValue zurückgegeben werden.
	 */
	public Object execute(T oldValue, T eventValue);
	public String toString(String indent);
	public String getName();
}
