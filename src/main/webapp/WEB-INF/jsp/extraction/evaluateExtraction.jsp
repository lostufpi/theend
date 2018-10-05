<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(document).ready(function(){
	// atualiza percentual já extraído
	var percent = $('.progress-bar').attr("style");
	$('.progress-bar').attr('style', percent.replace(",", "."));

	var selectAlternative = function(){
		$('.alternative_list_id').each(function(idx){
// 			console.log('ele ', $(this));
			var qid = $(this).attr('id');
			var i = qid.lastIndexOf('_');
			qid = qid.slice(i + 1, qid.length);			
			var aid = $(this).val();
			var value = $('#alternative_list_value_' + qid).val();
			
			$("#alternative_id_" + qid).val(aid);	
			var test = $('#select2-alternative_id_' + qid + '-container');
			test.attr('title', value);
			test.html(value);
		});

	};

	selectAlternative();

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

			
	var actualizeArticle = function(article, extraction, comment){
// 		console.log('entra atualiza article', article.source);
		//alterar a url para caso seja realizado F5
		var url = window.location.href;
		url = url.substr(0, url.lastIndexOf('/')) + '/' + article.id;

// 		console.log(url);
		window.history.pushState("", "", url);
		//window.location.reload();
		//TODO vai mudar quando poder cadastrar bases
		var mySource = function (s){
// 			console.log("sss: ", s);
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

		//alterar dados do artigo
		$('#articleReadId').html(article.id);
		$('#articleReadTitle').html(article.title);
		$('#articleReadAbstract').html(article.abstrct);
		$('#articleReadKeywords').html(article.keywords);
		$('#articleReadAuthor').html(article.author);
		$('#articleReadDoctype').html(article.docType);
		$('#articleReadYear').html(article.year);
		$('#articlesource').val(mySource(article.source));
		$('#articleReadSource').html($('#articlesource').val());
		$('#articlescore').val(article.score);
		$('#comment').val(comment);

// 		console.log('Comentários: ' + $('#comment'). val());
		
// 		console.log('source encontrado', $('#articlesource'));


		//funcoes auxiliares para evitar repeticao de codigo
		var changeFormsIds = function(formId, article){
// 			console.log('FormId: ', formId);
// 			$(formId+' [name="articleid"]').val(article.id);
			$('#articleid').val(article.id);
// 			$(str+' [name="nextArticleId"]').val(0);
		}

		// questõ que foi extraida, alternativa selecionada, index é a posiçõa da questão no formulário inici EM 0
		var updateQuestions = function(question, alternative, index){
			if (question.type == 'LIST'){
				$("#alternative_id_" + index).val(alternative.id);	
				var test = $('#select2-alternative_id_' + index + '-container');
				test.attr('title', alternative.value);
				test.html(alternative.value);
// 			    console.log('LIST', alternative.id + " | " + alternative.value);
			  }else if (question.type == 'MULT'){
			  	  // setar check box
				  $('.questionMult').each(function() {
					if ($(this).val() == alternative.id) {
						$(this).prop('checked', true);
					}
// 						console.log('MULT',$(this).val());
				  });
			  }else{
				$('#alternative_value_' + index).val(alternative.value);
// 				console.log('SIMPLES',alternative.id + " | " + alternative.value);
			  }
// 			  console.log('alternativa selecionada: ' + alternative.value);
// 			  console.log('-------');
		};

		var setQuestions = function (extraction){
			if (extraction.length > 0){
				$('.group_question').each(function(idx, elem){
					var $elem = $(elem);
					var id = $elem.find('.group_question_id').val();
					var question_id = (id == undefined ? null : id);

// 					console.log('index : ' + idx + ' question_id:' + question_id);
					
					$.each(extraction, function( index, elemento ) {
// 						console.log('QUestion: ' + elemento.question.id);
						if (elemento.question.id == question_id){
// 							console.log('alternative: ' + elemento.alternative);
							updateQuestions(elemento.question, elemento.alternative, idx);
							if (elemento.question.type != 'MULT')
								return false;
						}
	  				});
				});
			}
		}

		var resetQuestions = function (){
// 			console.log('entrou reset');
			$('.group_question').each(function(idx, elem){
				var $elem = $(elem);
				$elem.find('.group_alternative').each(function(idx_a, elem_a) {
// 					console.log('reset: ' + idx_a);
					$(elem_a).find('.group_alternative_id').val('');
					var obj = $('#alternative_id_' + idx + ' option:selected');
// 					console.log(obj);
					obj.remove();
					var test = $('#select2-alternative_id_' + idx + '-container');
					test.attr('title', '');
					test.html('');
// 					console.log(obj);
// 					$(elem_a).find('.group_alternative_id').text('');
					$(elem_a).find('.group_alternative_value').val('');
				});

			});

			$('.questionMult').each(function() {
					$(this).prop('checked', false);
			 });
		}

		changeFormsIds('#forExtraction', article);
		resetQuestions();
		

		var user = '${userInfo.user}';
// 		console.log('user: ', user)
		
		if (extraction != null) {
// 			console.log('extraction: ', extraction);
			// Pegar dados da extração caso exista e adicionar a tela
			// Fazer uma forma de pegar os dados preenchidos na extração e passar para o formulário carregado na pagina
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
		var mapid = $('#mapid').val();
		url = url.replace("1", actualid);
// 		url = url.replace("0", mapid);
		
// 		console.log("URL: " + mapid);

		$.ajax({ 
			url: url,
			type: 'GET',
			success: function(data){
				var article = data['article'];
				var extraction = data['extraction'];
				var comment = data['comment'];
// 				console.log('article read: ', article);
// 				console.log('extraction: ', extraction);
				actualizeArticle(article, extraction, comment);
				messages('info', 'Article '+article.id, 'Carregado com sucesso');
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
			
// 			console.log('Question: ', question);
			
			$elem.find('.group_alternative').each(function(idx_a, elem_a) {
// 				console.log("S/L");
				var alternative = {};
				var id_a  = $(elem_a).find('.group_alternative_id').val();
				alternative.id = (id_a == undefined ? null : id_a);

// 				console.log('ID: ', alternative.id);
				
				var value = $(elem_a).find('.group_alternative_value').val();
				alternative.value = (value == undefined ? null : value);

// 				console.log('VALUE: ', alternative.value);
				
				if ((alternative.value != null && alternative.value != "") || 
					(alternative.id != null && alternative.id != "")){
					question.alternatives.push(alternative);
// 					console.log('Alternative: ', alternative);
				}	
								
			});

			var $check = $elem.find('.questionMult');

// 			console.log("M");

			$check.each(function() {
				var isChecked = $(this).prop('checked');
				var alternative = {};
// 				console.log('C: ', $(this).val(), 'cheked', isChecked);
				if (isChecked) {
					alternative.id = $(this).val();
					alternative.value = $(this).attr('alt-value');
// 					console.log('ID: ', alternative.id);
// 					console.log('VALUE: ', alternative.value);
					question.alternatives.push(alternative);
// 					console.log('Alternative: ', alternative);
				}
			});

// 			console.log('Question: ', question);

			if (question.id != null && question.id != ""){
				questions.push(question);
			}			
// 			console.log('tam: ' + questions.length);	
		});

// 		console.log('Lista de questões: ', questions);
		
		return questions;
	}

	var actualizeList = function (articleid, source, score){
// 		console.log('atualiza list entrou: score' +score);
// 		console.log('Source: ' + source);
		var $article = $(".tBodyArticlesToEvaluate .readArticle[nextid=\""+articleid+"\"]");
		var newhref = $article.attr('href');
		if (newhref == undefined){
// 			console.log('' + newhref);
			return;
		}
		
		//http://localhost:8080/SystematicMap/maps/extraction/5/article/53
		
		var url = 'extraction/article/';
		var pos = newhref.indexOf(url);
		newhref = newhref.slice(0,pos) + url + articleid + '/load';

// 		console.log('newhref', newhref);
		
		///SystematicMap/extraction/article/{articleid}/load
		tableToEvaluate.row($article.parents('tr')).remove().draw();
		tableEvaluated.row.add([
       		articleid, score,
       		'<a class="readArticle" actualid="'+articleid+'" href="' + newhref + '">'+$article.html()+'</a>', source]).draw();
// 			console.log('atualiza list success');
		};

	var actualizePercent = function (percent){
		var p = percent.replace(",", ".");
		
		$('.progress-bar').attr('style', "min-width: 3em; width: "+p+"%");
		$('.progress-bar').attr('aria-valuenow', p);
		$('.progress-bar').html(percent + '%');

// 		console.log(p);
	};

	// ajax para salvar avaliações dos artigos
	var evaluate = function(event){
		event.preventDefault();
		var mapid = $('#mapid').val();
		var articleid = $('#articleid').val();
		var questions = obtainQuestions();
		var source = $('#articlesource').val();
		var score = $('#articlescore').val();
		var id = null;
		var comment = $('#comment').val();
		
// 		console.log($('#articlesource'));
// 		console.log('Source eval: ', source)
		
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
					  "comment" : comment,
					  "nextArticle" : id
					};

// 	param = {"questionVO" : questionVO};
	
// 	console.log('Endereço: ', address);
// 	console.log('Parametros: ', param);

// 	console.log('JSON: ', JSON.stringify(questionVO));
// 	console.log("NORMAL", questionVO);
// 	console.log('JQ' + jQuery.parseJSON(JSON.stringify(param)));

	$.ajax({ 
		url : address,
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		traditional : true,
		data : JSON.stringify(questionVO),
		success : function(data) {
// 			console.log("sucesso!");
			// atualiza listagens de artigos e carrega proximo artigo na tela
			var article = data['article'];
			var percent = data['percent'];
			var extraction = data['extraction'];
			
// 			console.log('article que veio do sistema: ', article);
// 			console.log('extraction que veio do artigo do sistema: ', extraction);

			messages('info', 'Artigo '+articleid, 'Extra&ccedil;&atilde;o do artigo realizada com sucesso');
			
			if (article == null || article.id == -1) {
// 				var $notice = $('#notices');
// 				var b = $notice.find('b').html('teste');
				messages('warning', 'Article', 'Todos os artigos j&aacute; foram extra&itilde;dos com sucesso');
			} else {
// 				console.log('vou chamar o actualizar');
				actualizeArticle(article, extraction);
				
			}
			
			actualizePercent(percent);
			
			if(extraction == null || extraction.length == 0){
				actualizeList(articleid, source, score);
			}
		},
		error : function(e) {
			console.log("erro!");
			console.error(e);
		}
	});

};

	// Captura o click no botão de avaliar
	$(document).on('click', '.buttonextraction', function(event) {
		evaluate(event);
	});
	
	
});
					
var messages = function (type, category, text){
// 	console.log(type, category, text);
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
</script>

<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li class="active"><fmt:message key="mapstudy.extraction"/></li>
</ol>

<h3 class="color-primary">
	${map.title} <a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="button.back"/></a>
</h3>
<hr/>

<p>Percentual de Trabalhos Avaliados</p>
<div class="progress">
	<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="${percentExtracted}" aria-valuemin="0" aria-valuemax="100" style="min-width: 3em; width: ${percentExtracted}%">
		${percentExtracted}%
	</div>
</div>
<div class="clear-both"></div>

<div class="row">
	<div class="col-md-12">
	 <!-- Depois colocar todos os dados assim como na seleção e por o feito de spoiler -->
  		<div class="" id="infoarticle">
			<h2><fmt:message key="mapstudy.article"/> - <span id="articleReadId">${article.id}</span></h2>
			<p><strong><fmt:message key="mapstudy.article.title"/>:
			</strong> <span id="articleReadTitle">${article.title}</span></p>
			
			<p><strong><fmt:message key="mapstudy.article.abstract"/>:
			</strong><span id="articleReadAbstract">${article.abstrct}</span></p>
		
			<p><strong>Palavras-chave:</strong> <span id="articleReadKeywords">${article.keywords}</span></p>
			<hr/>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.form" /></b>
			</div>
			<div class="panel-body">
				<h4><fmt:message key="mapstudy.form.evaluate" /></h4>
				<form action="${linkTo[ExtractionController].evaluateAjax}" method="post" id="#forExtraction">
					<input type="hidden" name="mapid"  id="mapid" value="${map.id}" />
					<input type="hidden" name="articleid" id="articleid" value="${article.id}" />
					<input type="hidden" id="articlesource" name="articlesource" value="${article.sourceView(article.source)}" />
					<input type="hidden" id="articlescore" name="articlescore" value="${article.score}" />

					<c:forEach var="question" items="${form.questions}" varStatus="q">
					
<%-- 						<c:set var="actualQuestionAlternatives" value="${article.alternative(question, userInfo.user)" /> --%>
						
						<div class="group_question">
							<input type="hidden" name="questions[${q.index}].id" value="${question.id}" class="group_question_id" id="question_id_${q.index}"/>
							<div class="padding-left-none">
								<strong class="group_question_name" id="question_name_${q.index}">${question.name}:</strong>
							</div>
							<div class="group_alternative form-group">
							
								<c:if test="${question.type == 'MULT'}">

									<c:forEach items="${question.alternatives}" var="alt" varStatus="c">
										<c:set var="containsExc" value="false" />
										<c:forEach var="done" items="${article.alternative(question, userInfo.user)}">
											<c:if test="${alt.id eq done.id}">
												<c:set var="containsExc" value="true" />
											</c:if>
										</c:forEach>

										<div class="checkbox">
											<input class="questionMult" type="checkbox" value="${alt.id}" ${containsExc ? 'checked="checked"' : '' } alt-value="${alt.value}" />${alt.value} 
										</div>
									</c:forEach>
								</c:if>
								
								<c:if test="${question.type == 'LIST'}">
									<select	data-placeholder="<fmt:message key="mapstudy.form.choose" />" class="form-control select2 group_alternative_id" name="alternatives[${q.index}].id" id="alternative_id_${q.index}" tabindex="2">								
										<c:forEach var="alt" items="${question.alternatives}">
											<option value="${alt.id}" data-email="">${alt.value}</option>
										</c:forEach>
									</select>
								
									<c:if test="${not empty article.alternative(question, userInfo.user)}">
										<input type="hidden" value="${article.alternative(question, userInfo.user).get(0).id}" class="alternative_list_id" id="alternative_list_id_${q.index}"/>
										<input type="hidden" value="${article.alternative(question, userInfo.user).get(0).value}" class="alternative_list_value" id="alternative_list_value_${q.index}"/>
									</c:if>
								</c:if>
								
								
								<c:if test="${question.type == 'SIMPLE'}">
									<input type="text" class="form-control group_alternative_value" name="alternatives[${q.index}].value" id="alternative_value_${q.index}" value="${article.alternative(question, userInfo.user).size() > 0 ? article.alternative(question, userInfo.user).get(0).value : ''}"/>
								</c:if>							
							</div>		
 						</div> 
					</c:forEach>		
					
<!-- 					Comentários -->
					<p>
						<strong><fmt:message key="mapstudy.article.comments"/>:</strong><br/>
						<textarea class="form-control" id="comment" name="comment" rows="5" cols="">${article.getCommentsUser(userInfo.user.id)}</textarea>
					</p>
								
					<button type="submit" id="submit" class="btn btn-large btn-primary buttonextraction">
						<fmt:message key="salve" />
					</button>
					
					<div class="clear-both"></div>
				</form>
			</div>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading"><b><fmt:message key="mapstudy.articles.list" /></b></div>
			<div class="panel-body">
				<h4><fmt:message key="mapstudy.articles.list.toreview"/></h4>
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
							<c:forEach var="article" items="${articlesToExtraction}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${article.id}</td>
									<td>${article.score}</td>
									<td><a class="readArticle" actualid="${article.id}" nextid="${article.id }" href="${linkTo[ExtractionController].loadArticleAjax(map.id, article.id)}">${article.title}</a></td>
									<td>${article.sourceView(article.source)}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<hr/>
				<h4><fmt:message key="mapstudy.articles.list.extracted"/></h4>
				<div class="dataTable_wrapper">
					<table class="table table-striped table-bordered table-hover datatable-evaluated">
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center"><fmt:message key="mapstudy.article.score" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.source" /></th>
							</tr>
						</thead>
						<tbody class="tBodyArticlesEvaluate">
							<c:forEach var="eval" items="${extractions}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${eval.id}</td>
									<td>${eval.score}</td>
									<td><a class="readArticle" actualid="${eval.id}" href="${linkTo[ExtractionController].loadArticleAjax(map.id, eval.id)}">${eval.title}</a></td>
									<td>${article.sourceView(article.source)}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
