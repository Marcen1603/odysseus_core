/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

public class OwnerColorManager {
	
	private static class OwnerColor {
		public final Color backgroundColor;
		public final Color textColor;
		
		public final Color inactiveBackgroundColor;
		public final Color inactiveTextColor;
		
		public OwnerColor( int bgColorID, int txtColorID, int inactiveBGColor, int inactiveTxtColor ) {
			textColor = getSystemColor(txtColorID);
			backgroundColor = getSystemColor(bgColorID);
			inactiveBackgroundColor = getSystemColor(inactiveBGColor);
			inactiveTextColor = getSystemColor(inactiveTxtColor);
		}
		
		public OwnerColor( int bgColorID, int txtColorID) {
			this(bgColorID, txtColorID, bgColorID, txtColorID);
		}
		
		public OwnerColor( Color bgColor, Color txtColor, Color inactiveBGColor, Color inactiveTxtColor ) {
			textColor = txtColor;
			backgroundColor = bgColor;
			inactiveBackgroundColor = inactiveBGColor;
			inactiveTextColor = inactiveTxtColor;
		}
		
		public OwnerColor( Color bgColor, Color txtColor) {
			this(bgColor, txtColor, bgColor, txtColor);
		}
	}
	
	private static final OwnerColor[] OWNER_COLORS = new OwnerColor[] {
		new OwnerColor( SWT.COLOR_DARK_BLUE, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_DARK_CYAN, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_DARK_GRAY, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_DARK_GREEN, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_DARK_YELLOW, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_DARK_RED, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_BLACK, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_BLUE, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_RED, SWT.COLOR_WHITE),
		new OwnerColor( SWT.COLOR_YELLOW, SWT.COLOR_BLACK),
		new OwnerColor( SWT.COLOR_GRAY, SWT.COLOR_BLACK),
		new OwnerColor( SWT.COLOR_MAGENTA, SWT.COLOR_BLACK),
		new OwnerColor( SWT.COLOR_CYAN, SWT.COLOR_BLACK),
	};
	
	private static final List<Color> LOADED_COLORS = Lists.newArrayList();

	private OwnerColorManager() {
		
	}
	
	public static Color getOwnerBackgroundColor( int ownerID ) {
		return getOwnerColor(ownerID).backgroundColor;
	}
	
	public static Color getOwnerTextColor( int ownerID ) {
		return getOwnerColor(ownerID).textColor;
	}
	
	public static Color getInactiveOwnerTextColor( int ownerID ) {
		return getOwnerColor(ownerID).inactiveTextColor;
	}
	
	public static Color getInactiveOwnerBackgroundColor( int ownerID ) {
		return getOwnerColor(ownerID).inactiveBackgroundColor;
	}
	
	private static OwnerColor getOwnerColor( int ownerID ) {
		return OWNER_COLORS[ownerID % OWNER_COLORS.length];
	}

	private static Color getSystemColor(int color) {
		return PlatformUI.getWorkbench().getDisplay().getSystemColor(color);
	}
	
	private static Color getColor( int r, int g, int b ) {
		for( Color color : LOADED_COLORS ) {
			if( isColorEqualToRGB(color, r, g, b) ) {
				return color;
			}
		}
		
		Color newColor = new Color(Display.getDefault(), r, g, b);
		LOADED_COLORS.add(newColor);
		return newColor;
	}

	private static boolean isColorEqualToRGB(Color color, int r, int g, int b) {
		return color.getRed() == r && color.getGreen() == g && color.getBlue() == b;
	}
	
	public static void disposeColors() {
		for( Color color : LOADED_COLORS ) {
			color.dispose();
		}
		
		LOADED_COLORS.clear();
	}
}
