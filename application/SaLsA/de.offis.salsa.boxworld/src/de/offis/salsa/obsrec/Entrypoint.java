package de.offis.salsa.obsrec;

import javax.swing.JFrame;

import de.offis.salsa.obsrec.ls.DebugLaserScanner;
import de.offis.salsa.obsrec.ui.ObjectworldWindow;

public class Entrypoint {

	@SuppressWarnings("unused")
	private static JFrame frm;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		Objektwelt objWelt = new Objektwelt();
//		SickLaserScanner s = objWelt.addSickLaserScanner("192.168.1.20", 2111);
//		ReadingLaserScanner s = objWelt.addReadingLaserScanner("lms1.data");
//		SavingLaserScanner s = objWelt.addSavingLaserScanner("192.168.1.20", 2111, "lms1.data");
		DebugLaserScanner s = objWelt.addDebugLaserScanner();
		s.connect();
		frm = new ObjectworldWindow(objWelt);		
		

		s.start();
		
	}

}
