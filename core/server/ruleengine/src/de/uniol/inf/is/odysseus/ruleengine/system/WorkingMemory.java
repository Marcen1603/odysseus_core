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
package de.uniol.inf.is.odysseus.ruleengine.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class WorkingMemory {

    public static Logger LOGGER = LoggerFactory.getLogger("ruleengine");

    private List<Pair<Object, IRule<?, ?>>> notexecuted = new ArrayList<>();
    private List<Pair<Object, IRule<?, ?>>> executed = new ArrayList<>();

    final private IWorkingEnvironment<?> env;
    private Collection<Object> objects = new HashSet<Object>();
    private volatile boolean hasChanged = false;
    private int executedRules = 0;
    
    private Map<String, Object> keyValueMap = new HashMap<String, Object>(); 

    private Map<Class<?>, Collection<Object>> objectMap = new HashMap<Class<?>, Collection<Object>>();

    public WorkingMemory(IWorkingEnvironment<?> env) {
        this.env = env;
    }

    public void removeObject(Object o) {
        this.removeObject(o, false);
    }

    public void clear() {
        objectMap.clear();
    }

    public void insertObject(Object o) {
        this.insertObject(o, false);
    }

    private void insertObject(Object o, boolean isupdate) {
        if (!isupdate) {
            LOGGER.trace("Inserted into memory: \t" + o);
        }
        this.objects.add(o);
        insertIntoTree(o);
        this.hasChanged = true;
    }

    private void insertIntoTree(Object o) {
        for (Class<?> sc : getAllInterfacesAndSuperclasses(o)) {
            if (!objectMap.containsKey(sc)) {
                objectMap.put(sc, new HashSet<>());
            }
            objectMap.get(sc).add(o);
        }
    }

    private void removeFromTree(Object o) {
        for (Class<?> sc : getAllInterfacesAndSuperclasses(o)) {
            objectMap.get(sc).remove(o);
            if (objectMap.get(sc).isEmpty()) {
                objectMap.remove(sc);
            }
        }
    }

    private List<Class<?>> getAllInterfacesAndSuperclasses(Object o) {
        List<Class<?>> allClasses = new ArrayList<Class<?>>();
        allClasses.add(o.getClass());
        List<Class<?>> supercs = ClassUtils.getAllSuperclasses(o.getClass());
        if (supercs != null) {
            allClasses.addAll(supercs);
        }
        List<Class<?>> intercs = ClassUtils.getAllInterfaces(o.getClass());
        if (intercs != null) {
            allClasses.addAll(intercs);
        }
        return allClasses;

    }

    public void removeObject(Object o, boolean isupdate) {
        if (!isupdate) {
            LOGGER.trace("Removed from memory: \t" + o);
        }
        boolean removed = this.objects.remove(o);
        if (!removed) {
            throw new RuntimeException("Could not remove object " + o + " from working memory");
        }
        removeFromTree(o);
        this.hasChanged = true;
    }

    public void updateObject(Object o) {
        LOGGER.trace("Update memory: \t" + o);
    }

    public int process() throws RuleException {
        LOGGER.trace("Rule engine started and now looking for matches...");
        for (IRuleFlowGroup group : this.env.getRuleFlow()) {
            runGroup(group);
            while (hasChanged) {
                runGroup(group);
            }
            LOGGER.trace("Group finished: " + group);
        }
        return executedRules;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void runGroup(IRuleFlowGroup group) throws RuleException {
        hasChanged = false;
        Set<Class<?>> clazzes = new HashSet(objectMap.keySet());
        Iterator<IRule<?, ?>> iterator = this.env.getRuleFlow().iteratorRules(group, clazzes);

        while (iterator.hasNext()) {
            IRule rule = iterator.next();
            Collection<Object> matchingObjects = objectMap.get(rule.getConditionClass());
            LOGGER.trace("Trying to run rule " + rule + " on " + matchingObjects);
            if (matchingObjects == null) {
                // no objects for this condition clazz
                continue;
            }
            rule.setCurrentWorkingMemory(this);
            for (Object matchingObject : matchingObjects) {
                if (rule.isExecutable(matchingObject, this.env.getConfiguration())) {
                    LOGGER.trace("Running rule " + rule + " on " + matchingObject);
                    rule.execute(matchingObject, this.env.getConfiguration());
                    if (LOGGER.isDebugEnabled()) {
                        Pair<Object, IRule<?, ?>> pair = new Pair(matchingObject, rule);
                        executed.add(pair);
                        if (this.notexecuted.contains(pair)) {
                            this.notexecuted.remove(pair);
                        }
                    }
                    if (this.hasChanged) {
                        // if wm was changed: stop...
                        return;
                    }
                }
                else {
                    if (LOGGER.isDebugEnabled()) {
                        notexecuted.add(new Pair(matchingObject, rule));
                    }
                }
            }
        }
    }

    public Collection<Object> getCurrentContent() {
        return this.objects;
    }

    public String getDebugTrace() {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Following rules were executed, because there was a successful matching to an object:\n");
            if (this.executed.size() > 0) {
                for (Pair<Object, IRule<?, ?>> entry : this.executed) {
                    sb.append("Object: ").append(entry.getE1().getClass().getSimpleName()).append("(").append(entry.getE1().getClass().getCanonicalName()).append(")");
                    sb.append("\t\tRule: ").append(entry.getE2().getName()).append("(").append(entry.getE2().getClass().getCanonicalName()).append(")");
                    sb.append("\n");
                }
            }
            else {
                sb.append("None\n");
            }
            sb.append("\n");
            sb.append("Following rules were NOT executed although there was a successful matching to an object:\n");
            if (this.executed.size() > 0) {
                for (Pair<Object, IRule<?, ?>> entry : this.notexecuted) {
                    sb.append("Object: ").append(entry.getE1().getClass().getSimpleName()).append("(").append(entry.getE1().getClass().getCanonicalName()).append(")");
                    sb.append("\t\tRule: ").append(entry.getE2().getName()).append("(").append(entry.getE2().getClass().getCanonicalName()).append(")");
                    sb.append("\n");
                }
            }
            else {
                sb.append("None\n");
            }
            sb.append("\n");

            return sb.toString();
        }
        return "Please enable debug level";
    }
    
    public void addToKeyValueMap(String key, Object value){
    	this.keyValueMap.put(key,value);
    }
    
    public void removeFromKeyValueMap(String key){
    	this.keyValueMap.remove(key);
    }
    
    public Object getFromKeyValueMap(String key){
    	return this.keyValueMap.get(key);
    }
    
}
