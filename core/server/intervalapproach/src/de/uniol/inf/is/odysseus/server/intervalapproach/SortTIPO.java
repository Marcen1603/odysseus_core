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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SortTIPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {
    /** The sweep area to hold the data. */
    private ITimeIntervalSweepArea<T> area;
    private Comparator<T> comparator;
    private int[] sortAttributePos;
    private boolean[] ascending;

    public SortTIPO(ITimeIntervalSweepArea<T> area, int[] sortAttributePos, boolean[] ascending) {
        this.area = area;
        this.sortAttributePos = sortAttributePos;
        this.ascending = ascending;
    }

    public SortTIPO(SortTIPO<T> po) {
        super(po);
        this.area = po.area.clone();
        this.sortAttributePos = po.sortAttributePos;
        this.ascending = po.ascending;
        this.comparator = po.comparator;
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;
    }

    @Override
    protected void process_next(T object, int port) {
        Iterator<T> iter = this.area.extractElementsBefore(object.getMetadata().getStart());
        sendElements(iter);

        this.area.insert(object);

    }

	private void sendElements(Iterator<T> iter) {
		List<T> elements = new ArrayList<>();
        while (iter.hasNext()) {
            T next = iter.next();
            int index = Collections.binarySearch(elements, next, this.comparator);
            elements.add((index > -1) ? index : (-index) - 1, next);
        }
        for (T element : elements) {
            transfer(element);
        }
	}

	@Override
	protected void process_done(int port) {
    	sendElements(this.area.extractAllElements());
    	super.process_done(port);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		//	sendPunctuation(punctuation);
	}

    /**
     * @param area
     *            the area to set
     */
    public void setArea(ITimeIntervalSweepArea<T> area) {
        this.area = area;
    }

    /**
     * @return the area
     */
    public ITimeIntervalSweepArea<T> getArea() {
        return this.area;
    }

    /**
     * @param comparator
     *            the comparator to set
     */
    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * @return the comparator
     */
    public Comparator<T> getComparator() {
        return this.comparator;
    }

    /**
     * @return the sortAttributePos
     */
    public int[] getSortAttributePos() {
        return this.sortAttributePos;
    }

    /**
     * @return the ascending
     */
    public boolean[] getAscending() {
        return this.ascending;
    }

}
