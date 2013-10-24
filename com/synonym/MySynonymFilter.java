package com.synonym;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class MySynonymFilter extends TokenFilter {

	private TermAttribute termAtt;
	HashMap<String, String> synonymMap = new HashMap<String, String>(10);
	private Stack<String> tokenStack = new Stack<String>();
	protected MySynonymFilter(TokenStream input) {
		super(input);
		termAtt = addAttribute(TermAttribute.class);
		
		synonymMap.put("lucene", "information retrieval");
		synonymMap.put("c#", "csharp");
	}

	@Override
	public boolean incrementToken() throws IOException {
		
		if (!tokenStack.isEmpty()) {
			termAtt.setTermBuffer(tokenStack.pop());
			return true;
		}
		
		if (!input.incrementToken()) return false;
	    		
		String token = String.copyValueOf(termAtt.termBuffer(), 0, termAtt.termLength());
        token = token.trim();
        if (token == null) return false;
        
        String synText = synonymMap.get(token);
        if (synText == null) {
        	termAtt.setTermBuffer(token);
        }
        else {
        	String text = token + " " + synText;
        	String [] subTokens = text.split("\\s");
        	for (String subToken : subTokens) {
        		tokenStack.add(subToken);
        	}
        	
        	termAtt.setTermBuffer(tokenStack.pop());
        }
		
		return true;
	}

	
}
