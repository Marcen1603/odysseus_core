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
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.RGB.X11Col;

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
		
		public OwnerColor( X11Col bgColorID, X11Col txtColorID, X11Col inactiveBGColor, X11Col inactiveTxtColor ) {
			textColor = getColor(txtColorID);
			backgroundColor = getColor(bgColorID);
			inactiveBackgroundColor = getColor(inactiveBGColor);
			inactiveTextColor = getColor(inactiveTxtColor);
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
		
		@SuppressWarnings("unused")
		public OwnerColor( Color bgColor, Color txtColor) {
			this(bgColor, txtColor, bgColor, txtColor);
		}
	}

	private static OwnerColor[] ownerColors;
	
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
		if( ownerColors == null ) {
			createOwnerColors();
		}
		
		return ownerColors[ownerID % ownerColors.length];
	}

	private static Color getSystemColor(int color) {
		return PlatformUI.getWorkbench().getDisplay().getSystemColor(color);
	}
	
	public static Color getColor(X11Col color){
		RGB rgb = RGB.getRGB(color);
		return getColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}
	
	@SuppressWarnings("unused")
	private static Color getColor(RGB rgb){
		return getColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}
	
	private static void createOwnerColors() {
		ownerColors = new OwnerColor[] {
				new OwnerColor( X11Col.CornflowerBlue,  X11Col.gray100,X11Col.CornflowerBlue, X11Col.gray100 ),
				new OwnerColor( X11Col.DodgerBlue,  X11Col.gray100,X11Col.DodgerBlue, X11Col.gray100 ),
				new OwnerColor( X11Col.DodgerBlue3,  X11Col.gray100,X11Col.DodgerBlue3, X11Col.gray100 ),
				new OwnerColor( X11Col.DarkOliveGreen3,  X11Col.gray100,X11Col.DarkOliveGreen3, X11Col.gray100 ),
				new OwnerColor( X11Col.LightSeaGreen,  X11Col.gray100,X11Col.LightSeaGreen, X11Col.gray100 ),
				new OwnerColor( X11Col.LimeGreen,  X11Col.gray100,X11Col.LimeGreen, X11Col.gray100 ),
				new OwnerColor( X11Col.OliveDrab3,  X11Col.gray100,X11Col.OliveDrab3, X11Col.gray100 ),
				new OwnerColor( X11Col.chartreuse3,  X11Col.gray100,X11Col.chartreuse3, X11Col.gray100 ),
				new OwnerColor( X11Col.green4,  X11Col.gray100,X11Col.green4, X11Col.gray100 ),
				new OwnerColor( X11Col.brown3,  X11Col.gray100,X11Col.brown3, X11Col.gray100 ),
				new OwnerColor( X11Col.chocolate3,  X11Col.gray100,X11Col.chocolate3, X11Col.gray100 ),
				new OwnerColor( X11Col.tan3,  X11Col.gray100,X11Col.tan3, X11Col.gray100 ),
				new OwnerColor( X11Col.DarkGoldenrod3,  X11Col.gray100,X11Col.DarkGoldenrod3, X11Col.gray100 ),
				new OwnerColor( X11Col.LemonChiffon3,  X11Col.gray100,X11Col.LemonChiffon3, X11Col.gray100 ),
				new OwnerColor( X11Col.LightGoldenrod3,  X11Col.gray100,X11Col.LightGoldenrod3, X11Col.gray100 ),
				new OwnerColor( X11Col.gold3,  X11Col.gray100,X11Col.gold3, X11Col.gray100 ),
				new OwnerColor( X11Col.goldenrod3,  X11Col.gray100,X11Col.goldenrod3, X11Col.gray100 ),
				new OwnerColor( X11Col.yellow3,  X11Col.gray100,X11Col.yellow3, X11Col.gray100 ),		
				new OwnerColor( SWT.COLOR_DARK_BLUE, SWT.COLOR_WHITE, SWT.COLOR_DARK_BLUE, SWT.COLOR_WHITE),
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
				new OwnerColor( SWT.COLOR_CYAN, SWT.COLOR_BLACK)
			};
	}
	
	private static Color getColor( int r, int g, int b ) {
		for( Color color : LOADED_COLORS ) {
			if( isColorEqualToRGB(color, r, g, b) ) {
				return color;
			}
		}
		
		Color newColor = new Color(PlatformUI.getWorkbench().getDisplay(), r, g, b);
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
