/**
 * 
 */
package br.com.ufpi.systematicmap.learn.controller;

import java.io.Serializable;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.ufpi.systematicmap.interceptor.UserInfo;

/**
 * @author Gleison Andrade
 *
 */
@Controller
public class AutomaticSelectionController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Result result;
	private Validator validator;
	private UserInfo userInfo;
	private Logger logger;
	
	@Deprecated
	protected AutomaticSelectionController() {
		this(null, null, null, null);
	}

	/**
	 * @param result
	 * @param validator
	 * @param userInfo
	 * @param logger
	 */
	@Inject
	public AutomaticSelectionController(Result result, Validator validator, UserInfo userInfo, Logger logger) {
		this.result = result;
		this.validator = validator;
		this.userInfo = userInfo;
		this.logger = logger;
	}
	
	
	

}
