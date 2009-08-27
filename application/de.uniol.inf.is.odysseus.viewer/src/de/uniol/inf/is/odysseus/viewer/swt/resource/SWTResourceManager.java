package de.uniol.inf.is.odysseus.viewer.swt.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

/**
 * Verwaltet die Resourcen wie Bilder und Farben, die vom SWT angefordert wurden.
 * Jedes Bild und jede Farbe, welches verwendet werden soll, sollte über diese 
 * Klasse angefordert werden. 
 * 
 * Dadurch ist eine zentrale Stelle vorhanden, womit alle angeforderten Resourcen
 * leicht freigegeben werden können. Dabei werden Bilder eindeutige Namen
 * zugeordnet, womit sie zur weiteren Laufzeit angesprochen werden können.
 * 
 * Diese Klasse ist mittels dem Singleton-Pattern
 * implementiert, d.h. es existiert maximal eine Instanz. Diese kann über die statische
 * Methode getInstance() erreicht werden. 
 * 
 * Neue Bilder werden über die Methode load() hinzugefügt. Dabei wird das Device 
 * (oder Display) der SWT-Umgebung, sowie eine ResourceConfiguration, welche 
 * beinhaltet, welche Bilder unter welchen Namen geladen werden soll, benötigt.
 * Ist das Laden erfolgreich, so kann mittels getImage() und dem Identifikationsnamen
 * auf die Bilder zugegriffen werden.
 * 
 * Es ist wichtig, dass alle Resourcen, welche mit dieser Klasse geladen wurden,
 * nicht extern explizit über dispose() freigegeben werden. Werden alle Bilder
 * und andere Resourcen nicht mehr benötigt, so können diese über freeAllResources()
 * freigegeben werden. Alternativ können auch nur die Bilder (freeImageResources()) 
 * oder auch nur die Farben (freeColorResources()) freigegeben werden. 
 * 
 * @author Timo Michelsen
 * @see IResourceConfiguration
 * @see Image
 * @see Color
 */
public class SWTResourceManager {

	private static final Logger logger = LoggerFactory.getLogger( SWTResourceManager.class );
	
	// Geladene Farben
	private Collection<Color> loadedColors = new HashSet<Color>();
	
	// Geladene Bilder. Zuordnung zwischen Identifikationsnamen und Imageinstanz
	private Map<String, Image> loadedImages = new HashMap<String, Image>();
	
	// Einzige Instanz dieser Klasse. Siehe dazu Singleton-Pattern.
	private static SWTResourceManager instance;
	
	/**
	 * Standardkonstruktor. Privat. Erstellt eine neue SWTResourceManager-Instanz.
	 */
	private SWTResourceManager() {
		logger.debug( "SWTResourceManager started!" );
	}
	
	/**
	 * Liefert die einzige Instanz dieser Klasse. Wurde sie bisher noch
	 * nicht erstellt, wird dies nachgeholt, d.h. der erste Aufruf dieser
	 * Methode erstellt die Instanz. 
	 * 
	 * @return Einzige Instanz der SWTResourceManager-Klasse
	 */
	public static SWTResourceManager getInstance() {
		if( instance == null )
			instance = new SWTResourceManager();
		return instance;
	}
	
	/**
	 * Diese Methode läd die in der configuration definierten Bilder und stellt
	 * sie über getImage() zur Verfügung. 
	 * 
	 * Es werden nacheinander die Bilder geladen. Vorausgesetzt, die verwendetetn
	 * Identifikationsnamen wurden zuvor noch nicht verwendet. Andernfalls
	 * wird das Bild nicht geladen.
	 * 
	 * Über diese Methode geladene Bilder werden mittels freeImageResources()
	 * freigegeben.
	 * 
	 * @param device SWT-Device zum Laden der Bilder
	 * @param configuration Konfiguration, welches die Zuordnung zwischen 
	 * Identifikationsnamen und Bildresource beinhaltet.
	 * @exception IllegalArgumentException Wenn einer der Parameter null ist oder 
	 * die übergebene Konfiguration null zurückgibt.
	 */
	public void load( Device device, IResourceConfiguration configuration ) {
		logger.info( "Loading Resources" );
		
		// Parameter prüfen
		if( device == null ) 
			throw new IllegalArgumentException("device is null!");
			
		if( configuration == null ) 
			throw new IllegalArgumentException("configuration is null!");
			
		// Zu ladende Resourcen besorgen
		Map<String, String> resources = configuration.getResources();
		
		if( resources == null ) 
			throw new IllegalArgumentException("resources is null!");
		
		
		logger.debug( "Loading " + resources.keySet().size() + " resources" );
		
		// Jede dieser Resource nun konkret laden
		for( String key : resources.keySet()) {
			if( key == "" ) {
				logger.warn( "ResourcenName is empty!" );
				continue;
			}
			if( loadedImages.containsKey( key )) {
				logger.warn( "Resourcename '" + key + "' already used." );
				continue;
			}
				
			String fileToLoad = resources.get( key );
			
			if( fileToLoad == null ) {
				logger.warn( "Resourcename '" + key + "' has no file to load." );
				continue;
			}
			if( fileToLoad.isEmpty() ) {
				logger.warn( "Resourcename '" + key + "' has empty filename." );
				continue;
			}
			
			// Bild laden und ggfs. abspeichern
			Image img = loadImage(device, fileToLoad);
			if( img != null )
				loadedImages.put( key, img );
		}	
		
		logger.info( "Loading Resources complete" );

	}
	
	/**
	 * Liefert zu dem gegebenen Identifikationsnamen das zugeordnete Bild.
	 * Ist der Name nicht bekannt, wird null zurückgegeben.
	 * 
	 * @param name Identifikationsnamen des Bildes
	 * @return Image, welches dem Namen zugeordnet ist, oder null, falls der Name 
	 * nicht bekannt ist.
	 */
	public Image getImage( String name ) {
		if( name == null )
			return null;
		
		if( !loadedImages.containsKey( name ) )
			logger.warn( "Imageresource '" + name + "' does not exist!" );
		return loadedImages.get( name );
	}
	
	/**
	 * Liefert alle Namen der geladenen Bilder. Wurden keine Bilder geladen, so wird
	 * eine leere Collection zurück gegeben.
	 * 
	 * @return Collection von Namen aller geladenen Bilder
	 */
	public Collection<String> getNames() {
		return loadedImages.keySet();
	}

	/**
	 * Liefert zu den angegebenen RGB-Werten die entsprechende SWT-Color-Instanz.
	 * Wurde die Farbe zuvor noch nicht angefordert, so wird diese neu über den
	 * Device-Parameter geladen und innerhalb der SWTResourceManager-Klasse
	 * gespeichert. Bei erneutem Aufruf mit den gleicher RGB-Werten wird die
	 * entsprechende Color-Instanz zurückgegeben, ohne eine neue Instanz zu
	 * erzeugen.
	 * 
	 * Über diese Funktion geladene Farben werden mit freeColorResourcs() 
	 * freigegeben.
	 * 
	 * @param r Rot-Anteil der gewünschten Farbe
	 * @param g Grün-Anteil der gewünschten Farbe
	 * @param b Blau-Anteil der gewünschten Farbe
	 * @return Color-Instanz mit den angegebenen RGB-Werten
	 * 
	 * @exception IllegalArgumentException Wenn der gegebene device null ist.
	 */
	public Color getColor( Device device, int r, int g, int b ) {
		if( device == null) 
			throw new IllegalArgumentException("device is null!");
		
		// Nachschauen, ob diese RGB-Werte schon im Speicher sind
		for( Color c : loadedColors ) {
			if( c.getRed() == r && c.getGreen() == g && c.getBlue() == b ) {
				logger.debug( "Colorresource already loaded:" + r + " " + g + " " + b );
				return c;
			}
		}
		// Wenn wir hier ankommen, dann haben wir diese RGB-Werte noch
		// nicht als Color-Instanz. Dies wird dann nachgeholt und
		// abgespeichert.
		final Color color = new Color(device, r,g,b);
		
		logger.debug( "Colorresource created:" + r + " " + g + " " + b );

		loadedColors.add( color );
		return color;
	}
	
	/**
	 * Gibt alle über load() geladenen Image-Instanzen wieder frei.
	 * Dazu wird von jedem Image dispose() aufgerufen. Anschließend
	 * sind die Images über ihre Identifikationsnamen mit getImage()
	 * nicht mehr erreichbar.
	 */
	public void freeImageResources() {
		for( String key : loadedImages.keySet() ) {
			Image img = loadedImages.get( key );
			if( img != null )
				img.dispose();
		}
		loadedImages.clear();
		
		logger.info( "Image-resources freed" );
	}
	
	/**
	 * Gibt alle über getColor() geladenen Color-Instanzen wieder frei.
	 * Dazu wird von jedem Color dispose() aufgerufen. 
	 */
	public void freeColorResources() {
		for( Color color : loadedColors ) {
			if( color != null )
				color.dispose();
		}
		loadedColors.clear();
		
		logger.info( "Color-resources freed" );
	}
	
	/**
	 * Gibt alle geladenen Resourcen wieder frei. Diese Methode
	 * ruft freeImageResources() und freeColorResources() auf.
	 */
	public void freeAllResources() {
		freeImageResources();
		freeColorResources();		
	}
	
	// Läd eine Bilddatei in den Speicher. Liefert null, wenn etwas
	// schief gegangen ist.
	private static Image loadImage( Device device, String filename ) {
		try {
			Image image = new Image(device, filename);
			
			logger.debug( "Imageresource loaded:" + filename );
			
			return image;
		} 
		catch( Exception ex ) {
			logger.error( "Error during loading image: " + filename, ex );
			return null;
		}
	}}
