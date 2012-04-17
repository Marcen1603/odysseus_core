package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ColorManager {
	
	private static ColorManager instance = null;
	private Map<int[], Color> colors = new HashMap<int[], Color>();
	private Map<Object, Image> images = new HashMap<Object, Image>();

	
	public static ColorManager getInstance() {
		if( instance == null ) 
			instance = new ColorManager();
		return instance;
	}
	
	public Color getColor(RGB rgb) {
		int[] key = new int[]{rgb.red, rgb.green, rgb.blue};
		Color c = colors.get(key);
		if (c == null){
			colors.put(key, c = new Color(Display.getCurrent(), rgb));
		}	
		return c;
	}
	
	public Image getImage(Object o){
		Image i = images.get(o);
		return i;
	}
	
	public void addImage(Object o , Image i){
		Image old = images.put(o, i);
		if (old != null) old.dispose();
	}
	
	public void dispose(){
		for (Color c : colors.values()) {
			c.dispose();
		}
		colors.clear();
		for (Image i : images.values()) {
			i.dispose();
		}
		images.clear();
	}

	int[] random = {1, 255, 127, 63, 0};
	public Color randomColor() {
		int r = (random.length-1)/random[0];
		int g = (random.length-1)%random[0];
		int b = g/random[0]++;
		int[] key = new int[]{random[r], random[g], random[b]};
		Color c = colors.get(key);
		if (c == null){
			colors.put(key, c = new Color(Display.getCurrent(), new RGB(key[0], key[1], key[2])));
		}	
		return c;
	}

}
