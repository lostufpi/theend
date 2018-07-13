package br.com.ufpi.systematicmap.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.ufpi.systematicmap.dao.ArticleDao;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.ClassificationEnum;
import br.com.ufpi.systematicmap.model.enums.FieldEnum;
import br.com.ufpi.systematicmap.utils.Utils;

public class FilterArticles {

	private Map<String, String> regexList = new HashMap<>();
	private Set<Article> papers = new LinkedHashSet<>();
	private boolean filterStatus = true;
	private MapStudy mapStudy;
//	private ArticleDao articleDao;

	private void generateListRegex() {
		String[] termos = mapStudy.getRefinementParameters().getRegex().split(";");
		for (String t : termos) {
			if (t.length() > 1) {
				String[] strings = t.split(":");
				regexList.put(strings[0], strings[1]);
			}
		}
	}

	public boolean filter(MapStudy mapStudy, ArticleDao articleDao) {
		this.mapStudy = mapStudy;
//		this.articleDao = articleDao;
		this.papers = mapStudy.getArticles();
		
		try {
			int sumLimiar = mapStudy.getRefinementParameters().getLimiarTitle() + mapStudy.getRefinementParameters().getLimiarAbstract() + mapStudy.getRefinementParameters().getLimiarKeywords() + mapStudy.getRefinementParameters().getLimiarTotal();

			if (mapStudy.getRefinementParameters().getFilterAuthor() || mapStudy.getRefinementParameters().getFilterAbstract() || mapStudy.getRefinementParameters().getFilterLevenshtein() || sumLimiar > 0) {
				filterAll();
			}

			if (mapStudy.getRefinementParameters().getFilterLevenshtein()) {
				calcTitleLevenshteinDistance(mapStudy.getRefinementParameters().getLevenshtein() == -1 ? 0 : mapStudy.getRefinementParameters().getLevenshtein());
			} else {
				filterTitleEquals();
			}

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private void filterAll() {
		int contaRefinados = 0;
		generateListRegex();
		int sumLimiar = mapStudy.getRefinementParameters().getLimiarTitle() + mapStudy.getRefinementParameters().getLimiarAbstract() + mapStudy.getRefinementParameters().getLimiarKeywords() + mapStudy.getRefinementParameters().getLimiarTotal();

		for (Article article : papers) {
			if (mapStudy.getRefinementParameters().getFilterAuthor() && article.getAuthor().equals("")) {
				article.setClassification(ClassificationEnum.WITHOUT_AUTHORS);
				article.setComment(article.getComment() + ClassificationEnum.WITHOUT_AUTHORS.toString());
//				article = articleDao.update(article);
				continue;
			}

			if (mapStudy.getRefinementParameters().getFilterAbstract() && article.getAbstrct().equals("")) {
				article.setClassification(ClassificationEnum.WITHOUT_ABSTRACT);
				article.setComment(article.getComment() + ClassificationEnum.WITHOUT_ABSTRACT.toString());
//				article = articleDao.update(article);
				continue;
			}

			if (sumLimiar > 0) {
				Set<String> termos = new HashSet<>();

				if (mapStudy.getRefinementParameters().getLimiarTotal() > 0) {
					countRegex(article, FieldEnum.TITLE, mapStudy.getRefinementParameters().getLimiarTitle(), termos);
					countRegex(article, FieldEnum.ABS, mapStudy.getRefinementParameters().getLimiarAbstract(), termos);
					countRegex(article, FieldEnum.KEYS, mapStudy.getRefinementParameters().getLimiarKeywords(), termos);
				} else {
					if (mapStudy.getRefinementParameters().getLimiarTitle() > 0)
						countRegex(article, FieldEnum.TITLE, mapStudy.getRefinementParameters().getLimiarTitle(), termos);
					if (mapStudy.getRefinementParameters().getLimiarAbstract() > 0)
						countRegex(article, FieldEnum.ABS, mapStudy.getRefinementParameters().getLimiarAbstract(), termos);
					if (mapStudy.getRefinementParameters().getLimiarKeywords() > 0)
						countRegex(article, FieldEnum.KEYS, mapStudy.getRefinementParameters().getLimiarKeywords(), termos);
				}

				// p.setScore(p.getRegexAbs() + p.getRegexKeys() + p.getRegexTitle())

				article.setScore(article.getRegexAbs() + article.getRegexKeys() + article.getRegexTitle());

				if (mapStudy.getRefinementParameters().getLimiarTotal() > 0) {
					if (article.getScore() < mapStudy.getRefinementParameters().getLimiarTotal()) {
						article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
//						article = articleDao.update(article);
						contaRefinados++;
					}
				} else {
					if (article.getRegexTitle() < mapStudy.getRefinementParameters().getLimiarTitle()) {
						article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
//						article = articleDao.update(article);						
					}
					if (article.getRegexAbs() < mapStudy.getRefinementParameters().getLimiarAbstract()) {
						article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
//						article = articleDao.update(article);						
					}
					if (article.getRegexKeys() < mapStudy.getRefinementParameters().getLimiarKeywords()) {
						article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
//						article = articleDao.update(article);						
					}
				}

				filterStatus = false;
			}
		}
		
		System.out.println("TOTAL REFINADOS: " + contaRefinados);
	}

	public void filterTitleEquals() {
		for (Article p : papers) {
			if (p.getClassification() == null) {
				for (Article p2 : papers) {
					if (p.getId() != p2.getId() && p2.getClassification() == null) {

						if (p.getTitle().equalsIgnoreCase(p2.getTitle())) {

							p2.setClassification(ClassificationEnum.REPEAT);
							String comment = p.getComment() != null ? p.getComment() : "";
							p2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
							p2.setMinLevenshteinDistance(0);
							p2.setPaperMinLevenshteinDistance(p);
							//
							p.setMinLevenshteinDistance(0);
							p.setPaperMinLevenshteinDistance(p2);
						}

					}
				}
			}

		}
	}

	private int filterAuthors() {
		int count = 0;
		for (Article p : papers) {
			if (p.getAuthor().equals("")) {
				p.setClassification(ClassificationEnum.WITHOUT_AUTHORS);
				p.setComment(p.getComment() + ClassificationEnum.WITHOUT_AUTHORS.toString());
				// p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser()) +
				// ClassificationEnum.WITHOUT_AUTHORS.toString());
				count++;
			}
		}
		return count;
	}

	private int filterPatents() {
		int count = 0;
		for (Article p : papers) {
			if (p.getAbstrct().equals("")) {
				p.setClassification(ClassificationEnum.WITHOUT_ABSTRACT);
				p.setComment(p.getComment() + ClassificationEnum.WITHOUT_ABSTRACT.toString());
				// p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser()) +
				// ClassificationEnum.WITHOUT_ABSTRACT.toString());
				count++;
			}
		}
		return count;
	}

	private void filterRegex(int limiarTitle, int limiarAbs, int limiarKeys, int limiarTotal) {
		for (Article p : papers) {
			Set<String> termos = new HashSet<>();

			termos = countRegexAntigo(p, FieldEnum.TITLE, limiarTitle, termos);
			termos = countRegexAntigo(p, FieldEnum.ABS, limiarAbs, termos);
			termos = countRegexAntigo(p, FieldEnum.KEYS, limiarKeys, termos);

			// p.setScore(p.getRegexAbs() + p.getRegexKeys() + p.getRegexTitle())

			if ((limiarAbs + limiarKeys + limiarTitle + limiarTotal) > 0) {
				p.setScore(termos.size());
			}

			if (termos.size() < limiarTotal) {
				p.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
			}
		}
	}

	private Set<String> countRegexAntigo(Article p, FieldEnum fieldEnum, int limiar, Set<String> termos) {
		String s = "";

		if (fieldEnum.equals(FieldEnum.ABS)) {
			s = p.getAbstrct();
		} else if (fieldEnum.equals(FieldEnum.TITLE)) {
			s = p.getTitle();
		} else if (fieldEnum.equals(FieldEnum.KEYS)) {
			s = p.getKeywords();
		}

		Pattern pattern;
		Matcher regexMatcher;
		String comment = "";
		int count = 0;

		if (s != null && !s.equals("")) {
			Set<Entry<String, String>> set = regexList.entrySet();
			for (Entry<String, String> entry : set) {
				String regex = entry.getValue();
				pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				regexMatcher = pattern.matcher(s);

				boolean containRegex = false;
				while (regexMatcher.find()) {
					containRegex = true;
					// System.out.println(regexMatcher.group(1));
				}

				if (containRegex) {
					termos.add(entry.getKey());
					count++;
				} else {
					comment += entry.getKey() + ", ";
				}
			}
		}

		if (fieldEnum.equals(FieldEnum.ABS)) {
			p.setRegexAbs(count);
		} else if (fieldEnum.equals(FieldEnum.TITLE)) {
			p.setRegexTitle(count);
		} else if (fieldEnum.equals(FieldEnum.KEYS)) {
			p.setRegexKeys(count);
		}

		if (count < limiar) {
			p.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
//			p.setComments(p.getComments() + " " + ClassificationEnum.WORDS_DONT_MATCH.toString() + "-"
//					+ fieldEnum.toString() + " DONT contains=(" + comment + ");");
		}

		return termos;
	}

	private void calcTitleLevenshteinDistance(int limiar) {
		for (Article p : papers) {
			if (p.getClassification() == null) {
				for (Article p2 : papers) {
					if (p.getId() != p2.getId() && p2.getClassification() == null) {

						String titleArticleOne = p.getTitle().toLowerCase().replaceAll(" ", "");
						String titleArticleTwo = p2.getTitle().toLowerCase().replaceAll(" ", "");
						int dist = Utils.getLevenshteinDistance(titleArticleOne, titleArticleTwo);

						if (dist <= limiar) {

							p2.setClassification(ClassificationEnum.REPEAT);
							String comment = p.getComment() != null ? p.getComment() : "";
							p2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
							p2.setMinLevenshteinDistance(dist);
							p2.setPaperMinLevenshteinDistance(p);

							p.setMinLevenshteinDistance(dist);
							p.setPaperMinLevenshteinDistance(p2);
						}
					}
				}
			}
		}
	}

	private void countRegex(Article article, FieldEnum fieldEnum, int limiar, Set<String> termos) {
		String s = "";

		if (fieldEnum.equals(FieldEnum.ABS)) {
			s = article.getAbstrct();
		} else if (fieldEnum.equals(FieldEnum.TITLE)) {
			s = article.getTitle();
		} else if (fieldEnum.equals(FieldEnum.KEYS)) {
			s = article.getKeywords();
		}

		Pattern pattern;
		Matcher regexMatcher;
		 String comment = "";
		int count = 0;

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
					// termos.add(entry.getKey());
					comment += entry.getKey() + ", ";
					count++;
				} else {
//					 comment += entry.getKey() + ", ";
				}
			}
		}
		
		article.setComment(article.getComment() + comment);

		if (fieldEnum.equals(FieldEnum.ABS)) {
			article.setRegexAbs(count);
		} else if (fieldEnum.equals(FieldEnum.TITLE)) {
			article.setRegexTitle(count);
		} else if (fieldEnum.equals(FieldEnum.KEYS)) {
			article.setRegexKeys(count);
		}

		// if (count < limiar) {
		// article.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
		// article.setComment(article.getComment() + " " +
		// ClassificationEnum.WORDS_DONT_MATCH.toString() + "-"
		// + fieldEnum.toString() + " DONT contains=(" + comment + ");");
		// p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser())+"
		// "+ClassificationEnum.WORDS_DONT_MATCH.toString()+"-"+fieldEnum.toString()+"
		// DONT contains=("+comment+");");
		// }

		// return termos;
	}

	private int countPapers(ClassificationEnum ce) {
		int count = 0;
		for (Article paper : papers) {
			if (paper.getClassification() != null && paper.getClassification().equals(ce)) {
				count++;
			}
		}
		return count;
	}

	public Set<Article> getPappers() {
		return papers;
	}

	public boolean isFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(boolean filterStatus) {
		this.filterStatus = filterStatus;
	}
}
