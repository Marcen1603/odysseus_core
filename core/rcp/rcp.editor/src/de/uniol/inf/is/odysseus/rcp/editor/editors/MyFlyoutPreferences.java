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
package de.uniol.inf.is.odysseus.rcp.editor.editors;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;

public class MyFlyoutPreferences implements FlyoutPreferences {

	private int dockLocation = PositionConstants.WEST;
	private int paletteState = FlyoutPaletteComposite.STATE_PINNED_OPEN;
	private int width = 100;
	
	@Override
	public int getDockLocation() {
		return dockLocation;
	}

	@Override
	public int getPaletteState() {
		return paletteState;
	}

	@Override
	public int getPaletteWidth() {
		return width;
	}

	@Override
	public void setDockLocation(int location) {
		dockLocation = location;
	}

	@Override
	public void setPaletteState(int state) {
		paletteState = state;
	}

	@Override
	public void setPaletteWidth(int width) {
		this.width = width;
	}
}
