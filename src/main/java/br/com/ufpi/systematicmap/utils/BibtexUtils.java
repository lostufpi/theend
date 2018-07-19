package br.com.ufpi.systematicmap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.CharacterFilterReader;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicies;

public class BibtexUtils {

	public BibTeXDatabase parseBibTeX(File file) throws TokenMgrException, ParseException, IOException{
		InputStream is = null;
		InputStreamReader reader = null;
		CharacterFilterReader filterReader = null;
		try {
			 is = new FileInputStream(file);
			 reader = new InputStreamReader(is, StandardCharsets.UTF_8);
			filterReader = new CharacterFilterReader(reader);
			BibTeXParser parser = new BibTeXParser(BibTeXEntryKeyConflictResolutionPolicies.REKEY_SUBSEQUENT) {
				@Override
				public void checkStringResolution(Key key, BibTeXString string) {
					if (string == null) {
						System.err.println("Unresolved string: \"" + key.getValue() + "\"");
					}
				}

				@Override
				public void checkCrossReferenceResolution(Key key, BibTeXEntry entry) {
					if (entry == null) {
						System.err.println("Unresolved cross-reference: \"" + key.getValue() + "\"");
					}
				}
			};

			return parser.parse(filterReader);

		} finally {
			if(is != null)
				is.close();
			if(reader!=null)
				reader.close();
			if(filterReader!=null)
				filterReader.close();
			
		}
	}

	public void formatBibTeX(BibTeXDatabase database, File file) throws IOException {
		Writer writer = (file != null ? new FileWriter(file) : new OutputStreamWriter(System.out));

		try {
			BibTeXFormatter formatter = new BibTeXFormatter();

			formatter.format(database, writer);
		} finally {
			writer.close();
		}
	}

}