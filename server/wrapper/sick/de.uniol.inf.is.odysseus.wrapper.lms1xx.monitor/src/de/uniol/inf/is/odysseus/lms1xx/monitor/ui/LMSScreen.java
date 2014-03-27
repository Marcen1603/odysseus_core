/*******************************************************************************
 * LMS1xx protocol visualization and logging
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.lms1xx.monitor.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;

import de.uniol.inf.is.odysseus.lms1xx.monitor.LMS1xxConnection;
import de.uniol.inf.is.odysseus.lms1xx.monitor.LMS1xxListener;
import de.uniol.inf.is.odysseus.lms1xx.monitor.model.Measurement;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class LMSScreen extends JFrame implements LMS1xxListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2948080783488835765L;
    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;
    private final ScanMap map;

    public LMSScreen(final List<LMS1xxConnection> connections) {

        this.map = new ScanMap();
        this.setContentPane(this.map);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.map.setLayout(new GridLayout(1, 1));
        this.map.setVisible(true);
        this.map.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.map.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.map.setPreferredSize(new Dimension(LMSScreen.WIDTH + this.map.getX(), LMSScreen.HEIGHT + this.map.getX()));
        this.map.setDoubleBuffered(true);
        this.setTitle("Calibrator");
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        for (final LMS1xxConnection connection : connections) {
            connection.registerListener(this);
        }
    }

    @Override
    public void onScan(final Measurement measurement) {
        this.map.onMeasurement(measurement);
    }
}
