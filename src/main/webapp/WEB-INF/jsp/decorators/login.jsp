<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<link href="<c:url value="/vendor/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet">

<!-- MetisMenu CSS -->
<link href="<c:url value="/vendor/metisMenu/metisMenu.min.css"/>"
	rel="stylesheet">

<!-- Custom CSS -->
<link href="<c:url value="/css/sb-admin-2.css" />" rel="stylesheet">

<!-- Custom Fonts -->
<link
	href="<c:url value="/vendor/font-awesome/css/font-awesome.min.css" />"
	rel="stylesheet" type="text/css">

<link href="<c:url value="/css/systematicmap.css" />" rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

<!-- jQuery -->
<script src="<c:url value="/vendor/jquery/jquery.min.js" />"></script>
<!-- Bootstrap Core JavaScript -->
<script src="<c:url value="/vendor/bootstrap/js/bootstrap.min.js" />"></script>
<!-- jQuery Validate-->
<script src="<c:url value="/vendor/jquery/jquery.validate.min.js" />"></script>
<script
	src="<c:url value="/vendor/jquery/localization/messages_pt_BR.min.js" />"></script>


<script type="text/javascript">
	(function($) {
		$(document)
				.ready(
						function() {
							$.validator.addMethod("login", function(value,
									element) {
								return this.optional(element)
										|| /^[a-z0-9_]+$/.test(value);
							}, '<fmt:message key="invalid_login" />');
							$.validator.addMethod("user_name", function(value,
									element) {
								return this.optional(element)
										|| /^[a-zà-úA-ZÀ-Ú ]+$/.test(value);
							}, '<fmt:message key="invalid_name" />');
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
<body class="background_home">
	<div id="wrapper" style="text-align: center;">
		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
		<div class="navbar-header"></div>
		<ul class="nav navbar-top-links">
			<li><a class="page-scroll" href="${linkTo[HomeController].home}"><fmt:message
						key="home" /></a></li>
			<li><a class="page-scroll"
				href="${linkTo[HomeController].login}"><fmt:message key="login" /></a></li>
			<li><a class="page-scroll"
				href="${linkTo[HomeController].create}"><fmt:message
						key="signup" /></a></li>
			<li><a class="page-scroll" href="#"><fmt:message
						key="contact" /></a></li>
		</ul>
		</nav>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="col-md-12">
					<c:if test="${msg.getMessage().size() > 0}">
						<c:forEach var="mensagem" items="${msg.getMessage()}">
							<div class="${mensagem.getClasse()}" role="alert">
								<!-- <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> -->
								<!-- <span class="sr-only">Error:</span> -->
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<strong> <fmt:message key="${mensagem.category}" /></strong> - <fmt:message key="${mensagem.value}" />
						</div>
					</c:forEach>
				</c:if>
				${msg.clean()}
				
					<c:if test="${not empty errors}">
					<div class="alert alert-danger alert-dismissible" role="alert">
							<!-- <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> -->
							<!-- <span class="sr-only">Error:</span> -->
	 						<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<c:forEach items="${errors}" var="error">
							<b><fmt:message key="${error.category}" /></b> - <fmt:message key="${error.message}" /><br/>
						</c:forEach>
					</div>
				</c:if>
				</div>
				<decorator:body />
			</div>
		</div>
	</div>

	<!-- Footer -->
	<footer>
	<div class="container text-center" style="margin-top: 8px;">
		<p>© 2015-2018. Todos os direitos reservados.</p>
		<p>
			<a href="http://easii.ufpi.br">EaSII</a> - Laborat&oacute;rio de
			Engenharia de Software e Inform&aacute;tica Industrial
		</p>
	</div>
	</footer>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="<c:url value="/vendor/metisMenu/metisMenu.min.js" />"></script>
	<!-- Custom Theme JavaScript -->
	<script src="<c:url value="/js/sb-admin-2.js" />"></script>
</body>

</html>