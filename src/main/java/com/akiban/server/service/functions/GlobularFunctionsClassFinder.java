/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.service.functions;

import com.akiban.server.error.AkibanInternalException;
import com.akiban.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p></p>Does a very simple glob-style search of classes.</p>
 * <p>Rules:
 * <ul>
 *     <li>each "glob" consists of one or more segments, delimited by dots (.)</li>
 *     <ul>
 *      <li>the last segment is a class name glob</li>
 *      <li>previous segments are package globs</li>
 *     </ul>
 *     <li>within the class name glob segment <em>only</em>, the asterisk (*) is a wildcard</li>
 * </ul>
 * </p>
 */
final class GlobularFunctionsClassFinder implements FunctionsClassFinder {

    // FunctionsClassFinder interface

    @Override
    public Set<Class<?>> findClasses() {
        try {
            Set<Class<?>> results = new HashSet<Class<?>>();
            List<String> includes = Strings.dumpResource(GlobularFunctionsClassFinder.class, configFile);
            for (String include : includes) {
                int lastDot = include.lastIndexOf(".");
                String packageName = lastDot < 0 ? "." : include.substring(0, lastDot);
                String dir = packageName.replace(".", "/") + "/";
                Pattern pattern = compilePattern(lastDot < 0 ? include : include.substring(lastDot+1));
                List<String> dirContents = Strings.dumpURLs(
                        // we have to get the union of all classes in all resources. If we were to only get
                        // the default resource for dir, then in unit/ITs we'd get the test-classes dir, not main
                        GlobularFunctionsClassFinder.class.getClassLoader().getResources(dir)
                );
                for (String file : dirContents) {
                    Matcher matcher = pattern.matcher(file);
                    if (matcher.matches()) {
                        String className = packageName + "." + matcher.group(1);
                        Class<?> theClass = GlobularFunctionsClassFinder.class.getClassLoader().loadClass(className);
                        if (!results.add(theClass)) {
                            LOG.warn("ignoring duplicate class while looking for functions: {}", theClass);
                        }
                    }
                }
            }
            return new HashSet<Class<?>>(results);
        } catch (Exception e) {
            throw new AkibanInternalException("while looking for classes that contain functions", e);
        }
    }

    // GlobularFunctionsClassFinder interface

    public GlobularFunctionsClassFinder() {
        this("functionpath.txt");
    }

    // used for testing

    GlobularFunctionsClassFinder(String configFile) {
        this.configFile = configFile;
    }

    private static Pattern compilePattern(String from) {
        from = from.replace("*", ".*");
        from = "(" + from + ")" + Pattern.quote(".class");
        return Pattern.compile(from);
    }

    // object state

    private final String configFile;

    // class tate
    private static final Logger LOG = LoggerFactory.getLogger(GlobularFunctionsClassFinder.class);
}
