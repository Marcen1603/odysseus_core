package de.uniol.inf.is.odysseus.scars.rcp.view3d.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
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

public class View3D extends ViewPart {

	private Xith3DEnvironment env;
	private Canvas3D canvas;
	private InputHandler<?> ih;
	private TransformGroup tg;
	private RenderLoop rl;

	@Override
	public void createPartControl(Composite parent) {
		DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector.getImplementation(OpenGLLayer.JOGL_SWT).getDesktopMode();
		canvas = Canvas3DFactory.create(OpenGLLayer.JOGL_SWT, DEFAULT_DISPLAY_MODE, FullscreenMode.WINDOWED_UNDECORATED,true, FSAA.ON_8X, DisplayMode.getDefaultBPP(),parent);
		canvas.setCentered();
		
		env = new Xith3DEnvironment();
		env.addCanvas(canvas);
				
		tg = new TransformGroup();
		env.addPerspectiveBranch(createScene());
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
		rl.begin(RunMode.RUN_IN_SEPARATE_THREAD);
		parent.layout(true, true);
	}
	
	private BranchGroup createScene() {
		BranchGroup bg = new BranchGroup();
		
		Light light = new PointLight(Colorf.BLUE, new Point3f(5f, -5f, 5f),new Point3f(0.005f, 0.005f, 0.005f));
		
		 Appearance app = new Appearance();
		 app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE, Colorf.BLACK, 0.8f, Material.AMBIENT, true));
		
		Cube cube = new Cube(app);
		tg.addChild(cube);
		
		bg.addChild(tg);
		bg.addChild(light);
		return bg;
	}
	
	@Override
	public void setFocus() {
		
	}
}
