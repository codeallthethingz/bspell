package com.google.bspell.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.bspell.BSpellCheck;
import com.google.bspell.model.Configuration;
import com.google.bspell.model.Word;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;

public class BSpellTask extends Task {

    private Configuration config;

    protected Vector rcs = new Vector();
    protected boolean failonerror = true;

    public void addConfiguration(Configuration c) {
        this.config = c;
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void add(ResourceCollection res) {
        rcs.add(res);
    }

    private String getMessage(Exception ex) {
        return ex.getMessage() == null ? ex.toString() : ex.getMessage();
    }

    public void execute() {
        List<File> sources = new ArrayList<File>();
        for (int i = 0; i < rcs.size(); i++) {
            ResourceCollection rc = (ResourceCollection) rcs.elementAt(i);
            if (rc instanceof FileSet && rc.isFilesystemOnly()) {
                FileSet fs = (FileSet) rc;
                DirectoryScanner ds = null;
                try {
                    ds = fs.getDirectoryScanner(getProject());
                } catch (BuildException e) {
                    if (failonerror
                        || !getMessage(e).endsWith(" not found.")) {
                        throw e;
                    } else {
                        log("Warning: " + getMessage(e), Project.MSG_ERR);
                        continue;
                    }
                }
                File fromDir = fs.getDir(getProject());

                String[] srcFiles = ds.getIncludedFiles();
                for (String srcFile : srcFiles) {
                    sources.add(new File(fromDir, srcFile));
                }
            }
        }
        if (sources.isEmpty()) {
            log("Warning: No files found!");
        }

        BSpellCheck checker = new BSpellCheck();
        checker.init(this.config);

        Map<File, List<Word>> failures = new HashMap<File, List<Word>>();

        try {
            for (File source : sources) {
                List<Word> failed = checker.check(source);
                if (!failed.isEmpty()) {
                    failures.put(source, failed);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        handleOutput("Summary:");
        handleOutput("Processed " + sources.size() + " file(s), Found misspelt in " + failures.size() + " file(s)");
        dumpResults(failures);
    }

    private void dumpResults(Map<File, List<Word>> failures) {
        if (config.getFormat() == null || "console".equals(config.getFormat())) {
            handleOutput("bSpell check reports: ");
            for (File file : failures.keySet()) {
                handleOutput("=========================");
                handleOutput("file: " + file);
                handleOutput("=========================");
                for (Word word : failures.get(file)) {
                    handleOutput("*" + word.getValue().toString() + "* locations ---> " + word.getLocations() + " suggestion(s) ---> " + word.getSuggestions());
                }
            }
        }
    }
}

 