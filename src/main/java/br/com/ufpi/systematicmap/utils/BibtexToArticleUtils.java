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
		
		String author = getAttr(fields, BibTeXEntry.KEY_AUTHOR);
		
		author = remove(author);
		
		if (author.length() > 2000){
			author = author.substring(0, 1994) + "(...)";
		}
		
		article.setAuthor(author);
		
		String title = getAttr(fields, BibTeXEntry.KEY_TITLE);
		title = remove(title);
		
		if (title.length() > 2000){
			title = title.substring(0, 1994) + "(...)";
		}
		
		article.setTitle(title);
		
		String journal = getAttr(fields, BibTeXEntry.KEY_JOURNAL);
		journal = remove(journal);
		
		article.setJournal(journal);
		
		String volume = getAttr(fields, BibTeXEntry.KEY_VOLUME);
		volume = remove(volume);
		article.setVolume(volume);
		                        
		String pages = getAttr(fields, BibTeXEntry.KEY_PAGES);
		pages= remove(pages);
		article.setPages(pages);
		
		String doi = getAttr(fields, BibTeXEntry.KEY_DOI);
		doi = remove(doi);
		article.setDoi(doi);
		
		String year = getAttr(fields, BibTeXEntry.KEY_YEAR);
		year = remove(year);
		article.setYear(Integer.parseInt(year));
		
		String abstrct =  getAttr(fields, new Key("abstract"));
		abstrct = remove(abstrct);
		article.setAbstrct(abstrct);
		
		String keywords = getAttr(fields, new Key("keywords"));
		keywords = remove(keywords);

		if (keywords.length() > 2000){
			keywords = keywords.substring(0, 1994) + "(...)";
		}
		
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
			
			String author_keywords = getAttr(fields, new Key("author_keywords"));
			author_keywords = remove(author_keywords);
			article.setKeywords(author_keywords);
			
			String affiliation = getAttr(fields, new Key("affiliation"));
			affiliation = remove(affiliation);
			article.setNote(article.getNote() + affiliation);
			
		}else if(sourceEnum.equals(ArticleSourceEnum.WEB_OF_SCIENCE)){
			String docType = getAttr(fields, new Key("type"));
			docType = remove(docType);
			article.setDocType(docType);
			
		}else if(sourceEnum.equals(ArticleSourceEnum.SCIELO)){
			String doi2 = getAttr(fields, new Key("crossref"));
			doi2 = remove(doi2);
			article.setDoi(doi2);
			
			String docType = getAttr(fields, new Key("type"));
			docType = remove(docType);
			article.setDocType(docType);
		}else if(sourceEnum.equals(ArticleSourceEnum.ENGINEERING_VILLAGE)){
			String key2 = getAttr(fields, new Key("key"));
			key2 = remove(key2);
			article.setKeywords(article.getKeywords() + key2);
		}
		
		return article;
	}
	
	static public BibTeXDatabase articleToBibTeX(Article article) throws IOException {
		
		BibTeXDatabase bib = new BibTeXDatabase();
		
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_AUTHOR, new Key(article.getAuthor())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_TITLE, new Key(article.getTitle())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_JOURNAL, new Key(article.getJournal())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_VOLUME, new Key(article.getVolume())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_PAGES, new Key(article.getPages())));
		bib.addObject(new BibTeXEntry(BibTeXEntry.KEY_DOI, new Key(article.getDoi())));
		
		bib.addObject(new BibTeXEntry(new Key("abstract"), new Key(article.getAbstrct())));
		bib.addObject(new BibTeXEntry(new Key("keywords"), new Key(article.getKeywords())));
		bib.addObject(new BibTeXEntry(new Key("language"), new Key(article.getLanguage())));
		
//		BibTeXInclude teste = new BibTeXInclude(new StringValue("teste", Style.QUOTED), bib);
		
		return bib;
	}
	
	static private Integer getAttrInt(Map<Key, Value> fields, Key key){
		if(key == null) {
			return -1;
		}
		try{
			return Integer.parseInt(fields.get(key).toUserString());
		}catch(Exception e){
//			logger.error("Attr: " + key +" -> "+ e.getMessage());
			return -1;
		}
	}
	
	static private String getAttr(Map<Key, Value> fields, Key key){
		if(key == null) {
			return "";
		}
		try{
			return fields.get(key).toUserString();
		}catch(Exception e){
//			logger.error("Attr: " + key +" -> "+ e.getMessage());
			return "";
		}
	}
}
