package windscadaanwendung.views.dashboard;

import java.util.ArrayList;
import java.util.List;

import windscadaanwendung.ca.WKA;

public class DashboardPartViewObserver {
	
	static List<AbstractDashboardPartView> dpvList;
	
	public static void addDashboardPartView(AbstractDashboardPartView dpv) {
		if (dpvList == null) {
			dpvList = new ArrayList<AbstractDashboardPartView>();
		}
		dpvList.add(dpv);
	}
	
	public static void setFileName(String fileName) {
		for(AbstractDashboardPartView dpv: dpvList) {
			dpv.loadDashboardPartFile(fileName);
		}
	}
	
	public static void setWKA(WKA wka) {
		for(AbstractDashboardPartView dpv: dpvList) {
			System.out.println("Setze:");
			System.out.println(String.valueOf(wka.getID()) + "." + dpv.valueType + ".prt");
			//FIXME Funktioniert nicht
			dpv.loadDashboardPartFile(String.valueOf(wka.getID()) + "." + dpv.valueType + ".prt");
		}
		//TODO Fuer Farm und fuer null machen
	}

}
