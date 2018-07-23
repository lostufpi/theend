package br.com.ufpi.systematicmap.utils.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.FileWriterWithEncoding;

import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.FileDownload;
import br.com.ufpi.systematicmap.model.Article;

public class CSVBuilder {

	public static Download generateFile(List<Article> articles, String fileName) throws IOException {

		String temp = System.getProperty("java.io.tmpdir");

		File file = new File(temp + fileName);
		String encoding = "ISO-8859-1";
		FileWriterWithEncoding writer = new FileWriterWithEncoding(file, encoding, false);

		String delimiter = ";";

		// create header
		writer.append("Id" + delimiter);
		writer.append("Title" + delimiter);
		writer.append("Author" + delimiter);
		writer.append("Journal" + delimiter);
		writer.append("Doi" + delimiter);
		writer.append("DocType" + delimiter);
		writer.append("Source" + delimiter);
		writer.append('\n');

		for (Article a : articles) {
			writer.append(a.getId() + delimiter);
			String title = a.getTitle().replace('\n', ' ').replace(';', ' ');
			writer.append(title + delimiter);
			String author = a.getAuthor().replace('\n', ' ').replace(';', ' ');
			writer.append(author + delimiter);
			String journal = a.getJournal();
			journal = (journal != null ? journal.replace('\n', ' ').replace(';', ' ') : "");
			writer.append(journal + delimiter);
			writer.append((a.getDoi() != null ? a.getDoi() : "") + delimiter);
			writer.append((a.getDocType() != null ? a.getDocType() : "") + delimiter);
			writer.append(a.sourceView(a.getSource()) + delimiter);
			writer.append('\n');
		}
		writer.flush();
		writer.close();

		String contentType = "text/csv";
		return new FileDownload(file, contentType, fileName);
	}

	
}
