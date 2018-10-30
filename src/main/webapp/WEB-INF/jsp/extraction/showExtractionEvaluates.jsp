<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
$(document).ready(function(){
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
// 		console.log('entra atualiza article', article);
		//alterar a url para caso seja realizado F5
		var url = window.location.href;
		url = url.substr(0, url.lastIndexOf('/')) + '/' + article.id;

// 		console.log(url);
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
			$('#articleid').val(article.id);
		}

		var setQuestions = function (extraction){
			if (extraction.length > 0){
				$.each(extraction, function( index, elemento ) {
// 					console.log('set');
// 					  console.log(elemento.question.id + " | " + elemento.question.type + " | " + elemento.question.name);

					  if (elemento.question.type == 'LIST'){
// 						  var alternative_selected = elemento.alternative;
// 						  '<option selected="selected" value="15">2</option>'
						  $.each(elemento.question.alternatives, function( index2, elemento2 ) {
							  if (elemento2.value == elemento.alternative.value){
// 								  console.log('alternativa selecionada: ' + elemento.alternative.value);
// 								  $("#alternative_id_" + index).attr('selectedIndex', index2);
								$("#alternative_id_" + index).val(elemento.alternative.id);	
								var test = $('#select2-alternative_id_' + index + '-container');
								test.attr('title', elemento.alternative.value);
								test.html(elemento.alternative.value);
// 								$('select#alternative_id_' + index +' option').eq(index).css('backgroundColor', 'blue');
							  }
// 							  console.log(elemento2.id + " | " + elemento2.value);
						  });
					  }else{
// 						  $(elem_a).find('.group_alternative_value').val(elemento.alternative.value);
							$('#alternative_value_' + index).val(elemento.alternative.value);
					  }
// 					  console.log('alternativa selecionada: ' + elemento.alternative.value);
// 					  console.log('alternativas da questão: ');
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
		url = url.replace("1", actualid);

		$.ajax({ 
			url: url,
			type: 'GET',
			success: function(data){
				var article = data['article'];
				var extraction = data['extraction'];
// 				console.log('article read: ', article);
// 				console.log('extraction: ', extraction);
				actualizeArticle(article, extraction);
			},
			error: function(e){
				console.error(e);
			}
		});
	});
});
</script>


<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li class="active"><fmt:message key="mapstudy.viewextractions"/></li>
</ol>

<h3 class="color-primary">
	${map.title} <a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="button.back"/></a>
</h3>
<hr/>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading"><b><fmt:message key="mapstudy.details"/></b></div>
			<div class="panel-body">
				<dl class="dl-horizontal">
					<dt class="mydt"><strong><fmt:message key="mapstudy.title"/>:</strong></dt><dd class="mydd">${map.title}</dd>
				  	<dt class="mydt"><strong><fmt:message key="mapstudy.description"/>:</strong></dt><dd class="mydd">${map.description}</dd>
				  	<dt class="mydt"><strong><fmt:message key="mapstudy.evaluation.rate"/>:</strong></dt><dd class="mydd">${percentEvaluatedDouble}%</dd>
				</dl>
				<hr/>
				
				<dl class="dl-horizontal">
					<dt class="mydt"><strong><fmt:message key="mapstudy.extractions.compare"/>:</strong></dt>
					<dd class="mydd" style="margin-bottom: 10px;">
<%-- 						<c:if test="${percentEvaluatedDouble >= 100}"> --%>
							<a class="btn btn-primary btn-xs" style="width: 170px;" href="${linkTo[ExtractionController].compare(map.id)}"><fmt:message key="mapstudy.evaluations.compare"/></a>
<%-- 						</c:if> --%>
<%-- 						<c:if test="${percentEvaluatedDouble < 100}"> --%>
<%-- 							<fmt:message key="mapstudy.extractions.compare.undone"/> --%>
<%-- 						</c:if> --%>
					</dd>
					<dt class="mydt"><strong>Análise de concordância:</strong></dt>
					<dd class="mydd" style="margin-bottom: 10px;">
					<form action="${linkTo[ExtractionController].concordance}" method="post">
					<input type="hidden" name="mapid" value="${map.id}" />
						<button type="submit" id="concordance-extraction" class="btn btn-large btn-primary btn-xs" style="width: 170px;">
							Aplicar
						</button>
					</form>
					</dd>
				  	<dt class="mydt"><strong><fmt:message key="mapstudy.extractions.export"/>:</strong></dt>
				  	<dd class="mydd">
				  		<a class="btn btn-default btn-xs" style="width: 170px;" href="${linkTo[ExtractionController].downloadMine(map.id)}"><fmt:message key="mapstudy.extractions.export.csv.mine"/></a>
						<a class="btn btn-default btn-xs" style="width: 170px;" href="${linkTo[ExtractionController].downloadAll(map.id)}"><fmt:message key="mapstudy.extractions.export.csv.all"/></a>
				  	</dd>
				</dl>
			</div>
		</div>
		
		<!-- Exibir questões e alternativas selecionadas		 -->

		<div class="panel panel-default">
			<div class="panel-heading"><b><fmt:message key="mapstudy.extractions.user"/></b></div>
			<div class="panel-body">
				<c:forEach var="ext" items="${extractions}" varStatus="a">
					<div class="x_panel" style="height: auto;">
				 		 <div class="x_title">
				    		<b>${ext.key}</b>
				    		<ul class="nav navbar-right panel_toolbox">
				      			<li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a></li>
				      			<li><a class="close-link"><i class="fa fa-close"></i></a></li>
				    		</ul>
				    		<div class="clearfix"></div>
				  		</div>
					  	<div class="x_content" style="display: none;">		
					    	<div>
					      		<ul class="list-inline widget_tally">
					      			<c:forEach var="alt" items="${ext.value}" varStatus="aa">
					        			<li>
					          				<p>
					            				<span class="month">${alt.key} </span>
					            				<span class="count">${alt.value}</span>
					          				</p>
					        			</li>
					        		</c:forEach>
					      		</ul>
					    	</div>
					  	</div>
					</div>			
				</c:forEach>
			</div>
		</div>
		
		<!-- Inicio artigos -->
<!-- 		<div class="clear-both"></div> -->
<!-- 		<div class="hide" id="infoarticle"> -->
<%-- 			<h2><fmt:message key="mapstudy.article"/> - <span id="articleReadId">${article.id}</span></h2> --%>
			
<%-- 			<p><strong><fmt:message key="mapstudy.article.title"/>:</strong> <span id="articleReadTitle">${article.title}</span></p> --%>
<%-- 			<p><strong><fmt:message key="mapstudy.article.abstract"/>:</strong> <span id="articleReadAbstract">${article.abstrct}</span></p> --%>
<%-- 			<p><strong><fmt:message key="mapstudy.article.key"/>:</strong> <span id="articleReadKeywords">${article.keywords}</span></p> --%>
<%-- 			<p><strong><fmt:message key="mapstudy.article.author"/>:</strong> <span id="articleReadAuthor">${article.author}</span></p> --%>
<%-- 			<p><strong><fmt:message key="mapstudy.article.source"/>:</strong> <span id="articleReadSource">${article.source}</span></p> --%>
<!-- 			<hr/> -->
<!-- 		</div> -->
	</div>
</div>

<script type="text/javascript">

$('.close-link').click(function () {
    var $BOX_PANEL = $(this).closest('.x_panel');

    $BOX_PANEL.remove();
});

//Panel toolbox
$(function () {
    $('.collapse-link').on('click', function() {
        var $BOX_PANEL = $(this).closest('.x_panel'),
            $ICON = $(this).find('i'),
            $BOX_CONTENT = $BOX_PANEL.find('.x_content');
        
        // fix for some div with hardcoded fix class
        if ($BOX_PANEL.attr('style')) {
            $BOX_CONTENT.slideToggle(200, function(){
                $BOX_PANEL.removeAttr('style');
            });
        } else {
            $BOX_CONTENT.slideToggle(200); 
            $BOX_PANEL.css('height', 'auto');  
        }

        $ICON.toggleClass('fa-chevron-up fa-chevron-down');
    });

    $('.close-link').click(function () {
        var $BOX_PANEL = $(this).closest('.x_panel');

        $BOX_PANEL.remove();
    });
});
</script>