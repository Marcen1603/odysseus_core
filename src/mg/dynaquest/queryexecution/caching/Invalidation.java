/**
 * 
 */
package mg.dynaquest.queryexecution.caching;

/**
 * 
 * Dieses Interface soll von Klassen implementiert werden, welche die
 * Invalidierung von semantischen Regionen vornehmen
 * 
 * @author Tobias Hesselmann
 * 
 */
public interface Invalidation {

	/**
	 * Löscht alle ungültigen semantischen Regionen im Cache Memory
	 */
	abstract void invalidateSemanticRegions();

}
