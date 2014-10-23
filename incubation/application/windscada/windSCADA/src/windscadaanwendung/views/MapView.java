package windscadaanwendung.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import windscadaanwendung.Activator;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

/**
 * This class is not used in windSCADA anymore. It only documents the way, the
 * map could'nt be implemented into windSCADA. The way was about OpenLayers,
 * JavaScript and the swt-Browser. For additional information check the bachelor
 * thesis
 * "Datenstrommanagement zur Ueberwachung und Kurzzeitprognose der Leistung von Windparks"
 * by Mark Milster and Dennis Nowak
 * 
 * @author MarkMilster
 * 
 */
public class MapView extends ViewPart {

	static Browser browser;
	static String content;
	static String addScript;

	@Override
	public void createPartControl(Composite parent) {
		content = "";
		Bundle bundle = Activator.getDefault().getBundle();
		URL url = bundle.getResource("/resource/map.html");
		File file;
		try {
			file = new File(FileLocator.toFileURL(url).getPath());
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null) {
				content += str;
			}
			in.close();
		} catch (IOException e1) {
			System.out.println("Fehler beim einlesen der map.html");
			e1.printStackTrace();
		}
		System.out.println(content);
		url = bundle.getResource("/resource/addMarkerAtPos.js");
		File f;
		addScript = "";
		try {
			f = new File(FileLocator.toFileURL(url).getPath());
			BufferedReader in = new BufferedReader(new FileReader(f));
			String str;
			while ((str = in.readLine()) != null) {
				addScript += str;
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Fehler beim einlesen der JS Datei");
		}

		browser = new Browser(parent, SWT.NONE);
		browser.setText(content);
		// System.out.println(browser.getText());
	}

	/**
	 * This method will draw markers into the map on the lontude and latitude of every wind turbine in the given windFarm
	 * @param farm
	 * 			The windFarm which should be shown on the map
	 */
	public static void setSelectedFarm(WindFarm farm) {

		String pos;
		for (WKA wka : farm.getWkas()) {
			pos = "var pos = ol.proj.transform([" + wka.getLongtude() + ", "
					+ wka.getLatitude() + "], 'EPSG:4326', 'EPSG:3857');";
			browser.execute(pos);
			browser.execute(addScript);
		}
	}

	@Override
	public void setFocus() {
	}

}
