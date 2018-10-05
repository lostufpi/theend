<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(mapStudy.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li><a href="${linkTo[MapStudyController].showEvaluates(mapStudy.id)}"><fmt:message key="mapstudy.viewarticles"/></a></li>
  <li class="active"><fmt:message key="mapstudy.evaluations.compare"/></li>
</ol>


<h3 class="color-primary">
	${mapStudy.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].showEvaluates(mapStudy.id)}"><fmt:message key="button.back"/></a>
</h3>
<hr/>

<div class="row">
  	<div class="col-lg-12">
  		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.details"/></b>
			</div>
			<div class="panel-body">
				<p><strong><fmt:message key="mapstudy.title"/>:</strong> ${mapStudy.title}</p>
				<hr/>
				<p><strong><fmt:message key="mapstudy.evaluations.fleisskappa"/>:</strong> 
				<span id="kappa-members"><fmt:message key="mapstudy.evaluations.fleisskappa.all"/></span> = <span id="kappa-value">${kappa}</span></p>
				<p><strong><fmt:message key="mapstudy.evaluations.fleisskappa.calculate"/>:</strong>
					<form id="kappa-form">
						<input type="hidden" id="mapStudyId" name="mapStudyId" value="${mapStudy.id}" />
						<c:forEach var="member" items="${members}">
							<div class="checkbox">
							  <label>
							    <input type="checkbox" name="members[${u.index}]" value="${member.id}" />
							    ${member.name}
							  </label>
							</div>
						</c:forEach>
						<div class="btn-group">
						<a class="btn btn-default" id="kappa-submit" href="#"><fmt:message key="mapstudy.evaluations.fleisskappa.calculate"/></a>
						</div>
					</form>
					
					<div class="clear-both"></div>
				</p>
			</div>
		</div>
  		
		<h4><fmt:message key="mapstudy.evaluations.all"/></h4>
		<div class="dataTable_wrapper">
			<table class="table table-striped table-bordered table-hover personalized-table">
				<thead>
					<tr>
						<th class="text-center">ID</th>
						<c:forEach var="m" items="${members}" varStatus="s">
							<th class="text-center">${m.login}</th>
						</c:forEach>
						<th class="text-center"><fmt:message key="mapstudy.evaluations.article.final.evaluate" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="acvo" items="${articles}" varStatus="s">
						<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA text-center article-to-read">
							<td class="article-id">${acvo.article.id}</td>
							<c:forEach var="u" items="${members}" varStatus="s">
								<td class="${acvo.getEvaluationClassification(u) == 'ACCEPTED' ? 'success-eval' : (acvo.getEvaluationClassification(u) == 'REJECTED' ? 'error-eval' : 'no-eval')}">
									${acvo.getEvaluationClassification(u).description}
								</td>
							</c:forEach>
							<td class="${acvo.article.showFinalEvaluation() == 'ACCEPTED' ? 'success-eval' : (acvo.article.showFinalEvaluation() == 'REJECTED' ? 'error-eval' : 'no-eval')}">${acvo.article.showFinalEvaluation().description}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
		<p>
		
		<form action="${linkTo[MapStudyController].equalSelections}" method="post">
			<input type="hidden" name="mapStudyId" value="${mapStudy.id}" />
			<button type="submit" id="submit" class="btn btn-primary float-right">
				<fmt:message key="mapstudy.articles.equalselections"/>
			</button>
		</form>
		
		<hr/>
		
		<h4>
			<fmt:message key="mapstudy.evaluations.article.final.evaluate" />
		</h4>
		
		<div id="article-popup" style="display:none">
			<div class="panel panel-default">
				<div class="panel-heading">
					<b><fmt:message key="mapstudy.article.details"/> <span id="article-id"></span></b>
					<a href="#" class="btn btn-default pull-right article-close">
						<i class="glyphicon glyphicon-remove"></i>
					</a>
				</div>
				<div class="panel-body">
					<p> 
						<strong>
							<fmt:message key="mapstudy.article.title"/>:
						</strong>
						<span id="article-title"></span>
					</p>
					<hr/>
					<p>
						<strong>
							<fmt:message key="mapstudy.article.abstract"/>:
						</strong>
						<span id="article-abstract"></span>
					</p>
					<div id="article-evaluations"></div>
				</div>
			</div>
		</div>
		
		<form action="${linkTo[MapStudyController].finalEvaluate}" method="post">
			<input type="hidden" name="mapStudyId" value="${mapStudy.id}" />
			
			<div class="form-group">
				<div class="col-lg-4">
					<input required="true" type="text" class="form-control" id="articleId" name="articleId" placeholder="<fmt:message key="mapstudy.evaluations.articleid" />"/>
				</div>
				
				<div class="col-lg-6">
					<select class="form-control" name="evaluation">
						<c:forEach var="es" items="${evaluationStatus}">
							<option value="${es}">${es.description}</option>
						</c:forEach>
					</select>
				</div>
				
				<button type="submit" id="submit" class="col-lg-2 btn btn-large btn-primary float-right">
					<fmt:message key="mapstudy.evaluations.article.evaluate"/>
				</button>
				<div class="clear-both"></div>
			</div>
		</form>
		
		<hr/>
		
		<h4>
			<fmt:message key="mapstudy.evaluations.accepted"/>
		</h4>
		
		<div class="dataTable_wrapper">
			<table
				class="table table-striped table-bordered table-hover personalized-table">
				<thead>
					<tr>
						<th class="text-center">ID</th>
						<c:forEach var="m" items="${members}" varStatus="s">
							<th class="text-center">${m.login}</th>
						</c:forEach>
						<th class="text-center"><fmt:message key="mapstudy.evaluations.article.final.evaluate" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="acvo" items="${articlesAccepted}" varStatus="s">
						<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA text-center article-to-read">
							<td class="article-id">${acvo.article.id}</td>
							<c:forEach var="u" items="${members}" varStatus="s">
								<td title="${u.login} - ${acvo.getEvaluationClassification(u).description}" class="${acvo.getEvaluationClassification(u) == 'ACCEPTED' ? 'success-eval' : (acvo.getEvaluationClassification(u) == 'REJECTED' ? 'error-eval' : 'no-eval')}">
									${acvo.getEvaluationClassification(u).description}
								</td>
							</c:forEach>
							<td class="${acvo.article.showFinalEvaluation() == 'ACCEPTED' ? 'success-eval' : (acvo.article.showFinalEvaluation() == 'REJECTED' ? 'error-eval' : 'no-eval')}">${acvo.article.showFinalEvaluation().description}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

		<br/>
  		
	</div>
</div>


<script>
	(function($){
		$(document).ready(function(){
			
			$(document).on('click', '#kappa-submit', function(e){
				e.preventDefault();
				
				var ids = "";
				var count= 0;
			    $('#kappa-form :checked').each(function() {
// 			       console.log($(this).val());
			       ids += $(this).val() + ";";
			       count ++;
			    });

			    if (ids == null || ids === ""){
					alert('Nenhum usuario foi selecionado !');
					return;
				}
			    
			    var formData = {
					usersIds : ids,
					mapStudyId : $('#mapStudyId').val()
				}
			    
			    var url = "${linkTo[MapStudyController].calcKappa}";
			    
			    if (count > 1){
					$.ajax({
						data: formData,
						url: url,
						method: "POST",
						success: function(data){
// 							console.log(data);
							if(data.hashMap){
								$('#kappa-members').html(data.hashMap.members);
								$('#kappa-value').html(data.hashMap.kappa);
							}
						},
						error: function(err){
							console.log(err);
						}
					});			    	
			    }
			    
			});
			
			$(document).on('click', '.article-to-read', function(e){
				e.preventDefault();
				var id = $(this).children('.article-id').html();
// 				console.log('ID: ', id);
				var url = "${linkTo[MapStudyController].articleDetail(1)}";
				url = url.replace("1", id);

// 				console.log('URL: ', url);
				
				$.ajax({
					url: url,
					method: "GET",
					success: function(data){
// 						console.log(data);
						if(data.hashMap){
							var x = data.hashMap;
							$('#article-id').html(x.id);
							$('#articleId').val(x.id);
							$('#article-title').html(x.title);
							$('#article-abstract').html(x['abstract']);
							
							var evals = "";
							for(var user in x.evaluations){
								var criterias = x.evaluations[user].criterias;
								var comment = x.evaluations[user].comment;
								
								evals += '<hr/><p><strong>'+user+':</strong><ul>';
								for(var c in criterias){
									evals += '<li><span>'+criterias[c]+'</span></li>';
								}
								evals += '</ul></p>';
								
								if(comment){
									evals += '<p><b>Comment</b>: '+comment+'</p>';
								}
							}
							
							$('#article-evaluations').html(evals);
							
							$('#article-popup').fadeIn();
							
							$('html,body').stop().animate({scrollTop: $('#article-popup').offset().top}, 2000);
						}
					},
					error: function(err){
						console.log(err);
					}
				});
			});
			
			$(document).on('click', '.article-close', function(e){
				e.preventDefault();
				$("#article-popup").fadeOut();
			})
			
		});
	})(jQuery);
</script>