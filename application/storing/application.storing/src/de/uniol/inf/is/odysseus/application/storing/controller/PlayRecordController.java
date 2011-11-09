/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.application.storing.controller;

/**
 * 
 * @author Dennis Geesen Created at: 09.11.2011
 */
public class PlayRecordController {
	private static PlayRecordController instance = null;

	private PlayRecordController() {

	}

	public static synchronized PlayRecordController getInstance() {
		if (instance == null) {
			instance = new PlayRecordController();
		}
		return instance;
	}

	public void startPlaying() {
		System.out.println("start to play record....");
	}

	public void pausePlaying() {
		System.out.println("pause playing record...");
	}

	public void stopPlaying() {
		System.out.println("stop playing record....");
	}
}
