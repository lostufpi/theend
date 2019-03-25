package br.com.ufpi.systematicmap.utils.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.output.FileWriterWithEncoding;

import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.User;

public class CSVBuilder {

	public static File generateFile(List<Article> articles, String fileName, User user) throws IOException {

		String temp = System.getProperty("java.io.tmpdir");

		File file = new File(temp + fileName);
		String encoding = "UTF-8";//"ISO-8859-1";
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
		writer.append("Classification" + delimiter);
		writer.append("Evaluation" + delimiter);
		writer.append('\n');

		for (Article a : articles) {
			lineGeneratorCsv(writer, delimiter, a, user);
		}
		writer.flush();
		writer.close();
		return file;
	}

	private static void lineGeneratorCsv(FileWriterWithEncoding writer, String delimiter, Article a, User user)
			throws IOException {
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
		
		writer.append((a.getClassification() != null ? a.getClassification().getDescription() : "") + delimiter);
		
		if(user != null){
			String evaluationClassification = a.getEvaluationClassification(user);
			writer.append(evaluationClassification != null ? evaluationClassification : ""+ delimiter);
		}else{
			writer.append(a.getFinalEvaluation() != null ? a.getFinalEvaluation().getDescription() : "" + delimiter);
		}
		
		writer.append('\n');
	}

	
}