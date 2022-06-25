package swimsWeb.controller.auth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class AuthBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public AuthBean() {
		// TODO Auto-generated constructor stub
	}

	public String onPageLoad() {
		System.out.println("Authh");
		return null;
	}
}
