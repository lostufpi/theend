package br.com.ufpi.systematicmap.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

public class BibtexUtils {

	public BibTeXDatabase parseBibTeX(File file) throws IOException, TokenMgrException, ParseException {
		Reader reader = null;
		try {
			reader = new FileReader(file);
			// InputStream is = new FileInputStream(file);
			// InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
			BibTeXParser parser = new BibTeXParser() {
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

			return parser.parse(reader);

		} finally {
			reader.close();
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