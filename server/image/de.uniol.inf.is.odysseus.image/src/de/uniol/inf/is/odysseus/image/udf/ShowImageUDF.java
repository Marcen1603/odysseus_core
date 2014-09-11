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

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JComponent;
import javax.swing.JFrame;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@UserDefinedFunction(name = "ShowImage")
public class ShowImageUDF extends JFrame implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>>, ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 6737284952736741056L;
    private ImageCanvas canvas;
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
        this.canvas = new ImageCanvas();
        this.addKeyListener(new KeyListener() {

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
        this.setSize(700, 700);
        this.add(this.canvas, BorderLayout.CENTER);
        // this.canvas.createBufferStrategy(2);
        this.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
        final Image image = (Image) in.getAttribute(this.pos);
        if ((this.canvas != null) && (this.canvas.isVisible()) && (!this.pause.get())) {
            synchronized (this.canvas) {
            	
//                final Mat iplImage = OpenCVUtil.imageToIplImage(image);
//                BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//                bufferedImage.getRaster().setDataElements(0, 0, iplImage.cols(), iplImage.rows(), doubleToByteBuffer(image.getBuffer()));
                this.canvas.update(image.getImage());
                try {
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                finally {
//                    iplImage.release();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

//    private byte[] doubleToByteBuffer(double[] buffer) {
//        byte[] out = new byte[buffer.length];
//        for (int i = 0; i < buffer.length; i++) {
//            out[i] = (byte) ((1.0-buffer[i]) * 254.0);
//        }
//        return out;
//    }

    private static class ImageCanvas extends JComponent {

        /**
         * 
         */
        private static final long serialVersionUID = 5139434341118097558L;
        private BufferedImage image;

        public void update(BufferedImage image) {
            this.image = image;
            this.repaint();
        }

        @Override
		public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            AffineTransform tx = new AffineTransform();
            if (image != null) {
                double scalex = ((double) this.getWidth()) / ((double) this.image.getWidth());
                double scaley = ((double) this.getHeight()) / ((double) this.image.getHeight());
                tx.scale(scalex, scaley);
                g2.setTransform(tx);
                g2.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
            }
            g2.finalize();
        }
    }

}
