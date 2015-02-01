package de.uniol.inf.is.odysseus.sports.sportsql.physicaloperator;

import java.io.Serializable;
import java.util.HashMap;

public class SportsHeatMap implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -112246014045130024L;
	
	public HashMap<Integer, int[][]> heatmaps = new HashMap<Integer, int[][]>();
	public HashMap<Integer, Integer> maxHeatValues = new HashMap<Integer, Integer>(25);

}
