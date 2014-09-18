package windscadaanwendung.views.dashboard;

import java.util.ArrayList;
import java.util.List;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

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
			if (!dpv.isFarmPart()) {
				// otherwise dpv is a part for a hole windfarm
				// set wka for every valueType which shows a wkaPart
				System.out.println("Setze:");
				System.out.println(String.valueOf(wka.getID()) + "." + dpv.valueType + dpv.getFileEnding());
				//FIXME Funktioniert nicht -> vorher alle alten sachen disposen
				dpv.loadDashboardPartFile(String.valueOf(wka.getID()) + "." + dpv.valueType + dpv.getFileEnding());
			}
		}
		//TODO Fuer Farm und fuer null machen
		  
		 
	}
	
	public static void setWindFarm(WindFarm farm) {
		for(AbstractDashboardPartView dpv: dpvList) {
			if (dpv.isFarmPart()) {
				// dpv is a part for a hole windfarm
				// set farm for every valueType which shows a farrmPart
			}
		}
	}

}
