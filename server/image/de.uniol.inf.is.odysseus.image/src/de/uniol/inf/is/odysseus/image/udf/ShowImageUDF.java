/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.image.udf;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@UserDefinedFunction(name = "ShowImage")
public class ShowImageUDF
		implements
		IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>>,
		ImageObserver {
	private JFrame canvas;

	private final AtomicBoolean pause = new AtomicBoolean(false);
	private int pos;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(String initString) {
		if (initString != null) {
			try {
				this.pos = Integer.parseInt(initString);
			} catch (Exception e) {
			}
		}
		this.canvas = new JFrame("Image");
		this.canvas.createBufferStrategy(2);
		this.canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_SPACE) {
					ShowImageUDF.this.pause.set(!ShowImageUDF.this.pause.get());
				}
			}

			@Override
			public void keyReleased(final KeyEvent event) {

			}

			@Override
			public void keyTyped(final KeyEvent event) {

			}

		});

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tuple<? extends IMetaAttribute> process(
			Tuple<? extends IMetaAttribute> in, int port) {
		final Image image = (Image) in.getAttribute(this.pos);
		// if ((this.canvas != null) && (this.canvas.isVisible()) &&
		// (!this.pause.get())) {
		// synchronized (this.canvas) {
		final Mat iplImage = OpenCVUtil.imageToIplImage(image);
		BufferedImage bufferedImage = new BufferedImage(iplImage.cols(),
				iplImage.rows(), BufferedImage.TYPE_BYTE_GRAY);
		bufferedImage.getRaster().setDataElements(0, 0, iplImage.cols(),
				iplImage.rows(), image.getBuffer());
		this.canvas.getGraphics().drawImage(bufferedImage, 0, 0, this);
		try {
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			iplImage.release();
		}
		// }
		// }
		return in;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/* (non-Javadoc)
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int, int, int, int)
	 */
	@Override
	public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

}
