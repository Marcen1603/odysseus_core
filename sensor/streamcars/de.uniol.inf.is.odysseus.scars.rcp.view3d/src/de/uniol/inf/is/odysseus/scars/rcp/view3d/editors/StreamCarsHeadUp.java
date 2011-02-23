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
package de.uniol.inf.is.odysseus.scars.rcp.view3d.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;
import de.uniol.inf.is.odysseus.scars.rcp.view3d.Activator;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class StreamCarsHeadUp implements IStreamEditorType {

	private SDFAttributeList schema;
	private SchemaIndexPath carPath;
	private Image warn;
	private Image ok;
	private Label label;
	private Label labelText;
	private boolean warning;

	@Override
	public void streamElementRecieved(final Object element, int port) {
		MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) element;

		TupleIndexPath tuplePath = carPath.toTupleIndexPath(tuple);

		if (tuplePath.getLength() <= 0) {
			if (warning) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						if (label.isDisposed()) {
							return;
						}
						if (labelText.isDisposed()) {
							return;
						}
						label.setImage(ok);
						labelText.setText("");
						warning = false;
					}
				});
			}
		} else {
			double distance = 0;
			double distanceTMP = 0;
			for (TupleInfo car : tuplePath) {
				MVRelationalTuple<?> carTuple = (MVRelationalTuple<?>) car.tupleObject;

				float x = carTuple.getAttribute(2);
				float y = carTuple.getAttribute(3);
				float z = carTuple.getAttribute(4);

				distanceTMP = Math.sqrt(x * x + y * y + z * z);
				if (distanceTMP > distance) {
					distance = distanceTMP;
				}
			}

			final double setter = distance;
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (label.isDisposed()) {
						return;
					}
					if (labelText.isDisposed()) {
						return;
					}
					if (!warning) {
						label.setImage(warn);
						warning = true;
					}
					labelText.setText("Entfernung: " + setter);
				}
			});

		}
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {

	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		ISource<?> src = editorInput.getStreamConnection().getSources()
				.iterator().next();
		schema = src.getOutputSchema();
		SchemaHelper helper = new SchemaHelper(schema);
		carPath = helper.getSchemaIndexPath("scan:cars:car");
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Composite base = new Composite(parent, SWT.NONE);
		base.setLayout(new GridLayout(1, true));
		
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;
		
		label = new Label(base, SWT.NONE);
		label.setLayoutData(data);
		labelText = new Label(base, SWT.NONE);
		labelText.setLayoutData(data);
		
		warn = ImageDescriptor.createFromURL(
				Activator.getDefault().getBundle()
						.getResource("icons/warn.png")).createImage();

		ok = ImageDescriptor.createFromURL(
				Activator.getDefault().getBundle().getResource("icons/ok.png"))
				.createImage();

		label.setImage(ok);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		this.ok.dispose();
		this.warn.dispose();
	}

}
