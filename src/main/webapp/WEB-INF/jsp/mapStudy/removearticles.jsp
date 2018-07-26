<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ol class="breadcrumb u-margin-top">
	<li><a href="<c:url value="/" />"><fmt:message key="home" /></a></li>
	<li><a href="${linkTo[MapStudyController].list}"><fmt:message
				key="mapstudy.short.list" /></a></li>
	<li><a href="${linkTo[MapStudyController].show(map.id)}"><fmt:message
				key="mapstudy.details" /></a></li>
	<li><a href="${linkTo[MapStudyController].identification(map.id)}"><fmt:message
				key="mapstudy.searching" /></a></li>
	<li class="active"><fmt:message key="mapstudy.articles.list" /></li>
</ol>

<h3 class="color-primary">
	<fmt:message key="mapstudy.articles.list" />
	<a id="return" class="btn btn-default pull-right"
		href="${linkTo[MapStudyController].identification(map.id)}"><fmt:message
			key="button.back" /></a>
</h3>

<br />

<c:if test="${not empty articles}">

	<form action="${linkTo[MapStudyController].removearticlesform}"
		method="post" id="formRemoveArticles">
		<table
			class="table table-striped table-bordered table-hover personalized-table-simple">
			<thead>
				<tr>
					<th width="2%" class="text-center"><fmt:message key="remove" />
					<th width="2%" class="text-center"><fmt:message key="id" />
					<th width="80%" class="text-center"><fmt:message
							key="mapstudy.article" />
					<th width="16%" class="text-center"><fmt:message
							key="mapstudy.article.source" />
				</tr>
			</thead>
			<tbody>
				<c:forEach var="article" items="${articles}" varStatus="s">
					<tr>
						<td>
							<div class="text-center">
								<input type="checkbox" name="articlesIds[${s.index}]"
									value="${article.id}"></input>
							</div>
						</td>
						<td>
							<div class="text-center">${article.id}</div>
						</td>
						<td>${article.title}</td>
						<td>${article.sourceView(article.source)}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input type="hidden" name="mapId" id="mapId" value="${map.id}" />
		<button type="submit" class="btn btn-danger pull-rigth">
			<i class="glyphicon glyphicon-remove"></i>
			<fmt:message key="remove.selected" />
		</button>
				
		<a class="btn btn-danger pull-rigth" href="${linkTo[MapStudyController].removeAllArticles(map.id)}"><i class="glyphicon glyphicon-remove"></i>
			<fmt:message key="remove.all.selected" /></a>
	</form>
</c:if>
