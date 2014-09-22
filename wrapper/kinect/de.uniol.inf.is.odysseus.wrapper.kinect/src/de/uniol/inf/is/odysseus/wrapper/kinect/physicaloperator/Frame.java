package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class Frame extends JFrame {
    /** Auto generated serial UID. */
    private static final long serialVersionUID = -5273736224160113453L;

    /** Stores the last color image. */
    private BufferedImage colorImage;

    /** Stores the last depth image. */
    private BufferedImage depthImage;

    /** Stores the last calculated width. */
    private int lastWidth;

    /** Stores the last calculated height. */
    private int lastHeight;

    /**
     * Constructs a Frame.
     * @param name
     * Caption of the frame.
     */
    public Frame(String name) {
        super(name);
        this.colorImage = null;
        this.depthImage = null;
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.lastHeight = getHeight();
        this.lastWidth = getWidth();
        this.setVisible(true);
    }

    /**
     * Sets the color image and repaint the frame.
     * @param image
     * New color image.
     */
    public void setColorImage(BufferedImage image) {
        this.colorImage = image;
        recalculateSizes();
        repaint();
    }

    /**
     * Sets the depth image and repaint the frame.
     * @param image
     * New depth image.
     */
    public void setDepthImage(BufferedImage image) {
        this.depthImage = image;
        recalculateSizes();
        repaint();
    }

    /**
     * Calculates the needed size to show up the images (or one image) and resizes the
     * frame if it had the wrong size.
     */
    private void recalculateSizes() {
        int width = 0;
        int height = 0;
        if (colorImage != null) {
            width += colorImage.getWidth();
            height = Math.max(height, colorImage.getHeight());
        }
        if (depthImage != null) {
            width += depthImage.getWidth();
            height = Math.max(height, depthImage.getHeight());
        }
        if (lastWidth != width || lastHeight != height) {
            lastWidth = width;
            lastHeight = height;
            setSize(width, height);
        }
    }

    @Override
    public void paint(Graphics g) {
        int left = 0;
        if (colorImage != null) {
            g.drawImage(colorImage, 0, 0, null);
            left += colorImage.getWidth();
        }

        if (depthImage != null) {
            g.drawImage(depthImage, left, 0, null);
        }
    }
}
