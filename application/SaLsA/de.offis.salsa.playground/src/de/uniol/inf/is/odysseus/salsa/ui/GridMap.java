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
    private final List<Byte[][]> grids = new CopyOnWriteArrayList<Byte[][]>();

    public void onGrid(Byte[][] grid) {
        this.grids.add(grid);
    //    if (this.grids.size() > 100) {
            this.repaint();
     //   }
    }

    @Override
    public void paint(Graphics graphics) {
        if (grids.size() > 0) {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, 1000, 1000);
            final Color[] colors = new Color[] {
                    new Color(51, 128, 204), new Color(51, 204, 51), new Color(204, 51, 51)
            };
            for (final Byte[][] grid : this.grids) {
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        // int colorIndex = (int) (grid[i][j] + 1);
                        // if (grid[i][j] < 0.0) {
                        // graphics.setColor(new Color(204, 51, 51));
                        // }
                        // else {
                        // graphics.setColor(new Color(51, (int) (254 * grid[i][j]), 204));
                        // }
                        try {
                        graphics.setColor(new Color(grid[i][j] & 0xFF, grid[i][j] & 0xFF,
                                grid[i][j] & 0xFF));
                        graphics.fillRect(SCALE * i, SCALE * j, SCALE, SCALE);
                        }catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Value: "+ grid[i][j]);
                        }
                    }
                }
                break;
            }
            this.grids.clear();
        }
    }
}
