package de.uniol.inf.is.odysseus.rcp.logging.view.filter;

import org.apache.log4j.Level;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class LevelAndTextRCPLogFilter extends LevelRCPLogFilter {

	private final String textToFilter;
	
	public LevelAndTextRCPLogFilter(Level levelToFilter, String textToFilter) {
		super(levelToFilter);
		
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(textToFilter), "Text to filter must not be null or empty!");
		
		this.textToFilter = textToFilter;
	}
	
	public LevelAndTextRCPLogFilter(String levelToFilterString, String textToFilter) {
		this(Level.toLevel(levelToFilterString), textToFilter);
	}

	@Override
	public boolean isShown(RCPLogEntry entry) {
		return super.isShown(entry) && containsSomewhere(entry, textToFilter);
	}

	private static boolean containsSomewhere( RCPLogEntry entry, String text ) {
		
		if( !contains(entry.getClassName(), text)) {
			if( !contains(entry.getLoggerName(), text)) {
				if( !contains(entry.getMessage(), text)) {
					if( !contains(entry.getMethodName(), text)) {
						if( !contains(entry.getSimpleClassName(), text)) {
							if( !contains(entry.getThreadName(), text)) {
								return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	// null-secure check
	private static boolean contains( String text, String pattern ) {
		
		if( !Strings.isNullOrEmpty(text) && !Strings.isNullOrEmpty(pattern)) {
			return text.contains(pattern);
		}
		
		return false;
	}
}
