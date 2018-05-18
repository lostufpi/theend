package br.com.ufpi.systematicmap.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.enums.ClassificationEnum;
import br.com.ufpi.systematicmap.model.enums.FieldEnum;
import br.com.ufpi.systematicmap.utils.Utils;



public class FilterArticles {
	
	private Map<String, String> regexList = new HashMap<String, String>();
	private Set<Article> papers = new LinkedHashSet<Article>();
	private Integer levenshtein;
	private String regex; 
	private Integer limiartitulo;
	private Integer limiarabstract; 
	private Integer limiarkeywords; 
	private Integer limiartotal;
	private boolean filterAuthor;
	private boolean filterAbstract;
	private boolean filterLevenshtein;
	private boolean filterStatus = true;
	
	@Inject
	private UserInfo userInfo;
	
	/*
	 * {{
	    put("automatico", "(automat.*|semiautomati.*|semi-automati.*)");
	    put("web", "(web|website|internet|www)");
	    put("usabilidade", "(usability|usable)");
	    put("tecnica", "(evalu.*|assess.*|measur.*|experiment.*|stud.*|test.*|method.*|techni.*|approach.*)");
	}}
	 */
	
	public FilterArticles(Set<Article> set, Integer levenshtein, String regex, Integer limiartitulo, Integer limiarabstract, Integer limiarkeywords, Integer limiartotal, boolean filterAuthor, boolean filterAbstract, boolean filterLevenshtein){
		super();
		this.papers = set;
		this.levenshtein = levenshtein;
		this.regex = regex;
		this.limiartitulo = limiartitulo;
		this.limiarabstract = limiarabstract;
		this.limiarkeywords = limiarkeywords;
		this.limiartotal = limiartotal;
		this.filterAbstract = filterAbstract;
		this.filterAuthor = filterAuthor;
		this.filterLevenshtein = filterLevenshtein;
	}
	
	private void generateListRegex(){
		String[] termos = regex.split(";");
		for(String t : termos){
			if (t.length() > 1){
				String[] strings = t.split(":");
				regexList.put(strings[0], strings[1]);
			}				
		}
	}
	
	private void filterAll(){
		for(Article p : papers){
			if (filterAuthor){
				if(p.getAuthor().equals("")){
					p.setClassification(ClassificationEnum.WITHOUT_AUTHORS);
					p.setComment(p.getComment() + ClassificationEnum.WITHOUT_AUTHORS.toString());
//				p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser()) + ClassificationEnum.WITHOUT_AUTHORS.toString());
					continue;
				}
			}
			
			if (filterAbstract){
				if(p.getAbstrct().equals("")){
					p.setClassification(ClassificationEnum.WITHOUT_ABSTRACT);
					p.setComment(p.getComment() + ClassificationEnum.WITHOUT_ABSTRACT.toString());
//					p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser()) + ClassificationEnum.WITHOUT_ABSTRACT.toString());
					continue;
				}
			}
			
			int sumLimiar = limiartitulo + limiarabstract + limiarkeywords + limiartotal;
			
			if (sumLimiar > 0){
				generateListRegex();
				
				Set<String> termos = new HashSet<String>();
				
				termos = countRegex(p, FieldEnum.TITLE, limiartitulo, termos);
//				System.out.println("Termos[T]: " + termos.size());
				termos = countRegex(p, FieldEnum.ABS, limiarabstract, termos);
//				System.out.println("Termos[A]: " + termos.size());
				termos = countRegex(p, FieldEnum.KEYS, limiarkeywords, termos);
//				System.out.println("Termos[K]: " + termos.size());
				
				
//				System.out.println("Termos: " +termos );
//				System.out.println("SIze: " +termos.size() );
//				
//				System.out.println("ABS: " + p.getRegexAbs());
//				System.out.println("KEY: " + p.getRegexKeys());
//				System.out.println("TIT: " + p.getRegexTitle());
				
//				p.setScore(p.getRegexAbs() + p.getRegexKeys() + p.getRegexTitle())
				
				if (sumLimiar > 0){
					p.setScore(termos.size());				
				}
				
				if(termos.size() < limiartotal){
					p.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
				}
				filterStatus = false;
			}			
		}		
	}
	
	public boolean isFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(boolean filterStatus) {
		this.filterStatus = filterStatus;
	}

	public void filterTitleEquals(){
//		double count = 0, size = papers.size();
		for(Article p : papers){
			if(p.getClassification() == null){
				for(Article p2 : papers){
					if(p.getId() != p2.getId() && p2.getClassification() == null){
						
						if(p.getTitle().equalsIgnoreCase(p2.getTitle())){
							
//							System.out.println("p1:" + p + " p2: " +p2);
							
							p2.setClassification(ClassificationEnum.REPEAT);
							String comment = p.getComment() != null ? p.getComment() : "";
							p2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
//							String comment = p.getComment(userInfo.getUser()) != null ? p.getComment(userInfo.getUser()) : "";
//							p2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
//							p2.addComment(userInfo.getUser(), comment + " " + p.getComment(userInfo.getUser()) + ClassificationEnum.REPEAT.toString());
							p2.setMinLevenshteinDistance(0);
							p2.setPaperMinLevenshteinDistance(p);
							//
							p.setMinLevenshteinDistance(0);
							p.setPaperMinLevenshteinDistance(p2);
						}
						
					}
				}
			}
//			count++;
//			System.out.println("loading:"+(count/size)*100);
		}
	}
	
	public boolean filter(){
		try{
			
			filterAll();
			
			if(filterLevenshtein){
				calcTitleLevenshteinDistance(levenshtein == -1 ? 0 : levenshtein);
			}else{
				filterTitleEquals();
			}
			return true;
		}catch(Exception e){
//			e.printStackTrace();
			return false;
		}
	} 
	
	private int filterAuthors() {
		int count = 0;
		for(Article p : papers){
			if(p.getAuthor().equals("")){
				p.setClassification(ClassificationEnum.WITHOUT_AUTHORS);
				p.setComment(p.getComment() + ClassificationEnum.WITHOUT_AUTHORS.toString());
//				p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser()) + ClassificationEnum.WITHOUT_AUTHORS.toString());
				count++;
			}
		}
		return count;
	}
	
	private int filterPatents() {
		int count = 0;
		for(Article p : papers){
			if(p.getAbstrct().equals("")){
				p.setClassification(ClassificationEnum.WITHOUT_ABSTRACT);
				p.setComment(p.getComment() + ClassificationEnum.WITHOUT_ABSTRACT.toString());
//				p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser()) + ClassificationEnum.WITHOUT_ABSTRACT.toString());
				count++;
			}
		}
		return count;
	}
	
	private void filterRegex(int limiarTitle, int limiarAbs, int limiarKeys, int limiarTotal) {
		for(Article p : papers){
			Set<String> termos = new HashSet<String>();
			
			termos = countRegex(p, FieldEnum.TITLE, limiarTitle, termos);
//			System.out.println("Termos[T]: " + termos.size());
			termos = countRegex(p, FieldEnum.ABS, limiarAbs, termos);
//			System.out.println("Termos[A]: " + termos.size());
			termos = countRegex(p, FieldEnum.KEYS, limiarKeys, termos);
//			System.out.println("Termos[K]: " + termos.size());
			
			
//			System.out.println("Termos: " +termos );
//			System.out.println("SIze: " +termos.size() );
//			
//			System.out.println("ABS: " + p.getRegexAbs());
//			System.out.println("KEY: " + p.getRegexKeys());
//			System.out.println("TIT: " + p.getRegexTitle());
			
//			p.setScore(p.getRegexAbs() + p.getRegexKeys() + p.getRegexTitle())
			
			if ((limiarAbs + limiarKeys + limiarTitle + limiarTotal) > 0){
				p.setScore(termos.size());				
			}
			
			if(termos.size() < limiarTotal){
				p.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
			}
		}
	}
	
	private void calcTitleLevenshteinDistance(int limiar) {
//		double count = 0, size = papers.size();
		for(Article p : papers){
			if(p.getClassification() == null){
				for(Article p2 : papers){
					if(p.getId() != p2.getId() && p2.getClassification() == null){
						
						int dist = Utils.getLevenshteinDistance(p.getTitle().toLowerCase(), p2.getTitle().toLowerCase());
						
						if(dist <= limiar){
							
//							System.out.println("p1:" + p + " p2: " +p2);
							
							p2.setClassification(ClassificationEnum.REPEAT);
							String comment = p.getComment() != null ? p.getComment() : "";
							p2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
//							String comment = p.getComment(userInfo.getUser()) != null ? p.getComment(userInfo.getUser()) : "";
//							p2.setComment(comment + " " + ClassificationEnum.REPEAT.toString());
//							p2.addComment(userInfo.getUser(), comment + " " + p.getComment(userInfo.getUser()) + ClassificationEnum.REPEAT.toString());
							p2.setMinLevenshteinDistance(dist);
							p2.setPaperMinLevenshteinDistance(p);
							//
							p.setMinLevenshteinDistance(dist);
							p.setPaperMinLevenshteinDistance(p2);
						}
						
					}
				}
			}
//			count++;
//			System.out.println("loading:"+(count/size)*100);
		}
	}
	
	private Set<String> countRegex(Article p, FieldEnum fieldEnum, int limiar, Set<String> termos) {
		String s = "";
		
		if (fieldEnum.equals(FieldEnum.ABS)) {
			s = p.getAbstrct();
		}else if (fieldEnum.equals(FieldEnum.TITLE)) {
			s = p.getTitle();
		}else if (fieldEnum.equals(FieldEnum.KEYS)) {
			s = p.getKeywords();
		}
		
		Pattern pattern;
		Matcher regexMatcher;
		String comment = "";
		int count = 0;
		
		if(s != null && !s.equals("")){
			Set<Entry<String, String>> set = regexList.entrySet();
			for(Entry<String, String> entry : set){
				String regex = entry.getValue();
				pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			    regexMatcher = pattern.matcher(s);
			    
			    boolean containRegex = false;
			    while (regexMatcher.find()) {
			    	containRegex = true;
			    	//System.out.println(regexMatcher.group(1));
			    }
			    
			    if(containRegex){
			    	termos.add(entry.getKey());
			    	count++;
			    }else{
			    	comment += entry.getKey()+", ";
			    }
			}
		}
		
		if (fieldEnum.equals(FieldEnum.ABS)) {
			p.setRegexAbs(count);
		}else if (fieldEnum.equals(FieldEnum.TITLE)) {
			p.setRegexTitle(count);
		}else if (fieldEnum.equals(FieldEnum.KEYS)) {
			p.setRegexKeys(count);
		}
		
		if(count < limiar){
			p.setClassification(ClassificationEnum.WORDS_DONT_MATCH);
			p.setComment(p.getComment()+" "+ClassificationEnum.WORDS_DONT_MATCH.toString()+"-"+fieldEnum.toString()+" DONT contains=("+comment+");");
//			p.addComment(userInfo.getUser(), p.getComment(userInfo.getUser())+" "+ClassificationEnum.WORDS_DONT_MATCH.toString()+"-"+fieldEnum.toString()+" DONT contains=("+comment+");");
		}
		
		return termos;
	}
	
	private int countPapers(ClassificationEnum ce){
		int count = 0;
		for (Article paper : papers){
			if (paper.getClassification() != null && paper.getClassification().equals(ce)) {
				count++;
			}
		}
		return count;
	}
	
	public Set<Article> getPappers() {
		return papers;
	}
}
