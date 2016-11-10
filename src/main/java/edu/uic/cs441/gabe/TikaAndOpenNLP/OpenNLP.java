package edu.uic.cs441.gabe.TikaAndOpenNLP;

import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.util.Span;

import java.io.*;

/**
 * Created by gabe on 10/6/16.
 */
public class OpenNLP {
	public static final String NAMED_ENTITY_PERSON_BIN = "/en-ner-person.bin";
	public static final String NAMED_ENTITY_LOCATION_BIN = "/en-ner-location.bin";
	public static final String NAMED_ENTITY_MONEY_BIN = "/en-ner-money.bin";
	public static final String NAMED_ENTITY_ORGANIZATION_BIN = "/en-ner-organization.bin";
	public static final String NAMED_ENTITY_TIME_BIN = "/en-ner-time.bin";
	public static final String NAMED_ENTITY_DATE_BIN = "/en-ner-date.bin";

	public static String[] NAMED_ENTITY_BINS = {
			NAMED_ENTITY_PERSON_BIN,
			NAMED_ENTITY_LOCATION_BIN,
			NAMED_ENTITY_MONEY_BIN,
			NAMED_ENTITY_ORGANIZATION_BIN,
			NAMED_ENTITY_TIME_BIN,
			NAMED_ENTITY_DATE_BIN
	};

	public static final int NUM_OF_NER_BINS = NAMED_ENTITY_BINS.length;

	public static String SENTENCE_DETECTION_BIN = "/en-sent.bin";
	public static String TOKENIZER_BIN = "/en-token.bin";

	private String NAME_FINDER_BIN = null;
    public String getNameFinderBin(){
        return NAME_FINDER_BIN;
    }

	private InputStream name_finder_ModelIn = null;
	private InputStream sentence_detection_ModelIn = null;
	private InputStream tokenizer_ModelIn = null;
	private SentenceModel sentenceModel = null;
	private SentenceDetectorME sentenceDetectorME = null;
	private TokenizerModel tokenModel = null;
	private TokenizerME tokenizerME = null;
	private TokenNameFinderModel tokenNameFinderModel = null;
	private NameFinderME nameFinderME = null;

	private boolean active = true;

	private void init() throws IOException {
		if(sentenceModel == null) {
			sentenceModel = new SentenceModel(sentence_detection_ModelIn);
			sentenceDetectorME = new SentenceDetectorME(sentenceModel);
		}

		if(tokenModel == null ) {
			tokenModel = new TokenizerModel(tokenizer_ModelIn);
			tokenizerME = new TokenizerME(tokenModel);
		}

		if(tokenNameFinderModel == null) {
			if(name_finder_ModelIn == null) {
				this.name_finder_ModelIn = new FileInputStream(
						new File( OpenNLP.class.getResource( NAME_FINDER_BIN).getFile()) );
			}
			tokenNameFinderModel = new TokenNameFinderModel(name_finder_ModelIn);
			nameFinderME = new NameFinderME(tokenNameFinderModel);
		}
	}

	private OpenNLP() throws FileNotFoundException {}


	public OpenNLP(String NAMED_ENTITY_BIN) throws IOException {
		this.sentence_detection_ModelIn = new FileInputStream(
				OpenNLP.class.getResource(SENTENCE_DETECTION_BIN).getFile());

		this.tokenizer_ModelIn = new FileInputStream(
				new File (OpenNLP.class.getResource(TOKENIZER_BIN).getFile()));

		this.NAME_FINDER_BIN = NAMED_ENTITY_BIN;
		this.name_finder_ModelIn = new FileInputStream(
				new File(OpenNLP.class.getResource(NAME_FINDER_BIN).getFile() ));

		init();
	}

	private void close_name_finder_ModelIn(){
		if ( name_finder_ModelIn != null) {
			try {
				name_finder_ModelIn.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			finally {
				name_finder_ModelIn = null;
			}
		}
	}

	public void close() {
		if (sentence_detection_ModelIn != null) {
			try {
				sentence_detection_ModelIn.close();
			}
			catch (IOException e) {
			}
		}
		if ( tokenizer_ModelIn != null) {
			try {
				tokenizer_ModelIn.close();
			}
			catch (IOException e) {
			}
		}

		close_name_finder_ModelIn();
		active = false;
	}

	private void checkIfActice() {
		if(!active){
			System.err.println("You tried to use a closed, invalid instance of OpenNLP.");
			System.err.println("Please try instantiating a new instance or calling revive()");
			System.exit(1);
		}
	}

	public void revive(String NAMED_ENTITY_BIN) throws FileNotFoundException, IOException{
		this.sentence_detection_ModelIn = new FileInputStream(this.getClass().getResource(SENTENCE_DETECTION_BIN).getFile());
		this.tokenizer_ModelIn = new FileInputStream(this.getClass().getResource(TOKENIZER_BIN).getFile());
		this.NAME_FINDER_BIN = NAMED_ENTITY_BIN;
		this.name_finder_ModelIn = new FileInputStream(this.getClass().getResource(NAME_FINDER_BIN).getFile());
		init();
		active = true;
	}

	public void setNameEntityBin(String NAMED_ENTITY_BIN) throws IOException {
		if(name_finder_ModelIn != null) {
			close_name_finder_ModelIn();
		}
		NAME_FINDER_BIN = NAMED_ENTITY_BIN;
		name_finder_ModelIn = new FileInputStream(this.getClass().getResource(NAME_FINDER_BIN).getFile());
		tokenNameFinderModel = null;
		init();
	}

	public String[] detectSentences(String document) {
		return sentenceDetectorME.sentDetect(document);
	}

	public String[] tokenize(String sentence) throws IOException{
		return tokenizerME.tokenize(sentence);
	}

	public Span[] findEntitiesInTokens(String[] tokens){
		return nameFinderME.find(tokens);
	}

	public Span[] findEntitiesInSentence(String sentence) {
		return nameFinderME.find(tokenizerME.tokenize(sentence));
	}

	public SplitDocument splitDocument(String document) {
		String[] sentences = sentenceDetectorME.sentDetect(document);
		return new SplitDocument(sentences);
	}

	public SplitDocument splitDocument(TikaFile file) {
		return splitDocument(file.getBodyContentHandler().toString());
	}

//	public Span[][] findEntitiesInDocument(String document) {
//
//		String[] sentences = detectSentences(document);
//		Span[][] nameSpans = new Span[sentences.length][];
//
//		for(int i =0; i < sentences.length; i++){
//			nameSpans[i] = findEntitiesInSentence(sentences[i]);
//		}
//		return nameSpans;
//	}
}





