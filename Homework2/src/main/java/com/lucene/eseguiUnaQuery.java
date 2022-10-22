package com.lucene;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class eseguiUnaQuery 
{

    private static final String INDEX_DIR = "Indice";
 
    public static void main(String[] args) throws Exception 
    {
      
        IndexSearcher searcher = createSearcher();

        Scanner scanner = new Scanner(System.in);
        
        while (true){
         	 System.out.println("");
        	 System.out.println("Inserisci una parola o una frase qualsiasi.......");
    
        	 String inputString = scanner.nextLine();
        	 if(inputString.equals("")||inputString.equals("exit")) {
        		 System.out.println("Sto uscendo.....");
        		 break;
        	 }else {
             TopDocs documenti_trovati = cercaPerContenuto(inputString, searcher);
          
             System.out.println("Risultati :: " + documenti_trovati.totalHits);
            

             for (ScoreDoc sd : documenti_trovati.scoreDocs) {
                 Document d = searcher.doc(sd.doc);
                 System.out.println("File : "+ d.get("nome") + ", Score : " + sd.score);
                       
             }
             }
            	
            	 
        }
 
    }
	private static TopDocs cercaPerTitolo(String titolo, IndexSearcher searcher) throws Exception
	{
		QueryParser qp = new QueryParser("nome", new WhitespaceAnalyzer());
		Query titoloQuery = qp.parse(titolo);
		TopDocs hits = searcher.search(titoloQuery, 10);
		return hits;
	}
    

    private static TopDocs cercaPerContenuto(String contenuto, IndexSearcher searcher) throws Exception
    {
        CharArraySet stopWords = new CharArraySet(Arrays.asList("di", "a", "da", "dei", "il", "la","del","che","lo","i","in","con","su","per","tra","fra","gli"), true);
        QueryParser qp = new QueryParser("contenuto", new StandardAnalyzer(stopWords));
        Query query = qp.parse(contenuto);
 
        TopDocs hits = searcher.search(query, 10);
        return hits;
    }
 
    private static IndexSearcher createSearcher() throws IOException 
    {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
         

        IndexReader reader = DirectoryReader.open(dir);
         
    
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}