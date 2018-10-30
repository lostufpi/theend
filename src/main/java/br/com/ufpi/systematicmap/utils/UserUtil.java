/**
 * 
 */
package br.com.ufpi.systematicmap.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.ufpi.systematicmap.model.MapStudy;
import br.com.ufpi.systematicmap.model.User;
import br.com.ufpi.systematicmap.model.UsersMapStudys;
import br.com.ufpi.systematicmap.model.enums.Roles;

/**
 * @author Gleison Andrade
 *
 */
public class UserUtil {
	public static List<User> removeUserRole(MapStudy mapStudy, Roles role){
		List<User> users = new ArrayList<>();
		
		for (UsersMapStudys um : mapStudy.getUsersMapStudys()) {
			if(!um.isRemoved() && !um.getRole().equals(role)){
				users.add(um.getUser());
			}
		}
		
		return users;
	}

}
