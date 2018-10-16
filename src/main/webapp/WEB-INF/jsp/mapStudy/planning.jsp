<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	(function($) {
		$(document).ready(function() {
			
			var messages = function (type, category, text){
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
			
		var isLogado = function() {
			if ('${userInfo.user}' == null) {
				window.location.reload();
			}
		};
	
		// activenav deve ser o id do nav
		var newnavactive = function(activenavId) {
// 			console.log('activenavId: ', activenavId);
			// Tira status ativo do nav
			var navactualId = $('.mynav.active').attr('id');
// 			console.log('navactualId: ', navactualId);
			
			if (navactualId == undefined) {
				navactualId = $('#mydiv').val().substr(3);
// 				console.log('ficou indefinido: ', navactualId);
			}
	
			$('#' + navactualId).removeClass('active');
	
			// Esconde a div relacionada ao nav que estava ativo
			var divatual = '#div' + navactualId.replace('#', '');
			$(divatual).addClass('hide');
			
// 			console.log('divatual: ', divatual);
	
			// Muda status do nav selecionado para ativo
			$(activenavId).addClass('active'); // adiciona classe active no nav selecionado 
	
			// Exibe a div relacionada ao nav
			var divnovo = 'div' + activenavId.substr(1);
			
			$('#mydiv').val(divnovo);
			$('#' + divnovo).removeClass('hide');
			
// 			console.log('divnovo: ', divnovo);
		};
				
		var mydivid = '#' + $('#mydiv').val().substr(3);
		newnavactive(mydivid);
							
		$(document).on('click', '.mynav', function(event) {
			event.preventDefault();
			//event.stopPropagation();

			isLogado();
			var id = '#' + $(this).attr('id');			
			newnavactive(id);
		});

		$("#formInclusion").validate({
			rules : {
				'criteria.description' : {
					required : true
				/*, minlength : 1*/
				}
			},
			messages : {
				'criteria.description' : {
					required : '<fmt:message key="required" />'
					/*,minlength: '<fmt:message key="mapstudy.min.title" />'*/
				}
			}
		});

		$("#formExclusion").validate({
			rules : {
				'criteria.description' : {
					required : true
				/*, minlength : 1*/
				}
			},
			messages : {
				'criteria.description' : {
					required : '<fmt:message key="required" />'
					/*,minlength: '<fmt:message key="mapstudy.min.title" />'*/
				}
			}
		});
		
		// Parte do formulário de extração
		//Remove um campo
		var removeField = function(obj) {
			$(obj).remove();
		};

		// Adiciona questões
		addQuestion = function(divObj) {
			time = new Date().getTime();
			fieldContent = '<div class="form-group group_question" id="group_question_'+time+'">'
					+ '<div class="row">'
					+ '<div class="col-md-6">'
					+ '<label for="quest_'+time+'" class=""><fmt:message key="mapstudy.question.name" /></label> <input type="text"'
					+ 'class="form-control group_question_name" id="quest_'
					+ time
					+ '" name="questions[]"'
					+ 'placeholder="<fmt:message key="mapstudy.question.name"/>" />'
					+ '</div>'
					+ '<div class="col-md-3">'
					+ '<label for="type_'+time+'" class=""><fmt:message key="mapstudy.question.type" /></label>'
					+ '<select class="form-control selectiontype group_question_type" name="types[]" id="type_'+time+'">'
					+ '<c:forEach var="type" items="${questionTypes}">'
					+ '<option value="${type}">${type.description}</option>'
					+ '</c:forEach>'
					+ '</select>'
					+ '</div>'
					+ '<div class="col-md-3">'
					+ '<label class="" for="btnquestionremove_'+time+'">&nbsp;&nbsp;&nbsp;</label>'
					+ '<a href="#" class="btn btn-danger buttonquestionremove form-control" id="btnquestionremove_'+time+'" idquest="group_question_'+time+'">'
					+ '<i class="glyphicon glyphicon-remove"></i> <fmt:message key="mapstudy.question.remove" />'
					+ '</a>'
					+ '</div>'
					+ '</div>'
					+ '<hr />' + '</div>';
			divObj.append(fieldContent);
		};

		var addAlternative = function(divObj, isFirst, questassociation) {
			time = new Date().getTime();
			var content = '<div class="form_group group_alternative" id="group_alternative_'+time+'">'
					+ '<div class="col-md-6">'
					+ '<label for="alternative_'+time+'" class="">'
					+ '<fmt:message key="mapstudy.alternative.value" /></label> '
					+ '<input type="text" class="form-control group_alternative_value" id="alternative_'
					+ time
					+ '" name="alternatives[].value"'
					+ 'placeholder="<fmt:message key="mapstudy.alternative.value"/>" questassociation="'
					+ questassociation + '"/>' + '</div>';

			if (isFirst == true) {
				content += '<div class="col-md-3">'
						+ '<label class="" for="buttonalternativeadd_'+time+'">&nbsp;&nbsp;&nbsp;</label>'
						+ '<a href="#" class="btn btn-success buttonalternativeadd form-control" id="buttonalternativeadd_'+time+'" questassociation="'+questassociation+'">'
						+ '<i class="glyphicon glyphicon-plus"></i> <fmt:message key="mapstudy.alternative.add" />'
						+ '</a>' + '</div>' + '</div>';
			} else {
				content += '<div class="col-md-1">'
						+ '<label class="" for="buttonalternativeremove_'+time+'">&nbsp;&nbsp;&nbsp;</label>'
						+ '<a href="#" alternativeid="group_alternative_'+time+'" class="btn btn-danger buttonalternativeremove form-control" id="buttonalternativeremove_'+time+'">'
						+ '<i class="glyphicon glyphicon-remove"></i>'
						+
						//	 				<fmt:message key="mapstudy.alternative.remove" />
						'</a>' + '</div>' + '</div>';
			}

			$(divObj).append(content);
		};

		// Adiciona questão
		$(document).on('click', '.buttonquestionadd', function(event) {
			event.preventDefault();
			isLogado();

			divObj = $('#' + 'allquestions');
			addQuestion(divObj);
		});

		// Remove questão
		$(document).on('click', '.buttonquestionremove', function(event) {
			event.preventDefault();
			isLogado();
			
			var id = $(this).attr('idquest');
			removeField($('#' + id));
		});

		// Adiciona ALternativa
		$(document).on('click', '.buttonalternativeadd', function(event) {
			event.preventDefault();
			isLogado();

			var myid = $(this).attr('questassociation');
			addAlternative($('#group_question_'	+ myid), false, myid);
		});

		// Remove ALternativa
		$(document).on('click', '.buttonalternativeremove',	function(event) {
			event.preventDefault();
			isLogado();	

			var id = $(this).attr('alternativeid');
			removeField($('#' + id));
		});

		// Captura seleção de tipo
		$(document).on('change', '.selectiontype', function() {
			isLogado();
// 			console.log('type: ', $(this).val());
			if ($(this).val() == 'SIMPLE') {
				// se existir alternatives remover todas
				var myid = $(this).attr('id');
				myid = myid.substring(myid.lastIndexOf('_') + 1, myid.length);
				var objId = "#group_question_" + myid;
				
				// assim ele vai pegar os group_alternative "filhos" de objId
				$(objId	+ " .group_alternative").each(function(index) {
					$(this).remove();
				});

			} else {
			// inserir alternative
				var myid = $(this).attr('id');
				myid = myid.substring(myid.lastIndexOf('_') + 1, myid.length);
				var objId = "#group_question_" + myid;

				if ($(objId).children('.group_alternative').length == 0) {
					addAlternative($(objId), true, myid);
				} else {
// 					console.log('ja existe alternative');
				}
			}
		});

		//Salvar Formulário
		$(document)	.on('click', '.buttonextraction', function(event) {
			event.preventDefault();
			var questions = [];
			
			$('.group_question').each(function(idx,	elem) {
				var $elem = $(elem);
				var question = {};
				question.id = null;
				question.name = $elem.find('.group_question_name').val();
				question.type = $elem.find('.group_question_type').val();
				question.alternatives = [];
				
				$elem.find('.group_alternative').each(function(idx_a, elem_a) {
					var alternative = {};
					alternative.id = null;
					alternative.value = $(elem_a).find('.group_alternative_value').val();
					
					if (alternative.value != null && alternative.value != "") {
						question.alternatives.push(alternative);
					}
				});

				if (question.name != null && question.name != "") {
					questions.push(question);
				}
				
			});

			var address = "${linkTo[ExtractionController].formAjax}";
			var mapid = $('#mapid').val();

			var questionVO = {
				"mapid" : mapid,
				"questions" : questions
			};

			param = {
				"questionVO" : questionVO
			};
			
// 			console.log(param);
			
			/*
			$.ajax({
				url : address,
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				traditional : true,
				data : JSON.stringify(param),
				success : function(data) {
// 					console.log(data);
				},
				error : function(e) {
					console.error(e);
				}
			});
			*/
		});
		
		/* QUESTOES PARA EXTRACAO */
		
		var mapId = $('#mapid').val();
		var $divsAlternatives = $('.subquestion-hasalternatives'),
			$divAddSubquestionExt = $('#div-add-subquestion-extraction'),
			$divTableSubquestionExt = $('#div-table-subquestions-extraction'),
			$divAlternativesExt = $('#form-add-subquestion-extraction-alternatives');
		
		function showPreloader() {
			$('#preloader').show();
		}
		function hidePreloader() {
			$('#preloader').hide();
		}
		
		function showListSubQuesiton() {
			$divAddSubquestionExt.hide();
			$divTableSubquestionExt.show();
		}
			
		function showFormSubQuesiton() {
			$divAddSubquestionExt.show();
			$divTableSubquestionExt.hide();
		}
		
		$(document).on('click', '#btn-add-subquestion-extraction', function(e) {
			e.preventDefault();
			renderQuestionNew();
		});
		
		$(document).on('click', '#btn-back-subquestion-extraction', function(e) {
			e.preventDefault();
			loadAllQuestions(function(){
				showListSubQuesiton();
			});
		});
		
		$(document).on('click', '#btn-add-alternative-subquestion-extraction', function(e) {
			e.preventDefault();
			$divAlternativesExt.append(renderAlternative());
		});
		
		$(document).on('click', '.subquestion-alternative-rmv', function(e) {
			e.preventDefault();
			$(this).closest('.subquestion-alternative').remove();
		});
		
		$(document).on('change', '#subquestion-type', function(e) {
			var val = $(this).val();
			if (val == 'LIST' || val == 'MULT') {
				$divsAlternatives.show();
			} else {
				$divAlternativesExt.html('');
				$divsAlternatives.hide();
			}
		});
		
		$(document).on('click', '#btn-save-subquestion-extraction', function(e){
			e.preventDefault();
			
			var question = {
				name: $('#subquestion-name').val(),
				type: $('#subquestion-type').val()
			};
			var alternatives = [];
			var questionId = $('#subquestion-id').val();
			if (questionId) {
				question.id = questionId;
			}
			
			$('.subquestion-alternative').each(function(idx){
				var id = $(this).find('.subquestion-alternative-id').val();
				var title = $(this).find('.subquestion-alternative-title').val();

				console.log("alternative dados: " + id + "|" + title);
				
				if (title !== ""){
					alternatives.push({
						value: title
					});
				}
			});
			
			question.alternatives = alternatives;

			console.log('QA: ' + question);
			
			saveQuestion(question);
		});
		
		$(document).on('click', '.subquestions-extraction-edit', function(e){
			e.preventDefault();
			var questionId = $(this).attr('data-question-id');
			loadQuestion(questionId, function(data) {
				console.log('questionId: ' + questionId + ' dados: ' + data);
				if (data.status == 'SUCESSO' && data.data) {
					renderQuestionEdit(data.data);
				}
			});
		});
		
		var viewType = function(type){
// 			console.log(type);
			if (type == "SIMPLE"){
				return "Texto";
			}else if (type == "LIST"){
				return "Listagem";
			}else if (type == "MULT"){
				return "Multiplas opções";
			}
		}

		function loadAllQuestions(callback) {
			showPreloader();
			$.ajax({
				url : '${linkTo[ExtractionController].loadQuestions}',
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				data : JSON.stringify({
					"mapid" : mapId
				}),
				success : function(data) {
					var html = '', count = 0;
					for (var i in data) {
						var q = data[i];
						html += renderLinhaQuestion(q.id, q.name, viewType(q.type));
						count++;
					}
					if (count == 0) {
						html = renderLinhaQuestionEmpty();
					}
					$('#table-subquestions-extraction tbody').html(html);
					hidePreloader();
					
					if(callback && typeof(callback) === "function") {
			            callback(data);
			        }
				},
				error : function(e) {
					hidePreloader();
// 					alert('Ops, ocorreu um problema ao carregar a lista de questões. Tente novamente');
					messages('danger', 'Quest&otilde;es', 'Ops, ocorreu um problema ao carregar a lista de questões. Tente novamente.');
					console.log(e);
				}
			});
		}
		
		function loadQuestion(questionid, callback, callbackError) {
			showPreloader();
			$.ajax({
				url : '${linkTo[ExtractionController].getQuestion}',
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				data : JSON.stringify({
					"mapid" : mapId,
					"questionId" : questionid
				}),
				success : function(data) {
					hidePreloader();
					if(callback && typeof(callback) === "function") {
			            callback(data);
			        }
				},
				error : function(e) {
					hidePreloader();
// 					alert('Ops, ocorreu um problema ao carregar a questão. Tente novamente');
					messages('danger', 'Quest&otilde;es', 'Ops, ocorreu um problema ao carregar a questão. Tente novamente.');
					if(callbackError && typeof(callbackError) === "function") {
						callbackError(e);
			        }
				}
			});
		}

		function saveQuestion(question) {
			showPreloader();

			console.log("SQ: " + question);
			
			$.ajax({
				url : '${linkTo[ExtractionController].addQuestion}',
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				traditional : true,
				data : JSON.stringify({
					"mapid" : mapId,
					"question" : question
				}),
				success : function(data) {
					//hide pelo load
					loadAllQuestions(function(){
						showListSubQuesiton();
					});
					//TODO
					messages('info', question.name, 'Foi salva com sucesso !');
				},
				error : function(e) {
					hidePreloader();
// 					alert('Ops, ocorreu um problema ao salvar a questão. Tente novamente');
					messages('danger', 'Quest&otilde;es', 'Ops, ocorreu um problema ao salvar a quest&atilde;o. Tente novamente.');

					console.error(e);
				}
			});
		}
		
		function removeQuestion(questionid) {
			showPreloader();
			$.ajax({
				url : '${linkTo[ExtractionController].removeQuestion}',
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				traditional : true,
				data : JSON.stringify({
					"mapid" : mapId,
					"questionId" : questionid
				}),
				success : function(data) {
					//hide pelo load
					loadAllQuestions();
				},
				error : function(e) {
					hidePreloader();
// 					alert('Ops, ocorreu um problema ao remover a questão. Tente novamente');
					messages('danger', 'Quest&otilde;es', 'Ops, ocorreu um problema ao remover a questão. Tente novamente.');
					console.error(questionid, e);
				}
			});
		}
		
		function renderQuestionNew() {
			$('#subquestion-id').val('');
			$('#subquestion-name').val('');
			$divAlternativesExt.html('');
			showFormSubQuesiton();
		}
		
		function renderQuestionEdit(question) {
			$('#subquestion-id').val(question.id);
			$('#subquestion-name').val(question.name);
			$('#subquestion-type').val(question.type).trigger('change');
			
			$divAlternativesExt.html('');
			for (var i in question.alternatives) {
				var a = question.alternatives[i];
				console.log('reender alt: ' + a);
				$divAlternativesExt.append(renderAlternative(a.id, a.value));
			}
			showFormSubQuesiton();
		}
		
		function renderLinhaQuestionEmpty() {
			return '<tr><td colspan="3" class="text-center">Sem Subquestões Cadastradas</td></tr>';
		}
		
		function renderLinhaQuestion(id, name, type) {
// 			console.log(type);
			var str = '<tr><td>'+name+'</td><td class="text-center">'+type+'</td><td class="text-center">';
			str += '<a style="width: 48%;" class="btn btn-primary btn-sm subquestions-extraction-edit" data-question-id="'+id+'" href="#"><i class="glyphicon glyphicon-pencil"></i> Editar</a>';
			str += '<a style="width: 48%;" class="btn btn-danger btn-sm confirmation-modal subquestions-extraction-remove" data-question-id="'+id+'" data-conf-modal-body="<fmt:message key="mapstudy.excluir.message" />" href="#" data-conf-modal-callback="window.removeSubQuestion('+id+')" ><i class="glyphicon glyphicon-remove"></i> Remover</a>';
			str += '</td></tr>';
			return str;
		}
	
		function renderAlternative(id, val) {
			val = val ? val : '';
			id = id ? id : '';
			var result = '<div class="form-group subquestion-alternative"><div class="row"><div class="col-sm-10">';
			result += '<input class="subquestion-alternative-id" type="hidden" value="' + id + '"/>';
			result += '<input required type="text" name="avalue" class="form-control subquestion-alternative-title" placeholder="Alternativa" value="' + val + '"/>'; 
			result += '</div><div class="col-sm-2"><a class="btn btn-danger subquestion-alternative-rmv" href="#"><i class="glyphicon glyphicon-remove"></i> <fmt:message key="remove"/></a></div></div></div>';
			return result;
		}
		
		window.removeSubQuestion = function(questionid) {
			removeQuestion(questionid);
		}
		
		//init
		$divsAlternatives.hide();
		loadAllQuestions(function(){
			showListSubQuesiton();
		});
		
		 var conectors = function (text){
	    	saida = "";
	    	re = /OR/g;
	    	re2 = /AND/g;
	    	saida = text.replace(re, "OR".bold());
	    	saida = saida.replace(re2, "AND".bold());
	    	return saida;		    	
		 };
		 
		 var searchBold = function (){
			var listSearch = $(".search-description");		
            
            $.each(listSearch, function() {
            	var searchDesc = "" + $(this).html();
            	$(this).html(conectors(searchDesc));	
//             	console.log($(this).html());
            });
		 };
		 
		 searchBold();
		
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

		// Captura seleção de tipo
		$(document).on('change', '.selection-algorithm', function() {
			isLogado();
			selectedValue = $("#algorith-type option:selected").val();
			selectedName = $("#algorith-type option:selected").html(); 
			console.log('selectedValue', selectedValue);
			console.log('selectedName', selectedName);

			if(selectedValue != -1){
				searchLearningStats($("#mapid").val(), selectedValue, selectedName);
			}else{
				messages('danger', 'Algoritmo', 'Selecione um algoritmo.');
				hidePanelStats();
			}
		});

		function searchLearningStats(mapid, algorithm, selectedName) {
			$.ajax({
				url : '${linkTo[AutomaticSelectionController].calculateStats}',
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				traditional : true,
				data : JSON.stringify({
					"mapid" : mapid,
					"algorithm" : algorithm
				}),
				success : function(data) {
					setStats(data, algorithm, selectedName);
					showPanelStats();
				},
				error : function(e) {
					hidePreloader();
// 					alert('Ops, ocorreu um problema ao remover a questão. Tente novamente');
					messages('danger', 'Estatísticas', 'Ops, ocorreu um problema ao calcular as estatísticas do algoritmo. Tente novamente.');
					console.error(mapid, e);
				}
			});
		};

		function showPanelStats() {
			$('#panel-stats-algorithm').removeClass('hide');
		};

		function hidePanelStats() {
			$('#panel-stats-algorithm').addClass('hide');
		};

		
		function setStats(data, algorithm, selectedName) {
			console.log(algorithm);
			$('#algorithmSelected').val(algorithm);
			$('#algorithmName').html(selectedName);
			$('#numberarticles').html(data.learningStats['numberArticles']);
			$('#numberArticlesAccepted').html(data.learningStats['numberArticlesAccepted']);
			$('#numberArticlesRejected').html(data.learningStats['numberArticlesRejected']);
			$('#wss').html(data.learningStats['wss']);
			$('#recall').html(data.learningStats['recall']);
			$('#accuracy').html(data.learningStats['accuracy']);
			$('#error').html(data.learningStats['error']);
			$('#rocArea').html(data.learningStats['rocArea']);
			$('#fMeasure').html(data.learningStats['fMeasure']);
			$('#numberArticlesTraining').html(data.learningStats['numberArticlesTraining']);
		};
				
		$(document).on('click', '#button-save-algorithm', function(e){
			e.preventDefault();
			var mapid = Number($('#mapid').val());
			var algorithm = $('#algorithmSelected').val();
			
			var learningConfiguration = {};
			learningConfiguration.numberArtclesValidation = Number($('#numberArtclesValidation').val());
			learningConfiguration.useResearcher = $('#useResearcher').prop('checked');
			learningConfiguration.showSelection = $('#showSelection').prop('checked');

			//params = JSON.stringify({"mapid" : mapid, "algorithm" : algorithm, "learningConfiguration" : learningConfiguration});
			params = {"mapid" : mapid, "algorithm" : algorithm, "learningConfiguration" : learningConfiguration};
			address = "${linkTo[AutomaticSelectionController].addconfiguration}";

		 	console.log('JSON: ', JSON.stringify(params));
		 	console.log("NORMAL", params);
		 	console.log('JQ', jQuery.parseJSON(JSON.stringify(params)));
			console.log("address", address );

			$.ajax({ 
				url : address,
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				traditional : true,
				data : JSON.stringify(params),
				success : function(data) {
					console.log("success!");
					messages('info', 'Configurações', 'Configurações salvas com sucesso\!');
// 					window.location.reload();
				},
				error : function(e) {
					messages('danger', 'Configurações', 'Ops, ocorreu um problema ao salvar configurações. Tente novamente.');
					console.error(e);
				}
			});

		});
		
	    
// 	    $("#form-add-subquestion-extraction").validate({ 
//             rules: {
//            	 'name': { 
//                	 required : true,
//                	 minlength : 1
//                 },
//                 'avalue': {
//                     required: true,
//                     minlength : 1
//                 }
//              }, messages: {
//            	  'name': {
//                      required: '<fmt:message key="required" />',
//                      minlength: '<fmt:message key="question.name.min" />'
//                  },
//                  'avalue': {
//                      required: '<fmt:message key="required" />',
//                      minlength: '<fmt:message key="alternative.value.min" />'
//                  }
//              }
// 		});
		
	});
})(jQuery);
</script>

<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li class="active"><fmt:message key="mapstudy.planning"/></li>
</ol>

<h3 class="color-primary">
	${map.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].show(map.id)}"><fmt:message key="button.back"/></a>
</h3>

<hr/>

<form><input type="hidden" name="mydiv" value="${mydiv}" id="mydiv"/></form>
<input type="hidden" name="mapid" id="mapid" value="${map.id}" />

<ul class="nav nav-tabs" style="background-color: #f8f8f8;">
<!-- border: 1px solid #E7E7E7;"> -->
  <li role="presentation" class="mynav" id="goals"><a href="#"><fmt:message key="mapstudy.goals" /> <!--  <span class="glyphicon glyphicon-ok"></span>--></a></li>
  <li role="presentation" class="mynav" id="question"><a href="#" ><fmt:message key="mapstudy.research.question" /></a></li>
  <li role="presentation" class="mynav" id="string"><a href="#" ><fmt:message key="mapstudy.search.string" /></a></li>
  <li role="presentation" class="mynav" id="criterias"><a href="#" ><fmt:message key="mapstudy.inclusion.and.exclusion.criterias" /></a></li>
  <li role="presentation" class="mynav" id="extraction"><a href="#" ><fmt:message key="mapstudy.form" /></a></li>
  <li role="presentation" class="mynav" id="automaticselection"><a href="#" ><fmt:message key="mapstudy.automaticselection" /></a></li>
</ul>

<p>

<!-- Formulário de extração -->
<div  id="divextraction" class="hide">
	<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.form" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div id="div-table-subquestions-extraction">
					<h4>
						<fmt:message key="mapstudy.extraction.subquestions.list" />
						<a id="btn-add-subquestion-extraction" href="#" class="btn btn-primary u-btn-pull-right">
						<fmt:message key="mapstudy.extraction.subquestions.add" /></a> 
					</h4>
					<hr/>
					<table id="table-subquestions-extraction" class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th style="width: 50%" class="text-left"><fmt:message key="mapstudy.title" /></th>
								<th style="width: 20%" class="text-center"><fmt:message key="mapstudy.type" /></th>
								<th style="width: 30%" class="text-center"><fmt:message key="actions" /></th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty questions}">
								<tr><td class="text-center" colspan="3"><fmt:message key="mapstudy.extraction.subquestions.list.empy" /></td></tr>
							</c:if>
									
							<c:forEach var="question" items="${questions}" varStatus="s">
								<tr>
									<td>${question.name}</td>
									<td class="text-center">${question.type.description}</td>
									<td class="text-center">
										<a class="btn btn-primary btn-sm subquestions-extraction-edit" data-question-id="${question.id}" href="#"><i class="glyphicon glyphicon-pencil"></i> Editar</a>
										<a class="btn btn-danger btn-sm confirmation-modal subquestions-extraction-remove" data-question-id="${question.id}" data-conf-modal-body="<fmt:message key="mapstudy.excluir.message" />" href="#" data-conf-modal-callback="window.removeSubQuestion(${question.id})" ><i class="glyphicon glyphicon-remove"></i> Remover</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
				<div id="div-add-subquestion-extraction">
					<h4>
						<fmt:message key="mapstudy.extraction.subquestions.add.ex" />
						<a id="btn-back-subquestion-extraction" href="#" class="btn btn-default u-btn-pull-right"><fmt:message key="mapstudy.extraction.subquestions.list.back" /></a> 
					</h4>
					<hr/>
					<form action="#" method="post" id="form-add-subquestion-extraction">
						<input type="hidden" name="id" id="subquestion-id" value="" />
						<div class="form-group">
							<div class="row">
								<div class="col-sm-6">
									<label for="subquestion-name" class=""><fmt:message key="mapstudy.question.name"/></label>
									<input type="text" class="form-control" name="name" id="subquestion-name" value=""/>
								</div>
								<div class="col-sm-6">
									<label for="subquestion-type" class=""><fmt:message key="mapstudy.question.type" /></label> 
									<select class="form-control selectiontype group_question_type" name="type" id="subquestion-type">
										<c:forEach var="type" items="${questionTypes}">
											<option value="${type}">${type.description}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						
						<div class="form-group subquestion-hasalternatives">
							<hr/>
							<h4>
								<fmt:message key="mapstudy.extraction.subquestions.alternatives" />
								<a class="btn btn-default u-btn-pull-right" id="btn-add-alternative-subquestion-extraction" href="#"><fmt:message key="mapstudy.extraction.subquestions.alternatives.add" /></a>
							</h4>
							<div id="form-add-subquestion-extraction-alternatives">
							</div>
						</div>
					
						<div class="form-group">	
							<hr/>
							<button type="submit" class="btn btn-primary pull-right" id="btn-save-subquestion-extraction"><fmt:message key="salve" /></button>
						</div>	
					</form>
				</div>
				
			</div>
		</div>
</div>

<!-- Objetivos -->
<div  id="divgoals" class="hide">
	<div class="panel panel-default">
		<div class="panel-heading"><b><fmt:message key="mapstudy.goals" /></b></div>
		<div class="panel-body">
			<h4><fmt:message key="mapstudy.goals.add" /></h4>
			<div class="row">
				<div class="col-md-7">
					<form action="${linkTo[MapStudyController].addgoals}" method="post" id="formGoals">
						<input type="hidden" name="id" id="id" value="${map.id}" />
						<div class="form-group">
							<textarea rows="8" class="form-control" id="goal" name="goals" placeholder="<fmt:message key="mapstudy.goals"/>"></textarea>
						</div>
						<div class="form-group">
							<button type="submit" style="width: 100%;" class="btn btn-large btn-primary"><fmt:message key="add" /></button>
						</div>
					</form>
				</div>
				<div class="col-md-5">
					<div class="panel panel-default">
						<div class="panel-body">
							<p class="text-justify"><strong>Objetivos:</strong> o pesquisador deve especificar o propósito da realização do Mapeamento Sistemático. 
							Exemplo: em [Braga et al. 2015], o objetivo é identificar as ferramentas que apoiam pesquisadores e profissionais 
							no desenvolvimento de sistemas baseados em Inteligência Computacional.</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<c:if test="${map.goals != null}">				
						<h4><fmt:message key="mapstudy.goals"/></h4><hr/>
						<p><span>${map.goals}</span></p>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Questões de Pesquisa -->
<div  id="divquestion" class="hide">
	<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.research.question" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<h4>
					<fmt:message key="mapstudy.research.question.add" />
				</h4>
				<form action="${linkTo[MapStudyController].addquestion}"
					method="post" id="formQuestion">
					<input type="hidden" name="id" id="id" value="${map.id}" />
					<div class="form-group">
						<div class="col-lg-9 padding-left-none">
							<input type="text" class="form-control" id="questiondescription" name="description"
								placeholder="<fmt:message key="mapstudy.research.question.description"/>" />
						</div>
						<button type="submit" id="buttonquestion" class="btn buttonquestion btn-large btn-primary col-lg-3 float-right">
							<fmt:message key="add" />
						</button>
						<div class="clear-both"></div>
					</div>
				</form>

				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th><fmt:message key="mapstudy.research.question" /></th>
								<th><fmt:message key="actions" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="quest" items="${map.researchQuestions}" varStatus="s">
								<tr>
									<td>${quest.description}</td>
									<td class="text-center">
										<a class="btn btn-danger btn-sm confirmation-modal" data-conf-modal-body="<fmt:message key="mapstudy.research.question.excluir.message" />" style="width: 90%" href="${linkTo[MapStudyController].removequestion(map.id, quest.id)}"><i class="glyphicon glyphicon-remove"></i> Remover</a>
<%-- 										<a class="btn btn-primary" href="${linkTo[MapStudyController].editquestion(map.id, quest.id)}"><i class="glyphicon glyphicon-edit"></i></a> --%>
									</td>
								</tr>								
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>

</div>

<!-- String de Busca -->
<div  id="divstring" class="hide">
	<div class="panel panel-default">
		<div class="panel-heading">
			<b><fmt:message key="mapstudy.search.string" /></b>
		</div>
		<div class="panel-body">
			<h4><fmt:message key="mapstudy.search.string.add" /></h4>
			<div class="row">
				<div class="col-md-7">
					<form action="${linkTo[MapStudyController].addstring}" method="post" id="formString">
						<input type="hidden" name="id" id="id" value="${map.id}" />
						<div class="form-group">
								<select class="form-control" name="source">
									<c:forEach var="source" items="${sources}">
										<option value="${source}">${source.description}</option>
									</c:forEach>
								</select>
						</div>
						<div class="form-group">
							<textarea rows="8" class="form-control" id="stringdescription" name="string" placeholder="<fmt:message key="mapstudy.search.string.description"/>"></textarea>
						</div>
						<div class="form-group">
							<button style="width: 100%;" type="submit" id="buttonquestion" class="btn buttonquestion btn-primary"><fmt:message key="add" /></button>
						</div>
					</form>
				</div>
				<div class="col-md-5">
					<div class="panel panel-default">
						<div class="panel-body">
							<p class="text-justify">A <strong>String de Busca</strong> representa os termos que serão aplicados nas bases de dados para 
							a busca de estudos primários. É importante que seja realizada uma pesquisa piloto para verificar se os 
							termos estão retornando resultados esperados. O pesquisador deve definir um conjunto de trabalhos para 
							validar e ajustar a string durante a fase de planejamento para evitar ameaças à validade do Mapeamento.</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
 				<div class="col-md-12">
					<h4><fmt:message key="mapstudy.search.string.list"/></h4><hr/>
					<c:forEach var="search" items="${map.searchString}" varStatus="s">
<%-- 						<p><strong><fmt:message key="mapstudy.search.string.source"/>:</strong> <span>${search.source.description}</span></p> --%>
<%-- 						<p><strong><fmt:message key="mapstudy.search.string"/>:</strong> <span>${search.description}</span></p><hr/> --%>
<!-- 						Exibir as strings -->
						<div class="widget widget_tally_box">
							<div class="x_panel" style="height: auto;" graph="0">
								<div class="x_title">
									<b>${search.source.description}</b>
									  <ul class="nav navbar-right panel_toolbox">
									    <li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a></li>
									    <li><a class="close-link"><i class="fa fa-close"></i></a></li>
									  </ul>
								  <div class="clearfix"></div>
								</div>
								<div class="x_content" style="display: none;">	
									<p class="search-description" style="text-align: justify !important;">	
										${search.description} 
									</p>
								</div>
							</div>			
						</div>					
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- Criterios de inclusão e exclusão -->
<div  id="divcriterias" class="hide">
	<div class="panel panel-default">
		<div class="panel-heading"><b><fmt:message key="mapstudy.inclusion.criterias" /></b></div>
			<div class="panel-body">
				<h4><fmt:message key="mapstudy.inclusion.criteria.add" /></h4>
				<form action="${linkTo[MapStudyController].addinclusion}" method="post" id="formInclusion">
					<input type="hidden" name="id" id="id" value="${map.id}" />
					<div class="form-group">
						<div class="col-lg-9 padding-left-none">
							<input type="text" class="form-control" id="incdescription" name="description" placeholder="<fmt:message key="mapstudy.inclusion.criteria"/>" />
						</div>
						<button type="submit" id="buttoninclusion" class="btn buttoninclusion btn-large btn-primary col-lg-3 float-right">
							<fmt:message key="add" />
						</button>
						<div class="clear-both"></div>
					</div>
				</form>

				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th style="width: 80%"><fmt:message key="mapstudy.criteria.description" /></th>
								<th style="width: 20%" class="text-center"><fmt:message key="actions" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="criteria" items="${map.inclusionCriterias}"
								varStatus="s">
								<tr>
									<td>${criteria.description}</td>
									<td class="text-center">
										<a class="btn btn-danger btn-sm" style="width: 90%;" href="${linkTo[MapStudyController].removeinclusioncriteriapage(map.id, criteria.id)}"><i class="glyphicon glyphicon-remove"></i> Remover</a>
<%-- 										<a class="btn btn-primary" href="${linkTo[MapStudyController].editinclusioncriteria(map.id, criteria.id)}"><i class="glyphicon glyphicon-edit"></i></a> --%>
									</td>
								
								</tr>								
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.exclusion.criterias" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<h4>
					<fmt:message key="mapstudy.exclusion.criteria.add" />
				</h4>
				<form action="${linkTo[MapStudyController].addexclusion}"
					method="post" id="formExclusion">
					<input type="hidden" name="id" id="id" value="${map.id}" />
					<div class="form-group">
						<div class="col-lg-9 padding-left-none">
							<input type="text" class="form-control" id="excdescription"	name="description"
								placeholder="<fmt:message key="mapstudy.exclusion.criteria"/>" />
						</div>
						<button type="submit" id="buttonexclusion" class="btn buttonexclusion btn-large btn-primary col-lg-3 float-right">
							<fmt:message key="add" />
						</button>
						<div class="clear-both"></div>
					</div>
				</form>

				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th style="width: 80%"><fmt:message key="mapstudy.criteria.description" /></th>
								<th style="width: 20%" class="text-center"><fmt:message key="actions" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="criteria" items="${map.exclusionCriterias}"
								varStatus="s">
								<tr>
									<td>${criteria.description}</td>
									<td class="text-center">
										<a class="btn btn-danger btn-sm" style="width: 90%;" href="${linkTo[MapStudyController].removeexclusioncriteriapage(map.id, criteria.id)}"><i class="glyphicon glyphicon-remove"></i> Remover</a>
<%-- 										<a class="btn btn-primary" href="${linkTo[MapStudyController].editexclusioncriteria(map.id, criteria.id)}"><i class="glyphicon glyphicon-edit"></i></a> --%>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
</div>

<!-- Configuração de Seleção Semiautomática -->
<div id="divautomaticselection" class="hide">
	<form action="#" method="post" id="form-add-algorithm">
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.automaticselection" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<input type="hidden" name="mapid" id="mapid" value="${map.id}" />
				<div class="form-group">
					<div class="row">
						<div class="col-sm-12">
							<label for="algorith-type" class=""><fmt:message
									key="mapstudy.learningConfiguration.algorithm" /></label> 
							<select	class="form-control selection-algorithm" name="algorithm" id="algorith-type">
							<c:choose>
								<c:when test="${learningConfiguration.algorithm != null}">
								<option value="-1">Selecione um algoritmo</option>
<%-- 									<option selected value="${learningConfiguration.algorithm}">${learningConfiguration.algorithm.name}</option> --%>
								</c:when>
								<c:otherwise>
									<option selected value="-1">Selecione um algoritmo</option>
								</c:otherwise>
							</c:choose>
							<c:forEach var="a" items="${algorithms}">
								<c:choose>
									<c:when test="${learningConfiguration.algorithm eq a}">
										<option selected value="${a}">${a.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${a}">${a.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<label for="numberArtclesValidation" class=""><fmt:message
									key="mapstudy.learningConfiguration.numberArtclesValidation" /></label><br />
							<input type="number" name="learningConfiguration.numberArtclesValidation"
								id="numberArtclesValidation"
								value="${learningConfiguration.numberArtclesValidation}" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<input	type="checkbox" id="useResearcher" name="learningConfiguration.useResearcher"	value="${learningConfiguration.useResearcher}" ${learningConfiguration.useResearcher ? 'checked="checked"' : '' }/> <fmt:message	key="mapstudy.learningConfiguration.useResearcher" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<input type="checkbox" id="showSelection" name="learningConfiguration.showSelection" value="${learningConfiguration.showSelection}" ${learningConfiguration.showSelection ? 'checked="checked"' : '' } /> <fmt:message key="mapstudy.learningConfiguration.showSelection" />
						</div>
					</div>
					<div class="row">
						<button type="submit" id="button-save-algorithm" class="btn btn-large btn-primary" style="margin-left: 2%;">
							<fmt:message key="add" />
						</button>
					</div>
					<div class="clear-both"></div>
				</div>
			</div>
			</div>

		
<!-- 		Painel de informações do algoritmo selecionado -->
		<div class="panel panel-default ${learningConfiguration.algorithm != null ? '' : 'hide'}" id="panel-stats-algorithm">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.learningStats" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
			<input type="hidden" name="algorithmSelected" id="algorithmSelected" value="${learningStats.algorithm}" />
				<p>
					<strong><fmt:message key="mapstudy.learningConfiguration.algorithm" />:</strong> 
					<span id="algorithmName">${learningStats.algorithm.name}</span>
				<p>
				<p>
					<strong><fmt:message key="mapstudy.learningStats.numberarticles" />:</strong> 
					<span id="numberarticles">${learningStats.numberArticles}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.numberArticlesAccepted" />:</strong> 
					<span id="numberArticlesAccepted">${learningStats.numberArticlesAccepted}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.numberArticlesRejected" />:</strong> 
					<span id="numberArticlesRejected">${learningStats.numberArticlesRejected}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.wss" />:</strong> 
					<span id="wss">${learningStats.wss}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.recall" />:</strong> 
					<span id="recall">${learningStats.recall}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.accuracy" />:</strong> 
					<span id="accuracy">${learningStats.accuracy}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.error" />:</strong> 
					<span id="error">${learningStats.error}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.rocArea" />:</strong> 
					<span id="rocArea">${learningStats.rocArea}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.fMeasure" />:</strong> 
					<span id="fMeasure">${learningStats.fMeasure}</span>
				<p>
				
				<p>
					<strong><fmt:message key="mapstudy.learningStats.numberArticlesTraining" />:</strong> 
					<span id="numberArticlesTraining">${learningStats.numberArticlesTraining}</span>
				<p>
			</div>
		</div>

	</form>
</div>