package br.com.ufpi.systematicmap.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.enums.ClassificationEnum;
import br.com.ufpi.systematicmap.model.enums.FieldEnum;
import br.com.ufpi.systematicmap.utils.Utils;

public class FilterArticles {

	private Map<String, String> regexList = new HashMap<>();
	private List<Article> papers;
	private MapStudy mapStudy;
	private ArticleDao articleDao;

	public FilterArticles(MapStudy mapStudy, List<Article> articles) {
		super();
		this.mapStudy = mapStudy;
		this.papers = articles;
	}

	private void generateListRegex() {
		String[] termos = mapStudy.getRefinementParameters().getRegex().split(";");
		for (String t : termos) {
			if (t.length() > 1) {
				String[] strings = t.split(":");
				regexList.put(strings[0], strings[1]);
			}
		}
	}

	public boolean filter() {
		try {
			if (mapStudy.getRefinementParameters().getFilterLevenshtein()) {
				calcTitleLevenshteinDistance(mapStudy.getRefinementParameters().getLevenshtein() == -1 ? 0
						: mapStudy.getRefinementParameters().getLevenshtein());
			} else {
				filterTitleEquals();
			}
			
			int sumLimiar = mapStudy.getRefinementParameters().getLimiarTitle()
					+ mapStudy.getRefinementParameters().getLimiarAbstract()
					+ mapStudy.getRefinementParameters().getLimiarKeywords()
					+ mapStudy.getRefinementParameters().getLimiarTotal();

			if (mapStudy.getRefinementParameters().getFilterAuthor()
					|| mapStudy.getRefinementParameters().getFilterAbstract()
					|| mapStudy.getRefinementParameters().getFilterLevenshtein() || sumLimiar > 0) {
				filterAll();
			}


			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private void filterAll() {
		generateListRegex();
		int sumLimiar = mapStudy.getRefinementParameters().getLimiarTitle()
				+ mapStudy.getRefinementParameters().getLimiarAbstract()
				+ mapStudy.getRefinementParameters().getLimiarKeywords()
				+ mapStudy.getRefinementParameters().getLimiarTotal();

		for (Article a : papers) {
			if(a.getClassification() == null) {
				Article article = articleDao.find(a.getId());
				if (mapStudy.getRefinementParameters().getFilterAuthor() && article.getAuthor().equals("")) {
					article.setClassification(ClassificationEnum.WITHOUT_AUTHORS);
					article.setComment(article.getComment() + ClassificationEnum.WITHOUT_AUTHORS.toString());
					articleDao.update(article);
					continue;
				}
				
				if (mapStudy.getRefinementParameters().getFilterAbstract() && article.getAbstrct().equals("")) {
					article.setClassification(ClassificationEnum.WITHOUT_ABSTRACT);
					article.setComment(article.getComment() + ClassificationEnum.WITHOUT_ABSTRACT.toString());
					articleDao.update(article);
					continue;
				}
				
				if (sumLimiar > 0) {
					applyRegexFilter(article);
					article.setScore(article.getRegexAbs() + article.getRegexKeys() + article.getRegexTitle());
					classificationArticle(article);
				}
			}
		}
	}

	private void applyRegexFilter(Article article) {
		if (mapStudy.getRefinementParameters().getLimiarTotal() > 0) {
			countRegex(article, FieldEnum.TITLE);
			countRegex(article, FieldEnum.ABS);
			countRegex(article, FieldEnum.KEYS);
		} else {
			if (mapStudy.getRefinementParameters().getLimiarTitle() > 0)
				countRegex(article, FieldEnum.TITLE);
			if (mapStudy.getRefinementParameters().getLimiarAbstract() > 0)
				countRegex(article, FieldEnum.ABS);
			if (mapStudy.getRefinementParameters().getLimiarKeywords() > 0)
				countRegex(article, FieldEnum.KEYS);
		}
	}

	private void classificationArticle(Article article) {
		if (mapStudy.getRefinementParameters().getLimiarTotal() > 0) {
			if (article.getScore() < mapStudy.getRefinementParameters().getLimiarTotal()) {
				article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
				articleDao.update(article);
			}
		} else {
			if (article.getRegexTitle() < mapStudy.getRefinementParameters().getLimiarTitle()) {
				article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
				articleDao.update(article);
			}
			if (article.getRegexAbs() < mapStudy.getRefinementParameters().getLimiarAbstract()) {
				article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
				articleDao.update(article);
			}
			if (article.getRegexKeys() < mapStudy.getRefinementParameters().getLimiarKeywords()) {
				article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
				articleDao.update(article);
			}
		}
	}

	public void filterTitleEquals() {
		for (Article p : papers) {
			Article article1 = articleDao.find(p.getId());
			if (article1.getClassification() == null) {
				for (Article p2 : papers) {
					Article article2 = articleDao.find(p2.getId());
					if (article1.getId() != article2.getId() && article2.getClassification() == null && article1
							.getTitle().replaceAll(" ", "").equalsIgnoreCase(article2.getTitle().replaceAll(" ", ""))) {
						article2.setClassification(ClassificationEnum.REPEAT);
						String comment = article1.getComment() != null ? article1.getComment() : "";
						article2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
						article2.setMinLevenshteinDistance(0);
						article2.setPaperMinLevenshteinDistance(article1);
						
						article1.setMinLevenshteinDistance(0);
						article1.setPaperMinLevenshteinDistance(article2);

						articleDao.update(article1);
						articleDao.update(article2);
					}
				}
			}

		}
	}

	private void calcTitleLevenshteinDistance(int limiar) {
		for (Article p : papers) {
			Article article = articleDao.find(p.getId());
			if (article.getClassification() == null) {
				for (Article p2 : papers) {
					Article article2 = articleDao.find(p2.getId());
					if (article.getId() != article2.getId() && article2.getClassification() == null) {

						String titleArticleOne = article.getTitle().toLowerCase().replaceAll(" ", "");
						String titleArticleTwo = article2.getTitle().toLowerCase().replaceAll(" ", "");
						int dist = Utils.getLevenshteinDistance(titleArticleOne, titleArticleTwo);

						if (dist <= limiar) {

							article2.setClassification(ClassificationEnum.REPEAT);
							String comment = article.getComment() != null ? article.getComment() : "";
							article2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
							article2.setMinLevenshteinDistance(dist);
							article2.setPaperMinLevenshteinDistance(article);

							article.setMinLevenshteinDistance(dist);
							article.setPaperMinLevenshteinDistance(article2);

							articleDao.update(article);
							articleDao.update(article2);
						}
					}
				}
			}
		}
	}

	private void countRegex(Article article, FieldEnum fieldEnum) {
		String s = fieldFilter(article, fieldEnum);

		Pattern pattern;
		Matcher regexMatcher;
		String comment = "";
		int count = 0;
		
		String foundTerms = "";
		String notFoundTerms = "";

		if (s != null && !s.equals("")) {
			Set<Entry<String, String>> set = regexList.entrySet();
			for (Entry<String, String> entry : set) {
				String regex = entry.getValue();
				pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				regexMatcher = pattern.matcher(s);

				boolean containRegex = false;
				
				while (regexMatcher.find()) {
					containRegex = true;
				}

				if (containRegex) {
					foundTerms = foundTerms.concat(entry.getKey().concat(", "));
					count++;
				} else {
					notFoundTerms = notFoundTerms.concat(entry.getKey().concat(", "));
				}
			}
		}
		
		if(!foundTerms.isEmpty()) {
			comment = "TERMS FOUND = ".concat(foundTerms).concat("-");
		}
		
		if(!foundTerms.isEmpty()) {
			comment = comment.concat("TERMS NOT FOUND = ".concat(notFoundTerms));
		}

		article.setComment(article.getComment() + comment);

		if (fieldEnum.equals(FieldEnum.ABS)) {
			article.setRegexAbs(count);
		} else if (fieldEnum.equals(FieldEnum.TITLE)) {
			article.setRegexTitle(count);
		} else if (fieldEnum.equals(FieldEnum.KEYS)) {
			article.setRegexKeys(count);
		}
	}

	private String fieldFilter(Article article, FieldEnum fieldEnum) {
		if (fieldEnum.equals(FieldEnum.ABS)) {
			return article.getAbstrct();
		} else if (fieldEnum.equals(FieldEnum.TITLE)) {
			return article.getTitle();
		} else if (fieldEnum.equals(FieldEnum.KEYS)) {
			return article.getKeywords();
		}
		
		return "";
	}

	public List<Article> getPappers() {
		return papers;
	}

	/**
	 * @return the articleDao
	 */
	public ArticleDao getArticleDao() {
		return articleDao;
	}

	/**
	 * @param articleDao
	 *            the articleDao to set
	 */
	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}
}
