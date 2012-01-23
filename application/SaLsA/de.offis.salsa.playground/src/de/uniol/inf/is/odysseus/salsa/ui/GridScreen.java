package de.uniol.inf.is.odysseus.salsa.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import de.uniol.inf.is.odysseus.salsa.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridScreen extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -3253151235916286303L;
    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private final GridMap map;

    public GridScreen() {
        this.map = new GridMap();

        this.setContentPane(this.map);
        this.map.setLayout(new GridLayout(1, 1));
        this.map.setVisible(true);
        this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.map.setPreferredSize(new Dimension(GridScreen.WIDTH + this.map.getX(),
               GridScreen.HEIGHT + this.map.getX()));
        this.map.setDoubleBuffered(true);
        this.setTitle("Scan");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void onGrid(final Grid grid) {
        this.map.onGrid(grid);
    }
}
