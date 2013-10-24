package com.test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import com.email.EmailAnalyzer;
import com.synonym.SynonymAnalyzer;

public class LuceneTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 */
	public static void main(String[] args) throws Exception {
		/*
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
	 	Directory fsDir = FSDirectory.open(new File("E:\\Work\\Lucene\\Index"));
		//IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_30, analyzer);
		IndexWriter writer = new IndexWriter(fsDir, analyzer, MaxFieldLength.LIMITED);
		
		Document doc = new Document();
		doc.add(new Field("Address", "data", Store.YES, Index.ANALYZED_NO_NORMS));
		
		writer.addDocument(doc);
		writer.commit();
		*/
		
		//testSimpleAnalyzer();
		
		testStandardAnalyzer();
			

	}
	
	public static void testSimpleAnalyzer() throws Exception {
		
		Analyzer analyzer = new SimpleAnalyzer();
		TokenStream ts = analyzer.tokenStream("Address", new StringReader("Ganesh-in12@domain.de"));
		
		ts.reset();
		while (ts.incrementToken()) {
			//System.out.println("token: " +  ts.toString());
			System.out.println("token: " +  ts.getAttribute(TermAttribute.class).term());
		}
		ts.close();
	}
	
	public static void testStandardAnalyzer() throws Exception {
		
		System.out.println("Standard Analyzer");
		
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		TokenStream ts = analyzer.tokenStream("Field", new StringReader("The quick brown fox jumps over lazy dog"));
		
		ts.reset();
		while (ts.incrementToken()) {
			//System.out.println("token: " +  ts.toString());
			System.out.println("token: " +  ts.getAttribute(TermAttribute.class).term());
		}
		ts.close();
	}

	public static void testSynonymAnalyzer() throws Exception {
		
		Analyzer analyzer = new SynonymAnalyzer();
		TokenStream ts = analyzer.tokenStream("Address", new StringReader("Expertise in C# and Lucene"));
		
		ts.reset();
		while (ts.incrementToken()) {
			//System.out.println("token: " +  ts.toString());
			System.out.println("token: " +  ts.getAttribute(TermAttribute.class).term());
		}
		ts.close();
	}

}
