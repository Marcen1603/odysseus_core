package de.uniol.inf.is.odysseus.rcp.benchmarker.gui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
//		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}
}
