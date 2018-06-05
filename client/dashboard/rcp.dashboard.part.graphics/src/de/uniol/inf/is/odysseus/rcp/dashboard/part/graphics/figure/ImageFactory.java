package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.figure;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.Activator;

public class ImageFactory {

	private static Map<String, Image> imageMap = new HashMap<>();

	public static synchronized Image getImage(String filename) {
		return getImage(filename, Activator.getImage("image.png"));
	}

	private static synchronized Image getImage(String filename,
			ImageDescriptor defaultImage) {
		Image image = imageMap.get(filename);
		try {
			if (image == null) {
				System.err.println("Loading image " + filename);
				image = new Image(Display.getDefault(), new ImageData(filename));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (defaultImage != null) {
				image = new Image(Display.getDefault(),
						defaultImage.getImageData());
			}
		}
		imageMap.put(filename, image);
		return image;
	}

	public static synchronized Image getBackgroundImage(String filename) {
		return getImage(filename,null);
	}

}
