package com.synonym;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.util.Version;


public class SynonymAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String arg0, Reader reader) {
		WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
		TokenStream result = new LowerCaseFilter(tokenizer);
		result = new MySynonymFilter(result);
		result = new StandardFilter(result);
		//result = new StopFilter(result);
    	return result;
	}

	

}
