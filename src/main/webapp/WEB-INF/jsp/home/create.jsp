<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<meta name="decorator" content="login"/>

<script type="text/javascript">
	(function($){
		$(document).ready(function(){
			$("#formCreateUser").validate({ 
                 rules: {
                	 'user.name': { 
                    	 required : true,
                    	 minlength : 3,
                    	 maxlength : 100,
                    	 user_name: true
                     },
                     'user.login': {
                         required: true,
                         minlength : 3,
                    	 maxlength : 20,
                    	 login: true
                     },
                     'user.email' : {
                         required: true,
                         email: true
                      },
                      'user.password' : {
                          required : true,
                          minlength : 6
                      },
                      'repassword' : {
                    	  required : true,
                          minlength : 6,
                          equalTo: '#password'
                      }
                  }, messages: {
	                	  'user.name': { 
	                		 required: '<fmt:message key="required" />',
	                     	 minlength : '<fmt:message key="name_min" />',
	                     	 maxlength : '<fmt:message key="name_max" />',
	                     	 user_name: '<fmt:message key="invalid_name" />'
	                      },
	                      'user.login': {
	                    	 required: '<fmt:message key="required" />',
	                         minlength : '<fmt:message key="login_min" />',
	                     	 maxlength : '<fmt:message key="login_max" />',
	                     	 login: '<fmt:message key="invalid_login" />'
	                      },
	                      'user.email' : {
	                          required: '<fmt:message key="required" />',
	                          email: '<fmt:message key="invalid_email" />'
	                      },
	                	  'password': {
	                          required: '<fmt:message key="required" />',
	                          minlength: '<fmt:message key="password_min" />'
	                      },
	                      'repassword': {
	                          required: '<fmt:message key="required" />',
	                          minlength: '<fmt:message key="password_min" />',
	                          equalTo: '<fmt:message key="password_different" />'
	                      }
                  }
			});
		});
	})(jQuery);
</script>

<div class="row">
	<div class="col-md-4">
	</div>
	
	<div class="col-md-4">
		<div class="login-panel panel panel-default" >
		   <div class="panel-heading">
		       <h3 class="panel-title"><fmt:message key="signup"/></h3>
		   </div>
		   <div class="panel-body">
		       <form action="${linkTo[UsersController].add}" method="post" class="" autocomplete="off" id="formCreateUser">
		           <fieldset>
		               <div class="form-group">
						<label class="sr-only" for="name"><fmt:message key="user.name"/></label>
						<input type="text" class="form-control" id="newname" name="user.name" value="${user.name}" placeholder="<fmt:message key="user.name"/>"/>
					</div>
					<div class="form-group">
						<label class="sr-only" for="login"><fmt:message key="user.login"/></label>
						<input type="text" class="form-control" id="newlogin" name="user.login" value="${user.login}" placeholder="<fmt:message key="user.login"/>"/>
					</div>
					<div class="form-group">
						<label class="sr-only" for="email"><fmt:message key="user.email"/></label>
						<input type="text" class="form-control" id="newemail" name="user.email" value="${user.email}" placeholder="<fmt:message key="user.email"/>"/>
					
					</div>
					<div class="form-group">
						<label class="sr-only" for="password"><fmt:message key="user.password"/></label>
						<input type="password" id="password" class="form-control" name="user.password" value="${user.password}" placeholder="<fmt:message key="user.password"/>"/>
					</div>
					
					<div class="form-group">
						<label class="sr-only" for="password"><fmt:message key="user.password"/></label>
						<input type="password" id="repassword" class="form-control" name="repassword" value="" placeholder="<fmt:message key="recovery.repassword"/>"/>
					</div>
					
		               <!-- Change this to a button or input when using this as a form -->
		               <button type="submit" id="submit" class="btn btn-lg btn-primary btn-block" style="margin-bottom: 10px;">
						<fmt:message key="create"/>
					</button>
		             </fieldset>
		         </form>
		         <a id="return" class="btn btn-lg btn-default btn-block" href="${linkTo[HomeController].login()}"><fmt:message key="button.back"/></a>
		     </div>
		 </div>
	</div>
</div>