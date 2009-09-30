package de.uniol.inf.is.odysseus.visualquerylanguage.swt.cursor;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;

public class CursorManager {
	
	private static boolean isConnection = false;
	private static boolean isStandard = true;

	public static Cursor setCursor(INodeContent content) {
		if(content == null) {
			isStandard = true;
			return null;
		}else if (content.getImage() != null){
			isStandard = false;
			return new Cursor(Display.getDefault(), content.getImage().getImageData(), 0, 0);
		}
		isStandard = false;
		return new Cursor(Display.getDefault(), SWTResourceManager.getInstance().getImage("default").getImageData(), 0, 0);
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
