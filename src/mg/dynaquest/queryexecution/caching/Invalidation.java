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
	 * L�scht alle ung�ltigen semantischen Regionen im Cache Memory
	 */
	abstract void invalidateSemanticRegions();

}
