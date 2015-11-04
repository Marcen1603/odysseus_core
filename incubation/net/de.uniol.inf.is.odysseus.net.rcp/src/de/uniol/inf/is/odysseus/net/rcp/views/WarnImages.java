package de.uniol.inf.is.odysseus.net.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class WarnImages {

	private Image[] warnImages;

	public WarnImages() {
		warnImages = new Image[4];
		
		warnImages[0] = createWarnImage(SWT.COLOR_BLUE);
		warnImages[1] = createWarnImage(SWT.COLOR_GREEN);
		warnImages[2] = createWarnImage(SWT.COLOR_YELLOW);
		warnImages[3] = createWarnImage(SWT.COLOR_RED);
	}
	
	public void dispose() {
		for (Image image : warnImages) {
			image.dispose();
		}
		warnImages = null;
	}
	
	public Image getWarnImage(double perc) {
		int imageIndex = Math.max(0, Math.min(3, (int) (perc / 25.0)));
		return warnImages[imageIndex];
	}

	private static Image createWarnImage(int color) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		Image img = new Image(display, 10, 10);

		GC gc = new GC(img);
		gc.setBackground(display.getSystemColor(color));
		gc.fillRectangle(0, 0, 10, 10);
		gc.dispose();
		return img;
	}
}
