package de.offis.salsa.simulator.car.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class GridScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2948080783488835765L;
	private GridMap map;

	public GridScreen() {
		this.map = new GridMap();
		this.setContentPane(this.map);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.map.setLayout(new GridLayout(1, 1));
		this.map.setVisible(true);
		this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.map.setPreferredSize(new Dimension(GridScreen.WIDTH
				+ this.map.getX(), GridScreen.HEIGHT + this.map.getX()));
		this.setTitle("Car Simulator");
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	public void onGridData(int x, int y, int length, int width, int height,
			int cell, Float[][][] grid) {
		this.map.onGridData(x, y, length, width, height, cell, grid);
	}
}
