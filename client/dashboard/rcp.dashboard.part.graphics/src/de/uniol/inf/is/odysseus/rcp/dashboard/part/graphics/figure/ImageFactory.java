package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;

public class ImageFactory {

	private static Map<String, Image> imageMap = new HashMap<>();

	public static synchronized Image getImage(String filename) {
		Image image = imageMap.get(filename);
		try {
			if (image == null) {
				System.err.println("Loading image " + filename);
				image = new Image(Display.getDefault(), new ImageData(filename));
			}
		} catch (Exception e) {
			e.printStackTrace();
			image = new Image(Display.getDefault(), Activator.getImage(
					"image.png").getImageData());
		}
		imageMap.put(filename, image);
		return image;
	}

}
