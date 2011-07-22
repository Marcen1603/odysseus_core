package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

public class GridMap extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 8285127861043019769L;
    private static final int SCALE = 20;
    private final List<Double[][]> grids = new CopyOnWriteArrayList<Double[][]>();

    public void onGrid(Double[][] grid) {
        this.grids.add(grid);
        if (this.grids.size() > 10) {
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 1000, 1000);
        final Color[] colors = new Color[] {
                new Color(254, 0, 0), new Color(0, 254, 0), new Color(0, 0, 254)
        };
        for (final Double[][] grid : this.grids) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    int colorIndex = (int) (grid[i][j] + 1);
                    graphics.setColor(colors[colorIndex]);
                    // graphics.fillRect((int) (i * 50 / SCALE), (int) (1000 - (j * 50 / SCALE)),
                    // (int) (50 / SCALE), (int) (50 / SCALE));
                    graphics.fillRect(250 + 2 * i, 250 + 2 * j, 2, 2);
                }
            }
            break;
        }
        this.grids.clear();
    }
}
