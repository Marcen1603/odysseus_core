/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author DGeesen
 * 
 */
public class ColorManager {

	private static List<Color> loadedColors = new ArrayList<>();

	public static Color createColor(RGB rgb) {
		return createColor(rgb.red, rgb.green, rgb.blue);
	}

	public static Color createColor(int red, int green, int blue) {
		for (Color c : loadedColors) {
			if (c.getRGB().red == red && c.getRGB().green == green && c.getRGB().blue == blue) {
				return c;
			}
		}
		Color color = new Color(Display.getCurrent(), red, green, blue);
		loadedColors.add(color);
		return color;
	}

	public static void disposeColors() {
		for (Color c : loadedColors) {
			c.dispose();
		}
		loadedColors.clear();
	}

}
