package de.uniol.inf.is.odysseus.scars.rcp.view3d.editors;

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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class Editor3D implements IStreamEditorType {

	private Xith3DEnvironment env;
	private Canvas3D canvas;
	private InputHandler<?> ih;
	private TransformGroup carGroup;
	private RenderLoop rl;
	private SDFAttributeList schema;
	private SchemaIndexPath carPath;
	private SchemaIndexPath carsPath;

	private TransformGroup[] cubes = new TransformGroup[50];
	private static int activeCubes = 0;

	@Override
	public void streamElementRecieved(final Object element, int port) {

		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;

		TupleIndexPath carsTuple = carsPath.toTupleIndexPath(tuple);
		if (((MVRelationalTuple<?>) carsTuple.getTupleObject()).getAttributeCount() == 0) {

			for (int i = 0; i < activeCubes; i++) {
				Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100, -1000 - i * 100, 1000 + i * 100));
				cubes[i].setTransform(t);
			}
			activeCubes = 0;

			return;
		}

		TupleIndexPath tuplePath = carPath.toTupleIndexPath(tuple);

		int counter = 0;
		for (TupleInfo car : tuplePath) {

			MVRelationalTuple<?> carTuple = (MVRelationalTuple<?>) car.tupleObject;

			float x = carTuple.getAttribute(3);
			float y = carTuple.getAttribute(4);
			float z = carTuple.getAttribute(5);

			Transform3D t = new Transform3D(y, z, -x);
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

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources().iterator().next();
		schema = src.getOutputSchema();
		SchemaHelper helper = new SchemaHelper(schema);
		carPath = helper.getSchemaIndexPath("scan:cars:car");
		carsPath = helper.getSchemaIndexPath("scan:cars");
	}

	@Override
	public void createPartControl(Composite parent) {
		DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector.getImplementation(OpenGLLayer.JOGL_SWT).getDesktopMode();
		canvas = Canvas3DFactory.create(OpenGLLayer.JOGL_SWT, DEFAULT_DISPLAY_MODE, FullscreenMode.WINDOWED_UNDECORATED, true, FSAA.ON_8X, DisplayMode.getDefaultBPP(), parent);
		canvas.setCentered();

		env = new Xith3DEnvironment();
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
			ih = new ObjectRotationInputHandler(root);
			InputSystem.getInstance().addInputHandler(ih);
		} catch (InputSystemException e) {
			e.printStackTrace();
		}

		rl = new RenderLoop();
		rl.setXith3DEnvironment(env);
		rl.setTimingMode(TimingMode.NANOSECONDS);
		rl.setStopOperation(StopOperation.DESTROY);
		rl.setPauseMode(RenderLoop.PAUSE_TOTAL);
		rl.setMaxFPS(60);
		rl.begin(RunMode.RUN_IN_SEPARATE_THREAD);
		parent.layout(true, true);
	}

	private void createCubes(TransformGroup carGroup) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));

		for (int i = 0; i < cubes.length; i++) {
			cubes[i] = new TransformGroup();
			Cube c = new Cube(2, app);
			cubes[i].addChild(c);

			Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100, -1000 - i * 100, 1000 + i * 100));
			cubes[i].setTransform(t);

			carGroup.addChild(cubes[i]);
		}
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

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {

	}

}
