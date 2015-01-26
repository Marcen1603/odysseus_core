package windscadaanwendung.views.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

/**
 * Handles the change of the WKA or Windfarm for the registered
 * DashboardPartViews
 * 
 * @author MarkMilster
 * 
 */
public class DashboardPartViewObserver {

	static List<WindDashboardPartView> dpvList;

	/**
	 * Register a new DashboardPartView to handle by this Observer
	 * 
	 * @param dpv
	 *            The DashboardPartView
	 */
	public static void addDashboardPartView(WindDashboardPartView dpv) {
		if (dpvList == null) {
			dpvList = new ArrayList<WindDashboardPartView>();
		}
		if (!dpvList.contains(dpv)) {
			dpvList.add(dpv);
		}
	}

	/**
	 * You have to call this if you dispose the WindDashboardPartView
	 * 
	 * @param dpv
	 */
	public static void removeDashboardPartView(WindDashboardPartView dpv) {
		System.out.print("Remove dpv " + dpv.toString());
		System.out.println(dpvList.remove(dpv));
	}

	/**
	 * set farm for every valueType which shows a wkaPart
	 * 
	 * @param wka
	 */
	public static void setWKA(final WKA wka) {
		for (final WindDashboardPartView dpv : dpvList) {
			if (!dpv.isFarmPart()) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						setFileName(dpv, String.valueOf(wka.getID())
								+ dpv.valueType + dpv.getFileEnding());
					}

				});
			}
		}
	}

	/**
	 * set farm for every valueType which shows a farmPart
	 * 
	 * @param farm
	 */
	public static void setWindFarm(final WindFarm farm) {
		for (final WindDashboardPartView dpv : dpvList) {
			if (dpv.isFarmPart()) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						setFileName(dpv, String.valueOf(farm.getID())
								+ dpv.valueType + dpv.getFileEnding());
					}

				});
			}
		}
	}

	/**
	 * Loads a new XML-File which represents a DashboardPart in the
	 * DashboardPartView
	 * 
	 * @param dpv
	 *            The DashboardPartView
	 * @param fileName
	 *            The path of the XML-File
	 */
	public static void setFileName(final WindDashboardPartView dpv,
			final String fileName) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				System.out.println(System.currentTimeMillis());
				dpv.unshowDashboardPart();
				dpv.loadDashboardPartFile(fileName);
			}

		});
	}

}
