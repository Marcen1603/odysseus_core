package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;


/**
 * Abstrakte Oberklasse, die die Menge der Symboltabellen-Operatoren definiert.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public abstract class AbstractSymbolTableOperation<T, E> implements ISymbolTableOperation<T, E> {
	
	/**
	 * Liefert den Namen der Operation
	 * @return Der Name der Operation
	 */
	final public String getName() {
		return this.getClass().getSimpleName();
	}

}
