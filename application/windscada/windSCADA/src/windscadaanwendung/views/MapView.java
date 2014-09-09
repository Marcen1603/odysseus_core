package windscadaanwendung.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

public class MapView extends ViewPart {
	
	static Browser browser;
	static String content;
	static String addScript;

	public MapView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

	    content = "";
	    File file = new File("/mapData/map.html");
	    System.out.println(file.getAbsolutePath());
	    if (file.exists()) {
	    	System.out.println("JA");
	    } else {
	    	System.out.println("NEIN");
	    }
	    
	    
	    try {
	        BufferedReader in = new BufferedReader(new FileReader(file));
	        String str;
	        while ((str = in.readLine()) != null) {
	            content +=str;
	        }
	        in.close();
	    } catch (IOException e) {
	    	System.out.println("Fehler beim einlesen der map.html");
	    }
	    
	    File f = new File("/mapData/addMarkerAtPos.js");
	    addScript = "";
		try {
	        BufferedReader in = new BufferedReader(new FileReader(f));
	        String str;
	        while ((str = in.readLine()) != null) {
	            addScript +=str;
	        }
	        in.close();
	    } catch (IOException e) {
	    	System.out.println("Fehler beim einlesen der JS Datei");
	    }
	    

		
		browser = new Browser(parent, SWT.NONE);
		browser.setText(content);
		System.out.println(browser.getText());
	}
	
	public static void setSelectedFarm(WindFarm farm) {
		
		String pos;
		for (WKA wka: farm.getWkas()) {
			pos = "var pos = ol.proj.transform([" + wka.getLongtude() + ", " + wka.getLatitude() + "], 'EPSG:4326', 'EPSG:3857');";
			browser.execute(pos);
			browser.execute(addScript);
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

}
