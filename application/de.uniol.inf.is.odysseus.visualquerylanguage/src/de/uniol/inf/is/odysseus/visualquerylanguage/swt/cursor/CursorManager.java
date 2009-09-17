package de.uniol.inf.is.odysseus.visualquerylanguage.swt.cursor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;

public class CursorManager {
	
	private static boolean isConnection = false;
	private static boolean isStandard = true;

	public static Cursor setStandardCursor() {
		isStandard = true;
		return Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW);
	} 
	
	public static Cursor dragButtonCursor(String imageName) {
		isStandard = false;
		if(SWTResourceManager.getInstance().getImage(imageName) != null) {
			return new Cursor(Display.getDefault(), SWTResourceManager.getInstance().getImage(imageName).getImageData(), 0, 0);
		}else {
			return setStandardCursor();
		}
	}
	
	public static Cursor connectionCursor(boolean value, String imageName) {
		isConnection = value;
		return new Cursor(Display.getDefault(), SWTResourceManager.getInstance().getImage(imageName).getImageData(), 0, 0);
	}
	
	public static void isNotConnection() {
		isConnection = false;
	}
	
	public static boolean getIsConnection() {
		return isConnection;
	}
	
	public static boolean getIsStandardCursor() {
		return isStandard;
	}

}
