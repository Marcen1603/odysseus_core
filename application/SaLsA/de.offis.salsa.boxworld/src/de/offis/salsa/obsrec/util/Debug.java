package de.offis.salsa.obsrec.util;

import de.offis.salsa.obsrec.ui.BoxWorldPanel;
import de.offis.salsa.obsrec.ui.paint.CanvasElement;

public class Debug {

	private static BoxWorldPanel boxWorldPanel = null;
	
	public static void registerPaintPanel(BoxWorldPanel boxWorldPanel) {
		Debug.boxWorldPanel = boxWorldPanel;
	}

	public static void addDebugObject(CanvasElement debugMarker) {
		if(boxWorldPanel != null){
			boxWorldPanel.addDebugPaint(debugMarker);
		}
	}
}
