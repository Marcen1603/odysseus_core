package de.uniol.inf.is.odysseus.sports.sportsql.parser.ddcaccess;

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
/**
 * @author Simon
 *
 */
public class AbstractSportsDDCAccess {
	// Note: class can not be abstract due to the OSGi-binding

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractSportsDDCAccess.class);

	/**
	 * The first key for field properties (boundaries).
	 */
	private static final String KEY_FIELD_PREFIX = "field";

	/**
	 * The key for the lowest x coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_XMIN = new DDCKey(new String[] {
			AbstractSportsDDCAccess.KEY_FIELD_PREFIX, "xmin" });

	/**
	 * The key for the highest x coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_XMAX = new DDCKey(new String[] {
			AbstractSportsDDCAccess.KEY_FIELD_PREFIX, "xmax" });

	/**
	 * The key for the lowest y coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_YMIN = new DDCKey(new String[] {
			AbstractSportsDDCAccess.KEY_FIELD_PREFIX, "ymin" });

	/**
	 * The key for the highest y coordinate of the field.
	 */
	private static final DDCKey KEY_FIELD_YMAX = new DDCKey(new String[] {
			AbstractSportsDDCAccess.KEY_FIELD_PREFIX, "ymax" });
	
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
				AbstractSportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
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
				AbstractSportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
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
				AbstractSportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
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
				AbstractSportsDDCAccess.KEY_SENSOR_PREFIX + String.valueOf(sensor_id),
				"team_id" });

	}

	/**
	 * The representation of null values.
	 */
	protected static String VALUE_NULL = "<null>";

	/**
	 * The DDC.
	 */
	protected static IDistributedDataContainer ddc;

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
		AbstractSportsDDCAccess.ddc = ddc;
		AbstractSportsDDCAccess.LOG.debug("Bound {} as a DDC", ddc.getClass()
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
		if (AbstractSportsDDCAccess.ddc == ddc) {

			AbstractSportsDDCAccess.ddc = null;
			AbstractSportsDDCAccess.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The lowest x coordinate of the field.
	 * @throws MissingDDCEntryException if "field,xmin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,xmin" could no be cast to Double.
	 */
	public static double getFieldXMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_FIELD_XMIN)).doubleValue();
		
	}
	
	/**
	 * The highest x coordinate of the field.
	 * @throws MissingDDCEntryException if "field,xmax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,xmax" could no be cast to Double.
	 */
	public static double getFieldXMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_FIELD_XMAX)).doubleValue();
		
	}
	
	/**
	 * The lowest y coordinate of the field.
	 * @throws MissingDDCEntryException if "field,ymin" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,ymin" could no be cast to Double.
	 */
	public static double getFieldYMin() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_FIELD_YMIN)).doubleValue();
		
	}
	
	/**
	 * The highest y coordinate of the field.
	 * @throws MissingDDCEntryException if "field,ymax" is not a key of the DDC.
	 * @throws NumberFormatException if the value of "field,ymax" could no be cast to Double.
	 */
	public static double getFieldYMax() throws MissingDDCEntryException, NumberFormatException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Double.valueOf(AbstractSportsDDCAccess.ddc.getValue(KEY_FIELD_YMAX)).doubleValue();
		
	}
	
	/**
	 * The list of all sensor_ids.
	 * @throws MissingDDCEntryException if "sensoridlist" or "sensoridseparator" is not a key of the DDC.
	 * @throws NumberFormatException if a list entry of "sensoridlist" could not be cast to Integer.
	 */
	public static ImmutableCollection<Integer> getSensorIds() throws MissingDDCEntryException, NumberFormatException {
		
		String separator = AbstractSportsDDCAccess.ddc.getValue(KEY_SENSOR_LIST_SEPARATOR);
		String[] strIds = AbstractSportsDDCAccess.ddc.getValue(KEY_SENSOR_LIST).split(separator);
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
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return Integer.valueOf(AbstractSportsDDCAccess.ddc.getValue(AbstractSportsDDCAccess.buildKeyEntityId(sensor_id))).intValue();
		
	}
	
	/**
	 * The entity for a given sensor_id.
	 * @param sensor_id The given sensor_id.
	 * @throws MissingDDCEntryException if "sensorid.<sensor_id>,entity" is not a key of the DDC.
	 */
	public static String getEntity(int sensor_id) throws MissingDDCEntryException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		return AbstractSportsDDCAccess.ddc.getValue(AbstractSportsDDCAccess.buildKeyEntity(sensor_id));
		
	}
	
	/**
	 * The remark for a given sensor_id.
	 * @param sensor_id The given sensor_id.
	 * @return {@link Optional#absent()}, if there is no remark.
	 * @throws MissingDDCEntryException if "sensorid.<sensor_id>,remark" is not a key of the DDC.
	 */
	public static Optional<String> getRemark(int sensor_id) throws MissingDDCEntryException {
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		String possNullRemark = AbstractSportsDDCAccess.ddc.getValue(AbstractSportsDDCAccess.buildKeyRemark(sensor_id));
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
		
		Preconditions.checkNotNull(AbstractSportsDDCAccess.ddc, "No DDC bound!");
		
		String possNullTeamId = AbstractSportsDDCAccess.ddc.getValue(AbstractSportsDDCAccess.buildKeyTeamId(sensor_id));
		if(possNullTeamId.equals(VALUE_NULL)) {
			
			return Optional.absent();
			
		}
		
		return Optional.of(Integer.valueOf(possNullTeamId));
		
	}
	
	
	
	/**
	 * Calculates the x value for the center spot
	 * @return
	 * @throws NumberFormatException
	 * @throws MissingDDCEntryException
	 */
	public static Double calculateCenterSpotX() throws NumberFormatException, MissingDDCEntryException{
		
		return (getFieldXMax() + getFieldXMin())/2;
		
	}
	
	/**
	 * Calculates the x value for the center spot
	 * @return
	 * @throws NumberFormatException
	 * @throws MissingDDCEntryException
	 */
	public static Double calculateCenterSpotY() throws NumberFormatException, MissingDDCEntryException{
		
		return (getFieldYMax() + getFieldYMin())/2;
		
	}

	
}