/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.application;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Appender fuer das Eclipse Plugin ui.console.MessageConsole.<br>
 * Alle Meldungen werden auf dieser ausgegeben.<br>
 * Dazu wird <code>MessageConsoleStream</code> verwendet.
 * 
 * @author Georg Kuffer
 */
public class MessageConsoleAppender extends AppenderSkeleton {
	protected MessageConsoleStream messageConsoleStream = null;

	// public static final String DEFAULT_CONVERSION_PATTERN =
	// "%d [%t] %-5p %c - %m%n";

	public static final String DEFAULT_CONVERSION_PATTERN = "%d{dd.MM.yyyy HH:mm:ss} %m%n";

	public MessageConsoleAppender(MessageConsoleStream messageConsoleStream) {
		super();
		name = "MessageConsoleAppender";
		this.messageConsoleStream = messageConsoleStream;
		this.layout = new PatternLayout(DEFAULT_CONVERSION_PATTERN);
	}

	@Override
	public void close() {
		this.closed = true;
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	public void append(LoggingEvent event) {
		String text = this.layout.format(event);
		String trace[];
		try {
			messageConsoleStream.print(text);
			if ((trace = event.getThrowableStrRep()) != null) {
				for (int i = 0; i < trace.length && trace.length > 0; i++) {
					messageConsoleStream.println(trace[i]);
				}
			}
		} catch (Exception ex) {
		}
	}
}
