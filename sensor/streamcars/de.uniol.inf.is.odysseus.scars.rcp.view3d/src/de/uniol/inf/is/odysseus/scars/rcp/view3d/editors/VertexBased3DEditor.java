package de.uniol.inf.is.odysseus.scars.rcp.view3d.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.InputSystemException;
import org.jagatoo.input.handlers.InputHandler;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.ObjectRotationInputHandler;
import org.xith3d.loop.RenderLoop;
import org.xith3d.loop.RenderLoop.RunMode;
import org.xith3d.loop.RenderLoop.StopOperation;
import org.xith3d.loop.UpdatingThread.TimingMode;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.Canvas3DFactory;
import org.xith3d.render.config.DisplayMode;
import org.xith3d.render.config.DisplayMode.FullscreenMode;
import org.xith3d.render.config.DisplayModeSelector;
import org.xith3d.render.config.FSAA;
import org.xith3d.render.config.OpenGLLayer;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.Light;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.PointLight;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Cube;
import org.xith3d.scenegraph.primitives.Line;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class VertexBased3DEditor implements IStreamEditorType {

	private String x_pos_name = "posx";
	private String y_pos_name = "posy";
	private String z_pos_name = "posz";
	private String x_pos_name_np = "posx_np";
	private String y_pos_name_np = "posy_np";
	private String z_pos_name_np = "posz_np";
	private String car_vertices_name = "points";
	private TransformGroup carGroup;
	private SchemaIndexPath carPath;
	
	
	private Car[] cubes = new Car[20];
	private static int activeCubes = 0;

	private Composite parent;
	
	public void streamElementRecieved(final Object element, int port) {

		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;

		TupleIndexPath tuplePath = carPath.toTupleIndexPath(tuple);
		
		if (((MVRelationalTuple<?>) tuplePath.getTupleObject()).getAttributeCount() == 0) {
			for (int i = 0; i < activeCubes; i++) {
				Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100, -1000 - i * 100, 1000 + i * 100));
				cubes[i].setTransform(t);
			}
			activeCubes = 0;

			return;
		}
		
		int counter = 0;
		for (TupleInfo car : new TupleIterator(tuple, tuplePath.clone(), 0)) {
			Point3f position = getVertex(tuple, car.tupleIndexPath, x_pos_name, y_pos_name, z_pos_name);
			Point3f positionnp = getVertex(tuple, car.tupleIndexPath, x_pos_name_np, y_pos_name_np, z_pos_name_np);
			List<Point3f> vertices = new ArrayList<Point3f>();

			for (TupleInfo carObject :  new TupleIterator(tuple, car.tupleIndexPath, 1)) {
				if (carObject.attribute.getAttributeName().equals(car_vertices_name)) {
					for (TupleInfo vertex : new TupleIterator(tuple, carObject.tupleIndexPath, 1)) {
						if (!vertex.attribute.getAttributeName().equals(car_vertices_name)) 
							vertices.add(getVertex(tuple, vertex.tupleIndexPath, x_pos_name, y_pos_name, z_pos_name));
					}
				}
			}
			
			cubes[counter].setCenterPosition(position);
			cubes[counter].setNearPosition(positionnp);
			cubes[counter].setVertices(vertices);
			
			Transform3D t = new Transform3D(new Tuple3f(0, 0, 0));
			cubes[counter].setTransform(t);
			counter++;
		}

		if (activeCubes > counter) {
			for (int i = counter; i < activeCubes; i++) {
				Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100, -1000 - i * 100, 1000 + i * 100));
				cubes[i].setTransform(t);
			}
		}
		activeCubes = counter;

	}
//
////	@Override
////	public void streamElementRecieved(final Object element, int port) {
////		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;
////
////		TupleIndexPath tuplePath = carPath.toTupleIndexPath(tuple);
////		
////		System.out.println(carGroup.getTotalNumChildren());
////		
////		for (int i = 0; i < carGroup.getTotalNumChildren(); i++) {
////			carGroup.getChild(i).detach();
////		}
////
////		carGroup.removeAllChildren();
////
////		for (TupleInfo car : new TupleIterator(tuple, tuplePath.clone(), 0)) {
////			Point3f position = getVertex(tuple, car.tupleIndexPath, x_pos_name, y_pos_name, z_pos_name);
////			Point3f positionnp = getVertex(tuple, car.tupleIndexPath, x_pos_name_np, y_pos_name_np, z_pos_name_np);
////			List<Point3f> vertices = new ArrayList<Point3f>();
////
////			for (TupleInfo carObject :  new TupleIterator(tuple, car.tupleIndexPath, 1)) {
////				if (carObject.attribute.getAttributeName().equals(car_vertices_name)) {
////					for (TupleInfo vertex : new TupleIterator(tuple, carObject.tupleIndexPath, 1)) {
////						if (!vertex.attribute.getAttributeName().equals(car_vertices_name)) 
////							vertices.add(getVertex(tuple, vertex.tupleIndexPath, x_pos_name, y_pos_name, z_pos_name));
////					}
////				}
////			}
////
////			carGroup.addChild(buildCube(this.carGroup, position, positionnp, vertices));
////		}
//	}

	private Point3f getVertex(MVRelationalTuple<?> tuple, TupleIndexPath iterator, String xpos, String ypos, String zpos) {
		Point3f vertex = new Point3f();

		for (TupleInfo carObject : new TupleIterator(tuple, iterator.clone(), 1)) {
			if (carObject.attribute.getAttributeName().equals(xpos)) {
				vertex.setZ(- (Float) carObject.tupleObject);
			} else if (carObject.attribute.getAttributeName().equals(ypos)) {
				vertex.setX(- (Float) carObject.tupleObject);
			} else if (carObject.attribute.getAttributeName().equals(zpos)) {
				vertex.setY((Float) carObject.tupleObject);
			}
		}
		return vertex;
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources().iterator().next();
		SDFAttributeList schema = src.getOutputSchema();
		SchemaHelper helper = new SchemaHelper(schema);
		carPath = helper.getSchemaIndexPath("scan:cars:car");
		x_pos_name = "posx";
		y_pos_name = "posy";
		z_pos_name = "posz";
		x_pos_name_np = "posx_np";
		y_pos_name_np = "posy_np";
		z_pos_name_np = "posz_np";
		car_vertices_name = "points";
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector.getImplementation(OpenGLLayer.JOGL_SWT).getDesktopMode();
		Canvas3D canvas = Canvas3DFactory.create(OpenGLLayer.JOGL_SWT, DEFAULT_DISPLAY_MODE, FullscreenMode.WINDOWED_UNDECORATED, true, FSAA.ON_8X, DisplayMode.getDefaultBPP(), parent);
		canvas.setCentered();

		Xith3DEnvironment env = new Xith3DEnvironment();
		env.addCanvas(canvas);

		TransformGroup root = new TransformGroup();

		carGroup = new TransformGroup();
		root.addChild(carGroup);

		TransformGroup scene = new TransformGroup();
		root.addChild(scene);

		BranchGroup bg = new BranchGroup();

		Light light = new PointLight(Colorf.BLUE, new Point3f(5f, -5f, 5f), new Point3f(0.005f, 0.005f, 0.005f));

		bg.addChild(light);
		bg.addChild(root);

		createScene(scene);
		createCubes(carGroup);

		env.addPerspectiveBranch(bg);
		env.getView().lookAt(new Tuple3f(0, 2, 0), new Tuple3f(0, 0, -100), new Tuple3f(0, 1, 0));

		try {
			InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
			InputHandler<?> ih = new ObjectRotationInputHandler(root);
			InputSystem.getInstance().addInputHandler(ih);
		} catch (InputSystemException e) {
			e.printStackTrace();
		}

		RenderLoop rl = new RenderLoop();
		rl.setXith3DEnvironment(env);
		rl.setTimingMode(TimingMode.NANOSECONDS);
		rl.setStopOperation(StopOperation.DESTROY);
		rl.setPauseMode(RenderLoop.PAUSE_TOTAL);
		rl.setMaxFPS(60);
		rl.begin(RunMode.RUN_IN_SEPARATE_THREAD);
		parent.layout(true, true);
	}

	private void createScene(TransformGroup sceneGroup) {

		for (float z = -100.0f; z < 100.0f; z += 10.0f) {
			Line l = new Line(new Tuple3f(-100.0f, 0.0f, z), new Tuple3f(100.0f, 0.0f, z), new Colorf(0, 1, 0));

			sceneGroup.addChild(l);
		}

		for (float x = -100.0f; x < 100.0f; x += 10.0f) {
			Line l = new Line(new Tuple3f(x, 0.0f, 100), new Tuple3f(x, 0.0f, -100), new Colorf(0, 1, 0));

			sceneGroup.addChild(l);
		}

	}
	
	private void createCubes(TransformGroup carGroup) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));

		for (int i = 0; i < cubes.length; i++) {
			cubes[i] = new Car();
			Cube c = new Cube(app);
			cubes[i].addChild(c);

			Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100, -1000 - i * 100, 1000 + i * 100));
			cubes[i].setTransform(t);

			carGroup.addChild(cubes[i]);
		}
	}

	@Override
	public void setFocus() {
		this.parent.setFocus();
	}

	@Override
	public void dispose() {

	}
}
