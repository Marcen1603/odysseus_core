package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridMap extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 8285127861043019769L;
    private static final int SCALE = 10;
    private final List<Double[][]> grids = new CopyOnWriteArrayList<Double[][]>();

    public void onGrid(Double[][] grid) {
        this.grids.add(grid);
        if (this.grids.size() > 10) {
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (grids.size() > 0) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, 1000, 1000);
            for (final Double[][] grid : this.grids) {
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        if (grid[i][j] < 0.0) {
                            graphics.setColor(new Color(204, 51, 51));
                        }
                        else {
                            graphics.setColor(new Color(51, (int) (254 * grid[i][j]), 204));
                        }

                        // graphics.setColor(new Color(grid[i][j] & 0xFF, grid[i][j] & 0xFF,
                        // grid[i][j] & 0xFF));
                        graphics.fillRect(SCALE * i, SCALE * j, SCALE, SCALE);

                    }
                }
                break;
            }
            this.grids.clear();
        }
    }
}
