/**
 * 
 */
package mg.dynaquest.queryexecution.caching;

import mg.dynaquest.queryexecution.caching.memory.SemanticRegion;

/**
 * @author Tobias Hesselmann
 * 
 */
public interface Replacement {

	/**
	 * 
	 * @return Semantische Region, die anhand der verwendeten
	 *         Verdrängungsstrategie als Opfer auserkoren wurde
	 */
	abstract SemanticRegion getSemanticRegionToReplace();
}
