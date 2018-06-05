/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@355d420a
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

import com.google.inject.Guice;

/**
 * Created by marcus on 28.11.14.
 */
public class Application {

    public static void main(String[] args) throws InterruptedException {
    	System.out.println(new File("Scenario/").getAbsolutePath());
        Guice.createInjector(new SumoZeroMQExposerModule(args)).getInstance(ApplicationLoop.class).run();
    }
}
