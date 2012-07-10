/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

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
