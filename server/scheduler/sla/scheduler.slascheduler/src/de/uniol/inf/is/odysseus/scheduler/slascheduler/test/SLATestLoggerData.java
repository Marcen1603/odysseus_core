/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SLATestLoggerData {
	
	public SLATestLoggerData(String id, int maxMessages, int skip) {
		File file = new File(SLATestLogger.PATH, id + ".csv");
		try {
			this.writer = new FileWriter(file);
			this.messageBuffer = new ArrayList<String>();
			this.skip = skip;
			this.maxMessages = maxMessages;
			this.bufferSize = SLATestLogger.bufferSize;
			this.closed = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileWriter writer;
	public List<String> messageBuffer;
	public int skip;
	public int maxMessages;
	public int bufferSize;
	public boolean closed;
	
}
