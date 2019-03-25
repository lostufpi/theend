package br.com.ufpi.systematicmap.controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.ufpi.systematicmap.dao.UserDao;
import br.com.ufpi.systematicmap.interceptor.Public;
import br.com.ufpi.systematicmap.interceptor.UserInfo;
import br.com.ufpi.systematicmap.model.Mensagem;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.enums.TypeMessage;
import br.com.ufpi.systematicmap.utils.GenerateHashPasswordUtil;

@Controller
public class HomeController {

	private final Result result;
	private final UserInfo userInfo;
	private final UserDao dao;

	/**
	 * @deprecated CDI eyes only
	 */
	protected HomeController() {
		this(null, null, null);
	}

	@Inject
	public HomeController(UserDao dao, UserInfo userInfo, Result result) {
		this.dao = dao;
		this.result = result;
		this.userInfo = userInfo;
	}

	@Public
	@Get("/")
	public void home() {
		if (userInfo != null && userInfo.getUser() != null) {
			result.redirectTo(MapStudyController.class).home();
		} else {
			result.redirectTo(this).login();
		}
	}

	@Post
	@Public
	public void login(String login, String password) {
		GenerateHashPasswordUtil generateHashPasswordUtil = new GenerateHashPasswordUtil();
		User currentUser = dao.find(login);

		if (currentUser != null && generateHashPasswordUtil.isPasswordValid(password, currentUser.getPassword())) {
			if (!generateHashPasswordUtil.isDefaultEncoder(currentUser.getPassword())) {
				currentUser.setPassword(generateHashPasswordUtil.generateCodeRecovery(password));
				currentUser = dao.update(currentUser);
			}

			userInfo.login(currentUser);

			MessagesController.addMessage(new Mensagem("user.login", "mapstudy.login.success", TypeMessage.SUCCESS));
			result.redirectTo(MapStudyController.class).home();
		} else {
			MessagesController.addMessage(new Mensagem("login", "invalid_login_or_password", TypeMessage.ERROR));
			result.redirectTo(this).login();
			return;
		}
	}

	public void logout() {
		userInfo.logout();
		result.redirectTo(this).home();
	}

	@Public
	@Get
	public void login() {
	}

	@Public
	@Get
	public void create() {
	}

	@Public
	@Get
	public void recovery() {
	}

	@Public
	@Get
	public void contact() {
	}

}
