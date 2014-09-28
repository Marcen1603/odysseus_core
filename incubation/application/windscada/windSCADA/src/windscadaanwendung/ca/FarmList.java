package windscadaanwendung.ca;

import java.util.Date;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import windscadaanwendung.db.DBConnectionHD;

public class FarmList {
	
	private static List<WindFarm> farmList;
	private static Thread updateThread;
	private static long updateInterval = 10000;

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
		DBConnectionHD.refreshHitWKAData(new Date(0));
		DBConnectionHD.refreshHitFarmData(new Date(0));
		if (updateThread == null) {
			updateThread = new Thread(new Runnable() {
//FIXME der startet den thread nicht? -> es soll in einem bestimmtem intervall aktualisiert werden
				@Override
				public void run() {
					while (true) {
						final Display disp = PlatformUI.getWorkbench().getDisplay();
						if (!disp.isDisposed()) {
							disp.asyncExec(new Runnable() {
								@Override
								public void run() {
									System.out.println("Mache UPDATE!");
									DBConnectionHD.refreshHitWKAData(new Date(0));
									DBConnectionHD.refreshHitFarmData(new Date(0));
								}
							});
						}
						waiting(updateInterval);
					}
				}

			});
		}
	}
	
	private static void waiting(long length) {
		try {
			Thread.sleep(length);
		} catch (final InterruptedException e) {
		}
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
