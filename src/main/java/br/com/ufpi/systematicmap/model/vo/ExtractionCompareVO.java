package br.com.ufpi.systematicmap.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.ufpi.systematicmap.model.Alternative;
import br.com.ufpi.systematicmap.model.Article;
import br.com.ufpi.systematicmap.model.Question;
import br.com.ufpi.systematicmap.model.User;

public class ExtractionCompareVO {
    private Article article;
    private List<ExtractionVO> extractions;

    public ExtractionCompareVO() {
    }

    public ExtractionCompareVO(Article article) {
        this.article = article;
        this.extractions = new ArrayList<ExtractionVO>();
    }

    public ExtractionCompareVO(Article article, List<ExtractionVO> extractions) {
        this(article);
        this.extractions = extractions;
    }


    /**
     * Monta o objeto para associar todas as extrações feitas para uma questão.
     * 
     * @param question questão que vai associar as extrações
     * @param alternative alternativa que o usuário selecionar em sua extração
     * @param user usuário que realizou a extração
     */
    public void addQueston(Question question, Alternative alternative, User user) {
        UserAndAlternative userAndAlternative = new UserAndAlternative(user, alternative); // Associa usuário a alternativa
        ExtractionVO evo = new ExtractionVO(question);
        boolean newExtraction = true;
        
        for (ExtractionVO ev : this.extractions) {
            if (!ev.equals(evo)) continue;
	            evo = ev;
	            newExtraction = false;
            break;
        }
        
        if (evo.getUserAndAlternatives() == null) {
            evo.setUserAndAlternatives(new ArrayList<UserAndAlternative>());
        }
        
        evo.addUserAndAlternatives(userAndAlternative, question);
        
        if (newExtraction) {
            this.extractions.add(evo);
        }
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<ExtractionVO> getExtractions() {
        return this.extractions;
    }

    public void setExtractions(List<ExtractionVO> extractions) {
        this.extractions = extractions;
    }

    public String toString() {
        return "ExtractionCompareVO [article=" + this.article + ", extractions=" + this.extractions + "]";
    }
}

