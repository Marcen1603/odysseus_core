package de.uniol.inf.is.odysseus.rcp.editor.editorpart;

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
