<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb u-margin-top">
  <li><a href="<c:url value="/" />"><fmt:message key="home"/></a></li>
  <li><a href="${linkTo[MapStudyController].show(mapStudy.id)}"><fmt:message key="mapstudy.details"/></a></li>
  <li class="active"><fmt:message key="mapstudy.viewarticles"/></li>
</ol>

<h3 class="color-primary">
	<fmt:message key="mapstudy.evaluations.results"/> - ${mapStudy.title}
	<a id="return" class="btn btn-default pull-right" href="${linkTo[MapStudyController].show(mapStudy.id)}"><fmt:message key="button.back"/></a>
</h3>

<div class="row">
	<div class="col-lg-12">
	
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.details"/></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<p> 
					<strong>
						<fmt:message key="mapstudy.title"/>:
					</strong> ${mapStudy.title}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.description"/>:
					</strong> ${mapStudy.description}
				<p>
				
				<p>
					<strong>
						<fmt:message key="mapstudy.evaluation.rate"/>:
					</strong> ${percentEvaluated}%
				<p>
				
				<hr/>
				
				<p>
					<strong>
						<fmt:message key="mapstudy.evaluations.compare"/>:
					</strong>
					<c:if test="${percentEvaluatedDouble >= 100}">
						<a class="btn btn-primary" href="${linkTo[MapStudyController].compareEvaluations(mapStudy.id)}"><fmt:message key="mapstudy.evaluations.compare"/></a>
					</c:if>
					<c:if test="${percentEvaluatedDouble < 100}">
						<fmt:message key="mapstudy.evaluations.compare.undone"/>
					</c:if>
					<div class="clear-both"></div>
				<p>
				
				<p>
					<strong>
						<fmt:message key="mapstudy.evaluations.export"/>:
					</strong> 
					<a class="btn btn-default" href="${linkTo[MapStudyController].downloadMine(mapStudy.id)}"><fmt:message key="mapstudy.evaluations.export.csv.mine"/></a>
					<a class="btn btn-default" href="${linkTo[MapStudyController].downloadAll(mapStudy.id)}"><fmt:message key="mapstudy.evaluations.export.csv.all"/></a>
					<div class="clear-both"></div>
				<p>
				 
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.evaluations.results"/></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<p> 
					<strong class="color-primary">
						<fmt:message key="mapstudy.evaluations.count.done"/>:
					</strong> ${countRejected + countAccepted}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.accepted"/>:
					</strong> ${countAccepted}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.rejected"/>:
					</strong> ${countRejected}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.todo"/>:
					</strong> ${countWithoutClassification - countAccepted - countRejected}
				<p>
				
				<hr/>
				
				<p> 
					<strong class="color-primary">
						<fmt:message key="mapstudy.evaluations.count.filtred"/>:
					</strong> ${countClassified}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.repeated"/>:
					</strong> ${countRepeated}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.dontmatch"/>:
					</strong> ${countDontMatch}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.withoutauthors"/>:
					</strong> ${countWithoutAuthors}
				<p>
				
				<p> 
					<strong>
						<fmt:message key="mapstudy.evaluations.count.withoutabstract"/>:
					</strong> ${countWithoutAbstracts}
				<p>
				
				<hr/>
				
				<p> 
					<strong class="color-primary">
						<fmt:message key="mapstudy.evaluations.count.total"/>:
					</strong> ${countClassified+countWithoutClassification}
				<p>
				
				<hr/>
			</div>
		</div>
		
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.articles.list" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="dataTable_wrapper">
					<table
						class="table table-striped table-bordered table-hover personalized-table">
						<thead>
							<tr>
								<th><fmt:message key="id"/></th>
								<th class="text-center"><fmt:message key="mapstudy.article.title" /></th>
								<th class="text-center"><fmt:message key="mapstudy.article.classification" /></th>
								<th><fmt:message key="mapstudy.article.evaluation"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="article" items="${articles}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${article.id}</td>
									<td><a href="${linkTo[MapStudyController].evaluateArticle(mapStudy.id, article.id)}">${article.title}</a></td>
									<td>${article.classification.description}</td>
									<td>${article.getEvaluationClassification(user)}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.evaluations.percent.inclusion" /> - ${countAccepted}</b>
			</div>
			<div class="panel-body">
				<div class="dataTable_wrapper">
					<table
						class="table table-striped table-bordered table-hover personalized-table">
						<thead>
							<tr>
								<th><fmt:message key="mapstudy.inclusion.criteria"/></th>
								<th>Count</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="c" items="${inclusionCriteriasMap}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${c.key.description}</td>
									<td>${c.value}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.evaluations.percent.exclusion" /> - ${countRejected}</b>
			</div>
			<div class="panel-body">
				<div class="dataTable_wrapper">
					<table
						class="table table-striped table-bordered table-hover personalized-table">
						<thead>
							<tr>
								<th><fmt:message key="mapstudy.exclusion.criteria"/></th>
								<th>Count</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="c" items="${exclusionCriteriasMap}" varStatus="s">
								<tr class="${s.index % 2 == 0 ? 'even' : 'odd'} gradeA">
									<td>${c.key.description}</td>
									<td>${c.value}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>