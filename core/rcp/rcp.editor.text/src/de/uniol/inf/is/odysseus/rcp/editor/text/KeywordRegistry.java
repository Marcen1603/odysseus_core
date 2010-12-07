package de.uniol.inf.is.odysseus.rcp.editor.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class KeywordRegistry {

	private static class RGBColor {
		public int r;
		public int g;
		public int b;
	}
	
	private static KeywordRegistry instance;
	
	private Map<String, String[]> keywords = new HashMap<String, String[]>();
	private Map<String, RGBColor> colors = new HashMap<String, RGBColor>();
	
	private KeywordRegistry() {
		
	}
	
	public static KeywordRegistry getInstance() {
		if( instance == null ) 
			instance = new KeywordRegistry();
		return instance;
	}
	
	public void loadExtensions() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(IEditorTextConstants.KEYWORD_EXTENSION_ID);
		for (IConfigurationElement e : config) {

			String groupName = e.getAttribute("name");
			String className = e.getAttribute("class");
			String colorR = e.getAttribute("colorR");
			String colorG = e.getAttribute("colorG");
			String colorB = e.getAttribute("colorB");
			
			int r = 0;
			int g = 0; 
			int b = 0;
			if( colorR != null && colorG != null && colorB != null ) {
				if( colorR.length() != 0 && colorG.length() != 0 && colorB.length() != 0 ) {
					try {
						r = Integer.valueOf(colorR);
						g = Integer.valueOf(colorG);
						b = Integer.valueOf(colorB);
					} catch( Exception ex ) {
						r = 0;
						g = 0;
						b = 0;
					}
				}
			}
			RGBColor color = new RGBColor();
			color.r = r;
			color.g = g;
			color.b = b;
			if( className == null || className.length() == 0 ) {
				 IConfigurationElement[] children = e.getChildren();
				 ArrayList<String> words = new ArrayList<String>();
				 for( IConfigurationElement child : children ) {
					 words.add(child.getAttribute("name"));
				 }
				 this.keywords.put(groupName, words.toArray(new String[words.size()]));
				 this.colors.put(groupName, color);
			} else {
				try {
					IKeywordGroup grp = (IKeywordGroup)e.createExecutableExtension("class");
					String[] keywords = grp.getKeywords();
					if (keywords != null){
						this.keywords.put(groupName, keywords);
						this.colors.put(groupName, color);
					}
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
	
	public String[] getKeywordGroups() {
		return keywords.keySet().toArray(new String[0]);
	}
	
	public String[] getKeywords( String group ) {
		if( !keywords.containsKey(group))
			throw new IllegalArgumentException("groupname " + group + "not found");

		return keywords.get(group);
	}
	
	public void addKeywordGroup( String groupName, String[] keywords ) {
		if( groupName == null )
			throw new IllegalArgumentException("groupname is null");
		if( groupName.length() == 0 ) 
			throw new IllegalArgumentException("groupname has length=0");
		if( keywords == null )
			throw new IllegalArgumentException("keywords is null");
		if( keywords.length == 0 ) 
			throw new IllegalArgumentException("keywords-array has length=0");

		this.keywords.put(groupName, keywords);
	}
	
	public int getGroupColorR( String group ) {
		if( !colors.containsKey(group))
			throw new IllegalArgumentException("groupname " + group + "not found");

		return colors.get(group).r;
	}

	public int getGroupColorG( String group ) {
		if( !colors.containsKey(group))
			throw new IllegalArgumentException("groupname " + group + "not found");

		return colors.get(group).g;
	}
	
	public int getGroupColorB( String group ) {
		if( !colors.containsKey(group))
			throw new IllegalArgumentException("groupname " + group + "not found");

		return colors.get(group).b;
	}
	
}
