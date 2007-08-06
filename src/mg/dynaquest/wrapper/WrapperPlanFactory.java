/*
 * Created on 19.01.2005
 *
 */
package mg.dynaquest.wrapper;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import mg.dynaquest.queryexecution.po.access.RMIDataAccessPO;
import mg.dynaquest.queryexecution.po.access.WEAStreamAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;
;

/**
 * @author Marco Grawunder
 *
 */
public class WrapperPlanFactory {

    // Erst mal statisch, später hier dynamisch anpassen
	
    public static PlanOperator getAccessPlan(AccessPO accessPO){
        // Achtung! Hier für jeden Wrapper einen neuen Plan erstellen
        // keinen Cache verwenden und Pläne teilen!!
        PlanOperator accessPlan = null;
        SDFSource source = accessPO.getSource();
        
        if (source.equals("http://134.106.52.176/Automarkt2004Quelle1")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle1", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle1b")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle1b", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle1c")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle1", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle1d")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle1", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle1e")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle1", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle2")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle2", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle3")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle3", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle3b")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle3b", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2004Quelle4")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","AutomarktQuelle4", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2006Quelle1")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","Automarkt2006Quelle1", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2006Quelle2")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","Automarkt2006Quelle1", accessPO);
        }else if (source.equals("http://134.106.52.176/Automarkt2006Quelle3")){
            accessPlan = new RMIDataAccessPO("134.106.52.176","Automarkt2006Quelle1", accessPO);
        }else if (source.equals("http://.../quelle1")){
        	//TODO: Hier sinnvolle Eingabe-Ausgabe-Kombinationen bilden
        	accessPlan = new WEAStreamAccessPO(accessPO);
    		try {
				((WEAStreamAccessPO)accessPlan).setInputStream(new InputStreamReader(new Socket("...", 10000).getInputStream()));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        
//        if (source.equals("Dummy")){
//            accessPlan = new CollectorPO();
//        }
//            
       
        
        return accessPlan;
    }

  
}
