package de.offis.salsa.obsrec.ui;

import javax.swing.JFrame;

import de.offis.salsa.obsrec.Objektwelt;

@SuppressWarnings("serial")
public class ObjectworldWindow extends JFrame {

	public ObjectworldWindow(Objektwelt objWelt) {		
		setSize(600, 500);
		add(new ObjectworldWidget(objWelt));
		setVisible(true);		
	}	
}
