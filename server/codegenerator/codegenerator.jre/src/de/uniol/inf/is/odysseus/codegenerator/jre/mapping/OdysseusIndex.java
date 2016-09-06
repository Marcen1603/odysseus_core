package de.uniol.inf.is.odysseus.codegenerator.jre.mapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.utils.FileFinder;
/**
 * this class index all files in the odysseus code path
 * Was a part of the automatic class import for the 
 * codegeneration process.  
 * 
 * @author MarcPreuschaft
 *
 */
public class OdysseusIndex {
	
	private static List<Path> odysseusIndex = new ArrayList<Path>();
	private static boolean init = false;
	private static OdysseusIndex instance = null;
	
	public static OdysseusIndex getInstance() {
		if (instance == null) {
			instance = new OdysseusIndex();
		}
		return instance;
	}
	
	public void search(String odysseusPath){
		if(!init){
			// create a FileFinder instance with a naming pattern
			FileFinder finder = new FileFinder("*.java");
		
			// pass the initial directory and the finder to the file tree walker
			try {
				Files.walkFileTree(Paths.get(odysseusPath), finder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			// get the matched paths
			Collection<Path> matchedFiles = finder.getMatchedPaths();
		
			// print the matched paths
			for (Path path : matchedFiles) {
				odysseusIndex.add(path);
			}
			init = true;
		}
	}
	
	/**
	 * return a list with all resolved *.java files
	 * @return
	 */
	public List<Path> getOdysseusIndex(){
		return odysseusIndex;
		
	}
	
	public void clearOdysseusIndex(){
		odysseusIndex.clear();
		init = false;
		instance = null;
	}
}
