package de.uniol.inf.is.odysseus.scars.rcp.view3d;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.scars.rcp.view3d.util.GLRefresher;
import de.uniol.inf.is.odysseus.scars.rcp.view3d.util.GLScene;

public class OpenGLViewPart extends ViewPart {

	private GLScene scene;
	
	public void createPartControl(Composite parent) {
		setScene( createScene(parent) );
		runScene();
	}

	public void setFocus() {
		scene.setFocus();
	}
	
	protected GLScene createScene(Composite parent) {
		return new GLScene(parent);
	}
	
	protected void runScene() {
		Display.getCurrent().asyncExec(new GLRefresher(getScene()));
	}

	public GLScene getScene() {
		return scene;
	}

	private void setScene(GLScene scene) {
		Assert.isNotNull(scene);
		this.scene = scene;
	}
	
	
}