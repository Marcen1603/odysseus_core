/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.image.physicaloperator.access;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class CameraTransportHandler extends AbstractPushTransportHandler {
    static final Logger LOG = LoggerFactory.getLogger(CameraTransportHandler.class);
    private static final String NAME = "Camera";
    public static final String DEVICE_ID = "deviceid";
    public static final String DEVICE_URL = "url";

    private CameraReader reader;
    private CameraFrame output;
    private String deviceURL;
    private int deviceId;

    /**
     *
     */
    public CameraTransportHandler() {
        super();
    }

    /**
     *
     * Class constructor.
     *
     * @param protocolHandler
     *            The protocol handler
     * @param options
     *            The options
     */
    private CameraTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final CameraTransportHandler instance = new CameraTransportHandler(protocolHandler, options);
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return CameraTransportHandler.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen()  {
    }

    @Override
    public void processInStart() throws StartFailedException{
        try {
            if (this.getDeviceURL() != null) {
                this.reader = new CameraReader(this.getDeviceURL());
            }
            else {
                this.reader = new CameraReader(this.getDeviceId());

            }
            this.reader.start();
        }
        catch (final Throwable e) {
            throw new StartFailedException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        try {
            this.output = new CameraFrame();
        }
        catch (final Throwable e) {
            throw new IOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        this.reader.close();
        this.reader.interrupt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        if (this.output != null) {
            this.output.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        if (this.output != null) {
            this.output.write(message);
            // FIXME Create image here
        }
    }

    /**
     * @param deviceURL
     *            the deviceURL to set
     */
    public void setDeviceURL(final String deviceURL) {
        this.deviceURL = deviceURL;
    }

    /**
     * @return the deviceURL
     */
    public String getDeviceURL() {
        return this.deviceURL;
    }

    /**
     * @param deviceId
     *            the deviceId to set
     */
    public void setDeviceId(final int deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return the deviceId
     */
    public int getDeviceId() {
        return this.deviceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof CameraTransportHandler)) {
            return false;
        }
        final CameraTransportHandler other = (CameraTransportHandler) o;
        if ((this.deviceURL != other.getDeviceURL()) || (this.deviceId != other.getDeviceId())) {
            return false;
        }
        return true;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        this.deviceId = options.getInt(CameraTransportHandler.DEVICE_ID, 0);
        this.deviceURL = options.get(CameraTransportHandler.DEVICE_URL, null);
    }

    private class CameraReader extends Thread {
        private VideoCapture capture;

        /**
         * Class constructor.
         *
         *
         */
        public CameraReader(final String url) {
            this.capture = new VideoCapture();
            this.capture.open(url);
        }

        /**
         * Class constructor.
         *
         *
         */
        public CameraReader(final int deviceId) {
            this.capture = new VideoCapture();
            this.capture.open(deviceId);
        }

        /**
         *
         */
        public void close() {
            this.capture.release();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                while ((!this.isInterrupted()) && (this.capture != null) && (this.capture.isOpened())) {
                    final Mat frame = new Mat();
                    if (this.capture.read(frame)) {
                        final BufferedImage image = OpenCVUtil.iplImageToImage(frame);
                        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", stream);
                        final ByteBuffer byteBuffer = ByteBuffer.wrap(stream.toByteArray());
                        byteBuffer.position(byteBuffer.capacity());
                        CameraTransportHandler.this.fireProcess(byteBuffer);
                    }
                }
            }
            catch (final Exception e) {
                CameraTransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                if (this.capture != null) {
                    this.capture.release();
                    this.capture = null;
                }
            }
        }
    }

    private class CameraFrame extends JFrame {

        /**
         *
         */
        private static final long serialVersionUID = 863541789005219153L;
        private final ImageCanvas canvas;
        final AtomicBoolean pause = new AtomicBoolean(false);

        /**
         * Class constructor.
         *
         *
         */
        public CameraFrame() {
            this.canvas = new ImageCanvas();

            this.addKeyListener(new KeyListener() {

                @Override
                public void keyPressed(final KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                        CameraFrame.this.pause.set(!CameraFrame.this.pause.get());
                    }
                }

                @Override
                public void keyReleased(final KeyEvent event) {
                    // Empty block
                }

                @Override
                public void keyTyped(final KeyEvent event) {
                    // Empty block
                }
            });
            this.setSize(800, 600);
            this.add(this.canvas, BorderLayout.CENTER);
            // this.canvas.createBufferStrategy(2);
            this.setVisible(true);
        }

        /**
         *
         */
        public void close() {
            this.dispose();
        }

        public void write(final byte[] data) throws IOException {
            final BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            this.canvas.update(image);
        }

    }

    private static class ImageCanvas extends JComponent {

        /**
         *
         */
        private static final long serialVersionUID = 5139434341118097558L;
        private BufferedImage image;

        /**
         * Class constructor.
         *
         */
        public ImageCanvas() {
        }

        public void update(final BufferedImage i) {
            this.image = i;
            this.repaint();
        }

        @Override
        public void paint(final Graphics g) {
            final Graphics2D g2 = (Graphics2D) g;
            final AffineTransform tx = new AffineTransform();
            if (this.image != null) {
                final double scalex = ((double) this.getWidth()) / ((double) this.image.getWidth());
                final double scaley = ((double) this.getHeight()) / ((double) this.image.getHeight());
                tx.scale(scalex, scaley);
                g2.setTransform(tx);
                g2.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), this);
            }
            g2.finalize();
        }
    }
}
