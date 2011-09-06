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

package de.uniol.inf.is.odysseus.mining.memory.tiltedtimeframe;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshot;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshotMergeFunction;
import de.uniol.inf.is.odysseus.mining.memory.ITimeCapsule;

/**
 * 
 * @author Dennis Geesen Created at: 01.09.2011
 */
public class TiltedTimeWindow<T extends ISnapshot> implements ITimeCapsule<T> {

	private LinkedList<TiltedTimeFrame<T>> frames = new LinkedList<TiltedTimeFrame<T>>();
	private ISnapshotMergeFunction<T> datamergeFunction;

	public TiltedTimeWindow(ISnapshotMergeFunction<T> mergeFunction) {
		this.datamergeFunction = mergeFunction;
	}

	@Override
	public void store(T snapshot) {
		TimeFrame<T> newFrame = new TimeFrame<T>(snapshot);
		insert(newFrame, 0);

	}

	private void insert(TimeFrame<T> toInsert, int position) {
		if (isEmptyFrame(position)) {
			frames.add(new TiltedTimeFrame<T>(toInsert));
		} else {
			TiltedTimeFrame<T> frame = frames.get(position);
			TimeFrame<T> shifted = frame.insert(toInsert, this.datamergeFunction);
			if (shifted != null) {
				position++;
				insert(shifted, position);
			}
		}

	}

	private boolean isEmptyFrame(int index) {
		if (index >= this.frames.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		String str = "";
		int i = 0;
		for (TiltedTimeFrame<T> t : this.frames) {
			str = str + "Frame " + i + ": ";
			str = str + System.getProperty("line.separator");
			str = str + t.toString();
			str = str + System.getProperty("line.separator");
			str = str + "----------------";
			str = str + System.getProperty("line.separator");
			i++;
		}
		return str;
	}

	@Override
	public T retrieve(int frame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T retrieve(PointInTime timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

}
