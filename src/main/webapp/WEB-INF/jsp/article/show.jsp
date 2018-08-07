<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
$(document).ready(function(){
	var mySource = function (s){
// 		console.log("sss", s);
		if (s == "SCOPUS"){
			return "Scopus" 
		}else if (s == "ENGINEERING_VILLAGE"){
			return "Engineering Village";
		}else if (s == "WEB_OF_SCIENCE"){
			return "Web Of Science";
		}else if (s == "OTHER"){
			return "Outros";
		}else if (s =="MANUALLY"){
			return "Manual";
		}else if (s == "PUBMED"){
			return "PubMed";
		}else if (s == "APA"){
			return "American Psychological Association";
		}	
	};
});
</script>

<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
  <li><a href="<c:url value="/"/>"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li><a href="${linkTo[MapStudyController].identification(map.id)}"><fmt:message key="mapstudy.searching"/></a></li>
  <li class="active">Detalhes do artigo</li>
</ol>

<h3 class="color-primary">
	${map.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].identification(map.id)}"><fmt:message key="button.back"/></a>
</h3>
<hr/>

<!-- <div class="clear-both"></div> -->

<!-- <hr> -->
<!-- <div class="row"> -->
<!-- 	<div class="col-md-6"></div> -->
<!--   	<div class="col-md-6"> -->
<%--   		<form id="formClassification" action="${linkTo[MapStudyController].classificationArticle}" method="post"> --%>
<%--   			<input type="hidden" name="mapid" value="${map.id}" /> --%>
<%-- 			<input type="hidden" id="articleIdFiltro" name="articleid" value="${article.id}" /> --%>
<!-- 			<div class="row"> -->
<!-- 				<div class="col-md-6"> -->
<!-- 					<button type="submit" id="submitClass" class="btn btn-primary form-control">Filtrar Manualmente</button> -->
<!-- 				</div> -->
<!-- 				<div class="col-md-6"> -->
<!-- 					<select class="form-control" name="classification"> -->
<%-- 						<c:forEach var="classif" items="${classifications}"> --%>
<%-- 							<option value="${classif}">${classif.description}</option> --%>
<%-- 						</c:forEach> --%>
<!-- 					</select> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!--   		</form> -->
<!--   	</div> -->
<!-- </div> -->

<!-- <hr> -->

<div class="row">
  	<div class="col-lg-12">
		<h2><fmt:message key="mapstudy.article"/> - <span id="articleReadId">${article.id}</span></h2>
		
		<p><strong><fmt:message key="mapstudy.article.title"/>:</strong> <span id="articleReadTitle">${article.title}</span></p>
		<p class="text-justify"><strong><fmt:message key="mapstudy.article.abstract"/>:</strong> <span id="articleReadAbstract">${article.abstrct}</span></p>
		<p><strong>Palavras-chave:</strong> <span id="articleReadKeywords">${article.keywords}</span></p>
		<p><strong><fmt:message key="mapstudy.article.author"/>:</strong> <span id="articleReadAuthor">${article.author}</span></p>
		<p><strong><fmt:message key="mapstudy.article.source"/>:</strong> <span id="articleReadSource">${article.sourceView(article.source)}</span></p>
		<p><strong><fmt:message key="mapstudy.article.year"/>:</strong> <span id="articleReadYear">${article.year}</span></p>
		<p><strong><fmt:message key="mapstudy.article.doctype"/>:</strong> <span id="articleReadDocType">${article.docType}</span></p>
		
		<p><strong>Jornal:</strong> <span id="articleJournal">${article.journal != null ? article.journal : 'Não possui'}</span></p>
		<p><strong>DOI:</strong> <span id="articleDOI">${article.doi != null ? article.doi : 'Não possui'}</span></p>
		<p><strong>Note:</strong> <span id="articleNote">${article.note != null ? article.note : 'Não possui'}</span></p>
		<p><strong>URL:</strong> <span id="articleURL">${article.url != null ? article.url : 'Não possui'}</span></p>
		<p><strong>Idioma:</strong> <span id="articleLanguage">${article.language != null ? article.language : 'Não possui'}</span></p>
		
		
		<p><strong>Classificação:</strong> <span id="articleClassification">${article.classification != null ? article.classification.description : 'Não possui'}</span></p>
<%-- 		<p><strong>Artigo duplicado:</strong> <span id="articlePaperMinLevenshteinDistance">${article.paperMinLevenshteinDistance != null ? article.paperMinLevenshteinDistance.title : 'Não possui'}</span></p> --%>
		<c:if test="${article.paperMinLevenshteinDistance != null}">
			<p><strong>Disntância Levenshtein:</strong> <span id="articleMinLevenshteinDistance">${article.minLevenshteinDistance != null ? article.minLevenshteinDistance : '0'}</span></p>
			<p><strong>Artigo duplicado:</strong> <a id="articlePaperMinLevenshteinDistance" href="${linkTo[ArticleController].show(map.id, article.paperMinLevenshteinDistance.id)}">${article.paperMinLevenshteinDistance.title}</a>
		</c:if>

		<p><strong>Quantidade de termos no título:</strong> <span id="articleRegexTitle">${article.regexTitle != null ? article.regexTitle : '0'}</span></p>
		<p><strong>Quantidade de termos no abastract:</strong> <span id="articleRegexAbs">${article.regexAbs != null ? article.regexAbs : '0'}</span></p>
		<p><strong>Quantidade de termos nas palavras chave:</strong> <span id="articleRegexKey">${article.regexKeys != null ? article.regexKeys : '0'}</span></p>
		<p><strong>Score:</strong> <span id="articleScore">${article.score != null ? article.score : '0'}</span></p>
		
		<p><strong>Infos:</strong> <span id="articleInfos">${article.infos != null ? article.infos : 'Não possui'}</span></p>
		<p><strong>Avaliação Final:</strong> <span id="articleFinalEvaluation">${article.finalEvaluation != null ? article.finalEvaluation.description : 'Não possui'}</span></p>
	</div>
</div>	