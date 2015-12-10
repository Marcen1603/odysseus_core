/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class DistinctTIPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {
    /** The sweep area to hold the data. */
    final private ITimeIntervalSweepArea<T> area;

    public DistinctTIPO(ITimeIntervalSweepArea<T> area) {
        this.area = area;
    }

    public DistinctTIPO(DistinctTIPO<T> po) {
        super(po);
        this.area = po.area.clone();
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;
    }

    @Override
    protected void process_next(T object, int port) {
        Iterator<T> iter = this.area.extractElementsStartingBeforeOrEquals(object.getMetadata().getStart());
        while (iter.hasNext()) {
            T next = iter.next();
            if (next.equals(object)) {
                if (!next.getMetadata().getStart().equals(object.getMetadata().getStart())) {
                    if (next.getMetadata().getEnd().after(object.getMetadata().getStart())) {
                        next.getMetadata().setEnd(object.getMetadata().getStart());
                    }
                    transfer(next);
                }
            }
            else {
                // Assume equal end timestamp (slide window)
                transfer(next);
            }
        }

        this.area.insert(object);
    }

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
    
}
