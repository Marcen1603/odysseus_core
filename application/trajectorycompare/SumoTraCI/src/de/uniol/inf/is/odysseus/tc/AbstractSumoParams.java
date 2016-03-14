/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@5b1bd1b2
 * Copyright (c) ${year}, ${owner}, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package de.uniol.inf.is.odysseus.tc;

import java.io.File;

/**
 * Created by marcus on 03.12.14.
 */
public abstract class AbstractSumoParams implements ISumoParams {

    @Override
	public String getSumoFilePath(String filename) {
        return new File(this.getProjectPath(), filename).toString();
    }
}
