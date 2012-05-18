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

package de.uniol.inf.is.odysseus.spatial.grid.physicaloperator;

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualGridSinkPO extends AbstractSink<Object> {
	private CanvasFrame canvas;
	private final SDFSchema schema;
	private final AtomicBoolean pause = new AtomicBoolean(false);
	private int fps;
	private long last;
	private final CvFont font = new CvFont(
			opencv_core.CV_FONT_HERSHEY_SCRIPT_SIMPLEX, 0.4, 1);

	public VisualGridSinkPO(final SDFSchema schema) {
		this.schema = schema;
	}

	public VisualGridSinkPO(final VisualGridSinkPO po) {
		this.schema = po.schema;
	}

	@Override
	public void open() throws OpenFailedException {
		super.open();
		this.fps = 0;
		last = 0l;
		this.canvas = new CanvasFrame("Grid");
		canvas.getCanvas().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_SPACE) {
					pause.set(!pause.get());
				}
			}

			@Override
			public void keyReleased(KeyEvent event) {

			}

			@Override
			public void keyTyped(KeyEvent event) {

			}

		});
	}

	@Override
	public void close() {
		super.close();
		if (this.canvas != null) {
			this.canvas.dispose();
			this.canvas = null;
		}
	}

	@Override
	public VisualGridSinkPO clone() {
		System.out.println("Create clone");
		return new VisualGridSinkPO(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(final Object object, final int port,
			final boolean isReadOnly) {
		this.fps = (int) (1 / (((double) (((Tuple<TimeInterval>) object)
				.getMetadata().getStart().getMainPoint() - last)) / 1000.0) + 0.5);
		this.last = ((Tuple<TimeInterval>) object).getMetadata().getStart()
				.getMainPoint();
		if ((this.canvas != null) && (canvas.isVisible()) && (!pause.get())) {
			CartesianGrid grid = (CartesianGrid) ((Tuple<TimeInterval>) object)
					.getAttribute(0);
			IplImage image = grid.getImage();
			cvRectangle(image, new CvPoint(0, 0),
					new CvPoint(150,50),
					opencv_core.cvScalarAll(0.5), CV_FILLED, 8, 0);
			opencv_core.cvPutText(image,
					"FPS: " + fps + " Lag: "
							+ (System.currentTimeMillis() - last), new CvPoint(
							5, 15), font, CvScalar.RED);

			this.canvas.showImage(image);
		}
	}

	@Override
	public void processPunctuation(final PointInTime timestamp, final int port) {
		if ((this.canvas != null) && (canvas.isVisible()) && (!pause.get())) {
			this.canvas.setBackground(new Color(255, 0, 0));
		}
	}

}
