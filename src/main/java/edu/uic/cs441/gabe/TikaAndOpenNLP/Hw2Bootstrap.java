package edu.uic.cs441.gabe.TikaAndOpenNLP;

/**
 * Created by gabe on 10/4/16.
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class Hw2Bootstrap {

    final static Logger logger = Logger.getLogger(Hw2Bootstrap.class);
    static {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}

	public static final String DOWNLOAD_DIRECTORY = System.getProperty("user.dir") + "/inputs";

//    public static void main(String[] args) throws SecurityException, SAXException, IOException, TikaException{
//	    // Set up a simple configuration that logs on the console.
//	    BasicConfigurator.configure();
//
//        File pdf_dir = new File(DOWNLOAD_DIRECTORY);
//
//        // if the directory does not exist, create it
//        if (!pdf_dir.exists()) {
//            pdf_dir.mkdir();
//        }
//
//        File[] files = new File(DOWNLOAD_DIRECTORY).listFiles();
//        if( files.length < 1) {
//            System.err.println("Error: Downloads folder is empty.");
//            System.exit(1);
//        }
//
//        List<TikaFile> tikaFileList = new ArrayList<>();
//
//        for(File file : files ) {
//
//            TikaFile tf = new TikaFile(file);
//            tikaFileList.add(tf);
//
//            for( String name : tf.getMetadata().names()) {
//                System.out.println("name: " + name);
//            }
//
//            System.out.println("file: " + file.getName());
//        }
//
//        OpenNLP openNLP = null;
//        try {
//            openNLP = new OpenNLP(OpenNLP.NAMED_ENTITY_PERSON_BIN);
//            SplitDocument doc = openNLP.splitDocument(tikaFileList.get(0));
//
////            List<Text> sentences  = doc.getSentences();
////            for(Text sentence : sentences) {
////                sentence.
////            }
//
//            System.out.println("Hello");
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//	        if(openNLP != null) {
//		        openNLP.close();
//	        }
//        }
//
//    }

}
