<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(document).ready(function(){
	var actualizeArticle = function(article){
		//alterar a url para caso seja realizado F5
		var url = window.location.href;
		url = url.substr(0, url.lastIndexOf('/')) + '/' + article.id;
		window.history.pushState("", "", url);
		//window.location.reload();

		//alterar dados do artigo
		$('#articleReadId').html(article.id);
		$('#articleReadTitle').html(article.title);
		$('#articleReadAbstract').html(article.abstrct);
		$('#articleReadKeywords').html(article.keywords);
		$('#articleReadAuthor').html(article.author);


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
		
		//caso haja avalia��o, altera-la
		if (article.evaluations.length > 0) {
			var evaluate = article.evaluations[0];
			if (evaluate.exclusionCriterias.length > 0) {
				markCriterias(evaluate.exclusionCriterias, false);
				$('#commentExclude').val(evaluate.comment);
				console.log('possui avalia��es exclusion');
			} else if (evaluate.inclusionCriterias.length > 0) {
				markCriterias(evaluate.inclusionCriterias, true);
				$('#commentInclude').val(evaluate.comment);
				console.log('possui avalia��es inclusion');
			}
		}
	}

	// ajax para carregar artigos na pagina
	$(document).on('click', '.readArticle', function(event){
		event.preventDefault();
		var actualid = $(this).attr("actualid");
		var url = "${linkTo[MapStudyController].loadArticle(0, 1)}";
		url = url.replace("1", actualid);

		$.ajax({ 
			url: url,
			type: 'GET',
			success: function(data){
				var article = data.article;
// 				console.log('article read: ', article);
				actualizeArticle(article);
			},
			error: function(e){
				console.error(e);
			}
		});
	});

	// Obter criterios de inclus�o e exclus�o selecionados
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

	var actualizeList = function (articleid, isInclusion){
		var $article = $(".tBodyArticlesToEvaluate .readArticle[nextid=\""+articleid+"\"]");
		var classEvenOdd = $('.tBodyArticlesEvaluate tr').length % 2 === 0 ? 'even' : 'odd';
		var classification = isInclusion ? 'ACCEPTED' : 'REJECTED';
		var htmlEvaluated = '<tr class="'+classEvenOdd+' gradeA"><td>'+articleid+'</td><td><a class="readArticle" actualid="'+articleid+'" href="#">'+$article.html()+'</a></td><td>'+classification+'</td></tr>';
		$article.closest('tr').remove();
		$('.tBodyArticlesEvaluate').append(htmlEvaluated);
// 		console.log(htmlEvaluated);
	};

	var actualizePercent = function (percent){
		var per = $('#per').html();
		percent = '(' + percent + '%)';
		$('#per').html(percent);
// 		console.log('percent: ', percent,'per: ', per);
	};

	// ajax para salvar avalia��es dos artigos
	var evaluate = function(event, isInclusion){
		event.preventDefault();
		var mapid = $('#mapid').val();
		var articleid = $('#articleid').val();
		var criterias = selectCriterias(isInclusion);
		var comment = isInclusion ? $('#commentInclude').val() : $('#commentExclude').val();
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

			$.ajax({
		        url: address,
		        type: 'POST',
		        data: params,
		        success: function (data) {
			        // atualiza listagens de artigos e carrega proximo artigo na tela
				        var article = data['article'];
				        var percent = data['percent'];
// 						console.log('article: ', article);
						if (article.id == -1){
							alert('Sem mais artigos para avaiar!');
// 							window.location.reload();
						}else {
							actualizeArticle(article);
						}
						actualizePercent(percent);
						actualizeList(articleid, isInclusion);
		        },
				error: function(e){
					console.error(e);
				}
			});
			
		} else {
			//apresentar mensagem para o usuario -> ao menos um criterio deve serselecionado
			alert('Nenhum criterio foi selecionado !');
		}		
	};

	$(document).on('click', '.btnEvaluateInclusion', function(event){
		evaluate(event, true);
	});
	
	$(document).on('click', '.btnEvaluateExclusion', function(event){
		evaluate(event, false);
	});
});
</script>


<h3 class="color-primary"><fmt:message key="mapstudy.evaluation"/> - ${map.title} </h3><h3 class="color-primary" id="per">(${percentEvaluated}%)</h3>
<div class="row">
  	<div class="col-lg-12">
  		
		<h2><fmt:message key="mapstudy.article"/> - <span id="articleReadId">${article.id}</span></h2>
		
		<p> 
			<strong>
				<fmt:message key="mapstudy.article.title"/>:
			</strong> <span id="articleReadTitle">${article.title}</span>
		<p>
		
		<p> 
			<strong>
				<fmt:message key="mapstudy.article.abstract"/>:
			</strong> <span id="articleReadAbstract">${article.abstrct}</span>
		<p>
		
		<p> 
			<strong>
				<fmt:message key="mapstudy.article.key"/>:
			</strong> <span id="articleReadKeywords">${article.keywords}</span>
		<p>
		
		<p> 
			<strong>
				<fmt:message key="mapstudy.article.author"/>:
			</strong> <span id="articleReadAuthor">${article.author}</span>
		<p>
		
		<hr/>
		
		<form id="formInclude" action="${linkTo[MapStudyController].includearticle}" method="post">
			<input type="hidden" id="mapid" name="mapid" value="${map.id}" />
			<input type="hidden" id="articleid" name="articleid" value="${article.id}" />
			<p> 
				<strong>
					<fmt:message key="mapstudy.inclusion.criterias"/>:
				</strong><br/>
				<c:forEach items="${inclusionOrdered}" var="criteria" varStatus="c">
				
					<c:set var="containsExc" value="false" />
					<c:forEach var="done" items="${evaluationDone.inclusionCriterias}">
					  <c:if test="${criteria.id eq done.id}">
					    <c:set var="containsExc" value="true" />
					  </c:if>
					</c:forEach>
					
	                <input class="inclusions" type="checkbox" name="inclusions[${c.index}]" value="${criteria.id}" ${containsExc ? 'checked="checked"' : '' } />  
	                ${criteria.description}<br/>  
	            </c:forEach>
			<p>
			
			<p> 
				<strong>
					<fmt:message key="mapstudy.article.comments"/>:
				</strong><br/>
				<textarea class="form-control" id="commentInclude" name="comment" rows="3" cols="">${evaluationDone.comment}</textarea>
			<p>
			
			<button type="submit" id="submitInclusion" class="btn btn-large btn-success btnEvaluateInclusion" style="display: inline-block;">
				<fmt:message key="mapstudy.include"/>
			</button>
		</form>
		
		<hr/>
			
		<form id="formExclude" action="${linkTo[MapStudyController].excludearticle}" method="post">
			<input type="hidden" id="mapid" name="mapid" value="${map.id}" />
			<input type="hidden" id="articleid" name="articleid" value="${article.id}" />
			<p> 
				<strong>
					<fmt:message key="mapstudy.exclusion.criterias"/>:
				</strong><br/>
				
				<c:forEach items="${exclusionOrdered}" var="criteria" varStatus="c">
				
					<c:set var="containsExc" value="false" />
					<c:forEach var="done" items="${evaluationDone.exclusionCriterias}">
					  <c:if test="${criteria.id eq done.id}">
					    <c:set var="containsExc" value="true" />
					  </c:if>
					</c:forEach>
				
	                <input class="exclusions" type="checkbox" name="exclusions[${c.index}]" value="${criteria.id}" ${containsExc ? 'checked="checked"' : '' } />
	                ${criteria.description}<br/>
	            </c:forEach>
			<p> 
			
			<p> 
				<strong>
					<fmt:message key="mapstudy.article.comments"/>:
				</strong><br/>
				<textarea class="form-control" id="commentExclude" name="comment" rows="3" cols="">${evaluationDone.comment}</textarea>
			<p>
			
			<button type="submit" id="submitExclusion" class="btn btn-large btn-danger btnEvaluateExclusion" style="display: inline-block;">
				<fmt:message key="mapstudy.exclude"/>
			</button>
		</form>
  		
	</div>
</div>

<hr/>

<div class="row">
	<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.articles.list" /></b>
			</div>
			<div class="panel-body">
			
				<h4>
					<fmt:message key="mapstudy.articles.list.toreview"/>
				</h4>
				<div class="dataTable_wrapper">
					<table
						class="table table-striped table-bordered table-hover personalized-table">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesToEvaluate">
							<c:forEach var="article" items="${articlesToEvaluate}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${article.id}</td>
									<td><a class="readArticle" actualid="${article.id}" nextid="${article.id }" href="${linkTo[MapStudyController].loadArticle(map.id, article.id)}">${article.title}</a></td>
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
						class="table table-striped table-bordered table-hover personalized-table">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.evaluation" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesEvaluate">
							<c:forEach var="eval" items="${evaluations}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${eval.article.id}</td>
<%-- 									<td><a class="btnEvaluate" href="${linkTo[MapStudyController].evaluateArticle(map.id, eval.article.id)}">${eval.article.title}</a></td> --%>
									<td><a class="readArticle" actualid="${eval.article.id}" href="${linkTo[MapStudyController].loadArticle(map.id, eval.article.id)}">${eval.article.title}</a></td>
									<td>${eval.classification}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
</div>
