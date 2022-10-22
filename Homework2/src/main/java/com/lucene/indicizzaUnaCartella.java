package com.lucene;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
 
public class indicizzaUnaCartella 
{
    public static void main(String[] args)
    {
     
        String docsPath = "Directory";
         
        
        String indexPath = "Indice";
 
   
        final Path docDir = Paths.get(docsPath);
 
        try
        {
    
            Directory dir = FSDirectory.open( Paths.get(indexPath) );
             
     
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
             

            IndexWriter writer = new IndexWriter(dir, iwc);
             
  
            indexDocs(writer, docDir);
   
            writer.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
     
    static void indexDocs(final IndexWriter writer, Path path) throws IOException 
    {
      
        if (Files.isDirectory(path)) 
        {

            Files.walkFileTree(path, new SimpleFileVisitor<Path>() 
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                {
                    try
                    {
                  
                    	
                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    } 
                    catch (IOException ioe) 
                    {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } 
        else
        {
       
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
 
    static void indexDoc(IndexWriter writer, Path file, long ultimaModifica) throws IOException 
    {
        try (InputStream stream = Files.newInputStream(file)) 
        {
 
            Document doc = new Document();
 
            doc.add(new StringField("nome", file.toString(), Field.Store.YES));
            doc.add(new TextField("contenuto", new String(Files.readAllBytes(file)), Store.YES));
             
 
   
            writer.updateDocument(new Term("nome", file.toString()), doc);
        }
    }
}
