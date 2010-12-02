package de.uniol.inf.is.odysseus.rcp.editor.text.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

	private static class ColorEntry {
		public int r;
		public int g;
		public int b;
		
		public ColorEntry( int r, int g, int b ) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + b;
			result = prime * result + g;
			result = prime * result + r;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColorEntry other = (ColorEntry) obj;
			if (b != other.b)
				return false;
			if (g != other.g)
				return false;
			if (r != other.r)
				return false;
			return true;
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
