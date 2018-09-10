package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.core.resources.IFile;

public final class DashboardSettings {

	private final IFile backgroundImageFile;
	private final boolean isLocked;
	private final boolean isBackgroundImageStretched;
	
	public static DashboardSettings getDefault() {
		return new DashboardSettings(null, false, false);
	}
	
	public DashboardSettings(IFile backgroundImageFile, boolean isLocked, boolean isBackgroundImageStretched) {
		this.backgroundImageFile = backgroundImageFile;
		this.isLocked = isLocked;
		this.isBackgroundImageStretched = isBackgroundImageStretched;
	}
	
	public IFile getBackgroundImageFile() {
		return backgroundImageFile;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public boolean isBackgroundImageStretched() {
		return isBackgroundImageStretched;
	}
}