package edu.uic.cs441.gabe.TikaAndOpenNLP;

import org.apache.hadoop.io.BinaryComparable;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.mortbay.jetty.HttpParser;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gabe on 10/6/16.
 */
public class TikaFile extends BinaryComparable{

	private static int UNLIMITED_BODY_LENGTH = -1;
	private AutoDetectParser parser;
	private BodyContentHandler handler;
	private Metadata metadata;
	private InputStream stream;

	public TikaFile (InputStream stream ) throws IOException, SAXException, TikaException {
		this.parser = new AutoDetectParser();
		this.handler = new BodyContentHandler(TikaFile.UNLIMITED_BODY_LENGTH);
		this.metadata = new Metadata();
		this.stream  = stream;
		parser.parse(stream, handler, metadata);
//		this.stream.close();
	}

	public TikaFile(File file) throws IOException, SAXException, TikaException{
		this.parser = new AutoDetectParser();
		this.handler = new BodyContentHandler(TikaFile.UNLIMITED_BODY_LENGTH);
		this.metadata = new Metadata();
		this.stream = new FileInputStream(file);
		parser.parse(stream, handler, metadata);
//        this.stream.close();
	}

	public TikaFile(String path) throws IOException, SAXException, TikaException{
		this.parser = new AutoDetectParser();
		this.handler = new BodyContentHandler(TikaFile.UNLIMITED_BODY_LENGTH);
		this.metadata = new Metadata();
		this.stream = new FileInputStream(new File(path));
		parser.parse(stream, handler, metadata);
//        this.stream.close();
	}

	public String getBodyAsString() {
		return handler.toString();
	}

	public BodyContentHandler getBodyContentHandler() {
		return handler;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public String getMetadataAsString() {
		return metadata.toString();
	}

	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public byte[] getBytes() {
		return new byte[0];
	}
}
