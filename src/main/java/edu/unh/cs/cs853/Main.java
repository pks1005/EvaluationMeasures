package edu.unh.cs.cs853;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.cs853.search.Indexer;
import edu.unh.cs.cs853.search.SearchEngine;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, CborException {

        try {

            System.setProperty("file.encoding", "UTF-8");
            File file = new File(args[0]);
            File paraFile = new File("/media/piyush/7C6446066445C41C/0 MS in CS/Fall-2017/IR/test200/train.test200.cbor.paragraphs");
            final FileInputStream fileInputStream = new FileInputStream(file);
            final FileInputStream paraFileInputStream = new FileInputStream(paraFile);

            // build a lucene index
            System.out.println("rebuildIndexes");
            Indexer indexer = new Indexer("page-index-directory");
            Indexer paraIndexer = new Indexer("index-directory");
            indexer.rebuildIndexes(fileInputStream);           
            paraIndexer.rebuildIndexes1(paraFileInputStream); 
            for(int i=0; i<2;i++){
            	//System.out.println("ID: "+indexer.nid.get(i).id + "\tname: "+indexer.nid.get(i).name);
            	//System.out.println("rebuildIndexes done");

                // perform search on the query
                // and retrieve the top 10 result
                System.out.println("performSearch");
                SearchEngine se = new SearchEngine();
                TopDocs topDocs = se.performSearch(indexer.nid.get(i).name, 5);

                System.out.println("Results found for Query "+indexer.nid.get(i).id + topDocs.totalHits);
                ScoreDoc[] hits = topDocs.scoreDocs;

                for (ScoreDoc scoreDoc :hits) {
                    Document doc = se.getDocument(scoreDoc.doc);
                    System.out.println(doc.get("id")
                            + " " + doc.get("name")
                            + " (" + scoreDoc.score + ")");
                }
            }
            
            
            
            System.out.println("performSearch done");
            indexer.cleanIndex();
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
            e.printStackTrace();
        }
    }

}
