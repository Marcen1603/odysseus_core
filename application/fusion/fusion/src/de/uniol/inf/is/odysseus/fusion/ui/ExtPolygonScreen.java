/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.fusion.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class ExtPolygonScreen extends JFrame{

    private static final long serialVersionUID = 253561357461155938L;
    private static final int HEIGHT = 805;
    private static final int WIDTH = 330;
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

    public void onFeature(final Tuple<? extends IMetaAttribute> segment) {
        this.map.onFeature(segment);
    }
    
    
    
}
