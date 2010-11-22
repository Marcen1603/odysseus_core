package de.uniol.inf.is.odysseus.scars.rcp.view3d;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.scars.rcp.view3d.util.GLRefresher;
import de.uniol.inf.is.odysseus.scars.rcp.view3d.util.GLScene;

public class Stream3DEditor implements IStreamEditorType {

	private GLScene scene;
	
	public Stream3DEditor() {
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
	}

	@Override
	public void createPartControl(Composite parent) {
		setScene( createScene(parent) );
		runScene();
	}

	@Override
	public void setFocus() {
		scene.setFocus();
	}

	@Override
	public void dispose() {
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
