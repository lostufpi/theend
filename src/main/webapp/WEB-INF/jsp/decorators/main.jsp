<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="gravatar"
	uri="http://www.paalgyula.hu/schemas/tld/gravatar"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="author" content="EaSII Team">
<meta name="description" content="TheEnd">

<title>Systematic Map</title>
<link rel="shortcut icon" href="<c:url value="/images/books.png" />"
	type="image/png">

<decorator:head />

<!-- Bootstrap Core CSS -->
<link href="<c:url value="/vendor/bootstrap/css/bootstrap.min.css" />"
	rel="stylesheet">
<!-- MetisMenu CSS -->
<link href="<c:url value="/vendor/metisMenu/metisMenu.min.css" />"
	rel="stylesheet">
<!-- Timeline CSS -->
<%-- <link href="<c:url value="/css/timeline.css" />" rel="stylesheet"> --%>
<!-- Custom CSS -->
<link href="<c:url value="/css/sb-admin-2.css" />" rel="stylesheet">
<!-- Morris Charts CSS -->
<%-- <link href="<c:url value="/vendor/morris/morris.css" />" rel="stylesheet"> --%>
<!-- Custom Fonts -->
<link
	href="<c:url value="/vendor/font-awesome/css/font-awesome.min.css" />"
	rel="stylesheet" type="text/css">

<!-- DataTables CSS -->
<link
	href="<c:url value="/vendor/datatables/css/dataTables.bootstrap.css" />"
	rel="stylesheet">
<!-- DataTables Responsive CSS -->
<link
	href="<c:url value="/vendor/datatables/css/dataTables.responsive.css" />"
	rel="stylesheet">

<!-- Autocomplete -->
<link href="<c:url value="/vendor/chosen/chosen.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/css/select2.min.css" />" rel="stylesheet" />
<link href="<c:url value="/css/bootstrap-fileupload.min.css"/>"
	rel="stylesheet" type="text/css" />
<link href="<c:url value="/css/systematicmap.css" />" rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

<!-- jQuery -->
<script src="<c:url value="/vendor/jquery/jquery.min.js" />"></script>
<!-- jQuery Validate-->
<script src="<c:url value="/vendor/jquery/jquery.validate.min.js" />"></script>
<script
	src="<c:url value="/vendor/jquery/localization/messages_pt_BR.min.js" />"></script>

<!-- Bootstrap Core JavaScript -->
<script src="<c:url value="/vendor/bootstrap/js/bootstrap.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/js/bootstrap-fileupload.min.js"/>"></script>
<script
	src="<c:url value="/vendor/datatables/js/jquery.dataTables.min.js" />"></script>
<script
	src="<c:url value="/vendor/datatables/js/dataTables.bootstrap.min.js" />"></script>

<script type="text/javascript">
	(function($) {
		$(document)
				.ready(
						function() {
							$.validator.addMethod("login", function(value,
									element) {
								return this.optional(element)
										|| /^[a-zA-Z0-9_]+$/.test(value);
							}, '<fmt:message key="invalid_login" />');
							$.validator
									.setDefaults({
										errorClass : "control-label control-label-block",
										onkeyup : function(element) {
											$(element).valid()
										},
										highlight : function(element) {
											if (!$(element).closest(
													'.form-group').is(
													'.has-feedback')) {
												$(element).closest(
														'.form-group')
														.addClass(
																'has-feedback');
											}

											$(element).closest('.form-group')
													.removeClass('has-success')
													.addClass('has-error');
											var id = this.idOrName(element);
											var id2 = id + '-men';
											id = id + '-icon';
											$('#' + id.toString()).remove();
											$('#' + id2.toString()).remove();

											$(element).attr("aria-describedby",
													id);
											$(element)
													.after(
															'<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="false" id = "'+id2+'"></span>')
													.after(
															'<span id="'+id+'" class="sr-only">(error)</span>');
										},
										unhighlight : function(element) {
											if (!$(element).closest(
													'.form-group').is(
													'.has-feedback')) {
												$(element).closest(
														'.form-group')
														.addClass(
																'has-feedback');
											}

											$(element).closest('.form-group')
													.removeClass('has-error')
													.addClass('has-success');
											var id = this.idOrName(element);
											var id2 = id + '-men';
											id = id + '-icon';
											$('#' + id.toString()).remove();
											$('#' + id2.toString()).remove();
											$(element).attr("aria-describedby",
													id);
											$(element)
													.after(
															'<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="false" id = "'+id2+'"></span>')
													.after(
															'<span id="'+id+'" class="sr-only">(success)</span>');
										},
										errorPlacement : function(error,
												element) {
											if (element.prop('type') === 'checkbox'
													|| element.prop('type') === 'radio') {
												error.insertAfter(element
														.parent());
											} else {
												error.insertAfter(element);
											}
										}
									});
						});
	})(jQuery);
</script>

</head>
<body class="background_main">
	<div id="preloader"></div>
	<div id="wrapper">
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0"> 

		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${linkTo[HomeController].home}"><fmt:message
						key="mapstudy.web" /></a>
			</div>
			<ul class="nav navbar-top-links navbar-right">
				<li><span class="color-primary">${userInfo.user.name}</span></li>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"><i class="fa fa-user fa-fw"></i>
						<i class="fa fa-caret-down"></i></a>
					<ul class="dropdown-menu dropdown-user">
						<li><a
							href="${linkTo[UsersController].profile(userInfo.user.id)}"><i
								class="fa fa-user fa-fw"></i> <fmt:message key="user.profile" /></a></li>
						<li class="divider"></li>

						<li><a href="${linkTo[MapStudyController].list}"><i
								class="glyphicon glyphicon-log-in"></i> <fmt:message
									key="mapstudy.my.short" /></a></li>
						<li class="divider"></li>

						<li><a href="${linkTo[HomeController].logout}"><i
								class="fa fa-sign-out fa-fw"></i> <fmt:message key="logout" /></a></li>
					</ul></li>
			</ul>
		</div>
		</nav>
	</div>

	<div class="container container_main">
		<div class="row">
			<div class="col-md-3" style="padding-top: 22px;">
				<div class="panel-group" id="accordion">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapseOne"> <span
									class="glyphicon glyphicon-folder-close marginRight10"></span>Dashboard
								</a>
							</h4>
						</div>
						<div id="collapseOne" class="panel-collapse collapse in">
							<div class="panel-body">
								<table class="table" style="margin-bottom: 0px;">
									<tr>
										<td style="border-top-width: 0px; padding: 10px 8px;"><span
											class="glyphicon glyphicon-home text-info marginRight10"></span>
											<a href="${linkTo[MapStudyController].home}">Home</a></td>
									</tr>
									<tr>
										<td style="padding: 10px 8px;"><span
											class="glyphicon glyphicon-list text-info marginRight10"></span>
											<a href="${linkTo[MapStudyController].list}">Listar
												Mapeamentos</a></td>
									</tr>
									<tr>
										<td style="padding: 10px 8px;"><span
											class="glyphicon glyphicon-plus text-info marginRight10"></span>
											<a href="${linkTo[MapStudyController].create}">Novo
												Mapeamento</a></td>
									</tr>
								</table>
							</div>
						</div>
					</div>

					<div id="messagesController">
						<script type="text/javascript">
							funcion()
						</script>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#accordion"
									href="#collapseThree"> <span
									class="glyphicon glyphicon-user marginRight10"></span>Conta
								</a>
							</h4>
						</div>
						<div id="collapseThree" class="panel-collapse collapse">
							<div class="panel-body">
								<table class="table" style="margin-bottom: 0px;">
									<tr>
										<td style="border-top-width: 0px; padding: 10px 8px;"><span
											class="glyphicon glyphicon-user text-info marginRight10"></span>
											<a
											href="${linkTo[UsersController].profile(userInfo.user.id)}">Perfil</a>
										</td>
									</tr>
									<tr>
										<td style="padding: 10px 8px;"><span
											class="glyphicon glyphicon-off text-info marginRight10"></span>
											<a href="${linkTo[HomeController].logout}">Logout</a></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-9" style="padding-top: 22px;">
				<c:if test="${msg.getMessage().size() > 0}">
					<c:forEach var="mensagem" items="${msg.getMessage()}">
						<div class="${mensagem.getClasse()}" role="alert">
							<!-- <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> -->
							<!-- <span class="sr-only">Error:</span> -->
							<button type="button" class="close" data-dismiss="alert"aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<strong> <fmt:message key="${mensagem.category}" /></strong> <fmt:message key="${mensagem.value}" />
						</div>
					</c:forEach>
					${msg.clean()}
			</c:if>

				<!--     			<div id="messages"></div> -->
<%-- 				<c:if test="${not empty errors}"> --%>
<!-- 					<div class="alert alert-danger alert-dismissible" role="alert"> -->
<!-- 						<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> -->
<!-- 						<span class="sr-only">Error:</span> -->
<!-- 						<button type="button" class="close" data-dismiss="alert" -->
<!-- 							aria-label="Close"> -->
<!-- 							<span aria-hidden="true">&times;</span> -->
<!-- 						</button> -->
<%-- 						<c:forEach items="${errors}" var="error"> --%>
<%-- 							<b><fmt:message key="${error.category}" /></b> - <fmt:message --%>
<%-- 								key="${error.message}" /> --%>
<!-- 							<br /> -->
<%-- 						</c:forEach> --%>
<!-- 					</div> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${not empty notice}"> --%>
<!-- 					<div class="alert alert-info alert-dismissible" role="alert" -->
<!-- 						id="notices"> -->
<!-- 						<button type="button" class="close" data-dismiss="alert" -->
<!-- 							aria-label="Close"> -->
<!-- 							<span aria-hidden="true">&times;</span> -->
<!-- 						</button> -->
<%-- 						<b><fmt:message key="${notice.category}" /></b> - --%>
<%-- 						<fmt:message key="${notice.message}" /> --%>
<!-- 						<br /> -->
<!-- 					</div> -->
<%-- 				</c:if> --%>

<%-- 				<c:if test="${not empty warning}"> --%>
<!-- 					<div class="alert alert-warning alert-dismissible" role="alert" -->
<!-- 						id="warning"> -->
<!-- 						<button type="button" class="close" data-dismiss="alert" -->
<!-- 							data-hide="alert">&times;</button> -->
<%-- 						<b><fmt:message key="${warning.category}" /></b> - --%>
<%-- 						<fmt:message key="${warning.message}" /> --%>
<!-- 						<br /> -->
<!-- 					</div> -->
<%-- 				</c:if> --%>
				<decorator:body />
			</div>
		</div>
	</div>

	<!-- Footer -->
	<!--     <footer> -->
	<!--         <div class="container text-center"> -->
	<!--             <p>Copyright &copy; Your Website 2014</p> -->
	<!--             <p>© 2015-2016. Todos os direitos reservados.</p> -->
	<!--             <p><a href="http://easii.ufpi.br">EASII</a> - Laborat&oacute;rio de Engenharia de Software e Inform&aacute;tica Industrial</p> -->
	<!--         </div> -->
	<!--     </footer> -->

	<!-- Modal -->
	<div id="generic-modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">
						<fmt:message key="confirm.delete" />
					</h4>
				</div>
				<div class="modal-body">
					<p>One fine body&hellip;</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<fmt:message key="close" />
					</button>
					<button type="button" class="btn btn-danger"
						id="generic-modal-confirmation">
						<fmt:message key="confirm.delete" />
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="<c:url value="/vendor/metisMenu/metisMenu.min.js" />"></script>
	<script src="<c:url value="/vendor/chosen/chosen.jquery.min.js" />"></script>
	<script src="<c:url value="/js/select2.min.js" />"></script>

	<!-- Morris Charts JavaScript -->
	<%-- <script src="<c:url value="/vendor/raphael/raphael-min.js" />"></script> --%>
	<%-- <script src="<c:url value="/vendor/morris/morris.min.js" />"></script> --%>
	<%-- <script src="<c:url value="/js/morris-data.js" />"></script> --%>

	<!-- Custom Theme JavaScript -->
	<script src="<c:url value="/js/sb-admin-2.js" />"></script>
	<script>
		$(document)
				.ready(
						function() {
							//tabela personalizada
							$('.personalized-table').dataTable({
								"scrollY" : "300px",
								"scrollCollapse" : true,
								"paging" : false
							});
							$('.personalized-table-simple').dataTable();

							//autocomplete
							$('.chosen-select')
									.chosen(
											{
												allow_single_deselect : true,
												search_contains : true,
												no_results_text : '<fmt:message key="${autocomplete.nothing}"/>'
											});

							function formatState(state) {
								if (!state.id) {
									return state.text;
								}
								var $state = $('<span class="select2-name">'
										+ state.text
										+ '<i class="select2-email">'
										+ $(state.element).data('email')
										+ '</i></span>');

								//console.log(state);
								return $state;
							}
							;
							$('.select2').select2({
								allowClear : true,
								templateResult : formatState
							});
							$('.select2-alternative-compare').select2({
								templateResult : formatState
							});

							$('.select2-alternative').select2();

							//padrao de modal
							$(document)
									.on(
											'click',
											'.confirmation-modal',
											function(e) {
												e.preventDefault();
												var $this = $(this), $modal = $('#generic-modal'), body = $this
														.data('conf-modal-body'), callBack = $this
														.data('conf-modal-callback');

												if (body) {
													$modal.find('.modal-body')
															.html(body);
												}

												$modal.modal('show');
												$modal
														.find(
																'#generic-modal-confirmation')
														.unbind('click')
														.on(
																'click',
																function(e) {
																	if (!callBack) {
																		document.location.href = $this
																				.attr('href');
																	} else {
																		eval(callBack);
																		$modal
																				.modal('hide');
																	}
																});
											});
						});
	</script>
</body>
<script type="text/javascript"
	src="<c:url value="/js/capt.mining.useskill.4-oracle.js"/>"></script>
<script type="text/javascript">
	//caso utilize JQuery no sistema, utilize o Jquery.ready() e o paramatro "waitdomready" = false 
	jQuery(document).ready(function() {
		try {
			useskill_capt_onthefly({
				client : "THEEND", //nome abreviado do sistema monitorado
				version : 1, //versao do script de captura

				url : "http://easii.ufpi.br/oracle-test/backend/actions",
				// 					url: "http://localhost:8080/oracle-test/backend/actions",
				waitdomready : false, //se for verdadeiro, aguarda o evento DOMContentLoaded ser disparado, caso contrario ja executa a captura
				sendactions : true, //enviar acoes para o servidor
				debug : false, //apresentar dados no console

				onthefly : true, //para scripts inseridos via onthefly
				plugin : false, //apenas para script via plugin do chrome

				captureback : false, //captura de eventos de voltar -> con
				capturehashchange : false, //capturar eventos de mudanca de hash
				jheat : false, //para sistemas baseados no jheat
				timetosubmit : 120, //periodicidade em segundos para envio de eventos

				//dados do usuario logado:
				username : "usuarioTeste", //pode ser retornado por funcao ou uma string
				role : "user", //pode ser retornado por funcao ou uma string,

				oracleElements : [ "#teste1", "#teste2", "#teste3", ".abc" ]
			});
		} catch (e) {
			console.log('usability analytics stopped!');
		}
	});
</script>

</html>