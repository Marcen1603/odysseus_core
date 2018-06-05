/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@7dc62d42
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

package de.uniol.inf.is.odysseus.tc.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 03.12.14.
 */
public class CSVMessageCreator implements IMessageCreator {


    @Override
    public List<byte[]> create(List<Object[]> oList) {
        final List<byte[]> result = new ArrayList<>();
        for(final Object[] oArr : oList) {
            final StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < oArr.length - 1; i++) {
                strBuilder.append(oArr[i].toString() + ",");
            }
            strBuilder.append(oArr[oArr.length - 1].toString());
            result.add(strBuilder.toString().getBytes());
            System.out.println(strBuilder.toString());
        }
        return result;
    }
}
