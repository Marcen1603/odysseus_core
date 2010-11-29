package de.uniol.inf.is.odysseus.scars.rcp.view3d.editors;

import org.eclipse.swt.widgets.Composite;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.InputSystemException;
import org.jagatoo.input.handlers.InputHandler;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
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
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Cube;

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
	private TransformGroup tg;
	private RenderLoop rl;
	private SDFAttributeList schema;
	private SchemaIndexPath carsPath;

	@Override
	public void streamElementRecieved(Object element, int port) {
		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;
		TupleIndexPath tuplePath = carsPath.toTupleIndexPath(tuple);
		
		rl.pauseRendering();
		tg.removeAllChildren();
		for( TupleInfo car : tuplePath ) {
			
			MVRelationalTuple<?> carTuple = (MVRelationalTuple<?>)car.tupleObject;
			
			float x = carTuple.getAttribute(3);
			float y = carTuple.getAttribute(4);
			float z = carTuple.getAttribute(5);
			
			System.out.print(x + " " + y + " " + z + " ---- ");
			
			TransformGroup t = buildCar(x,y,z);
			tg.addChild(t);
		}
		System.out.println();
		rl.resumeRendering();
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
		
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources().iterator().next();
		schema = src.getOutputSchema();
		SchemaHelper helper = new SchemaHelper(schema);
		carsPath = helper.getSchemaIndexPath("scan:cars:car");
	}

	@Override
	public void createPartControl(Composite parent) {
		DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector.getImplementation(OpenGLLayer.JOGL_SWT).getDesktopMode();
		canvas = Canvas3DFactory.create(OpenGLLayer.JOGL_SWT, DEFAULT_DISPLAY_MODE, FullscreenMode.WINDOWED_UNDECORATED,true, FSAA.ON_8X, DisplayMode.getDefaultBPP(),parent);
		canvas.setCentered();
		
		env = new Xith3DEnvironment();
		env.addCanvas(canvas);
				
		tg = new TransformGroup();
		env.addPerspectiveBranch(createScene(tg));
		try {
			InputSystem.getInstance().registerNewKeyboardAndMouse( canvas.getPeer() );
			ih = new ObjectRotationInputHandler(tg);
	        InputSystem.getInstance().addInputHandler(ih);
		} catch (InputSystemException e) {
			e.printStackTrace();
		}
		
        rl = new RenderLoop();
		rl.setXith3DEnvironment(env);
		rl.setTimingMode(TimingMode.NANOSECONDS);
		rl.setStopOperation(StopOperation.DESTROY);
		rl.setPauseMode(RenderLoop.PAUSE_TOTAL);
		rl.begin(RunMode.RUN_IN_SEPARATE_THREAD);
		parent.layout(true, true);
	}

	private BranchGroup createScene( TransformGroup tg ) {
		BranchGroup bg = new BranchGroup();
		
		Light light = new PointLight(Colorf.BLUE, new Point3f(5f, -5f, 5f),new Point3f(0.005f, 0.005f, 0.005f));

		bg.addChild(tg);
		bg.addChild(light);
		return bg;
	}
	
	private TransformGroup buildCar( float x, float y, float z) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));
		
		TransformGroup t = new TransformGroup();
		Cube cube = new Cube(app);
		t.addChild(cube);
		t.setPosition(z, x, y);
		return t;
	}
	
	@Override
	public void setFocus() {
		
	}

	@Override
	public void dispose() {
		
	}

}
