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
	private static final DDCKey KEY_GOALAREA_LEFT_YMIN = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "ymin" });

	/**
	 * The key for the highest x coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_YMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "ymax" });

	/**
	 * The key for the y coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_X = new DDCKey(new String[] {
			SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "x" });

	/**
	 * The key for the highest z coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_ZMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "left", "zmax" });

	/**
	 * The key for the lowest x coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_YMIN = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"ymin" });

	/**
	 * The key for the highest x coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_YMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"ymax" });

	/**
	 * The key for the y coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_X = new DDCKey(new String[] {
			SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right", "x" });

	/**
	 * The key for the highest z coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_ZMAX = new DDCKey(
			new String[] { SoccerDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"zmax" });
	
	private static final DDCKey LEFT_GOAL_TEAM_ID = new DDCKey("leftgoalteamid");
	private static final DDCKey RIGHT_GOAL_TEAM_ID = new DDCKey("rightgoalteamid");
	
	/**
	 * The lowest y coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,ymin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,ymin" could no be cast to Double.
	 */
	public static double getGoalareaLeftYMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_YMIN)).doubleValue();
		
	}
	
	/**
	 * The highest y coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,ymax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,ymax" could no be cast to Double.
	 */
	public static double getGoalareaLeftYMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_YMAX)).doubleValue();
		
	}
	
	/**
	 * The x coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,x" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,x" could no be cast to Double.
	 */
	public static double getGoalareaLeftX() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_X)).doubleValue();
		
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
	 * The lowest y coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,ymin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,ymin" could no be cast to Double.
	 */
	public static double getGoalareaRightYMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_YMIN)).doubleValue();
		
	}
	
	/**
	 * The highest y coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,ymax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,ymax" could no be cast to Double.
	 */
	public static double getGoalareaRightYMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_YMAX)).doubleValue();
		
	}
	
	/**
	 * The x coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,x" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,x" could no be cast to Double.
	 */
	public static double getGoalareaRightX() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_X)).doubleValue();
		
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
	
	public static int getLeftGoalTeamId() throws MissingDDCEntryException, NumberFormatException {
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		return Integer.valueOf(AbstractSportsDDCAccess.ddc.getValue(LEFT_GOAL_TEAM_ID));
	}
	
	public static int getRightGoalTeamId() throws MissingDDCEntryException, NumberFormatException {
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		return Integer.valueOf(AbstractSportsDDCAccess.ddc.getValue(RIGHT_GOAL_TEAM_ID));
	}
	
}