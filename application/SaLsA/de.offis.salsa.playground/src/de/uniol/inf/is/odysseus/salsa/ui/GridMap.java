package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import de.uniol.inf.is.odysseus.salsa.model.Grid2D;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridMap extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 8285127861043019769L;
    private final List<Grid2D> grids = new CopyOnWriteArrayList<Grid2D>();

    public void onGrid(Grid2D grid) {
        this.grids.add(grid);
        this.repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        if (grids.size() > 0) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, 1000, 1000);
            for (final Grid2D grid : this.grids) {
                int scale = 1000 / grid.grid.length;
                for (int l = 0; l < grid.grid.length; l++) {
                    for (int w = 0; w < grid.grid[l].length; w++) {
                        if (grid.get(l, w) < 0.0) {
                            graphics.setColor(new Color(204, 51, 51));
                        }
                        else {
                            graphics.setColor(new Color(51, (int) (254 * grid.get(l, w)), 204));
                        }

                        // graphics.setColor(new Color(grid[i][j] & 0xFF, grid[i][j] & 0xFF,
                        // grid[i][j] & 0xFF));
                        graphics.fillRect(l * scale, 1000 - w * scale, scale, scale);
                    }
                }
                break;
            }
            this.grids.clear();
        }
    }
}
