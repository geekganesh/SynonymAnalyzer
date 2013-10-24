
package com.email;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;


/**
 * This class amalyzes the Email Address
 * @author Ganesh Murugappan
 */

public class EmailFilter extends TokenFilter 
{
    private TermAttribute termAtt;
    private Stack<String> emailTokenStack;
    public EmailFilter(TokenStream in) {
        super(in);
        emailTokenStack = new Stack<String>();
        termAtt = addAttribute(TermAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException
    {
       if (!input.incrementToken())
       {
           if (emailTokenStack.size() > 0)
           {
                String subToken = emailTokenStack.pop();
                termAtt.setTermBuffer(subToken);
                return true;
           }

           return false;
       }

        String token = String.copyValueOf(termAtt.termBuffer());
        token = token.trim();
        if (token == null) return false;

        putPart(token);

        if (emailTokenStack.size() > 0)
        {
            String subToken = emailTokenStack.pop();
            termAtt.setTermBuffer(subToken);
            return true;
        }

        return false;
    }

    private void putPart(String token) throws IOException
    {
    	String emailAddress = token;
        //System.out.println("MAIN PART: " + emailAddress);
    	
    	String [] parts = extractEmailParts(emailAddress);
        for (int i = parts.length - 1; i >= 0; i--)
        {
            if ((parts[i]== null) || (parts[i].length() == 0))continue;

            //Token subToken = new Token(parts[i].toCharArray(), 0, parts[i].length(),
            //                     token.startOffset(), token.endOffset());
            //subToken.setPositionIncrement(0);
            emailTokenStack.push(parts[i]);
            //System.out.println(parts[i]);
        }
    }

    private String[] extractEmailParts(String email)
    {
    	if (email.indexOf('@') == -1)
    	     return extractWhitespaceParts(email);

    	ArrayList<String> partsList = new ArrayList<String>(4);
        String[] splitOnAT = email.split("@");
        for (int index =0; index < splitOnAT.length; index++)
        {
            String[] splitOnDot = splitOnAT[index].split("\\.");
            for (int i=0; i < splitOnDot.length; i++) {
                partsList.add(splitOnDot[i]);
            }
        }

        return partsList.toArray(new String[0]);
    }

    private String[] extractWhitespaceParts(String email)
    {
        email = email.replaceAll("\"", "");
    	String[] whitespaceParts = email.split(" ");
    	ArrayList<String> partsList = new ArrayList<String>(2);
    	for (int index=0; index < whitespaceParts.length; index++)
        {
            String[] splitOnDot = whitespaceParts[index].split("\\.");
            for (int i=0; i < splitOnDot.length; i++) {
                partsList.add(splitOnDot[i]);
            }
    	}
        return partsList.toArray(new String[0]);
    }
}

/*
    private String[] extractEmailParts(String email)
    {
    	if (email.indexOf('@') == -1)
    	     return extractWhitespaceParts(email);

    	ArrayList<String> partsList = new ArrayList<String>();

    	String[] whitespaceParts = extractWhitespaceParts(email);

    	 for (int w=0;w<whitespaceParts.length;w++) {

    		 if (whitespaceParts[w].indexOf('@')==-1)
    			 partsList.add(whitespaceParts[w]);
    		 else {
    			 partsList.add(whitespaceParts[w]);
    			 String[] splitOnAmpersand = whitespaceParts[w].split("@");
    			 try {
    				 partsList.add(splitOnAmpersand[0]);
    		         partsList.add(splitOnAmpersand[1]);
    		     } catch (ArrayIndexOutOfBoundsException ae) {}

    		    if (splitOnAmpersand.length > 0) {
    		    	String[] splitOnDot = splitOnAmpersand[0].split("\\.");
 		            for (int i=0; i < splitOnDot.length; i++) {
 		                partsList.add(splitOnDot[i]);
 		            }
    		    }
		        if (splitOnAmpersand.length > 1) {
		            String[] splitOnDot = splitOnAmpersand[1].split("\\.");
		            for (int i=0; i < splitOnDot.length; i++) {
		                partsList.add(splitOnDot[i]);
		            }

		            if (splitOnDot.length > 2) {
		                String domain = splitOnDot[splitOnDot.length-2] + "." + splitOnDot[splitOnDot.length-1];
		                partsList.add(domain);
		            }
		        }
    		 }
    	 }
        return partsList.toArray(new String[0]);
    }
  */


