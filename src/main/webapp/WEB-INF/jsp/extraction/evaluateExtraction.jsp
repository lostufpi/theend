<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(document).ready(function(){
	var percent = $('.progress-bar').attr("style");
	$('.progress-bar').attr('style', percent.replace(",", "."));

// 	var resetAlterative = function (){
// 		console.log('entrou reset alternative');
// 		$('.group_question').each(function(idx, elem){
// 			var $elem = $(elem);
// 			$elem.find('.group_alternative').each(function(idx_a, elem_a) {
// 				console.log('reset: ' + idx_a);
// 				$(elem_a).find('.group_alternative_id').val('');
// 				var obj = $('#alternative_id_' + idx + 'option:selected');
// 				console.log(obj);
// 				obj.remove();
// 				var test = $('#select2-alternative_id_' + idx + '-container');
// 				test.attr('title', 'Meu titulo');
// 				test.html('meu html');
// 				console.log(obj);
// 				$(elem_a).find('.group_alternative_value').val('');
// 			});

// 		});
// 	};

	var selectAlternative = function(){
		console.log('entrou no select alternative');
// 		var entrou = false;
		$('.alternative_list_id').each(function(idx){
			console.log('ele ', $(this));
			var qid = $(this).attr('id');
			var i = qid.lastIndexOf('_');
			qid = qid.slice(i + 1, qid.length);			
			var aid = $(this).val();
			var value = $('#alternative_list_value_' + qid).val();
			
			console.log('qid ', qid);
			console.log('aid ', aid);
			console.log('value ', value);

// 			var obj = $('#alternative_id_' + qid + 'option:selected');
// 			console.log('removendo select: ' , obj);
// 			obj.remove();
			
			$("#alternative_id_" + qid).val(aid);	
			var test = $('#select2-alternative_id_' + qid + '-container');
			test.attr('title', value);
			test.html(value);
// 			entrou = true;
		});

// 		if (entrou == false){
// 			resetAlterative();
// 		}
	};

	console.log('OP SLECTION: ' + $('#alternative_id_0 option:selected').text() + ' | ' + $('#alternative_id_0 option:selected').val());
	selectAlternative();
	console.log('OP SLECTION: ' + $('#alternative_id_0 option:selected').text() + ' | ' + $('#alternative_id_0 option:selected').val());

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
	var tableEvaluated = $('.datatable-evaluated').DataTable();
// 	{
//     "scrollY":        "300px",
//     "scrollCollapse": true,
//     "paging":         false
// }

		
	var actualizeArticle = function(article, extraction){
		console.log('entra atualiza article', article);
		//alterar a url para caso seja realizado F5
		var url = window.location.href;
		url = url.substr(0, url.lastIndexOf('/')) + '/' + article.id;

		console.log(url);
		window.history.pushState("", "", url);
		//window.location.reload();

		//alterar dados do artigo
		$('#articleReadId').html(article.id);
		$('#articleReadTitle').html(article.title);
		$('#articleReadAbstract').html(article.abstrct);
		$('#articleReadKeywords').html(article.keywords);
		$('#articleReadSource').html(article.source);
		$('#articleReadAuthor').html(article.author);


		//funcoes auxiliares para evitar repeticao de codigo
		var changeFormsIds = function(formId, article){
// 			console.log('FormId: ', formId);
// 			$(formId+' [name="articleid"]').val(article.id);
			$('#articleid').val(article.id);
// 			$(str+' [name="nextArticleId"]').val(0);
		}

		var setQuestions = function (extraction){
			if (extraction.length > 0){
// 			$('.group_question').each(function(idx, elem){
// 				var $elem = $(elem);
// // 				$elem.find('.group_question_id').val(extraction[idx].question.id);
// // 				$elem.find('.group_question_name').val(extraction[idx].question.name);

// 				// Aqui � um select pesquisar como setar valor
// // 				$elem.find('.group_question_type').val(questions[idx].type);
				
				
// 					$elem.find('.group_alternative').each(function(idx_a, elem_a) {
// 						if (extraction[idx].question.type == 'LIST'){
// 						 $select = $(elem_a).find('.group_alternative_id');
// // 						 console.log(select);
// 						 console.log($select);
// // 						 var id = $select.attr('id');
// // 						 console.log('id: ', id);
// // 						 $("#"+ id + " option:selected").remove();
// // 						 $("#" + id).append('<option data-email="" value="'+extraction[idx].alternative.id+'">'+extraction[idx].alternative.value +'</option>');
// 						}else{
// 							$(elem_a).find('.group_alternative_value').val(extraction[idx].alternative.value);
// 						}
// 					});
				

// 			});
				$.each(extraction, function( index, elemento ) {
					console.log('set');
					  console.log(elemento.question.id + " | " + elemento.question.type + " | " + elemento.question.name);

					  if (elemento.question.type == 'LIST'){
// 						  var alternative_selected = elemento.alternative;
// 						  '<option selected="selected" value="15">2</option>'
						  $.each(elemento.question.alternatives, function( index2, elemento2 ) {
							  if (elemento2.value == elemento.alternative.value){
								  console.log('alternativa selecionada: ' + elemento.alternative.value);
// 								  $("#alternative_id_" + index).attr('selectedIndex', index2);
								$("#alternative_id_" + index).val(elemento.alternative.id);	
								var test = $('#select2-alternative_id_' + index + '-container');
								test.attr('title', elemento.alternative.value);
								test.html(elemento.alternative.value);
// 								$('select#alternative_id_' + index +' option').eq(index).css('backgroundColor', 'blue');
							  }
							  console.log(elemento2.id + " | " + elemento2.value);
						  });
					  }else{
// 						  $(elem_a).find('.group_alternative_value').val(elemento.alternative.value);
							$('#alternative_value_' + index).val(elemento.alternative.value);
					  }
// 					  console.log('alternativa selecionada: ' + elemento.alternative.value);
// 					  console.log('alternativas da quest�o: ');
				});
			}
		}

		var resetQuestions = function (){
			console.log('entrou reset');
			$('.group_question').each(function(idx, elem){
				var $elem = $(elem);
// 				$elem.find('.group_question_id').val('');
// 				$elem.find('.group_question_name').val('');

				// Aqui � um select pesquisar como setar valor
// 				$elem.find('.group_question_type').val('');
				
				$elem.find('.group_alternative').each(function(idx_a, elem_a) {
					console.log('reset: ' + idx_a);
					$(elem_a).find('.group_alternative_id').val('');
					var obj = $('#alternative_id_' + idx + ' option:selected');
					console.log(obj);
					obj.remove();
					var test = $('#select2-alternative_id_' + idx + '-container');
					test.attr('title', '');
					test.html('');
					console.log(obj);
// 					$(elem_a).find('.group_alternative_id').text('');
					$(elem_a).find('.group_alternative_value').val('');
				});

			});
		}

		changeFormsIds('#forExtraction', article);
		resetQuestions();
		

		var user = '${userInfo.user}';
		console.log('user: ', user)
		
		if (extraction != null) {
			console.log('extraction: ', extraction);
			// Pegar dados da extra��o caso exista e adicionar a tela
			// Fazer uma forma de pegar os dados preenchidos na extra��o e passar para o formul�rio carregado na pagina
// 			if(extraction.length > 0){
				setQuestions(extraction);	
// 			}		
		}
	}

	// ajax para carregar artigos na pagina
	$(document).on('click', '.readArticle', function(event){
		event.preventDefault();
		var actualid = $(this).attr("actualid");
		var url = "${linkTo[ExtractionController].loadArticleAjax(0, 1)}";
		url = url.replace("1", actualid);

		$.ajax({ 
			url: url,
			type: 'GET',
			success: function(data){
				var article = data['article'];
				var extraction = data['extraction'];
				console.log('article read: ', article);
				console.log('extraction: ', extraction);
				actualizeArticle(article, extraction);
			},
			error: function(e){
				console.error(e);
			}
		});
	});

	// Obter os dados extraidos
	var obtainQuestions = function(){
		
		var questions = [];
		$('.group_question').each(function(idx, elem){
			var $elem = $(elem);
			var question = {};
			var id  = $elem.find('.group_question_id').val();
			question.id = (id == undefined ? null : id);
			var name = $elem.find('.group_question_name').val()
			question.name = (name == undefined ? null : name);
// 			question.type = $elem.find('.group_question_type').val();
			question.alternatives = [];
			
			console.log(question);
			
			$elem.find('.group_alternative').each(function(idx_a, elem_a) {
				var alternative = {};
				var id_a  = $(elem_a).find('.group_alternative_id').val();
				alternative.id = (id_a == undefined ? null : id_a);
				var value = $(elem_a).find('.group_alternative_value').val();
				alternative.value = (value == undefined ? null : value);
				
				if ((alternative.value != null && alternative.value != "") || (alternative.id != null && alternative.id != "")){
					question.alternatives.push(alternative);
					console.log(alternative);
				}					
			});

			if (question.id != null && question.id != ""){
				questions.push(question);
			}			

			console.log('tam: ' + questions.length);	
		});

		return questions;
	}

	var actualizeList = function (articleid, source){
		console.log('atualiza list entrou');
		var $article = $(".tBodyArticlesToEvaluate .readArticle[nextid=\""+articleid+"\"]");
		var newhref = $article.attr('href');
		if (newhref == undefined){
			console.log('' + newhref);
			return;
		}
		var url = 'extraction/article/';
		var pos = newhref.indexOf(url);
		newhref = newhref.slice(0,pos) + url + articleid + '/load';

		console.log('newhref', newhref);
		
		///SystematicMap/extraction/article/{articleid}/load
		tableToEvaluate.row($article.parents('tr')).remove().draw();
		tableEvaluated.row.add([
       		articleid, 
       		'<a class="readArticle" actualid="'+articleid+'" href="' + newhref + '">'+$article.html()+'</a>', source]).draw();
			console.log('atualiza list success');
		};

	var actualizePercent = function (percent){
		var p = percent.replace(",", ".");
		
		$('.progress-bar').attr('style', "min-width: 3em; width: "+p+"%");
		$('.progress-bar').attr('aria-valuenow', p);
		$('.progress-bar').html(percent + '%');

		console.log(p);
	};

	// ajax para salvar avalia��es dos artigos
	var evaluate = function(event){
		event.preventDefault();
		var mapid = $('#mapid').val();
		var articleid = $('#articleid').val();
		var questions = obtainQuestions();
		var source = $('#articlesource').val();
		var id = null;
		
		// assim ele vai pegar os readArticle "filhos" de tBodyArticlesToEvaluate
		$(".tBodyArticlesToEvaluate .readArticle").each(function (index) {
			var nextArticle = $(this).attr('nextid');
			if (nextArticle !== articleid){
				id = nextArticle;
				return false;
			}
		});

			var address = "${linkTo[ExtractionController].evaluateAjax}";
// 			var params = {
// 				"mapid": mapid,
// 				"articleid": articleid,
// 				"questions": questions,
// 				"nextArticleId" : id
// 			};

			
	var questionVO = {"mapid" : mapid,
					  "articleid" : articleid,
					  "questions" : questions,
					  "nextArticle" : id
					};

							param = {
								"questionVO" : questionVO
							};

							console.log('JSON: ', JSON.stringify(param));
							console.log('JQ'
									+ jQuery.parseJSON(JSON.stringify(param)));

							$.ajax({ url : address,
									 dataType : 'json',
									 contentType : 'application/json; charset=utf-8',
										type : 'POST',
										traditional : true,
										data : JSON.stringify(param),
										success : function(data) {
											// atualiza listagens de artigos e carrega proximo artigo na tela
											var article = data['article'];
											var percent = data['percent'];
											var extraction = data['extraction'];
											console.log('article post: ', article);
											console.log('extraction post: ', extraction);
											if (article == null || article.id == -1) {
												var $notice = $('#notices');
												var b = $notice.find('b').html('teste');
// 												alert('Sem mais artigos para avaliar!');
												//window.location.reload();
											} else {
												console.log('vou chamar o actualizar');
												actualizeArticle(article, extraction);
											}
											actualizePercent(percent);
											if(extraction == null || extraction.length == 0){
												actualizeList(articleid, source);
											}
										},
// 										done: function(body){
// 											console.log('done ' + body);
// 										},
										error : function(e) {
											console.error(e);
										}
									});

						};

						$(document).on('click', '.buttonextraction',
								function(event) {
									evaluate(event);
								});
					});
</script>

<ol class="breadcrumb u-margin-top">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li class="active"><fmt:message key="mapstudy.extraction"/></li>
</ol>

<h3 class="color-primary">
	<fmt:message key="mapstudy.evaluation"/> - ${map.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="button.back"/></a>
</h3>

<div class="progress">
	<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="${percentExtracted}" aria-valuemin="0" aria-valuemax="100" style="min-width: 3em; width: ${percentExtracted}%">
		${percentExtracted}%
	</div>
</div>
<div class="clear-both"></div>

<div class="row">
<!--   	<div class="col-lg-12"> -->
  	
  		<div class="" id="infoarticle">
  		
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
		
		<p> 
			<strong>
				<fmt:message key="mapstudy.article.source"/>:
			</strong> <span id="articleReadSource">${article.source}</span>
		<p>
		
		<hr/>
		
		</div>
		<div class="panel panel-default">
		<div class="panel-heading">
			<b><fmt:message key="mapstudy.form" /></b>
		</div>
		<!-- /.panel-heading -->
		<div class="panel-body">
			<h4>
				<fmt:message key="mapstudy.form.evaluate" />
			</h4>
		<form action="${linkTo[ExtractionController].evaluateAjax}" method="post" id="#forExtraction">
				<input type="hidden" name="mapid"  id="mapid" value="${map.id}" />
				<input type="hidden" name="articleid" id="articleid" value="${article.id}" />
				<input type="hidden" id="articlesource" name="articlesource" value="${article.source}" />

<!-- 				<div class="form-group"> -->
						<c:forEach var="question" items="${form.questions}" varStatus="q">
						<div class="form-group group_question">
						<input type="hidden" name="questions[${q.index}].id" value="${question.id}" class="group_question_id" id="question_id_${q.index}"/>
<%-- 						<input type="hidden" name="questions[${q.index}].name" value="${question.name}" /> --%>
						
						<div class="padding-left-none">
							<strong class="group_question_name" id="question_name_${q.index}">${question.name} :</strong>
						</div>
						<div class="float-right group_alternative">
							<c:if test="${question.type == 'LIST'}">
								<select	data-placeholder="<fmt:message key="mapstudy.form.choose" />" class="form-control select2 group_alternative_id" name="alternatives[${q.index}].id" id="alternative_id_${q.index}" tabindex="2">								
									<c:forEach var="alt" items="${question.alternatives}">
										<option value="${alt.id}" data-email="">${alt.value}</option>
									</c:forEach>
								</select>
								
								<c:if test="${article.alternative(question, userInfo.user) != null}">
									<input type="hidden" value="${article.alternative(question, userInfo.user).id}" class="alternative_list_id" id="alternative_list_id_${q.index}"/>
									<input type="hidden" value="${article.alternative(question, userInfo.user).value}" class="alternative_list_value" id="alternative_list_value_${q.index}"/>
<%-- 									<option data-email="" selected="selected" value="${article.alternative(question, userInfo.user).id}">${article.alternative(question, userInfo.user).value}</option> --%>
								</c:if>
							</c:if>
							<c:if test="${question.type == 'SIMPLE'}">
								<input type="text" class="form-control group_alternative_value" name="alternatives[${q.index}].value" id="alternative_value_${q.index}" value="${article.alternative(question, userInfo.user).value}"/>
							</c:if>							
						</div>		
<!-- 						<p>				 -->
						</div>
						</c:forEach>					
						<p>
					<button type="submit" id="submit" class="btn btn-large btn-primary buttonextraction">
						<fmt:message key="salve" />
					</button>
					<div class="clear-both"></div>
				</form>
				</div>
				</div>
		
<!-- 		</div>  		 -->
	</div>

<!-- <hr/> -->

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
						class="table table-striped table-bordered table-hover datatable-to-evaluate">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.source" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesToEvaluate">
							<c:forEach var="article" items="${articlesToExtraction}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${article.id}</td>
									<td><a class="readArticle" actualid="${article.id}" nextid="${article.id }" href="${linkTo[ExtractionController].loadArticleAjax(map.id, article.id)}">${article.title}</a></td>
									<td>${article.source}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
				<hr/>
				
				<h4>
					<fmt:message key="mapstudy.articles.list.extracted"/>
				</h4>
				<div class="dataTable_wrapper">
					<table
						class="table table-striped table-bordered table-hover datatable-evaluated">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.source" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesEvaluate">
							<c:forEach var="eval" items="${extractions}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${eval.id}</td>
<%-- 									<td><a class="btnEvaluate" href="${linkTo[MapStudyController].evaluateArticle(map.id, eval.article.id)}">${eval.article.title}</a></td> --%>
									<td><a class="readArticle" actualid="${eval.id}" href="${linkTo[ExtractionController].loadArticleAjax(map.id, eval.id)}">${eval.title}</a></td>
									<td>${eval.source}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
</div>
