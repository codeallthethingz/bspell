package com.google.bspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.bspell.model.Configuration;
import com.google.bspell.model.Word;
import com.google.bspell.parsers.Parser;
import com.google.bspell.parsers.ParserFactory;
import com.softcorporation.suggester.BasicSuggester;
import com.softcorporation.suggester.Suggestion;
import com.softcorporation.suggester.dictionary.BasicDictionary;
import com.softcorporation.suggester.tools.SpellCheck;
import com.softcorporation.suggester.util.BasicSuggesterConfiguration;
import com.softcorporation.suggester.util.Constants;
import com.softcorporation.suggester.util.SpellCheckConfiguration;
import com.softcorporation.util.Logger;

public class BSpellCheck {
    private Configuration config;

    public BSpellCheck() {
    }

    public void setConfiguration(final Configuration c) {
        this.config = c;
    }
    
    public List<Word> check(final File file) throws FileNotFoundException, IOException {
        if (config == null) {
            System.out.println("Warning: No configration is specified.");
        }

        BasicDictionary dictionary = new BasicDictionary("file://" + config.getDictionary());
        SpellCheckConfiguration configuration = new SpellCheckConfiguration("file://" + config.getSpellCheckConfig());
        BasicSuggester suggester = new BasicSuggester(configuration);
        suggester.attach(dictionary);


        SpellCheck spellCheck = new SpellCheck(configuration);
        spellCheck.setSuggester(suggester);
        spellCheck.setSuggestionLimit(3);

        Parser parser = ParserFactory.getInstance(config).getParser(file);
        List<Word> words = parser.parse(file);

        List<Word> failed = new ArrayList<Word>();

        for (Word word : words) {
            spellCheck.setText(word.getValue().toString(), Constants.DOC_TYPE_TEXT, "en");
            spellCheck.check();

            ArrayList suggestions = null;

            while (spellCheck.hasMisspelt())  {
                failed.add(word);
                String misspeltWord = spellCheck.getMisspelt();
                suggestions = spellCheck.getSuggestions();
                for (int j = 0; j < suggestions.size(); j++) {
                    Suggestion suggestion = (Suggestion) suggestions.get(j);
                    word.getSuggestions().add(suggestion.getWord());
                }
                spellCheck.checkNext();
            }
        }
         return failed;
     }
}