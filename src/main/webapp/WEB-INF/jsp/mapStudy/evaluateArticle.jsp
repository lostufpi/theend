<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
$(document).ready(function(){
	var percent = $('.progress-bar').attr("style");
	$('.progress-bar').attr('style', percent.replace(",", "."));

	var tableToEvaluate = $('.datatable-to-evaluate').DataTable({
        "initComplete": function () {
            var api = this.api();
            var data = api.data().toArray();
            api.clear();
            for (var d in data) {
                api.row.add(data[d]);
            }
            api.draw();
            tableToEvaluate = api;
        }
    });
	
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
	
	var tableEvaluated = $('.datatable-evaluated').DataTable();
// 	{
//     "scrollY":        "300px",
//     "scrollCollapse": true,
//     "paging":         false
// }

		
	var actualizeArticle = function(article, evaluation){
		//alterar a url para caso seja realizado F5
		var url = window.location.href;
		url = url.substr(0, url.lastIndexOf('/')) + '/' + article.id;
		window.history.pushState("", "", url);
		//window.location.reload();

		//alterar dados do artigo
		$('#articleReadId').html(article.id);
		$('#articleIdFiltro').val(article.id);
		$('#articleReadTitle').html(article.title);
		$('#articleReadAbstract').html(article.abstrct);
		$('#articleReadKeywords').html(article.keywords);
		$('#articleReadAuthor').html(article.author);
		$('#articleReadDoctype').html(article.docType);
		$('#articleReadYear').html(article.year);
		$('#articleReadSource').val(mySource(article.source));
// 		$('#articleReadSource').html(article.source);
		$('#articlesource').val($('#articleReadSource').val());		
		$('#articlescore').val(article.score);
		
		//funcoes auxiliares para evitar repeticao de codigo
		var changeFormsIds = function(isInclude, article){
			var str = isInclude ? '#formInclude' : '#formExclude';
			$(str+' [name="articleid"]').val(article.id);
// 			$(str+' [name="nextArticleId"]').val(0);
		}

		var markCriterias = function(criterias, isInclude){
			var str = isInclude ? '#formInclude .inclusions' : '#formExclude .exclusions';
			for (var i in criterias){
				$(str).each(function() {
					if ($(this).val() == criterias[i].id) {
						$(this).prop('checked', true);
					}
				});
			}
		}

		var resetCriterias = function(criterias, isInclude){
			$('#formInclude .inclusions').each(function() {
				$(this).prop('checked', false);
			});
			$('#formExclude .exclusions').each(function() {
				$(this).prop('checked', false);
			});

			$('#commentInclude').val('');
			$('#commentExclude').val('');
		}

		changeFormsIds(true, article);
		changeFormsIds(false, article);
		resetCriterias();

		var user = '${userInfo.user}';

		if (evaluation != null) {
// 			var evaluate = article.evaluations[0];
// 			console.log('evaluation: ', evaluation);
			if (evaluation.exclusionCriterias.length > 0) {
				markCriterias(evaluation.exclusionCriterias, false);
				$('#commentExclude').val(evaluation.comment);
// 				console.log('possui avaliações exclusion');
			} else if (evaluation.inclusionCriterias.length > 0) {
				markCriterias(evaluation.inclusionCriterias, true);
				$('#commentInclude').val(evaluation.comment);
// 				console.log('possui avaliações inclusion');
			}
		}
	}

	// ajax para carregar artigos na pagina
	$(document).on('click', '.readArticle', function(event){
		event.preventDefault();
		var actualid = $(this).attr("actualid");
		var url = "${linkTo[MapStudyController].loadArticle(0, 1)}";
		var mapid = $('#mapid').val();
		url = url.replace("1", actualid);
// 		url = url.replace("0", mapid);
		
// 		console.log("URL: " + mapid);

		$.ajax({ 
			url: url,
			type: 'GET',
			success: function(data){
				var article = data['article'];
				var evaluation = data['evaluation'];
 				console.log('article read: ', article);
// 				console.log('eval: ', evaluation);
				actualizeArticle(article, evaluation);
				messages('info', 'Artigo '+article.id, 'Artigo carregado com sucesso');
			},
			error: function(e){
				console.error(e);
			}
		});
	});

	// Obter criterios de inclusão e exclusão selecionados
	var selectCriterias = function(isInclude){
		var str = isInclude ? '#formInclude .inclusions' : '#formExclude .exclusions';
		var result = [];
		$(str).each(function() {
			var isChecked = $(this).prop('checked');
			if (isChecked) {
				result.push($(this).val());
			}
		});
		return result;
	}

	var actualizeList = function (articleid, isInclusion, source, score){
// 		console.log('source in list: ' + source);
		var $article = $(".tBodyArticlesToEvaluate .readArticle[nextid=\""+articleid+"\"]");
		var classification = isInclusion ? 'Aceito' : 'Rejeitado';
		var newhref = $article.attr('href');
		if (newhref == undefined){
			return;
		}
		var url = 'maps/article/';
		var pos = newhref.indexOf(url); 
		newhref = newhref.slice(0,pos) + url + articleid + '/load';
		
		///SystematicMap/maps/article/10/load 
		tableToEvaluate.row($article.parents('tr')).remove().draw();
		tableEvaluated.row.add([
       		articleid, score,
       		'<a class="readArticle" actualid="'+articleid+'" href="' + newhref + '">'+$article.html()+'</a>', source, classification]).draw();
	};

	var actualizePercent = function (percent){
		var p = percent.replace(",", ".");
		
		$('.progress-bar').attr('style', "min-width: 3em; width: "+p+"%");
		$('.progress-bar').attr('aria-valuenow', p);
		$('.progress-bar').html(percent + '%');

// 		console.log(p);
	};

	// ajax para salvar avaliações dos artigos
	var evaluate = function(event, isInclusion){
		event.preventDefault();
		var mapid = $('#mapid').val();
		var articleid = $('#articleid').val();
		var criterias = selectCriterias(isInclusion);
		var comment = isInclusion ? $('#commentInclude').val() : $('#commentExclude').val();
		var source = $('#articlesource').val();
		var score = $('#articlescore').val();
		var id = null;
		
		// assim ele vai pegar os readArticle "filhos" de tBodyArticlesToEvaluate
		$(".tBodyArticlesToEvaluate .readArticle").each(function (index) {
			var nextArticle = $(this).attr('nextid');
			if (nextArticle !== articleid){
				id = nextArticle;
				return false;
			}
		});
		
		if (criterias.length > 0) {
			var address = "${linkTo[MapStudyController].evaluateAjax}";
			var params = {
				"mapid": mapid,
				"articleid": articleid,
				"criterias": criterias,
				"comment": comment,
				"isInclusion": isInclusion,
				"nextArticleId" : id
			};
			
// 			console.log(address);
// 			console.log(params);
			

			$.ajax({
		        url: address,
		        type: 'POST',
		        data: params,
		        success: function (data) {
			        // atualiza listagens de artigos e carrega proximo artigo na tela
				        var article = data['article'];
				        var percent = data['percent'];
				        var evaluation = data['evaluation'];
						console.log('article: ', article);
// 						console.log('eval: ', evaluation);

						messages('info', 'Artigo '+articleid, 'Avalia&ccedil;&atilde;o do artigo realizada com sucesso.');
						
						if (article.id == articleid){
							messages('warning', 'Artigo', 'Todos os artigos j&aacute; foram avaliados.');
// 							alert('Sem mais artigos para avaliar!');
// 							window.location.reload();
						}//else {
							actualizeArticle(article, evaluation);
// 						}
// 						if (evaluation.length == 0){
							actualizePercent(percent);
							actualizeList(articleid, isInclusion, source, score);
// 						}
						
		        },
				error: function(e){
					console.error(e);
				}
			});
			
		} else {
			//apresentar mensagem para o usuario -> ao menos um criterio deve ser selecionado
			messages('warning', 'Crit&eacute;rios', 'Selecione pelo menos um crit&eacute;rio.');
// 			alert('Nenhum criterio foi selecionado !');
		}		
	};

	$(document).on('click', '.btnEvaluateInclusion', function(event){
		evaluate(event, true);
	});
	
	$(document).on('click', '.btnEvaluateExclusion', function(event){
		evaluate(event, false);
	});
	
	var messages = function (type, category, text){
// 		console.log(type, category, text);
		var msg = '';
	    $("#messages").empty();
	     
	    msg =	'<div class="alert alert-'+type+' alert-dismissible" role="alert" id="'+type+'">' +
			'<button type="button" class="close" data-dismiss="alert" data-hide="alert">&times;</button>' +
			'<b>' + category + '</b> - '+ text + '<br />' +
		'</div>';
		
	    $("#messages").append(msg);
	    
	    $('html, body').animate({ scrollTop: 0 }, 'slow');

	    $(".alert").click(function() {
	    	$(".alert").hide();
	    });
	};
	
});
</script>

<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li class="active"><fmt:message key="mapstudy.evaluation"/></li>
</ol>

<h3 class="color-primary">
	${map.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="button.back"/></a>
</h3>
<hr/>

<div class="row">
	<div class="col-md-3">
		<h4 class="color-primary" style="margin-top: 0px;">Andamento da seleção:</h4> 
	</div>
	<div class="col-md-9">
		<div class="progress">
			<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="${percentEvaluated}" aria-valuemin="0" aria-valuemax="100" style="min-width: 3em; width: ${percentEvaluated}%">
				${percentEvaluated}%
			</div>
		</div>
	</div>
</div>	

<div class="clear-both"></div>

<hr>
<div class="row">
	<div class="col-md-6"></div>
  	<div class="col-md-6">
  		<form id="formClassification" action="${linkTo[MapStudyController].classificationArticle}" method="post">
  			<input type="hidden" name="mapid" value="${map.id}" />
			<input type="hidden" id="articleIdFiltro" name="articleid" value="${article.id}" />
			<div class="row">
				<div class="col-md-6">
					<button type="submit" id="submitClass" class="btn btn-primary form-control">Filtrar Manualmente</button>
				</div>
				<div class="col-md-6">
					<select class="form-control" name="classification">
						<c:forEach var="classif" items="${classifications}">
							<option value="${classif}">${classif.description}</option>
						</c:forEach>
					</select>
				</div>
			</div>
  		</form>
  	</div>
</div>

<!-- <hr> -->

<div class="row">
  	<div class="col-lg-12">
		<h2><fmt:message key="mapstudy.article"/> - <span id="articleReadId">${article.id}</span></h2>
		<p><strong><fmt:message key="mapstudy.article.title"/>:</strong> <span id="articleReadTitle">${article.title}</span></p>
		<p><strong><fmt:message key="mapstudy.article.abstract"/>:</strong> <span id="articleReadAbstract">${article.abstrct}</span></p>
		<p><strong>Palavras-chave:</strong> <span id="articleReadKeywords">${article.keywords}</span></p>
		<p><strong><fmt:message key="mapstudy.article.author"/>:</strong> <span id="articleReadAuthor">${article.author}</span></p>
		<p><strong><fmt:message key="mapstudy.article.source"/>:</strong> <span id="articleReadSource">${article.sourceView(article.source)}</span></p>
		<p><strong><fmt:message key="mapstudy.article.year"/>:</strong> <span id="articleReadYear">${article.year}</span></p>
		<p><strong><fmt:message key="mapstudy.article.doctype"/>:</strong> <span id="articleReadDocType">${article.docType}</span></p>
		<hr/>
		
		<div class="row">
			<div class="col-md-6" style="min-height: ">
				<form id="formInclude" action="${linkTo[MapStudyController].includearticle}" method="post">
					<input type="hidden" id="mapid" name="mapid" value="${map.id}" />
					<input type="hidden" id="articleid" name="articleid" value="${article.id}" />
					<input type="hidden" id="articlesource" name="articlesource" value="${article.sourceView(article.source)}" />
					<input type="hidden" id="articlescore" name="articlescore" value="${article.score}" />
					
					<p><strong><fmt:message key="mapstudy.inclusion.criterias"/>:</strong></p>
					<c:forEach items="${inclusionOrdered}" var="criteria" varStatus="c">
						<c:set var="containsExc" value="false" />
						<c:forEach var="done" items="${evaluationDone.inclusionCriterias}">
						  	<c:if test="${criteria.id eq done.id}">
						    	<c:set var="containsExc" value="true" />
						  	</c:if>
						</c:forEach>
						
<!-- 						<div class="form-group"> -->
<%-- 		                	<input id="${criteria.id}" class="inclusions" type="checkbox" name="inclusions[${c.index}]" value="${criteria.id}" ${containsExc ? 'checked="checked"' : '' } />   --%>
<%-- 			                <label for="${criteria.id}">${criteria.description}</label>  --%>
<!-- 						</div> -->
						<div class="checkbox">
						    <label>
						       <input id="${criteria.id}" class="inclusions" type="checkbox" name="inclusions[${c.index}]" value="${criteria.id}" ${containsExc ? 'checked="checked"' : '' } /> ${criteria.description}
						    </label>
						</div>
			        </c:forEach>
					
					<p><strong><fmt:message key="mapstudy.article.comments"/>:</strong><br/>
					<textarea class="form-control" id="commentInclude" name="comment" rows="5" cols="">${evaluationDone.comment}</textarea></p>
					
					<button type="submit" id="submitInclusion" class="btn btn-large btn-success btnEvaluateInclusion" style="display: inline-block; width: 100%;">
						<fmt:message key="mapstudy.include"/>
					</button>
				</form>
			</div>
			<div class="col-md-6">
				<form id="formExclude" action="${linkTo[MapStudyController].excludearticle}" method="post">
					<input type="hidden" id="mapid" name="mapid" value="${map.id}" />
					<input type="hidden" id="articleid" name="articleid" value="${article.id}" />
					<input type="hidden" id="articlesource" name="articlesource" value="${artivle.sourceView(article.source)}" />
					<input type="hidden" id="articlescore" name="articlescore" value="${article.score}" />
					
					<p><strong><fmt:message key="mapstudy.exclusion.criterias"/>:</strong></p>
					<c:forEach items="${exclusionOrdered}" var="criteria" varStatus="c">
						<c:set var="containsExc" value="false" />
						<c:forEach var="done" items="${evaluationDone.exclusionCriterias}">
						  	<c:if test="${criteria.id eq done.id}">
						    	<c:set var="containsExc" value="true" />
						  	</c:if>
						</c:forEach>
<!-- 						<div class=""> -->
<%-- 			                <input id="${criteria.id}" class="exclusions" type="checkbox" name="exclusions[${c.index}]" value="${criteria.id}" ${containsExc ? 'checked="checked"' : '' } /> --%>
<%-- 							<label for="${criteria.id}">${criteria.description}</label> --%>
<%-- 			                ${criteria.description}<br/> --%>
<!-- 		                </div> -->
		                <div class="checkbox">
						    <label>
						       <input id="${criteria.id}" class="exclusions" type="checkbox" name="exclusions[${c.index}]" value="${criteria.id}" ${containsExc ? 'checked="checked"' : '' } /> ${criteria.description}
						    </label>
						</div>
		            </c:forEach>
					<p><strong><fmt:message key="mapstudy.article.comments"/>:</strong><br/>
					<textarea class="form-control" id="commentExclude" name="comment" rows="5" cols="">${evaluationDone.comment}</textarea></p>
					
					<button type="submit" id="submitExclusion" class="btn btn-large btn-danger btnEvaluateExclusion" style="display: inline-block; width: 100%;">
						<fmt:message key="mapstudy.exclude"/>
					</button>
				</form>
			</div>
		</div>
  		
	</div>
</div>

<hr/>

<div class="row">
	<div class="col-md-12">
	<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.articles.list" /></b>
			</div>
			<div class="panel-body">
			
				<h4>
					<fmt:message key="mapstudy.articles.list.toreview"/>
				</h4>
				<div class="dataTable_wrapper">
					<table class="table table-striped table-bordered table-hover datatable-to-evaluate">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.score" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.source" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesToEvaluate">
							<c:forEach var="article" items="${articlesToEvaluate}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${article.id}</td>
									<td>${article.score}</td>
									<td><a class="readArticle" actualid="${article.id}" nextid="${article.id }" href="${linkTo[MapStudyController].loadArticle(map.id, article.id)}">${article.title}</a></td>
									<td>${article.sourceView(article.source)}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
				<hr/>
				
				<h4>
					<fmt:message key="mapstudy.articles.list.evaluated"/>
				</h4>
				<div class="dataTable_wrapper">
					<table
						class="table table-striped table-bordered table-hover datatable-evaluated">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.score" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.source" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.evaluation" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesEvaluate">
							<c:forEach var="eval" items="${evaluations}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${eval.article.id}</td>
									<td>${eval.article.score}</td>
<%-- 									<td><a class="btnEvaluate" href="${linkTo[MapStudyController].evaluateArticle(map.id, eval.article.id)}">${eval.article.title}</a></td> --%>
									<td><a class="readArticle" actualid="${eval.article.id}" href="${linkTo[MapStudyController].loadArticle(map.id, eval.article.id)}">${eval.article.title}</a></td>
									<%-- <td><a class="readArticle" actualid="${article.id}" nextid="${article.id }" href="${linkTo[MapStudyController].loadArticle(map.id, article.id)}">${article.title}</a></td>--%>
									<td>${eval.article.sourceView(article.source)}</td>
									<td>${eval.classification}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
