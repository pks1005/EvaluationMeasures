package edu.unh.cs.cs853;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.cs853.search.Indexer;
import edu.unh.cs.cs853.search.SearchEngine;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, CborException {

        try {

            System.setProperty("file.encoding", "UTF-8");
            File file = new File(args[0]);
            final FileInputStream fileInputStream = new FileInputStream(file);

            // build a lucene index
            System.out.println("rebuildIndexes");
            Indexer indexer = new Indexer();
            indexer.rebuildIndexes(fileInputStream);
            System.out.println("rebuildIndexes done");

            // perform search on the query
            // and retrieve the top 10 result
            System.out.println("performSearch");
            SearchEngine se = new SearchEngine();
            TopDocs topDocs = se.performSearch("power nap benefits", 10);

            System.out.println("Results found: " + topDocs.totalHits);
            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc scoreDoc :hits) {
                Document doc = se.getDocument(scoreDoc.doc);
                System.out.println(doc.get("id")
                        + " " + doc.get("content")
                        + " (" + scoreDoc.score + ")");
            }
            System.out.println("performSearch done");
        } catch (Exception e) {
            System.out.println("Exception caught.\n");
        }
    }

}
