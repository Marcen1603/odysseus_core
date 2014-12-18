/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@3ab46147
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

/**
 * Created by marcus on 03.12.14.
 */
public class StaticSumoParams extends AbstractSumoParams {

    @Override
    public String getProjectPath() {
        return "/home/marcus/Dokumente/masterarbeit/sumo/test/";
    }

    @Override
    public String getConfigFilename() {
        return "harkebruegge.sumocfg";
    }

    @Override
    public int getSeed() {
        return 0;
    }
}
