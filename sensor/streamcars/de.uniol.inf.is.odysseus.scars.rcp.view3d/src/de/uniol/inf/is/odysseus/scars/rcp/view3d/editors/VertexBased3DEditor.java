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
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.TriangleStripArray;
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

	private static final float CAR_HEIGHT = 3;
	private String x_pos_name = "posx";
	private String y_pos_name = "posy";
	private String z_pos_name = "posz";
	private String car_vertices_name = "vertices";
	private TransformGroup carGroup;
	private SchemaIndexPath carPath;

	private Composite parent;

	@Override
	public void streamElementRecieved(final Object element, int port) {
		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;

		TupleIndexPath tuplePath = carPath.toTupleIndexPath(tuple);

		carGroup.removeAllChildren();
		
		for (TupleInfo car : new TupleIterator(tuple, tuplePath, 1)) {
			Point3f position = getVertex(tuple, car.tupleIndexPath);
			List<Point3f> vertices = new ArrayList<Point3f>();

			for (TupleInfo carObject : car.tupleIndexPath) {
				if (carObject.attribute.getAttributeName().equals(
						car_vertices_name)) {
					for (TupleInfo vertex : new TupleIterator(tuple, carObject.tupleIndexPath, 1)) {
						vertices.add(getVertex(tuple, vertex.tupleIndexPath));
					}
				}
			}

			carGroup.addChild(buildCube(this.carGroup, position, vertices));
		}
	}

	private Point3f getVertex(MVRelationalTuple<?> tuple, TupleIndexPath iterator) {
		Point3f vertex = new Point3f();

		for (TupleInfo carObject : new TupleIterator(tuple, iterator, 1)) {
			if (carObject.attribute.getAttributeName().equals(x_pos_name)) {
				vertex.setX((Float) carObject.tupleObject);
			} else if (carObject.attribute.getAttributeName()
					.equals(y_pos_name)) {
				vertex.setY((Float) carObject.tupleObject);
			} else if (carObject.attribute.getAttributeName()
					.equals(z_pos_name)) {
				vertex.setZ((Float) carObject.tupleObject);
			} else if (carObject.attribute.getAttributeName().equals(
					car_vertices_name)) {
			}
		}
		return vertex;
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources()
				.iterator().next();
		SDFAttributeList schema = src.getOutputSchema();
		SchemaHelper helper = new SchemaHelper(schema);
		carPath = helper.getSchemaIndexPath("scan:cars:car");
		x_pos_name = "x_pos";
		y_pos_name = "y_pos";
		z_pos_name = "z_pos";
		car_vertices_name = "vertices";
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector
				.getImplementation(OpenGLLayer.JOGL_SWT).getDesktopMode();
		Canvas3D canvas = Canvas3DFactory.create(OpenGLLayer.JOGL_SWT,
				DEFAULT_DISPLAY_MODE, FullscreenMode.WINDOWED_UNDECORATED,
				true, FSAA.ON_8X, DisplayMode.getDefaultBPP(), parent);
		canvas.setCentered();

		Xith3DEnvironment env = new Xith3DEnvironment();
		env.addCanvas(canvas);

		TransformGroup root = new TransformGroup();

		carGroup = new TransformGroup();
		root.addChild(carGroup);

		TransformGroup scene = new TransformGroup();
		root.addChild(scene);

		BranchGroup bg = new BranchGroup();

		Light light = new PointLight(Colorf.BLUE, new Point3f(5f, -5f, 5f),
				new Point3f(0.005f, 0.005f, 0.005f));

		bg.addChild(light);
		bg.addChild(root);

		createScene(scene);

		env.addPerspectiveBranch(bg);
		env.getView().lookAt(new Tuple3f(0, 2, 0), new Tuple3f(0, 0, -100),
				new Tuple3f(0, 1, 0));

		try {
			InputSystem.getInstance().registerNewKeyboardAndMouse(
					canvas.getPeer());
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

	private TransformGroup buildCube(TransformGroup carGroup2, Point3f position,
			List<Point3f> vertices) {
		List<Point3f> newVertices = new ArrayList<Point3f>(); 
		for (Point3f point3f : vertices) {
			Point3f topVertex = new Point3f(point3f);
			topVertex.addY(CAR_HEIGHT);
			newVertices.add(topVertex);
			newVertices.add(point3f);
		}
		
		Appearance app = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE,
				Colorf.BLACK, 0.8f, Material.AMBIENT, true));
		Shape3D car = new Shape3D(new TriangleStripArray(vertices.size() * 2), app);
		
		TransformGroup cube = new TransformGroup();
		cube.addChild(car);
		cube.setTransform(new Transform3D(-position.getY(), position.getZ(), -position.getX()));

		return cube;
	}

	private void createScene(TransformGroup sceneGroup) {

		for (float z = -100.0f; z < 100.0f; z += 10.0f) {
			Line l = new Line(new Tuple3f(-100.0f, 0.0f, z), new Tuple3f(
					100.0f, 0.0f, z), new Colorf(0, 1, 0));

			sceneGroup.addChild(l);
		}

		for (float x = -100.0f; x < 100.0f; x += 10.0f) {
			Line l = new Line(new Tuple3f(x, 0.0f, 100), new Tuple3f(x, 0.0f,
					-100), new Colorf(0, 1, 0));

			sceneGroup.addChild(l);
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
