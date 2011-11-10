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
    private static final int SCALE = 4;
    private final List<Grid2D> grids = new CopyOnWriteArrayList<Grid2D>();

    public void onGrid(Grid2D grid) {
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
            for (final Grid2D grid : this.grids) {
                for (int i = 0; i < grid.grid.length; i++) {
                    for (int j = 0; j < grid.grid[i].length; j++) {
                        if (grid.get(i, j) < 0.0) {
                            graphics.setColor(new Color(204, 51, 51));
                        }
                        else {
                            graphics.setColor(new Color(51, (int) (254 * grid.get(i, j)), 204));
                        }

                        // graphics.setColor(new Color(grid[i][j] & 0xFF, grid[i][j] & 0xFF,
                        // grid[i][j] & 0xFF));
                        graphics.fillRect(i * SCALE, j * SCALE, SCALE, SCALE);

                    }
                }
                break;
            }
            this.grids.clear();
        }
    }
}
