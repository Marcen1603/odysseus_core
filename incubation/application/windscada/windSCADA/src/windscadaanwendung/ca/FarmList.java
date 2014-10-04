package windscadaanwendung.ca;

import java.util.Date;
import java.util.List;

import windscadaanwendung.db.DBConnectionHD;

/**
 * This class is a static reference to get and store the loaded windFarms and WKAs in windSCADA
 * 
 * @author MarkMilster
 *
 */
public class FarmList {
	
	private static List<WindFarm> farmList;
//	private static Thread updateThread;
//	private static long updateInterval = 10000;

	/**
	 * 
	 * 
	 * @return the farmList
	 */
	public static List<WindFarm> getFarmList() {
		return farmList;
	}

	/**
	 * Stores the FarmList in a static reference. Also starts a thread to update the farmList out of the Database in a static specified updateInterval
	 * 
	 * @param farmList the farmList to set
	 */
	public static void setFarmList(List<WindFarm> farmList) {
		FarmList.farmList = farmList;
		DBConnectionHD.refreshHitWKAData(new Date(0));
		DBConnectionHD.refreshHitFarmData(new Date(0));
//		
//		if (updateThread == null) {
//			updateThread = new Thread(new Runnable() {
////FIXME der startet den thread nicht? -> es soll in einem bestimmtem intervall aktualisiert werden
//				@Override
//				public void run() {
//					while (true) {
//						final Display disp = PlatformUI.getWorkbench().getDisplay();
//						if (!disp.isDisposed()) {
//							disp.asyncExec(new Runnable() {
//								@Override
//								public void run() {
//									System.out.println("Mache UPDATE!");
//									DBConnectionHD.refreshHitWKAData(new Date(0));
//									DBConnectionHD.refreshHitFarmData(new Date(0));
//								}
//							});
//						}
//						waiting(updateInterval);
//					}
//				}
//
//			});
//		}
//		
	}
	
//	/**
//	 * The thread sleeps for the specified amount of ms
//	 * 
//	 * @param length
//	 */
//	private static void waiting(long length) {
//		try {
//			Thread.sleep(length);
//		} catch (final InterruptedException e) {
//		}
//	}
//	
	/**
	 * Returns a WKA with the specified id, no matter in which windFarm the wka appears.
	 * 
	 * @param id
	 * 			The id has to be unique over the hole farmList
	 * @return
	 * 		The wka with the id or null if it do'nt exists
	 */
	public static WKA getWKA(int id) {
		if (farmList != null) {
			for (WindFarm farm: farmList) {
				for (WKA wka: farm.getWkas()) {
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
	 * 			The id has to be unique
	 * @return 
	 * 		The windFarm or null if it do'nt exists
	 */
	public static WindFarm getFarm(int id) {
		for (WindFarm farm: farmList) {
			if (farm.getID() == id) {
				return farm;
			}
		}
		return null;
	}

}
