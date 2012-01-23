package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import de.uniol.inf.is.odysseus.salsa.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridMap extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 8285127861043019769L;
    private final List<Grid> grids = new CopyOnWriteArrayList<Grid>();

    public void onGrid(Grid grid) {
        this.grids.add(grid);
        this.repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        if (grids.size() > 0) {
            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, 1000, 1000);
            for (final Grid grid : this.grids) {
                int widthScale = 1000 / grid.width;
                int depthScale = 1000 / grid.depth;
                for (int w = 0; w < grid.width; w++) {
                    for (int d = 0; d < grid.depth; d++) {

                        graphics.setColor(new Color(grid.get(w, d) & 0xFF, grid.get(w, d) & 0xFF,
                                grid.get(w, d) & 0xFF));
                        // graphics.setColor(new Color(grid[i][j] & 0xFF, grid[i][j] & 0xFF,
                        // grid[i][j] & 0xFF));
                        graphics.fillRect(w * widthScale, 1000 - d * depthScale, widthScale, depthScale);
                    }
                }
                break;
            }
            this.grids.clear();
        }
    }
}
