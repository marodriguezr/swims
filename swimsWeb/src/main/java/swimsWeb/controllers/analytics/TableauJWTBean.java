package swimsWeb.controllers.analytics;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.analytics.managers.TableauJWTManager;
import swimsWeb.utilities.JSFMessages;

@SessionScoped
@Named
public class TableauJWTBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@EJB
	private TableauJWTManager tableauJWTManager;
	private String tableauJWT;

	public TableauJWTBean() {
	}

	@PostConstruct
	public void onLoad() {
		try {
			this.tableauJWT = tableauJWTManager.getTableauJWT();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR("Error en la Autenticación de Tableau " + e.getMessage());
			this.tableauJWT = null;
		}
	}

	public String getTableauJWT() {
		return tableauJWT;
	}

	public void setTableauJWT(String tableauJWT) {
		this.tableauJWT = tableauJWT;
	}

}
