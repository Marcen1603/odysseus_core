/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.scars.rcp.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleInfo;
import de.uniol.inf.is.odysseus.scars.rcp.StreamCarsRCPPlugIn;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SchemaIndexPath;

public class DoubleEditor3D implements IStreamEditorType {

	private Xith3DEnvironment env;
	private Canvas3D canvas;
	private InputHandler<?> ih;
	private TransformGroup carGroup;
	private RenderLoop rl;
	private SDFAttributeList schema;
	private SchemaIndexPath carPath;
	private SchemaIndexPath carsPath;
	private Image warn;
	private Image ok;
	private Label labelText;
	private boolean warning;

	private static final int MAX_CUBE_COUNT = 50;
	private TransformGroup[] cubes = new TransformGroup[MAX_CUBE_COUNT];
	private Canvas image;
	private static int activeCubes = 0;

	@Override
	public void streamElementRecieved(final Object element, int port) {

		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;

		TupleIndexPath carsTuple = TupleIndexPath.fromSchemaIndexPath(carsPath, tuple);
		if (((MVRelationalTuple<?>) carsTuple.getTupleObject())
				.getAttributeCount() == 0) {

			for (int i = 0; i < activeCubes; i++) {
				Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100,
						-1000 - i * 100, 1000 + i * 100));
				cubes[i].setTransform(t);
			}
			activeCubes = 0;

			return;
		}

		TupleIndexPath tuplePath = TupleIndexPath.fromSchemaIndexPath(carPath, tuple);

		int counter = 0;

		if (tuplePath.getLength() <= 0) {
			if (warning) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						image.redraw();
						warning = false;
					}
				});
			}
		} else {
			double distance = 0;
			double distanceTMP = 0;
			for (TupleInfo car : tuplePath) {
				MVRelationalTuple<?> carTuple = (MVRelationalTuple<?>) car.tupleObject;

				float x = carTuple.getAttribute(2);
				float y = carTuple.getAttribute(3);
				float z = carTuple.getAttribute(4);

				Transform3D t = new Transform3D(y, z, -x);
				cubes[counter].setTransform(t);

				counter++;

				distanceTMP = Math.sqrt(x * x + y * y + z * z);
				if (distanceTMP > distance) {
					distance = distanceTMP;
				}
			}

			final double setter = distance;
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!warning) {
						image.redraw();
						warning = true;
					}
					labelText.setText("Entfernung: " + setter);
				}
			});
		}

		if (activeCubes > counter) {
			for (int i = counter; i < Math.min(activeCubes, MAX_CUBE_COUNT); i++) {
				Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100,
						-1000 - i * 100, 1000 + i * 100));
				cubes[i].setTransform(t);
			}
		}
		activeCubes = Math.min(counter, MAX_CUBE_COUNT);
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources()
				.iterator().next();
		schema = src.getOutputSchema();
		SchemaHelper helper = new SchemaHelper(schema);
		carPath = helper.getSchemaIndexPath("scan:cars:car");
		carsPath = helper.getSchemaIndexPath("scan:cars");
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite base = new Composite(parent, SWT.NONE);
		base.setLayout(new GridLayout(2, true));

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;

		Composite base3d = new Composite(base, SWT.FILL);
		base3d.setLayoutData(data);
		base3d.setLayout(new FillLayout());

		DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector
				.getImplementation(OpenGLLayer.JOGL_SWT).getDesktopMode();
		canvas = Canvas3DFactory.create(OpenGLLayer.JOGL_SWT,
				DEFAULT_DISPLAY_MODE, FullscreenMode.WINDOWED_UNDECORATED,
				true, FSAA.OFF, DEFAULT_DISPLAY_MODE.getBPP(), base3d);
		canvas.setCentered();

		env = new Xith3DEnvironment();
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
		createCubes(carGroup);

		env.addPerspectiveBranch(bg);
		env.getView().lookAt(new Tuple3f(0, 2, 0), new Tuple3f(0, 0, -100),
				new Tuple3f(0, 1, 0));

		try {
			InputSystem.getInstance().registerNewKeyboardAndMouse(
					canvas.getPeer());
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

		Composite baseIcon = new Composite(base, SWT.BORDER);
		baseIcon.setLayout(new GridLayout(1, true));
		baseIcon.setLayoutData(data);

		image = new Canvas(baseIcon, SWT.NONE);
		image.setLayoutData(data);

		warn = ImageDescriptor.createFromURL(
				StreamCarsRCPPlugIn.getDefault().getBundle()
						.getResource("icons/warn.png")).createImage();

		ok = ImageDescriptor.createFromURL(
				StreamCarsRCPPlugIn.getDefault().getBundle().getResource("icons/ok.png"))
				.createImage();

		image.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				double scale = 0;
				int width = warn.getImageData().width;
				int height = warn.getImageData().height;
				if (width > image.getSize().x) {
					width = image.getSize().x;
					scale = (double) width / (double) warn.getImageData().width;
					height = (int) (height * scale);
				}
				if (height > warn.getImageData().height) {
					height = warn.getImageData().height;
					scale = (double) height / (double) warn.getImageData().height;
					width = (int) (width * scale);
				}
				if (warning) {
					e.gc.drawImage(warn, 0, 0, warn.getImageData().width,
							warn.getImageData().height, 0, 0, width, height);
				} else {
					e.gc.drawImage(ok, 0, 0, ok.getImageData().width,
							ok.getImageData().height, 0, 0, width, height);
				}
			}
		});

		labelText = new Label(baseIcon, SWT.CENTER);
		
		Font font = new Font(parent.getDisplay(), new FontData("Courier New", 20, SWT.NORMAL));
		labelText.setFont(font);
		
		GridData dataT = new GridData(0, 30);
		dataT.horizontalAlignment = SWT.FILL;
		dataT.grabExcessHorizontalSpace = true;
		labelText.setLayoutData(dataT);

		parent.layout(true, true);
	}

	private void createCubes(TransformGroup carGroup) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(Colorf.BLACK, Colorf.RED, Colorf.WHITE,
				Colorf.BLACK, 0.8f, Material.AMBIENT, true));

		for (int i = 0; i < cubes.length; i++) {
			cubes[i] = new TransformGroup();
			Cube c = new Cube(2, app);
			cubes[i].addChild(c);

			Transform3D t = new Transform3D(new Tuple3f(-1000 - i * 100, -1000
					- i * 100, 1000 + i * 100));
			cubes[i].setTransform(t);

			carGroup.addChild(cubes[i]);
		}
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
	}

	@Override
	public void dispose() {
		warn.dispose();
		ok.dispose();
		image.dispose();
	}

}
