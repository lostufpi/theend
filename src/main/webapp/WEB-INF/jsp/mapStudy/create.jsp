<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	(function($){
		$(document).ready(function(){
			$("#formAddMapStudy").validate({ 
                 rules: {
                	 'mapstudy.title': { 
                    	 required : true,
                    	 minlength : 3
                     },
                     'mapstudy.description': {
                         required: true,
                         minlength : 3
                     }
                  }, messages: {
                	  'mapstudy.title': {
                          required: '<fmt:message key="required" />',
                          minlength: '<fmt:message key="mapstudy.min.title" />'
                      },
                      'mapstudy.description': {
                          required: '<fmt:message key="required" />',
                          minlength: '<fmt:message key="mapstudy.min.description" />'
                      }
                  }
			});
		});
	})(jQuery);
</script>

<h3 class="color-primary" style="margin-top: 0px;"><fmt:message key="mapstudy.create"/></h3>
<hr/>
<div class="row">
	<div class="col-md-offset-2 col-md-8">
		<div class="well">
			<form action="${linkTo[MapStudyController].add}" method="post" id="formAddMapStudy">
				<div class="form-group">
					<label for="title" class=""><fmt:message key="mapstudy.title"/></label>
					<input type="text" class="form-control" name="mapstudy.title" id="title" value="${mapstudy.title}"/>
				</div>
				<div class="form-group">
					<label for="description" class=""><fmt:message key="mapstudy.description"/></label>
					<textarea class="form-control" rows="3" name="mapstudy.description" id="description">${mapstudy.description}</textarea>
				</div>
				<div class="form-group pull-right">	
					<button type="submit" class="btn btn-primary"><fmt:message key="mapstudy.create"/></button>
				</div>	
			</form>
		</div>
	</div>		
</div>