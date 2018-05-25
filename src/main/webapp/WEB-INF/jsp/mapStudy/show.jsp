<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(document).ready(function() {
		$('.progress-bar').each(function(idx) {
			var percent = $(this).attr("style");
			$(this).attr('style', percent.replace(",", "."));
		});
	});
</script>

<ol class="breadcrumb u-margin-top" style="margin-top: 0px;">
	<li><a href="<c:url value="/" />"><fmt:message key="home" /></a></li>
	<li><a href="${linkTo[MapStudyController].list}"><fmt:message
				key="mapstudy.short.list" /></a></li>
	<li class="active"><fmt:message key="mapstudy.details" /></li>
</ol>

<h3 class="color-primary">
	${map.title} <a id="return" class="btn btn-default pull-right"
		href="${linkTo[MapStudyController].list}"><fmt:message
			key="button.back" /></a>
</h3>
<hr />

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.details" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="row">
					<div class="col-md-10">
						<p>
							<strong><fmt:message key="mapstudy.title" />:</strong>
							${map.title}
						</p>
						<p>
							<strong><fmt:message key="mapstudy.description" />:</strong>
							${map.description}
						</p>
					</div>

					<div class="col-md-2">
						<a class="btn btn-primary pull-right"
							href="${linkTo[MapStudyController].edit(map.id)}"><i
							class="glyphicon glyphicon-pencil"></i> <fmt:message key="edit" /></a>
					</div>
				</div>

				<hr />
				<div class="row btn-group-toggle" role="group">
					<div class="col-md-12">
						<div class="row">
							<!-- 						Primeiro Botão  -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[MapStudyController].planning(map.id)}'">
								<span class="glyphicon glyphicon-pencil"></span>
								<fmt:message key="mapstudy.planning" />
							</button>
							<span class="glyphicon glyphicon-arrow-right"></span>
							<!-- 						Segundo Botão -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[MapStudyController].identification(map.id)}'">
								<span class="glyphicon glyphicon-search"></span>
								<fmt:message key="mapstudy.searching" />
							</button>
							<span class="glyphicon glyphicon-arrow-right"></span>

							<!-- 						Terceiro Botão -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[MapStudyController].evaluate(map.id)}'">
								<span class="glyphicon glyphicon-ok"></span>
								<fmt:message key="mapstudy.screening" />
							</button>
							<span class="glyphicon glyphicon-arrow-right"></span>
						</div>
						<div class="row">
							<!-- 						Quarto Botão -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[MapStudyController].showEvaluates(map.id)}'">
								<span class="glyphicon glyphicon-list-alt"></span>
								<fmt:message key="mapstudy.viewarticles" />
							</button>
							<!-- 						<span class="glyphicon glyphicon-arrow-right"></span> -->
							<!-- 						QUINTO Botão -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[ExtractionController].extraction(map.id)}'">
								<span class="glyphicon glyphicon-cog"></span>
								<fmt:message key="mapstudy.extraction" />
							</button>
							<span class="glyphicon glyphicon-arrow-right"></span>

							<!-- 						Sexto Botão -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[ExtractionController].showExtractionEvaluates(map.id)}'">
								<span class="glyphicon glyphicon-th-list"></span>
								<fmt:message key="mapstudy.viewextractions" />
							</button>
							<span class="glyphicon glyphicon-arrow-right"></span>

							<!-- 						Setimo Botão -->
							<button type="button" class="btn btn-default"
								style="margin-bottom: 1%;"
								onclick="location.href='${linkTo[MapStudyController].report(map.id)}'">
								<span class="fa fa-pie-chart"></span>
								<fmt:message key="mapstudy.report" />
							</button>
						</div>

					</div>
				</div>


				<hr />

				<div class="row">
					<div class="col-md-6">
						<p>
							<strong><fmt:message
									key="mapstudy.evaluation.selection.rate" />:</strong>
						</p>
						<div class="progress">
							<div class="progress-bar progress-bar-striped active"
								role="progressbar" aria-valuenow="${percentEvaluated}"
								aria-valuemin="0" aria-valuemax="100"
								style="min-width: 3em; width: ${percentEvaluated}%">
								${percentEvaluated}%</div>
						</div>
					</div>
					<div class="col-md-6">
						<p>
							<strong><fmt:message key="mapstudy.extraction.rate" />:</strong>
						</p>
						<div class="progress">
							<div class="progress-bar progress-bar-striped active"
								role="progressbar" aria-valuenow="${percentExtracted}"
								aria-valuemin="0" aria-valuemax="100"
								style="min-width: 3em; width: ${percentExtracted}%">
								${percentExtracted}%</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<b><fmt:message key="mapstudy.members" /></b>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<h4>
					<fmt:message key="mapstudy.members.add" />
				</h4>
				<form action="${linkTo[MapStudyController].addmember}" method="post">
					<input type="hidden" name="id" value="${map.id}" />

					<div class="form-group">
						<div class="col-md-6 padding-left-none">
							<select
								data-placeholder="<fmt:message key="mapstudy.members.choose" />"
								class="form-control select2" name="userId" tabindex="2">
								<c:forEach var="member" items="${mapStudyArentUsers}">
									<option value="${member.id}" data-email="${member.email}">${member.name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4">
							<select class="form-control" name="role">
								<c:forEach var="role" items="${roles}">
									<option value="${role}">${role.description}</option>
								</c:forEach>
							</select>
						</div>
						<button type="submit" id="submit"
							class="btn btn-large btn-primary col-md-2 float-right">
							<i class="glyphicon glyphicon-user"></i>
							<fmt:message key="add" />
						</button>
						<div class="clear-both"></div>
					</div>
					<div class="checkbox">
						<label> <input type="checkbox" name="notify"> <fmt:message
								key="notify.member" />
						</label>
					</div>
				</form>

				<div class="table-responsive">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th class="text-center">#</th>
								<th>Participante</th>
								<th>Função</th>
								<th class="text-center"><fmt:message
										key="mapstudy.evaluations.percentconclusion" /></th>
								<th class="text-center"><fmt:message
										key="mapstudy.extraction.percentconclusion" /></th>

								<c:choose>
									<c:when test="${map.isCreator(userInfo.user)}">
										<th class="text-center"><fmt:message key="remove" /></th>
									</c:when>
									<c:otherwise>
										<th class="text-center"><fmt:message key="exit" /></th>
									</c:otherwise>
								</c:choose>

							</tr>
						</thead>
						<tbody>
							<c:forEach items="${mapStudyUsers}" var="member" varStatus="s">
								<tr>
									<td class="text-center">${s.index + 1}</td>
									<td>${member.key.name}</td>
									<td>${map.role(member.key).description}</td>
									<td class="text-center">${member.value.selection}</td>
									<td class="text-center">${member.value.extraction}</td>
									<c:choose>
										<c:when test="${map.isCreator(userInfo.user)}">
											<c:choose>
												<c:when test="${!map.isCreator(member.key)}">
													<td class="text-center"><a
														class="btn btn-danger confirmation-modal"
														data-conf-modal-body="<fmt:message key="member.excluir.message" />"
														href="${linkTo[MapStudyController].removemember(map.id, member.key.id)}"><i
															class="glyphicon glyphicon-remove"></i></a></td>
												</c:when>
												<c:otherwise>
													<td></td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>

											<c:choose>
												<c:when test="${member.key.id eq userInfo.user.id}">
													<td class="text-center"><a
														class="btn btn-danger confirmation-modal"
														data-conf-modal-body="<fmt:message key="member.excluir.message" />"
														href="${linkTo[MapStudyController].exit(map.id, member.key.id)}"><i
															class="glyphicon glyphicon-remove"></i></a></td>
												</c:when>
												<c:otherwise>
													<td></td>
												</c:otherwise>
											</c:choose>

										</c:otherwise>
									</c:choose>

								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
