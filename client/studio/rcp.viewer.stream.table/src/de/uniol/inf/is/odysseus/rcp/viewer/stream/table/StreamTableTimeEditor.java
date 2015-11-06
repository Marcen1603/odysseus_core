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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * This StreamTableEditor shows all data stream elements that are valid at the
 * most recent observation point (the point in time of the interval start of the
 * most recent). Additionally, the most N recent observation points can be saved
 * to re-view them.
 * 
 * @author Marco Grawunder, Cornelius Ludmann
 *
 */
public class StreamTableTimeEditor extends StreamTableEditor {

	protected static final String LAST_STRING = "Most recent observation point";

	/**
	 * The number of recent observation point that should be saved.
	 */
	protected int noOfSavedObservationPoints = 0;
	protected Combo observationTimeCombo;
	protected String selectedObservationPoint = LAST_STRING;
	/**
	 * A map of observation points and all elements that are valid at this point
	 * in time.
	 */
	protected Map<PointInTime, List<Tuple<?>>> savedObservationPoints = new HashMap<>();
	protected Label recentObservationPointLabel;

	public StreamTableTimeEditor() {
		super(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateTuples(final Tuple<?> element) {
		// element must provide time interval, cast and risk cast error
		// instead of testing each time;
		final Tuple<? extends ITimeInterval> e = (Tuple<? extends ITimeInterval>) element;
		final PointInTime start = e.getMetadata().getStart();

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				recentObservationPointLabel.setText("  " + LAST_STRING + ": " + start.toString());
			}
		});

		if (LAST_STRING.equals(selectedObservationPoint)) {

			for (final Iterator<Tuple<?>> iterator = tuples.iterator(); iterator.hasNext();) {
				final Tuple<?> t = iterator.next();
				if (((Tuple<? extends ITimeInterval>) t).getMetadata().getEnd().beforeOrEquals(start)) {
					iterator.remove();
				}
			}
			super.updateTuples(element);

			if (noOfSavedObservationPoints > 0) {
				List<Tuple<?>> copy = new ArrayList<>(tuples.size());
				copy.addAll(tuples);
				savedObservationPoints.put(start, copy);
			}

			final List<PointInTime> keys = new ArrayList<>();
			keys.addAll(savedObservationPoints.keySet());
			Collections.sort(keys);

			while (savedObservationPoints.size() > noOfSavedObservationPoints) {
				savedObservationPoints.remove(keys.get(0));
				keys.remove(0);
			}

			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					observationTimeCombo.removeAll();
					observationTimeCombo.add(LAST_STRING);
					for (int i = keys.size() - 1; i >= 0; --i) {
						observationTimeCombo.add(keys.get(i).toString());
					}
					observationTimeCombo.select(0);
				}
			});

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.viewer.stream.table.StreamTableEditor#
	 * initToolbar(org.eclipse.swt.widgets.ToolBar)
	 */
	@Override
	public void initToolbar(ToolBar toolbar) {
		super.initToolbar(toolbar);
		ToolItem sep = new ToolItem(toolbar, SWT.SEPARATOR);

		observationTimeCombo = new Combo(toolbar, SWT.READ_ONLY);
		observationTimeCombo.add(LAST_STRING);
		observationTimeCombo.select(0);
		observationTimeCombo.pack();
		observationTimeCombo.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
			 * eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = ((Combo) e.getSource());
				if (!selectedObservationPoint.equals(combo.getItem(combo.getSelectionIndex()))) {
					selectedObservationPoint = combo.getItem(combo.getSelectionIndex());

					if (LAST_STRING.equals(selectedObservationPoint)) {
						tuples.clear();
						refresh();
					} else {
						tuples.clear();
						tuples.addAll(
								savedObservationPoints.get(PointInTime.parsePointInTime(selectedObservationPoint)));
						refresh();
					}
				}
			}
		});
		sep.setWidth(observationTimeCombo.getSize().x);
		sep.setControl(observationTimeCombo);

		ToolItem sep2a = new ToolItem(toolbar, SWT.SEPARATOR);
		Label label = new Label(toolbar, SWT.NONE);
		label.setText("  No. of saved observation points: ");
		label.pack();
		sep2a.setWidth(label.getSize().x);
		sep2a.setControl(label);

		ToolItem sep2 = new ToolItem(toolbar, SWT.SEPARATOR);

		Combo noOfSavedItemsCombo = new Combo(toolbar, SWT.NONE);
		noOfSavedItemsCombo.add("0");
		noOfSavedItemsCombo.add("10");
		noOfSavedItemsCombo.add("20");
		noOfSavedItemsCombo.add("50");
		noOfSavedItemsCombo.add("100");
		noOfSavedItemsCombo.add("200");
		noOfSavedItemsCombo.select(0);
		noOfSavedItemsCombo.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
			 * eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = ((Combo) e.getSource());
				try {
					noOfSavedObservationPoints = Integer.parseInt(combo.getItem(combo.getSelectionIndex()));
				} catch (NumberFormatException nfe) {
					noOfSavedObservationPoints = 0;
				}
			}
		});
		noOfSavedItemsCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Combo combo = ((Combo) e.getSource());
				try {
					noOfSavedObservationPoints = Integer.parseInt(combo.getText());
				} catch (NumberFormatException nfe) {
					noOfSavedObservationPoints = 0;
				}
			}
		});
		noOfSavedItemsCombo.pack();
		sep2.setWidth(noOfSavedItemsCombo.getSize().x);
		sep2.setControl(noOfSavedItemsCombo);

		ToolItem sep3 = new ToolItem(toolbar, SWT.SEPARATOR);
		recentObservationPointLabel = new Label(toolbar, SWT.NONE);
		// XXX: Set a better label width
		recentObservationPointLabel.setText("  " + LAST_STRING + ": " + System.currentTimeMillis());
		recentObservationPointLabel.pack();
		sep3.setWidth(recentObservationPointLabel.getSize().x);
		sep3.setControl(recentObservationPointLabel);
		recentObservationPointLabel.setText("");
	}

}
