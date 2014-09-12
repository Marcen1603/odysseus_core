package de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

/**
 * An access to the DDC, which provides concrete getters. Each getter performs a
 * key making and a call of
 * {@link IDistributedDataContainer#get(de.uniol.inf.is.odysseus.peer.ddc.DDCKey)}
 * .
 * 
 * @author Michael Brand
 *
 */
public class SoccerDDCAccess extends AbstractSportsDDCAccess {
	
	/**
	 * The first key for properties of goalareas (boundaries).
	 */
	private static final String KEY_GOALAREA_PREFIX = "goalarea.";

	/**
	 * The key for the lowest x coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_XMIN = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "xmin" });

	/**
	 * The key for the highest x coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_XMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "xmax" });

	/**
	 * The key for the y coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_Y = new DDCKey(new String[] {
			SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "y" });

	/**
	 * The key for the highest z coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_ZMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "zmax" });

	/**
	 * The key for the lowest x coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_XMIN = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"xmin" });

	/**
	 * The key for the highest x coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_XMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"xmax" });

	/**
	 * The key for the y coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_Y = new DDCKey(new String[] {
			SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right", "y" });

	/**
	 * The key for the highest z coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_ZMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"zmax" });
	
	/**
	 * The lowest x coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,xmin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,xmin" could no be cast to Double.
	 */
	public static double getGoalareaLeftXMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_XMIN)).doubleValue();
		
	}
	
	/**
	 * The highest x coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,xmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,xmax" could no be cast to Double.
	 */
	public static double getGoalareaLeftXMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_XMAX)).doubleValue();
		
	}
	
	/**
	 * The y coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,y" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,y" could no be cast to Double.
	 */
	public static double getGoalareaLeftY() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_Y)).doubleValue();
		
	}
	
	/**
	 * The highest z coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,zmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,zmax" could no be cast to Double.
	 */
	public static double getGoalareaLeftZMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_ZMAX)).doubleValue();
		
	}
	
	/**
	 * The lowest x coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,xmin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,xmin" could no be cast to Double.
	 */
	public static double getGoalareaRightXMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_XMIN)).doubleValue();
		
	}
	
	/**
	 * The highest x coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,xmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,xmax" could no be cast to Double.
	 */
	public static double getGoalareaRightXMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_XMAX)).doubleValue();
		
	}
	
	/**
	 * The y coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,y" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,y" could no be cast to Double.
	 */
	public static double getGoalareaRightY() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_Y)).doubleValue();
		
	}
	
	/**
	 * The highest z coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,zmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,zmax" could no be cast to Double.
	 */
	public static double getGoalareaRightZMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_ZMAX)).doubleValue();
		
	}

}