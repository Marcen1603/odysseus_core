package de.uniol.inf.is.odysseus.sports.mep;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

public class IsSensorLegFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = -1421280112062480906L;

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory	.getLogger(IsSensorLegFunction.class);

	private static boolean hasBeenSetUp = false;

	protected static IDistributedDataContainer ddc;

	private static List<Integer> legEntityIDList = new ArrayList<Integer>();
	private static List<Integer> legSensorIDList = new ArrayList<Integer>();

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.INTEGER }, { SDFDatatype.STRING } };

	public IsSensorLegFunction() {
		super("isSensorLeg", 2, accTypes, SDFDatatype.BOOLEAN);
	}

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
		IsSensorLegFunction.ddc = ddc;
		IsSensorLegFunction.LOG.debug("Bound {} as a DDC", ddc.getClass().getSimpleName());

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
		if (IsSensorLegFunction.ddc == ddc) {
			IsSensorLegFunction.ddc = null;
			IsSensorLegFunction.LOG.debug("Unbound {} as a DDC", ddc.getClass().getSimpleName());

		}

	}

	private static void setUpBallList() {
		try {
			String[] sensorList = AccessToDCCFunction.ddc.getValue(new DDCKey("sensoridlist")).split(",");

			for (int i = 0; i < sensorList.length; i++) {
				String[] searchKey = new String[2];
				searchKey[0] = "sensorid." + sensorList[i];
				searchKey[1] = "remark";

				String remark = IsSensorLegFunction.ddc.getValue(new DDCKey(searchKey));

				if (remark.toLowerCase().contains("leg")) {
					searchKey[1] = "entity_id";
					int entity_id = Integer.valueOf(IsSensorLegFunction.ddc.getValue(new DDCKey(searchKey)));

					legEntityIDList.add(entity_id);
					legSensorIDList.add(Integer.valueOf(sensorList[i]));
				}

			}

			hasBeenSetUp = true;

		} catch (MissingDDCEntryException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Boolean getValue() {

		if (!hasBeenSetUp) {

			setUpBallList();

		}

		Integer id = getInputValue(0);
		String type = getInputValue(1).toString();

		if (type.equals("sid")) {
			if (legSensorIDList.contains(id)) {
				return true;
			} else {
				return false;
			}
		} else if (type.equals("entity_id")) {
			if (legEntityIDList.contains(id)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}
