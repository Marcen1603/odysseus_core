package de.uniol.inf.is.odysseus.scars.rcp.view3d;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.scars.rcp.view3d.util.GLRefresher;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class Stream3DEditor implements IStreamEditorType {

	private String xPosName = "posx";
	private String yPosName = "posy";

	private GLSceneEx scene;
	private SDFAttributeList schema;
	private SchemaHelper helper;
	private SchemaIndexPath pathToList;

	public Stream3DEditor() {
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>)element;
	
		List<Car> cars = new ArrayList<Car>();
		for( TupleInfo info : this.pathToList.toTupleIndexPath(tuple) ) {
			
			MVRelationalTuple<?> car = (MVRelationalTuple<?>)info.tupleObject;
			Car c = new Car();
			
			for( int i = 0; i < info.attribute.getSubattributeCount(); i++ ) {
				if( info.attribute.getSubattribute(i).getAttributeName().equals(this.xPosName)) {
					c.setX((Double)car.getAttribute(i));
				} else if( info.attribute.getSubattribute(i).getAttributeName().equals(this.yPosName)) {
					c.setY((Double)car.getAttribute(i));
				}
			}	
			
			cars.add(c);
		}
		
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources().iterator().next();
		this.schema = src.getOutputSchema();
		this.helper = new SchemaHelper(this.schema);
		this.pathToList = this.helper.getSchemaIndexPath("scan:cars");
	}

	@Override
	public void createPartControl(Composite parent) {
		setScene(createScene(parent));
		runScene();
	}

	@Override
	public void setFocus() {
		scene.setFocus();
	}

	@Override
	public void dispose() {
	}

	protected GLSceneEx createScene(Composite parent) {
		return new GLSceneEx(parent);
	}

	protected void runScene() {
		Display.getCurrent().asyncExec(new GLRefresher(getScene()));
	}

	public GLSceneEx getScene() {
		return scene;
	}

	private void setScene(GLSceneEx scene) {
		Assert.isNotNull(scene);
		this.scene = scene;
	}
}
