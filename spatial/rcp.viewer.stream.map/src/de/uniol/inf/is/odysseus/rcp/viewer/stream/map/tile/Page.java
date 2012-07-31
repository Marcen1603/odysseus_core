/*******************************************************************************
 * Copyright (c) 2008, 2012 Stepan Rutz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stepan Rutz - initial implementation
 *******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author stepan.rutz
 * @version $Revision$
 */
public interface Page {
    Control getControl(PageContainer container, Composite parent);
}
