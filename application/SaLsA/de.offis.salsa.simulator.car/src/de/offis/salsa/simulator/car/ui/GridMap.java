package de.offis.salsa.simulator.car.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

public class GridMap extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5315446201613125311L;
	private static final int SCALE = 10;
	private final List<Float[][][]> grids = new CopyOnWriteArrayList<Float[][][]>();

	public void onGridData(int x, int y, int length, int width, int height,
			int cell, Float[][][] grid) {
		this.grids.add(grid);
		this.repaint();
	}

	@Override
	public void paint(Graphics graphics) {
		if (grids.size() > 0) {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, 1000, 1000);
			final Color[] colors = new Color[] { new Color(51, 128, 204),
					new Color(51, 204, 51), new Color(204, 51, 51) };
			for (final Float[][][] grid : this.grids) {
				for (int l = 0; l < grid.length; l++) {
					for (int w = 0; w < grid[l].length; w++) {
						for (int h = 0; h < grid[l][w].length; h++) {
							int colorIndex = (int) (grid[l][w][h] + 1);
							graphics.setColor(colors[colorIndex]);

							graphics.fillRect(SCALE * l, SCALE * w, SCALE,
									SCALE);
						}
					}
				}
				break;
			}
			this.grids.clear();
		}
	}
}
