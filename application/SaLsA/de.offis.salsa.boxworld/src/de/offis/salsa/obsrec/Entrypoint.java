package de.offis.salsa.obsrec;

import de.offis.salsa.obsrec.ls.DebugLaserScanner;
import de.offis.salsa.obsrec.ui.BoxWorldWindow;

public class Entrypoint {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Objectworld objWelt = new Objectworld();
//		SickLaserScanner s = objWelt.addSickLaserScanner("192.168.1.20", 2111);
//		ReadingLaserScanner s = objWelt.addReadingLaserScanner("lms1.data");
//		SavingLaserScanner s = objWelt.addSavingLaserScanner("192.168.1.20", 2111, "lms1.data");
		DebugLaserScanner s = objWelt.addDebugLaserScanner();
		s.connect();
		
		BoxWorldWindow window = new BoxWorldWindow(objWelt);
		
		s.start();
		
	}

}
