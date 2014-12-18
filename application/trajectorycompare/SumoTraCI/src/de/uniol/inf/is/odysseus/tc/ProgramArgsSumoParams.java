/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@2010a301
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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;

/**
 * Created by marcus on 03.12.14.
 */
public class ProgramArgsSumoParams extends AbstractSumoParams {


    private String projectPath;

    private String configFilename;

    private int seed;

    @Inject
    public ProgramArgsSumoParams(@ProgramArgs String[] args) {
        for(String a : args) {
            String[] keyValue = a.split("=");
            switch (keyValue[0]) {
                case "--proj-path":
                case "-p":
                    this.projectPath = new File(keyValue[1]).getAbsolutePath();
                    System.out.println(this.projectPath);
                    break;
                case "--sumo-config":
                case "-c":
                    this.configFilename = keyValue[1];
                    break;
                case "--seed":
                case "-s":
                    this.seed = Integer.parseInt(keyValue[1]);
                    break;
            }
        }
    }

    @Override
    public String getProjectPath() {
        return this.projectPath;
    }

    @Override
    public String getConfigFilename() {
        for(String file : new File(this.getProjectPath()).list()) {
            if(file.endsWith("cfg")) {
                return new File(file).getName();
            }
        }
        return this.configFilename;
    }

    @Override
    public int getSeed() {
        return this.seed;
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @BindingAnnotation
    public @interface ProgramArgs {

    }
}
