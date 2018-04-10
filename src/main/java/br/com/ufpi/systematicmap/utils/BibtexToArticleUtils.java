package br.com.ufpi.systematicmap.utils;

import java.io.IOException;
import java.util.Map;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.enums.ArticleSourceEnum;

public class BibtexToArticleUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(BibtexToArticleUtils.class);
	
	/**
	 * <p>Existem sistuações que um campo possui mais de uma { e }.</p>
	 * <p>Exemplo:</p>
	 * <p>  title = {{Título do artigo.}}</p>
	 * <p>Esse método verifica a existência de {, caso existe à remove da string retornada.</p>
	 * @param field string com o valor do campo do bibtex
	 * @return string sem { e } caso exista.
	 */
	static public String remove(String field){
		if (field.length() > 2 && field.charAt(0) == '{'){
			field = field.substring(1, (field.length()-1));
		}
		return field;
	}
	
	static public Article bibtexToArticle(BibTeXEntry entry, ArticleSourceEnum sourceEnum){
		Article article = new Article();
		Map<Key, Value> fields = entry.getFields();
		
		article.setSource(sourceEnum.toString());
//		article.setNumber(number);
		
		String author = getAttr(fields, BibTeXEntry.KEY_AUTHOR);
		
		author = remove(author);
		
		if (author.length() > 2000){
			author = author.substring(0, 1994) + "(...)";
		}
		
//		System.out.println("Author: " + author);
		
		article.setAuthor(author);
		
		String title = getAttr(fields, BibTeXEntry.KEY_TITLE);
		title = remove(title);
		
		if (title.length() > 2000){
			title = title.substring(0, 1994) + "(...)";
		}
		
//		System.out.println("Title: " + title);
		article.setTitle(title);
		
		String journal = getAttr(fields, BibTeXEntry.KEY_JOURNAL);
		journal = remove(journal);
		
		article.setJournal(journal);
		
//		System.out.println("Journal: " + journal);
		
		String volume = getAttrInt(fields, BibTeXEntry.KEY_VOLUME).toString();
		volume = remove(volume);
		article.setVolume(Integer.parseInt(volume));
		
//		System.out.println("Volume: " + volume);
		
		String pages = getAttr(fields, BibTeXEntry.KEY_PAGES);
		pages= remove(pages);
		article.setPages(pages);
		
//		System.out.println("Pages: " + pages);
		
		String doi = getAttr(fields, BibTeXEntry.KEY_DOI);
		doi = remove(doi);
		article.setDoi(doi);
		
//		System.out.println("Doi: " + doi);
		
		String year = getAttr(fields, BibTeXEntry.KEY_YEAR);
		year = remove(year);
		article.setYear(Integer.parseInt(year));
		
//		System.out.println("Year: " + year);
		
		String abstrct =  getAttr(fields, new Key("abstract"));
		abstrct = remove(abstrct);
		article.setAbstrct(abstrct);
		
//		System.out.println("Abstract: " + abstrct);
		
		String keywords = getAttr(fields, new Key("keywords"));
		keywords = remove(keywords);
		//System.out.println("Key: " + keywords.length());
		if (keywords.length() > 2000){
			keywords = keywords.substring(0, 1994) + "(...)";
			//System.out.println(keywords);
		}
		
//		System.out.println("Key: " + keywords);
		article.setKeywords(keywords);
		
		String language = getAttr(fields, new Key("language"));
		language = remove(language);
		article.setLanguage(language);
		
		String url = getAttr(fields,BibTeXEntry.KEY_URL);
		url = remove(url);
		article.setLanguage(url);
		
		if(sourceEnum.equals(ArticleSourceEnum.SCOPUS)){
			String docType = getAttr(fields, new Key("document_type"));
			docType = remove(docType);
			article.setDocType(docType);
			
//			System.out.println("DocType: " + docType);
			
			String note = getAttr(fields, new Key("authos_keywords"));
			note = remove(note);
			article.setNote("author keywords: " + note);
			
//			System.out.println("Note: " + note);
			
		}else if(sourceEnum.equals(ArticleSourceEnum.WEB_OF_SCIENCE)){
			String docType = getAttr(fields, new Key("type"));
			docType = remove(docType);
			article.setDocType(docType);
			
//			System.out.println("DocType: " + docType);
		}else if(sourceEnum.equals(ArticleSourceEnum.SCIELO)){
			String doi2 = getAttr(fields, new Key("crossref"));
			doi2 = remove(doi2);
			article.setDoi(doi2);
			
			String docType = getAttr(fields, new Key("type"));
			docType = remove(docType);
			article.setDocType(docType);
		}
		
		return article;
	}
	
	static public BibTeXDatabase articleToBibTeX(Article article) throws IOException {
		
		BibTeXDatabase bib = new BibTeXDatabase();
		
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_AUTHOR, new Key(article.getAuthor())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_TITLE, new Key(article.getTitle())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_JOURNAL, new Key(article.getJournal())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_VOLUME, new Key(article.getVolume().toString())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_PAGES, new Key(article.getPages())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_DOI, new Key(article.getDoi())));
		
		bib.addObject(new BibTeXEntry(new Key("abstract"), new Key(article.getAbstrct())));
		bib.addObject(new BibTeXEntry(new Key("keywords"), new Key(article.getKeywords())));
		bib.addObject(new BibTeXEntry(new Key("language"), new Key(article.getLanguage())));
		
//		BibTeXInclude teste = new BibTeXInclude(new StringValue("teste", Style.QUOTED), bib);
		
		return bib;
	}
	
	static private Integer getAttrInt(Map<Key, Value> fields, Key key){
		try{
			return Integer.parseInt(fields.get(key).toUserString());
		}catch(Exception e){
			logger.error(e.getMessage());
			return -1;
		}
	}
	
	static private String getAttr(Map<Key, Value> fields, Key key){
		try{
			return fields.get(key).toUserString();
		}catch(Exception e){
			logger.error(e.getMessage());
			return "";
		}
	}
}
