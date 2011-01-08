package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * Class to calculate the manhattan distance between an IClusteringObject and an
 * AbstractCluster represented by its center.
 * 
 * @author Kolja Blohm
 *
 */

public class ManhattanDistance<T extends IMetaAttribute> extends MinkowsiDistance<T> {

	/**
	 * Creates a new ManhattanDistance
	 */
	public ManhattanDistance() {
		super(1);
		
	}


}
