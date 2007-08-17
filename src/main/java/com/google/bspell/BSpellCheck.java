package com.google.bspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.bspell.model.SpellConfiguration;
import com.google.bspell.model.Word;
import com.google.bspell.parsers.Parser;
import com.google.bspell.parsers.ParserFactory;
import com.google.bspell.utils.StringUtils;

import com.softcorporation.suggester.BasicSuggester;
import com.softcorporation.suggester.Suggestion;
import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.tools.SpellCheck;
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SpellCheckConfiguration;

public class BSpellCheck {
    private SpellConfiguration config;
    private SpellCheck spellCheck;

    public BSpellCheck() {
    }

    public void init(final SpellConfiguration c) {
        this.config = c;

        BasicDictionary dictionary = new BasicDictionary("file://" + config.getDictionary());
        SpellCheckConfiguration configuration = new SpellCheckConfiguration("file://" + config.getSpellCheckConfig());
        BasicSuggester suggester = new BasicSuggester(configuration);
        suggester.attach(dictionary);

        spellCheck = new SpellCheck(configuration);
        spellCheck.setSuggester(suggester);
        spellCheck.setSuggestionLimit(5);
    }

    public List<Word> check(final File file) throws FileNotFoundException, IOException {
        if (config == null) {
            System.out.println("Warning: No configuration is specified.");
        }

        Parser parser = ParserFactory.getInstance(config).getParser(file);
        List<Word> words = parser.parse(file);

        List<Word> failed = new ArrayList<Word>();

        for (Word word : words) {
            spellCheck.setText(word.getValue().toString(), Constants.DOC_TYPE_TEXT, "en");
            spellCheck.check();

            List suggestions = null;

            while (spellCheck.hasMisspelt())  {
                suggestions = spellCheck.getSuggestions();

                String combined = getCombinedWithValidWords(suggestions, spellCheck.getMisspelt());
                if (combined != null) {
                    word.getSuggestions().add(combined);
                    break;
                }

                for (int j = 0; j < suggestions.size(); j++) {
                    Suggestion suggestion = (Suggestion) suggestions.get(j);
                    word.getSuggestions().add(suggestion.getWord());
                }
                spellCheck.checkNext();
            }
            if (word.getSuggestions().size() > 0) {
                failed.add(word);
            }
        }
        return failed;
    }

    public String getCombinedWithValidWords(List suggestions, String misspeltWord) {
        for (int j = 0; j < suggestions.size(); j++) {
            Suggestion suggestion = (Suggestion) suggestions.get(j);
            String word = suggestion.getWord();
            if (word.indexOf(" ") == -1) {
                continue;
            }
            StringTokenizer tokens = new StringTokenizer(word, " ");
            StringBuffer sample = new StringBuffer();
            boolean found = true;
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken().toLowerCase();
                if (hasMisspelt(token)) {
                    found = false;
                    break;
                }
                sample.append(StringUtils.capitalize(token));
                if (!misspeltWord.toLowerCase().startsWith(sample.toString().toLowerCase())) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return sample.toString();
            }
        }
        return null;
    }

    private boolean hasMisspelt(String word) {
        spellCheck.setText(word, Constants.DOC_TYPE_TEXT, "en");
        spellCheck.check();
        return spellCheck.hasMisspelt();
    }
}