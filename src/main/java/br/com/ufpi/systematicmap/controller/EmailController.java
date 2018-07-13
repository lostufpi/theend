package br.com.ufpi.systematicmap.controller;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.interceptor.Public;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.TipoMensagem;
import br.com.ufpi.systematicmap.utils.GenerateHashPasswordUtil;
import br.com.ufpi.systematicmap.utils.Linker;
import br.com.ufpi.systematicmap.utils.MailUtils;

/**
 * @author Gleison
 *
 */
@Controller
@Named
public class EmailController {
	private final UserDao userDao;
	private final Result result;
	private final MailUtils mailUtils;
	private final Linker linker;

	protected EmailController() {
		this(null, null, null, null);
	}

	@Inject
	public EmailController(UserDao userDao, Result result, MailUtils mailUtils, Linker linker) {
		this.userDao = userDao;
		this.result = result;
		this.mailUtils = mailUtils;
		this.linker = linker;
	}
	
	
	/**
	 * @param email
	 */
	@Public
	@Post
	@Path("/mail/recovery")
	public void recoveryPassword(String email){
		GenerateHashPasswordUtil generateHashPasswordUtil = new GenerateHashPasswordUtil();
		User user = userDao.findEmail(email);
		
		if (user == null) {
			MessagesController.addMessage(new Mensagem("user.email", "user.email.invalid", TipoMensagem.INFORMACAO));
			result.redirectTo(HomeController.class).recovery();
		}
		
		Random random = new Random();
		
		// Gerar code de recuperação
		String code = generateHashPasswordUtil.generateCodeRecovery(user.getLogin() + random.nextLong() + user.getEmail() + System.currentTimeMillis());
		
		user.setRecoveryCode(code);
		user = userDao.update(user);
		
		linker.buildLinkTo(this).validateCode(code);
		String url = "<a href=\""+linker.getURL()+"\" target=\"_blank\">Clique aqui</a> para criar uma nova senha";		
		
		String message = "<p>Ol&aacute; " + user.getName()+ ",</p>"
				+ "Login: " +user.getLogin()
				+ "<p>Seu pedido de altera&ccedil;&atilde;o de senha foi atendido com sucesso pelo sistema.</p>"
				+ "<p>Clique no link a seguir para realizar a altera&ccedil;&atilde;o de sua senha.</p>"
				+ "<p>"+ url+"</p>";
		
		//Send mail
		try {
			mailUtils.send("[TheEND] - Solicitação de alteração de senha", message, user.getEmail());
		} catch (Exception e) {
			user.setRecoveryCode(null);
			userDao.update(user);
			MessagesController.addMessage(new Mensagem("user.email", "error_email", TipoMensagem.ERRO));
			result.redirectTo(HomeController.class).recovery();
		}		
		
		MessagesController.addMessage(new Mensagem("user.email","email.recovery.success", TipoMensagem.SUCESSO));
		result.redirectTo(HomeController.class).recovery();
	}
	
	@Public
	@Get("/recovery/{code}")
	public void validateCode(String code){
		final User user = userDao.findCodeRecovery(code);
		
		if(user == null) {
			MessagesController.addMessage(new Mensagem("user.email", "invalid_code_recovery", TipoMensagem.ERRO));
			result.redirectTo(HomeController.class).login();
		}
		
		result.include("code", code);
	}
	
	@Public
	@Post("/recovery/newpassword")
	public void newPassword(String code, String password, String repassword){
		final User user = userDao.findCodeRecovery(code);
		
		if(!password.equals(repassword)) {
			MessagesController.addMessage(new Mensagem("user.password", "password_different", TipoMensagem.ERRO));
			result.redirectTo(this).validateCode(code);
		}
		
		user.setRecoveryCode(null);
		
		GenerateHashPasswordUtil generateHashPasswordUtil = new GenerateHashPasswordUtil();
        user.setPassword(generateHashPasswordUtil.generateHash(password));        
		
		userDao.update(user);		
		
		MessagesController.addMessage(new Mensagem("user.password", "password.changed.sucess", TipoMensagem.SUCESSO));
		result.redirectTo(HomeController.class).login();	
	}
}
