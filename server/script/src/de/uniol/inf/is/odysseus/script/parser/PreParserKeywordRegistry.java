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
package de.uniol.inf.is.odysseus.script.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Zentrale Registry für die Keywords, die vom PreParser verwendet werden
 * sollen. Erst, wenn eine Klasse, abgeleitet von der Schnittstelle
 * <code>IPreParserKeyword</code> hier hinzugefügt wurde, wird sie im PreParser
 * angewendet.
 * <p>
 * 
 * @author Timo Michelsen
 * 
 */
public class PreParserKeywordRegistry {
	
	public static final String PREPARSER_KEYWORD_EXTENSION_ID = "de.uniol.inf.is.odysseus.script.parser.PreParserKeyword";

	private Map<String, Class<? extends IPreParserKeyword>> keywords = null;

	/**
	 * Fügt ein neues Schlüsselwort in die Registry ein. Ist das Schlüsselwort
	 * schon in der Registry vorhanden, wird eine Exception geworfen.
	 * 
	 * @param keyword
	 *            Klasse des neuen Keywords, welcher der Registry hinzugefügt werden
	 *            soll. Darf nicht <code>null</code> sein.
	 * @param name
	 *            Name des Keywords. Wird vom PreParser verwendet und in
	 *            Querytexten gesucht. Darf nicht <code>null</code> sein.
	 * 
	 * @throws AssertionFailedException
	 *             Wenn der Parameter <code>null</code> ist
	 */
	public final void addKeyword(String name, Class<? extends IPreParserKeyword> keyword) {
//		Assert.isNotNull(keyword);
//		Assert.isNotNull(name);
		if (!existsKeyword(name)) {
			getKeywords().put(name, keyword);
		}
	}

	/**
	 * Liefert eine neue Instanz der <code>IPreParserKeyword</code>
	 * -Implementierung zu dem gehötigen Keyword. Dabei wird dessen
	 * Standardkonstruktor aufgerufen.
	 * 
	 * @param name
	 *            Keyword, dessen neues <code>IPreParserKeyword</code>
	 *            zurückgegeben werden soll. Darf nicht null sein.
	 * 
	 * @return Eine neue <code>IPreParserKeyword</code>-Implementierung
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn der angegebene Keyword-Name nicht in der Registry
	 *             vorhanden ist.
	 * 
	 * @throws AssertFailedException
	 *             Wenn der name-Parameter <code>null</code> ist.
	 * @throws IllegalArgumentException
	 *             Wenn die <code>IPreParserKeyword</code>-Implementierung nicht
	 *             geladen werden kann.
	 */
	public final IPreParserKeyword createKeywordExecutor(String name) {
		//Assert.isNotNull(name);
		if (!getKeywords().containsKey(name))
			throw new IllegalArgumentException("Keyword " + name + " is not registered");
		IPreParserKeyword keyword;
		try {
			keyword = getKeywords().get(name).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("cannot instanciate " + getKeywords().get(name).getName(), e);
		}
		return keyword;
	}

	/**
	 * Prüft, ob das angegebene Schlüsselwort schon in der Registry vorhanden
	 * ist. Liefert <code>true</code>, falls dies so ist, ansonsten
	 * <code>false</code>.
	 * 
	 * @param keyword
	 *            Das auf Existenz zu prüfende Keyword.
	 * @return <code>true</code>, falls das angegebene Keyword vorhanden ist,
	 *         sonst <code>false</code>.
	 * 
	 * @throws AssertionFailedException
	 *             Wenn der Parameter <code>null</code> ist.
	 */
	public final boolean existsKeyword(String keyword) {
		//Assert.isNotNull(keyword);
		return getKeywords().containsKey(keyword);
	}

	/**
	 * Entfernt das Keyword aus der Registry, sodass sie theoretisch wieder ohne
	 * Fehler hinzugefügt werden kann.
	 * 
	 * @param keyword
	 *            Keyword, welches entfernt werden soll. Darf nicht
	 *            <code>null</code> sein.
	 * 
	 * @throws AssertFailedException
	 *             Wenn der Parameter <code>null</code> ist.
	 * @throws IllegalArgumentException
	 *             Wenn das angegebene Keyword nicht in der Registry ist.
	 * 
	 */
	public final void removeKeyword(String keyword) {
		//Assert.isNotNull(keyword);
		if (!existsKeyword(keyword))
			throw new IllegalArgumentException("keyword is null");
		getKeywords().remove(keyword);
	}

	/**
	 * Liefert die Map mit den Keywords. Falls dieser (noch) <code>null</code>
	 * ist, wird dieser mit einer HashMap erstellt.
	 * <p>
	 * Wenn der Nutzer eine andere Map-Implementierung wünscht, so kann diese
	 * Überschrieben werden. Dabei sollte gewährleistet werden, dass nie
	 * <code>null</code> zurückgegeben wird.
	 * 
	 * @return Map der registrierten Keywords.
	 */
	protected Map<String, Class<? extends IPreParserKeyword>> getKeywords() {
		if (keywords == null)
			keywords = new HashMap<String, Class<? extends IPreParserKeyword>>();
		return keywords;
	}

	/**
	 * Liefert eine Liste aller registrierten Keywörter. Ist garantiert nicht
	 * <code>null</code>.
	 * 
	 * @return Liste aller registrierten Keywörter. Ist nicht <code>null</code>.
	 */
	public Set<String> getKeywordNames() {
		return getKeywords().keySet();
	}

	public boolean isDeprecated(String keyword) {
		Class<? extends IPreParserKeyword> keywordClass = getKeywords().get(keyword);
		if( keywordClass != null && ( keywordClass.getAnnotation(Deprecated.class) != null )) {
			return true;
		}
		
		return false;
	}

}
