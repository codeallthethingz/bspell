/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.google.bspell;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.bspell.BSpellCheck;
import com.google.bspell.model.SpellConfiguration;
import com.google.bspell.model.Word;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import java.util.Iterator;

/**
 * A spell check mojo
 *
 * @goal bspell
 * @description Spell check the source files
 * @requiresDependencyResolution test
 */

public class BSpellMojo extends AbstractMojo {
    /**
     * The Maven Project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * @component
     */
    protected ArtifactFactory artifactFactory;

    /**
     * Build directory
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File buildDirectory;

    /**
     * Base directory of the project.
     * @parameter expression="${basedir}"
     */
    private File baseDirectory;

    /**
     * A set of file patterns to include.
     * @parameter property="includes"
     */
    private String[] includes;

    /**
     * A set of file patterns to exclude.
     * @parameter property="excludes"
     */
    private String[] excludes;

    /**
     * A set of file patterns to exclude.
     * @parameter property="spellConfiguration"
     */
    private SpellConfiguration config;

    public void setSpellConfiguration(SpellConfiguration c) {
        this.config = c;
    }

    public void execute() throws MojoExecutionException {
        System.out.println("==== " + config.getFormat() + " == " + config.getRegistry());
        System.out.println("includes: " + Arrays.asList(includes));
        try {
            DirectoryScanner ds = new DirectoryScanner();
            ds.setIncludes(includes);
            ds.setExcludes(excludes);
            ds.setBasedir(baseDirectory);
            ds.setCaseSensitive(true);
            ds.scan();
            String[] files = ds.getIncludedFiles();

            BSpellCheck checker = new BSpellCheck();
            checker.init(this.config);

            Map failures = new HashMap();
            
            try {
                for (int i = 0; i < files.length; i++) {
                    File source = new File(baseDirectory, files[i]);
                    List failed = checker.check(source);
                    if (!failed.isEmpty()) {
                        failures.put(source, failed);
                    }
                }

                System.out.println("Summary:");
                System.out.println("Processed " + files.length + " file(s), Found misspelt in " + failures.size() + " file(s)");
                dumpResults(failures);

            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            getLog().debug(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void dumpResults(Map failures) {
        if (config.getFormat() == null || "console".equals(config.getFormat())) {
            System.out.println("bSpell check reports: ");
            for (Iterator iter = failures.keySet().iterator(); iter.hasNext();) {
                File file = (File) iter.next();
                System.out.println("=========================");
                System.out.println("file: " + file);
                System.out.println("=========================");
                for (Iterator iter1 = ((List)failures.get(file)).iterator(); iter1.hasNext();) {
                    Word word = (Word) iter1.next();
                    System.out.println("*" + word.getValue().toString() + "* locations ---> " + word.getLocations() + " suggestion(s) ---> " + word.getSuggestions());
                }
            }
        }
    }
}
