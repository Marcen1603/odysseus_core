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
import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

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
public class ShowImageUDF implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {
    private CanvasFrame canvas;

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
            }
            catch (Exception e) {
            }
        }
        this.canvas = new CanvasFrame("Image");
        this.canvas.createBufferStrategy(2);
        this.canvas.getCanvas().addKeyListener(new KeyListener() {

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
    public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
        final Image image = (Image) in.getAttribute(this.pos);
        if ((this.canvas != null) && (this.canvas.isVisible()) && (!this.pause.get())) {
            synchronized (this.canvas) {
                final IplImage iplImage = OpenCVUtil.imageToIplImage(image);
                try {
                    this.canvas.showImage(iplImage);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                finally {
                    iplImage.release();
                }
            }
        }
        return in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;
    }

}
