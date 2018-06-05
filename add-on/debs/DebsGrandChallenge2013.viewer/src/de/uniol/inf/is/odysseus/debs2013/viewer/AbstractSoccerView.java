package de.uniol.inf.is.odysseus.debs2013.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.debs2013.viewer.activator.ViewerStreamSoccerPlugIn;

public abstract class AbstractSoccerView {
	ConcurrentHashMap<String, Integer> attributeIndexMap;
	
	Composite soccerViewer;
	Canvas soccerFieldDraw;

	final int width = 862;
	final int height = 532;
	
	ArrayList<Integer> sidBalls;
	ArrayList<Integer> sidTeamA;
	ArrayList<Integer> sidTeamB;
	ArrayList<Integer> sidReferee;
	
	HashMap<Integer, Integer> sensorIdToPlayerId;
	
	void initMetadata() {
		sidBalls = new ArrayList<>();
		sidBalls.add(4);
		sidBalls.add(8);
		sidBalls.add(10);
		sidBalls.add(12);
		
		sidTeamA = new ArrayList<>();
		sidTeamA.add(13);
		sidTeamA.add(14);
		sidTeamA.add(97);
		sidTeamA.add(98);
		sidTeamA.add(47);
		sidTeamA.add(16);
		sidTeamA.add(49);
		sidTeamA.add(88);
		sidTeamA.add(19);
		sidTeamA.add(52);
		sidTeamA.add(53);
		sidTeamA.add(54);
		sidTeamA.add(23);
		sidTeamA.add(24);
		sidTeamA.add(57);
		sidTeamA.add(58);
		sidTeamA.add(59);
		sidTeamA.add(28);
		
		sidTeamB = new ArrayList<>();
		sidTeamB.add(61);
		sidTeamB.add(62);
		sidTeamB.add(99);
		sidTeamB.add(100);
		sidTeamB.add(63);
		sidTeamB.add(64);
		sidTeamB.add(65);
		sidTeamB.add(66);
		sidTeamB.add(67);
		sidTeamB.add(68);
		sidTeamB.add(69);
		sidTeamB.add(38);
		sidTeamB.add(71);
		sidTeamB.add(40);
		sidTeamB.add(73);
		sidTeamB.add(74);
		sidTeamB.add(75);
		sidTeamB.add(44);
		
		sidReferee = new ArrayList<>();
		sidReferee.add(105);
		sidReferee.add(106);
		
		sensorIdToPlayerId = new HashMap<Integer, Integer>();
		sensorIdToPlayerId.put(13, 1);
//		sensorIdToPlayerId.put(14, 1);
		sensorIdToPlayerId.put(47, 2);
//		sensorIdToPlayerId.put(16, 2);
		sensorIdToPlayerId.put(49, 3);
//		sensorIdToPlayerId.put(88, 3);
		sensorIdToPlayerId.put(19, 4);
//		sensorIdToPlayerId.put(52, 4);
		sensorIdToPlayerId.put(53, 5);
//		sensorIdToPlayerId.put(54, 5);
		sensorIdToPlayerId.put(23, 6);
//		sensorIdToPlayerId.put(24, 6);
		sensorIdToPlayerId.put(57, 7);
//		sensorIdToPlayerId.put(58, 7);
		sensorIdToPlayerId.put(59, 8);
//		sensorIdToPlayerId.put(28, 8);
		
		sensorIdToPlayerId.put(61, 11);
//		sensorIdToPlayerId.put(62, 11);
		sensorIdToPlayerId.put(63, 12);
//		sensorIdToPlayerId.put(64, 12);
		sensorIdToPlayerId.put(65, 13);
//		sensorIdToPlayerId.put(66, 13);
		sensorIdToPlayerId.put(67, 14);
//		sensorIdToPlayerId.put(68, 14);
		sensorIdToPlayerId.put(69, 15);
//		sensorIdToPlayerId.put(38, 15);
		sensorIdToPlayerId.put(71, 16);
//		sensorIdToPlayerId.put(40, 16);
		sensorIdToPlayerId.put(73, 17);
//		sensorIdToPlayerId.put(74, 17);
		sensorIdToPlayerId.put(75, 18);
//		sensorIdToPlayerId.put(44, 18);
		
		sensorIdToPlayerId.put(4, 4);
		sensorIdToPlayerId.put(8, 8);
		sensorIdToPlayerId.put(10, 10);
		sensorIdToPlayerId.put(12, 12);
	}
	
	void initView(Composite parent){
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		
		soccerViewer = new Composite(parent, SWT.BORDER);
		
		soccerFieldDraw = new Canvas(soccerViewer, SWT.BORDER);
		soccerFieldDraw.setSize(width, height);
		soccerFieldDraw.setBackgroundImage(getResizedImage(ViewerStreamSoccerPlugIn.getImageManager().get("soccer_field"),width,height));
	}
	private Image getResizedImage(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0,image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		return scaled;
	}
	
	int getCoordX(int absX){
		return (int)(((absX+33960)/67920f)*790)+36;
	}
	int getCoordY(int absY){
		return (int)((absY/52489f)*506)+15;
	}
}
