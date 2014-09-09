package windscadaanwendung.ca;

import java.util.List;

public class FarmList {
	
	private static List<WindFarm> farmList;

	/**
	 * @return the farmList
	 */
	public static List<WindFarm> getFarmList() {
		return farmList;
	}

	/**
	 * @param farmList the farmList to set
	 */
	public static void setFarmList(List<WindFarm> farmList) {
		FarmList.farmList = farmList;
	}
	
	public static WKA getWKA(int id) {
		for (WindFarm farm: farmList) {
			for (WKA wka: farm.getWkas()) {
				if (wka.getID() == id) {
					return wka;
				}
			}
		}
		return null;
	}
	
	public static WindFarm getFarm(int id) {
		for (WindFarm farm: farmList) {
			if (farm.getID() == id) {
				return farm;
			}
		}
		return null;
	}

}
