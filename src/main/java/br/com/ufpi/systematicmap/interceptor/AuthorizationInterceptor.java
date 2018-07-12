package br.com.ufpi.systematicmap.interceptor;

import javax.inject.Inject;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.ufpi.systematicmap.controller.HomeController;
import br.com.ufpi.systematicmap.controller.MessagesController;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.TipoMensagem;

/**
 * Interceptor to check if the user is in the session.
 */
@Intercepts
public class AuthorizationInterceptor {

	@Inject
	private UserInfo info;

	@Inject
	private UserDao dao;

	@Inject
	private Result result;
	
	@Accepts
	public boolean accepts(ControllerMethod method) {
		return !method.containsAnnotation(Public.class);
	}

	/**
	 * Intercepts the request and checks if there is a user logged in.
	 */
	@AroundCall
	public void intercept(SimpleInterceptorStack stack) {

		User current = info.getUser();
		try {
			dao.refresh(current);
		} catch (Exception e) {
			// could happen if the user does not exist in the database or if there's no user logged in.
		}

		
		if (current == null) {
			// remember added parameters will survive one more request, when there is a redirect
			MessagesController.addMessage(new Mensagem("user", "user.is.not.logged.in", TipoMensagem.ERRO));
			result.redirectTo(HomeController.class).home();
			return;
		}
		stack.next();
	}

}
