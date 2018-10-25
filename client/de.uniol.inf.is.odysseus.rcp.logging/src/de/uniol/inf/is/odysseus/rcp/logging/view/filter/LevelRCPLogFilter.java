package de.uniol.inf.is.odysseus.rcp.logging.view.filter;

import org.apache.log4j.Level;

import java.util.Objects;

import de.uniol.inf.is.odysseus.rcp.logging.RCPLogEntry;

public class LevelRCPLogFilter implements IRCPLogFilter {

	private final Level levelFilter;

	public LevelRCPLogFilter(Level levelToFilter) {
		Objects.requireNonNull(levelToFilter, "Level must not be null!");

		this.levelFilter = levelToFilter;
	}

	public LevelRCPLogFilter(String levelString) {
		this(Level.toLevel(levelString));		
	}

	@Override
	public boolean isShown(RCPLogEntry entry) {
		return entry.getLevel().isGreaterOrEqual(levelFilter);
	}
}
