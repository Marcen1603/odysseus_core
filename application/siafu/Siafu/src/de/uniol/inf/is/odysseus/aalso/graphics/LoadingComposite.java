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
package de.uniol.inf.is.odysseus.aalso.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class LoadingComposite extends Composite {

	public LoadingComposite(Composite parent) {
		super(parent, SWT.BORDER);
		GridLayout glLoadingComposite = new GridLayout();
		GridData gdLoadingComposite = new GridData(SWT.CENTER, SWT.CENTER,
				true, true);
		this.setSize(parent.getSize());
		this.setLayout(glLoadingComposite);
		this.setLayoutData(gdLoadingComposite);
	}

	@Override
    public void dispose() {
		super.dispose();
	}

}
