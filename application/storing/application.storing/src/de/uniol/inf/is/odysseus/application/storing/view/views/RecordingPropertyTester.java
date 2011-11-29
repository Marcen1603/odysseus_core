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

package de.uniol.inf.is.odysseus.application.storing.view.views;

import java.util.List;

import org.eclipse.core.expressions.PropertyTester;

import de.uniol.inf.is.odysseus.application.storing.controller.RecordEntry;

/**
 * 
 * @author Dennis Geesen Created at: 11.11.2011
 */
public class RecordingPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof List<?>) {
			List<?> l = ((List<?>) receiver);
			if (l.size() > 0) {
				if (l.get(0) instanceof RecordEntry) {
					RecordEntry record = (RecordEntry) l.get(0);
					if (property.equalsIgnoreCase(RecordingView.PROPERTY_IS_RECORDING_PAUSABLE)) {
						return (record.isStarted() && record.isPlayingStopped());
					}
					if (property.equalsIgnoreCase(RecordingView.PROPERTY_IS_RECORDING_STARTABLE)) {
						return ((record.isInitialized() || record.isStopped() || record.isPaused()) && record.isPlayingStopped());
					}
					if (property.equalsIgnoreCase(RecordingView.PROPERTY_IS_RECORDING_STOPPABLE)) {
						return record.isStarted() || record.isPaused();
					}
					if (property.equalsIgnoreCase(RecordingView.PROPERTY_IS_PLAYING_PAUSABLE)) {
						return record.isStopped() && record.isPlayingStarted();
					}
					if (property.equalsIgnoreCase(RecordingView.PROPERTY_IS_PLAYING_STOPPABLE)) {
						return record.isStopped() && (record.isPlayingStarted() || record.isPlayingPaused());
					}
					if (property.equalsIgnoreCase(RecordingView.PROPERTY_IS_PLAYING_STARTABLE)) {
						return record.isStopped() && (record.isPlayingPaused() || record.isPlayingStopped());
					}
					if(property.equalsIgnoreCase(RecordingView.PROPERTY_IS_DELETABLE)){
						return (record.isStopped() && record.isPlayingStopped()) || record.isInitialized();
					}					
				}
			}
		}
		return false;
	}

}
