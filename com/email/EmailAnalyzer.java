

package com.email;

import org.apache.lucene.analysis.*;
import org.apache.lucene.util.Version;

import java.io.*;

/**
 *
 * @author Ganesh Murugappan
 */
public class EmailAnalyzer extends Analyzer
{

    private int position = 0;

    public class LowercaseDelimiterAnalyzer extends Analyzer
    {
        private final char fDelim;

        public LowercaseDelimiterAnalyzer(char delim) {
            fDelim = delim;
        }

        @Override
        public TokenStream tokenStream(String fieldName, Reader reader)
        {
           TokenStream result = new CharTokenizer(reader)
           {
               @Override
               protected boolean isTokenChar(char c) {
                  return c != fDelim;
               }
           };

           result = new LowerCaseFilter(result);
           return result;
        }
    }

    @Override
    public final TokenStream tokenStream(String fieldName, final Reader reader)
    {
        //TokenStream result = new LowercaseDelimiterAnalyzer(',').tokenStream(fieldName, reader);
    	WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
    	TokenStream result = new LowerCaseFilter(tokenizer);
    	result = new EmailFilter(result);
        return result;
     }
}


