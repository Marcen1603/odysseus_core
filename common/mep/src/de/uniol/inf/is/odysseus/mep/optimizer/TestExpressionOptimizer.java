/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.optimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 * Test application for the expression optimizer.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: TestExpressionOptimizer.java | TestExpressionOptimizer.java |
 *          TestExpressionOptimizer.java $
 *
 */
public class TestExpressionOptimizer {
    public static void main(String[] args) {
        String packageName = "de.uniol.inf.is.odysseus.mep.functions";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> classes = new ArrayList<>();
        URL packageURL = classLoader.getResource(packageName.replace(".", "/"));
        URI uri;
        try {
            uri = new URI(packageURL.toString());
            File folder = new File(uri.getPath());
            classes = getClasses(folder, packageName);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (Class<?> cls : classes) {
            try {
                MEP.registerFunction((IMepFunction<?>) cls.newInstance());
            }
            catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            System.out.print("$:> ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            try {
                String line = in.readLine();
                if ("q".equalsIgnoreCase(line)) {
                    break;
                }
                IMepExpression<?> expression = MEP.getInstance().parse(line);
                System.out.println("<- " + expression);
                System.out.println("Optimized: " + BooleanExpressionOptimizer.optimize(expression));

                System.out.println("DNF: " + BooleanExpressionOptimizer.toDisjunctiveNormalForm(expression));

                System.out.println("CNF: " + BooleanExpressionOptimizer.toConjunctiveNormalForm(expression));
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    private static List<Class<?>> getClasses(File packageURL, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        File[] file = packageURL.listFiles();
        for (File cur : file) {
            if (cur.isDirectory()) {
                classes.addAll(getClasses(cur, packageName + "." + cur.getName()));
            }
            else {
                String name = cur.getName();
                name = cur.getName().substring(0, name.lastIndexOf('.'));
                try {
                    Class<?> clazz = Class.forName(packageName + "." + name);
                    if (IMepFunction.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                        classes.add(clazz);
                    }
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}
