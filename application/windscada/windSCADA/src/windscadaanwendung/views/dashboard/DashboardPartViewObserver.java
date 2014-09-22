package windscadaanwendung.views.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

/**
 * Nur für die DPVs die nicht in der uebersichtstabelle sind
 * 
 * @author MarkMilster
 *
 */
public class DashboardPartViewObserver {
	
	static List<AbstractDashboardPartView> dpvList;
	
	public static void addDashboardPartView(AbstractDashboardPartView dpv) {
		if (dpvList == null) {
			dpvList = new ArrayList<AbstractDashboardPartView>();
		}
		if (!dpvList.contains(dpv)) {
			dpvList.add(dpv);
		}
	}
	
	public static void setFileName(String fileName) {
		for(AbstractDashboardPartView dpv: dpvList) {
			dpv.loadDashboardPartFile(fileName);
		}
	}
	
	public static void setWKA(final WKA wka) {
		//FIXME: warum werden alle 2 mal aufgerufen?
		for(final AbstractDashboardPartView dpv: dpvList) {
			if (!dpv.isFarmPart()) {
				// otherwise dpv is a part for a hole windfarm
				// set wka for every valueType which shows a wkaPart
				System.out.println("Setze :" + String.valueOf(wka.getID()) + dpv.valueType + dpv.getFileEnding());
				// Funktioniert nicht -> vorher alle alten sachen vom dpv disposen und erzwingen dass es gejt auch wenn schon showing ist
				// oder evtl. bei allen dpvs einfach createPartControl() aufrufen und dabei eine wka ID uebergeben?! wohl besser !?
//				dpv.unshowDashboardPart();
//				dpv.createPartControl(dpv.getParent());
//				Composite p = dpv.getParent();
//				dpv = new CorrectedScorePart();
//				dpv.createPartControl(p);

				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						dpv.unshowDashboardPart();
						dpv.loadDashboardPartFile(String.valueOf(wka.getID()) + dpv.valueType + dpv.getFileEnding());
					}
					
				});
				
//				dpv.unshowDashboardPart();
//				dpv.loadDashboardPartFile(String.valueOf(wka.getID()) + dpv.valueType + dpv.getFileEnding());
//				dpv.loadDashboardPartFile(String.valueOf(wka.getID()) + dpv.valueType + dpv.getFileEnding());
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
