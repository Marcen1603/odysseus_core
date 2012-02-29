package de.uniol.inf.is.odysseus.fusion.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class ExtPolygonScreen extends JFrame{

    private static final long serialVersionUID = 253561357461155938L;
    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private final ExtPolygonMap map;

    public ExtPolygonScreen() {
        this.map = new ExtPolygonMap();

        this.setContentPane(this.map);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.map.setLayout(new GridLayout(1, 1));
        this.map.setVisible(true);
        this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.map.setPreferredSize(new Dimension(ExtPolygonScreen.WIDTH + this.map.getX(), ExtPolygonScreen.HEIGHT + this.map.getX()));
        this.setTitle("Scan");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void onFeature(final RelationalTuple<? extends IMetaAttribute> segment) {
        this.map.onFeature(segment);
    }
    
    
    
}
