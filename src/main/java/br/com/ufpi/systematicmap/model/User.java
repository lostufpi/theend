package br.com.ufpi.systematicmap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "required")
	@Length(min = 3, max = 20, message = "login_min_max")
	@Pattern(regexp = "[a-z0-9_]+", message = "invalid_login")
	private String login;

	@NotNull(message = "required")
	@Length(min = 6, message = "password_min")
	@SkipSerialization
	private String password;
	
	@SkipSerialization
	private String recoveryCode;
	
	@NotNull(message = "required")
	@Email(message = "invalid_email")
	private String email;

	@NotNull(message = "required")
	@Length(min = 3, max = 100)
	@Pattern(regexp = "[a-zà-úA-ZÀ-Ú ]+", message = "invalid_name")
	private String name;
	
	private boolean removed;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@SkipSerialization
	private Set<UsersMapStudys> usersMapStudys = new HashSet<>();
	
	@OneToMany(mappedBy="user")
	@SkipSerialization
	private Set<Evaluation> evaluations = new HashSet<>();

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Set<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the recoveryCode
	 */
	public String getRecoveryCode() {
		return recoveryCode;
	}

	/**
	 * @param recoveryCode the recoveryCode to set
	 */
	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	/**
	 * @return the removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * @param removed the removed to set
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * @return the usersMapStudys
	 */
	public Set<UsersMapStudys> getUsersMapStudys() {
		return usersMapStudys;
	}

	/**
	 * @param usersMapStudys the usersMapStudys to set
	 */
	public void setUsersMapStudys(Set<UsersMapStudys> usersMapStudys) {
		this.usersMapStudys = usersMapStudys;
	}	
	
	//TODO mostrar para o matheus e verificar se é melhor que buscar no banco
	public List<MapStudy> mapStudys(){
		List<MapStudy> mapStudys = new ArrayList<>();
		for (UsersMapStudys u : usersMapStudys) {
			mapStudys.add(u.getMapStudy());
		}
		return mapStudys;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
