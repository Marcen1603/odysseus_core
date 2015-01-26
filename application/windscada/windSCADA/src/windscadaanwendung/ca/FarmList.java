package windscadaanwendung.ca;

import java.util.Date;
import java.util.List;

import windscadaanwendung.db.DBConnectionHD;
import windscadaanwendung.views.Timer;

/**
 * This class is a static reference to get and store the loaded windFarms and
 * WKAs in windSCADA
 * 
 * @author MarkMilster
 * 
 */
public class FarmList {

	private static List<WindFarm> farmList;
	private static final int UPDATE_INTERVAL = 60000;
	public static Timer timer;

	/**
	 * List of all loaded Windfarms, which holds also their wind turbines
	 * 
	 * @return the farmList
	 */
	public static List<WindFarm> getFarmList() {
		return farmList;
	}

	/**
	 * Stores the FarmList in a static reference. Also starts a thread to update
	 * the farmList out of the Database in a static specified updateInterval
	 * 
	 * @param farmList
	 *            the farmList to set
	 */
	public static void setFarmList(List<WindFarm> farmList) {
		FarmList.farmList = farmList;
		DBConnectionHD.refreshHitWKAData(new Date(0));
		DBConnectionHD.refreshHitFarmData(new Date(0));
		timer = new Timer(UPDATE_INTERVAL);
		Thread updateThread = new Thread(timer);
		updateThread.start();
	}

	/**
	 * Returns a WKA with the specified id, no matter in which windFarm the wka
	 * appears.
	 * 
	 * @param id
	 *            The id has to be unique over the hole farmList
	 * @return The wka with the id or null if it do'nt exists
	 */
	public static WKA getWKA(int id) {
		if (farmList != null) {
			for (WindFarm farm : farmList) {
				for (WKA wka : farm.getWkas()) {
					if (wka.getID() == id) {
						return wka;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a windFarm with the specified id.
	 * 
	 * @param id
	 *            The id has to be unique
	 * @return The windFarm or null if it do'nt exists
	 */
	public static WindFarm getFarm(int id) {
		for (WindFarm farm : farmList) {
			if (farm.getID() == id) {
				return farm;
			}
		}
		return null;
	}

}
