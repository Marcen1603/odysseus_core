/*******************************************************************************
 * LMS1xx protocol handler for the Odysseus data stream management system
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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
package de.uniol.inf.is.odysseus.wrapper.lms1xx.udf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@UserDefinedFunction(name = "Laserscan")
public class PolarFrameUDF implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {
    /** The logger. */
    private final Logger LOG = LoggerFactory.getLogger(PolarFrameUDF.class);
    /** The screen. */
    private PolarFrame screen;
    /** Position of the distance matrix in the schema. */
    private int pos = 0;

    @Override
    public void init(String initString) {
        if (initString != null) {
            try {
                this.pos = Integer.parseInt(initString);
            }
            catch (Exception e) {
            }
        }
        try {
            this.screen = new PolarFrame();
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
        Object[] attributes = in.getAttributes();
        if (attributes.length > pos) {
            Object value = attributes[pos];
            if (value != null) {
                this.screen.process_next((double[][]) value);
            }
        }
        return in;
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    private class PolarFrame extends JFrame {

        /**
         * 
         */
        private static final long serialVersionUID = -1799834367557055057L;
        private static final int HEIGHT = 1000;
        private static final int WIDTH = 1000;
        private final PolarFrameMap map;

        public PolarFrame() {
            this.map = new PolarFrameMap();
            this.setContentPane(this.map);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.map.setLayout(new GridLayout(1, 1));
            this.map.setVisible(true);
            this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
            this.map.setPreferredSize(new Dimension(PolarFrame.WIDTH + this.map.getX(), PolarFrame.HEIGHT + this.map.getX()));
            this.map.setDoubleBuffered(true);
            this.setTitle("SCAN");
            this.setLocationRelativeTo(null);
            this.pack();
            this.setVisible(true);
        }

        public void process_next(final double[][] coordinate) {
            int nPoints = coordinate.length;
            int[] xPoints = new int[coordinate.length + 2];
            int[] yPoints = new int[coordinate.length + 2];
            xPoints[0] = 500;
            xPoints[coordinate.length + 1] = 500;
            yPoints[0] = 500;
            yPoints[coordinate.length + 1] = 500;
            for (int i = 0; i < coordinate.length; i++) {
                double x = coordinate[i][0] * Math.cos(coordinate[i][1]);
                double y = coordinate[i][0] * Math.sin(coordinate[i][1]);
                xPoints[i + 1] = 500 + (int) (x / this.map.getZoom());
                yPoints[i + 1] = 500 - (int) (y / this.map.getZoom());
            }
            this.map.draw(xPoints, yPoints, nPoints);
        }
    }

    private class PolarFrameMap extends JPanel implements KeyListener, MouseListener {

        /**
         * 
         */
        private static final long serialVersionUID = 3076158972425037339L;
        private int zoom = 20;
        private int[] x;
        private int[] y;
        private int i;
        private double mouseX;
        private double mouseY;

        public PolarFrameMap() {
            this.setFocusable(true);
            this.addKeyListener(this);
            this.addMouseListener(this);

        }

        /**
         * @return the zoom
         */
        public int getZoom() {
            return this.zoom;
        }

        public void draw(int x[], int[] y, int i) {
            this.x = x;
            this.y = y;
            this.i = i;

            this.repaint();
        }

        @Override
        public void paint(final Graphics graphics) 
        {
        	if ((x == null) || (y == null)) return;
        	
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, 1000, 1000);
            graphics.setColor(Color.WHITE);
            graphics.drawLine(500, 0, 500, 1000);
            graphics.drawLine(0, 500, 1000, 500);
            graphics.drawString("Zoom: " + zoom, 5, 45);
            graphics.setColor(Color.GREEN);

            graphics.fillPolygon(x, y, i);
            graphics.setColor(Color.RED);
            for (int i = 1; i < this.i - 1; i++) {
                graphics.drawRect(x[i], y[i], 1, 1);
            }
            graphics.setColor(Color.WHITE);
            graphics.drawString("Zoom: " + zoom, 5, 45);
            double distance = Math.floor(Math.sqrt(Math.pow(mouseX, 2) + Math.pow(mouseY, 2)));
            if (distance > 0.0) {
                graphics.drawString(mouseX + "mm/" + mouseY + "mm | " + distance + "mm", 5, 55);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            this.mouseX = (e.getX() - 500) * this.zoom;
            this.mouseY = (-e.getY() + 500) * this.zoom;
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_PAGE_UP:
                    if (event.isControlDown()) {
                        if (this.zoom > 1) {
                            this.zoom--;
                        }
                    }
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    if (event.isControlDown()) {
                        this.zoom++;
                    }
                    break;
            }
            this.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
