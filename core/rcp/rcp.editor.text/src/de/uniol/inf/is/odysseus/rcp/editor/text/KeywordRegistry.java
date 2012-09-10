/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class KeywordRegistry implements IRegistryEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(KeywordRegistry.class);
	
	private static class RGBColor {
		public int r;
		public int g;
		public int b;
	}
	
	private static KeywordRegistry instance;
	
	private Map<String, String[]> keywords = new HashMap<String, String[]>();
	private Map<String, RGBColor> colors = new HashMap<String, RGBColor>();
	
	private KeywordRegistry() {
		Platform.getExtensionRegistry().addListener(this, OdysseusRCPEditorTextPlugIn.KEYWORD_EXTENSION_ID);
		for(IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(OdysseusRCPEditorTextPlugIn.KEYWORD_EXTENSION_ID)) {
			addExtension(element);
		}		
	}
	
	public static synchronized KeywordRegistry getInstance() {
		if( instance == null ) 
			instance = new KeywordRegistry();
		return instance;
	}
	
	public void refresh() {
		keywords = Maps.newHashMap();
		colors = Maps.newHashMap();
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(OdysseusRCPEditorTextPlugIn.KEYWORD_EXTENSION_ID);
		
		for (IConfigurationElement e : config) {
			addExtension(e);
		}
	}

	public String[] getKeywordGroups() {
		refresh();
		
		return keywords.keySet().toArray(new String[0]);
	}
	
	public String[] getKeywords( String group ) {
		refresh();
		
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

	@Override
	public void added(IExtension[] extensions) {
		for( IExtension extension : extensions ) {
			for( IConfigurationElement conf : extension.getConfigurationElements()) {
				try {
					addExtension(conf);
				} catch( Throwable t ) {
					LOG.error("Could not load extension {}", conf, t);
				}
			}
		}
	}

	@Override
	public void removed(IExtension[] extensions) {
		for( IExtension extension : extensions ) {
			for( IConfigurationElement conf : extension.getConfigurationElements()) {
				try {
					removeExtension(conf);
				} catch( Throwable t ) {
					LOG.error("Could not unload extension {}", conf, t);
				}
			}
		}
	}

	@Override
	public void added(IExtensionPoint[] extensionPoints) {
	}

	@Override
	public void removed(IExtensionPoint[] extensionPoints) {
	}

	private void addExtension(IConfigurationElement e) {
		String groupName = e.getAttribute("name");
		String className = e.getAttribute("class");
		RGBColor color = getColor(e);
		
		if( Strings.isNullOrEmpty(className) ) {
			 this.keywords.put(groupName, getKeywords(e));
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
				LOG.error("Could not get keywords", e1);
			}
		}
	}

	private void removeExtension(IConfigurationElement conf) {
		String groupName = conf.getAttribute("name");
		keywords.remove(groupName);
		colors.remove(groupName);
	}
	
	private static String[] getKeywords( IConfigurationElement conf ) {
		 IConfigurationElement[] children = conf.getChildren();
		 ArrayList<String> words = new ArrayList<String>();
		 for( IConfigurationElement child : children ) {
			 words.add(child.getAttribute("name"));
		 }
		 return words.toArray(new String[words.size()]);
	}
	
	private static RGBColor getColor(IConfigurationElement e) {
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
		return color;
	}
}
