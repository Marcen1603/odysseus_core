package de.uniol.inf.is.odysseus.sports.sportsql.parser.helper;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
public class SportsDDCAccess {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(SportsDDCAccess.class);

	/**
	 * The first key for field properties (boundaries).
	 */
	private static final String KEY_FIELD_PREFIX = "field";

	/**
	 * The key for the lowest x coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_XMIN = new DDCKey(new String[] {
			SportsDDCAccess.KEY_FIELD_PREFIX, "xmin" });

	/**
	 * The key for the highest x coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_XMAX = new DDCKey(new String[] {
			SportsDDCAccess.KEY_FIELD_PREFIX, "xmax" });

	/**
	 * The key for the lowest y coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_YMIN = new DDCKey(new String[] {
			SportsDDCAccess.KEY_FIELD_PREFIX, "ymin" });

	/**
	 * The key for the highest y coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_YMAX = new DDCKey(new String[] {
			SportsDDCAccess.KEY_FIELD_PREFIX, "ymax" });

	/**
	 * The first key for properties of goalareas (boundaries).
	 */
	private static final String KEY_GOALAREA_PREFIX = "goalarea.";

	/**
	 * The key for the lowest x coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_XMIN = new DDCKey(
			new String[] { SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "xmin" });

	/**
	 * The key for the highest x coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_XMAX = new DDCKey(
			new String[] { SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "xmax" });

	/**
	 * The key for the y coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_Y = new DDCKey(new String[] {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "y" });

	/**
	 * The key for the highest z coordinate of the left goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_LEFT_ZMAX = new DDCKey(
			new String[] { SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "zmax" });

	/**
	 * The key for the lowest x coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_XMIN = new DDCKey(
			new String[] { SportsDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"xmin" });

	/**
	 * The key for the highest x coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_XMAX = new DDCKey(
			new String[] { SportsDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"xmax" });

	/**
	 * The key for the y coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_Y = new DDCKey(new String[] {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "right", "y" });

	/**
	 * The key for the highest z coordinate of the right goalarea.
	 */
	private static final DDCKey KEY_GOALAREA_RIGHT_ZMAX = new DDCKey(
			new String[] { SportsDDCAccess.KEY_GOALAREA_PREFIX + "right",
					"zmax" });
	
	/**
	 * The key for a list of all sensor_ids.
	 */
	private static final DDCKey KEY_SENSOR_LIST = new DDCKey("sensoridlist");
	
	/**
	 * The key for the separator within {@link #KEY_SENSOR_LIST}.
	 */
	private static final DDCKey KEY_SENSOR_LIST_SEPARATOR = new DDCKey("sensoridseparator");

	/**
	 * The first key for properties of sensor metadata.
	 */
	private static final String KEY_SENSOR_PREFIX = "sensorid.";

	/**
	 * The key for the entity_id for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	private static DDCKey buildKeyEntityId(int sensor_id) {

		return new DDCKey(new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"entity_id" });

	}

	/**
	 * The key for the entity for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	private static DDCKey buildKeyEntity(int sensor_id) {

		return new DDCKey(new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"entity" });

	}

	/**
	 * The key for the remark for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	private static DDCKey buildKeyRemark(int sensor_id) {

		return new DDCKey(new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"remark" });

	}

	/**
	 * The key for the team_id for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	private static DDCKey buildKeyTeamId(int sensor_id) {

		return new DDCKey(new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"team_id" });

	}

	/**
	 * The representation of null values.
	 */
	private static String VALUE_NULL = "<null>";

	/**
	 * The DDC.
	 */
	private static IDistributedDataContainer ddc;

	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		SportsDDCAccess.ddc = ddc;
		SportsDDCAccess.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (SportsDDCAccess.ddc == ddc) {

			SportsDDCAccess.ddc = null;
			SportsDDCAccess.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The lowest x coordinate of the field.
	 * @throws MissingDDCEntryException if "field,xmin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,xmin" could no be cast to Double.
	 */
	public static double getFieldXMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_FIELD_XMIN)).doubleValue();
		
	}
	
	/**
	 * The highest x coordinate of the field.
	 * @throws MissingDDCEntryException if "field,xmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,xmax" could no be cast to Double.
	 */
	public static double getFieldXMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_FIELD_XMAX)).doubleValue();
		
	}
	
	/**
	 * The lowest y coordinate of the field.
	 * @throws MissingDDCEntryException if "field,ymin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,ymin" could no be cast to Double.
	 */
	public static double getFieldYMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_FIELD_YMIN)).doubleValue();
		
	}
	
	/**
	 * The highest y coordinate of the field.
	 * @throws MissingDDCEntryException if "field,ymax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,ymax" could no be cast to Double.
	 */
	public static double getFieldYMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_FIELD_YMAX)).doubleValue();
		
	}
	
	/**
	 * The lowest x coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,xmin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,xmin" could no be cast to Double.
	 */
	public static double getGoalareaLeftXMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_XMIN)).doubleValue();
		
	}
	
	/**
	 * The highest x coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,xmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,xmax" could no be cast to Double.
	 */
	public static double getGoalareaLeftXMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_XMAX)).doubleValue();
		
	}
	
	/**
	 * The y coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,y" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,y" could no be cast to Double.
	 */
	public static double getGoalareaLeftY() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_Y)).doubleValue();
		
	}
	
	/**
	 * The highest z coordinate of the left goalarea.
	 * @throws MissingDDCEntryException if "goalarea.left,zmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.left,zmax" could no be cast to Double.
	 */
	public static double getGoalareaLeftZMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_LEFT_ZMAX)).doubleValue();
		
	}
	
	/**
	 * The lowest x coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,xmin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,xmin" could no be cast to Double.
	 */
	public static double getGoalareaRightXMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_XMIN)).doubleValue();
		
	}
	
	/**
	 * The highest x coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,xmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,xmax" could no be cast to Double.
	 */
	public static double getGoalareaRightXMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_XMAX)).doubleValue();
		
	}
	
	/**
	 * The y coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,y" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,y" could no be cast to Double.
	 */
	public static double getGoalareaRightY() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_Y)).doubleValue();
		
	}
	
	/**
	 * The highest z coordinate of the right goalarea.
	 * @throws MissingDDCEntryException if "goalarea.right,zmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "goalarea.right,zmax" could no be cast to Double.
	 */
	public static double getGoalareaRightZMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(SportsDDCAccess.ddc.getValue(KEY_GOALAREA_RIGHT_ZMAX)).doubleValue();
		
	}
	
	/**
	 * The list of all sensor_ids.
	 * @throws MissingDDCEntryException if "sensoridlist" or "sensoridseparator" is not a key of the DDC.
	 * @throws NumberFormatException if a list entry of "sensoridlist" could not be cast to Integer.
	 */
	public static ImmutableCollection<Integer> getSensorIds() throws MissingDDCEntryException, NumberFormatException {
		
		String separator = SportsDDCAccess.ddc.getValue(KEY_SENSOR_LIST_SEPARATOR);
		String[] strIds = SportsDDCAccess.ddc.getValue(KEY_SENSOR_LIST).split(separator);
		Collection<Integer> sensorids = Lists.newArrayList();
		
		for(String strID : strIds) {
			
			sensorids.add(Integer.valueOf(strID));
			
		}
		
		return ImmutableList.copyOf(sensorids);
		
	}
	
	/**
	 * The entity_id for a given sensor_id.
	 * @param sensor_id The given sensor_id.
	 * @throws MissingDDCEntryException if "sensorid.<sensor_id>,entity_id" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "sensorid.<sensor_id>,entity_id" could no be cast to Integer.
	 */
	public static int getEntityId(int sensor_id) throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return Integer.valueOf(SportsDDCAccess.ddc.getValue(SportsDDCAccess.buildKeyEntityId(sensor_id))).intValue();
		
	}
	
	/**
	 * The entity for a given sensor_id.
	 * @param sensor_id The given sensor_id.
	 * @throws MissingDDCEntryException if "sensorid.<sensor_id>,entity" is not a key of the DDC.
	 */
	public static String getEntity(int sensor_id) throws MissingDDCEntryException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		return SportsDDCAccess.ddc.getValue(SportsDDCAccess.buildKeyEntity(sensor_id));
		
	}
	
	/**
	 * The remark for a given sensor_id.
	 * @param sensor_id The given sensor_id.
	 * @return {@link Optional#absent()}, if there is no remark.
	 * @throws MissingDDCEntryException if "sensorid.<sensor_id>,remark" is not a key of the DDC.
	 */
	public static Optional<String> getRemark(int sensor_id) throws MissingDDCEntryException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		String possNullRemark = SportsDDCAccess.ddc.getValue(SportsDDCAccess.buildKeyRemark(sensor_id));
		if(possNullRemark.equals(VALUE_NULL)) {
			
			return Optional.absent();
			
		}
		
		return Optional.of(possNullRemark);
		
	}
	
	/**
	 * The team_id for a given sensor_id.
	 * @param sensor_id The given sensor_id.
	 * @return {@link Optional#absent()}, if there is no team_id.
	 * @throws MissingDDCEntryException if "sensorid.<sensor_id>,team_id" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "sensorid.<sensor_id>,team_id" could no be cast to Integer.
	 */
	public static Optional<Integer> getTeamId(int sensor_id) throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(SportsDDCAccess.ddc, "No DDC bound!");
		
		String possNullTeamId = SportsDDCAccess.ddc.getValue(SportsDDCAccess.buildKeyTeamId(sensor_id));
		if(possNullTeamId.equals(VALUE_NULL)) {
			
			return Optional.absent();
			
		}
		
		return Optional.of(Integer.valueOf(possNullTeamId));
		
	}
	
}