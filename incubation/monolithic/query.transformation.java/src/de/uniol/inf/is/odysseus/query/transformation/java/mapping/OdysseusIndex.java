package de.uniol.inf.is.odysseus.query.transformation.java.mapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.query.transformation.java.utils.FileFinder;

public class OdysseusIndex {
	
	private static List<Path> odysseusIndex = new ArrayList<Path>();
	private static boolean init = false;
	
	
	public static void search(String odysseusPath){
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
	
	public static List<Path> getOdysseusIndex(){
		return odysseusIndex;
		
	}
	
	public static void creadOdysseusIndex(){
		odysseusIndex.clear();
		init = false;
	}
}
