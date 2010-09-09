package de.uniol.inf.is.odysseus.rcp.editor.text.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

	private class ColorEntry {
		public int r;
		public int g;
		public int b;
		
		public ColorEntry( int r, int g, int b ) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		@Override
		public boolean equals(Object obj) {
			ColorEntry o = (ColorEntry)obj;
			return o.r == r && o.g == g && o.b == b;
		}
	}
	
	public Map<ColorEntry, Color> colors = new HashMap<ColorEntry, Color>();
	
	public Color get( int r, int g, int b ) {
		ColorEntry e = new ColorEntry(r,g,b);
		
		if( colors.containsKey(e) )
			return colors.get(e);
		
		Color col = new Color(Display.getCurrent(), r,g,b);
		colors.put(e, col);
		return col;
	}
	
	public void dispose() {
		for( Color col : colors.values() )
			col.dispose();
	}
}
