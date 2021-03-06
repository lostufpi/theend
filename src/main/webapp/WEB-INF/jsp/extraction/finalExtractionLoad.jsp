<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	$(document).ready(function(){
		var selectAlternative = function(){
			$('.alternative_list_id').each(function(idx){
//	 			console.log('ele ', $(this));
				var qid = $(this).attr('id');
				var i = qid.lastIndexOf('_');
				qid = qid.slice(i + 1, qid.length);		

				var aid = $(this).val();

				var value = $('#alternative_list_value_' + qid).val();

				$("#alternative_id_" + qid).val(aid);	
// 				var test = $('#select2-alternative_id_' + qid + '-container');
// 				select2-alternative_id_0
// 				select2-alternative_id_2
				var test = $('#select2-alternative_id_' + qid);
				test.attr('title', value);
				test.html(value);
			});

		};

		selectAlternative();
			
	});
</script>

<ol class="breadcrumb u-margin-top">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].list}"><fmt:message key="mapstudy.short.list"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(mapStudy.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li><a href="${linkTo[ExtractionController].showExtractionEvaluates(mapStudy.id)}"><fmt:message key="mapstudy.viewextractions"/></a></li>
  <li class="active"><fmt:message key="mapstudy.extractions.compare"/></li>
</ol>

<h3 class="color-primary">
	<fmt:message key="mapstudy.extractions.compare"/> - ${mapStudy.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[ExtractionController].showExtractionEvaluates(mapStudy.id)}"><fmt:message key="button.back"/></a>
</h3>

<div class="row">
  	<div class="col-lg-12">
  		<div class="panel panel-default">
<!-- 		<div class="panel-heading">
			<b><fmt:message key="mapstudy.articles.list" /></b>
			</div> -->
		<div class="panel-body">
  		<h4>
			<fmt:message key="mapstudy.extractions.all"/>
		</h4>
		
		<p> 
			<strong>
				<fmt:message key="mapstudy.article.title"/>:
			</strong> <span id="articleReadTitle">${extractionCompareVO.article.title}</span>
		<p>
		
		<form id="formExtractionFinal" action="${linkTo[ExtractionController].finalExtraction}" method="post">
			<input type="hidden" id="mapid" name="mapid" value="${mapStudy.id}" />
			<input type="hidden" id="articleid" name="articleid" value="${extractionCompareVO.article.id}" />	
		<div class="dataTable_wrapper">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
<!-- 						<th class="text-center">ID</th> -->
						<th style="width: 40%" class="">Quest&otilde;es</th>
						<th style="width: 60%" class="">Extra&ccedil;&otilde;es</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="ext" items="${extractionCompareVO.extractions}" varStatus="s">
						<input type="hidden" name="questions[${s.index}].questionId" value="${ext.question.id}"/>
						<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA article-to-read">
							<td class="question-name">${ext.question.name}</td>
							<td class="alternatives-values">
								<c:if test="${ext.question.type == 'MULT'}">
									<c:forEach var="alt" items="${ext.userAndAlternatives}" varStatus="a">
										<c:set var="containsExc" value="false" />
										<c:forEach var="done" items="${extractionCompareVO.article.alternativeFinal(ext.question)}">
											<c:if test="${alt.alternative.id eq done.id}">
												<c:set var="containsExc" value="true" />
											</c:if>
										</c:forEach>
									
										<div class="checkbox">
											<input class="questionMult" name="questions[${s.index}].alternatives[${a.index}]" type="checkbox" value="${alt.alternative.id}" ${containsExc ? 'checked="checked"' : '' } alt-value="${alt.alternative.value}" />${alt.alternative.value} 
										</div>
									</c:forEach>
								</c:if>
							
								<c:if test="${ext.question.type != 'MULT'}">
									<select data-placeholder="<fmt:message key="mapstudy.alternatives.choose" />" class="form-control select2-alternative-compare group_alternative_id" name="questions[${s.index}].alternatives[0]" id="alternative_id_${s.index}" tabindex="2">
										<c:forEach var="alt" items="${ext.userAndAlternatives}" varStatus="myAlt">
											<option value="${alt.alternative.id}" data-email="${alt.user.name}">${alt.alternative.value}</option>
										</c:forEach>
									</select>	
									
									<c:if test="${not empty extractionCompareVO.article.alternativeFinal(ext.question)}">
										<input type="hidden" value="${extractionCompareVO.article.alternativeFinal(ext.question).get(0).id}" class="alternative_list_id" id="alternative_list_id_${s.index}"/>
										<input type="hidden" value="${extractionCompareVO.article.alternativeFinal(ext.question).get(0).value}" class="alternative_list_value" id="alternative_list_value_${s.index}"/>
									</c:if>															
								</c:if>							
							</td>						
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
		<div class="row">
			<div class="col-md-12">
				<strong><fmt:message key="mapstudy.article.comments"/>:</strong><br/>
				<c:choose>
				    <c:when test="${extractionCompareVO.article.comments != null && not empty extractionCompareVO.article.comments}">
						<c:forEach var="comment" items="${extractionCompareVO.article.comments}" varStatus="count">
							<strong>${comment.user.name}:</strong><br/>	
								<p>
									${comment.value}
								</p>
								<hr>
						</c:forEach>			
				    </c:when>    
				    <c:otherwise>
				        <p>
							Sem coment&aacute;rios.
						</p>
				    </c:otherwise>
				</c:choose>
			</div>
		</div>
		
		<button type="submit" id="submit" class="btn btn-large btn-primary">
			<fmt:message key="salve" />
		</button>
	</form>

	</div>
	</div>

	<div class="panel panel-default">
<!-- 		<div class="panel-heading">
			<b><fmt:message key="mapstudy.articles.list" /></b>
			</div> -->
		<div class="panel-body">

<!--		Articles to extraction final -->

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
					<c:forEach var="art" items="${articlesToCompare}" varStatus="s">
						<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
							<td>${art.id}</td>
							<td><a class="readArticle" actualid="${art.id}" nextid="${art.id }" href="${linkTo[ExtractionController].finalExtractionLoad(mapStudy.id, art.id)}">${art.title}</a></td>
							<td>${art.sourceView(art.source)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
				
		<hr/>	


<!--		Articles extraction final -->
			<h4>
					<fmt:message key="mapstudy.articles.list.evaluated"/>
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
							<c:forEach var="eval" items="${articlesFinalExtracted}" varStatus="a">
								<tr class="${a.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${eval.id}</td>
									<td><a class="readArticle" actualid="${eval.id}" href="${linkTo[ExtractionController].finalExtractionLoad(mapStudy.id, eval.id)}">${eval.title}</a></td>
									<td>${eval.sourceView(eval.source)}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

		</div>
		
		</div>


<!--  fim col-lg-12 -->
  	</div> 
  	<!-- fim row -->
</div>  	
