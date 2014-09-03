package de.uniol.inf.is.odysseus.sports.sportsql.parser.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;

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
	 * The key for the lowest x coordinate.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_XMIN = {
			SportsDDCAccess.KEY_FIELD_PREFIX, "xmin" };

	/**
	 * The key for the highest x coordinate.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_XMAX = {
			SportsDDCAccess.KEY_FIELD_PREFIX, "xmax" };

	/**
	 * The key for the lowest y coordinate.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_YMIN = {
			SportsDDCAccess.KEY_FIELD_PREFIX, "ymin" };

	/**
	 * The key for the highest y coordinate.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_YMAX = {
			SportsDDCAccess.KEY_FIELD_PREFIX, "ymax" };

	/**
	 * The first key for properties of goalareas (boundaries).
	 */
	private static final String KEY_GOALAREA_PREFIX = "goalarea.";

	/**
	 * The key for the lowest x coordinate of the left gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_LEFT_XMIN = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "xmin" };

	/**
	 * The key for the highest x coordinate of the left gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_LEFT_XMAX = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "xmax" };

	/**
	 * The key for the y coordinate of the left gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_LEFT_Y = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "y" };

	/**
	 * The key for the highest z coordinate of the left gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_LEFT_ZMAX = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "left", "zmax" };

	/**
	 * The key for the lowest x coordinate of the right gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_RIGHT_XMIN = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "right", "xmin" };

	/**
	 * The key for the highest x coordinate of the right gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_RIGHT_XMAX = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "right", "xmax" };

	/**
	 * The key for the y coordinate of the right gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_RIGHT_Y = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "right", "y" };

	/**
	 * The key for the highest z coordinate of the right gooalarea.
	 */
	@SuppressWarnings("unused")
	private static final String[] KEY_GOALAREA_RIGHT_ZMAX = {
			SportsDDCAccess.KEY_GOALAREA_PREFIX + "right", "zmax" };

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
	@SuppressWarnings("unused")
	private static String[] buildKeyEntityId(int sensor_id) {

		return new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"entity_id" };

	}

	/**
	 * The key for the entity for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	@SuppressWarnings("unused")
	private static String[] buildKeyEntity(int sensor_id) {

		return new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"entity" };

	}

	/**
	 * The key for the remark for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	@SuppressWarnings("unused")
	private static String[] buildKeyRemark(int sensor_id) {

		return new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"remark" };

	}

	/**
	 * The key for the team_id for a given sensor_id.
	 * 
	 * @param sensor_id
	 *            The given sensor_id.
	 */
	@SuppressWarnings("unused")
	private static String[] buildKeyTeamId(int sensor_id) {

		return new String[] {
				SportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"team_id" };

	}

	/**
	 * The representation of null values.
	 */
	@SuppressWarnings("unused")
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

}