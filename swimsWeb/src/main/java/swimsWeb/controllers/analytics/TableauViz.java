package swimsWeb.controllers.analytics;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsWeb.utilities.JSFMessages;

@Named
@SessionScoped
public class TableauViz implements Serializable {

	private static final long serialVersionUID = 7421349093593404325L;
	private String url;
	private String componentPath;
	private String title;
	private boolean useApiV1;

	public TableauViz() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		JSFMessages.INFO("Credenciales de Tableau. Usuario: invitado Contraseña: invitado");
		this.url = "informative-area&#47;TTitulacinDocente";
		this.title = "Dashboard General";
		this.componentPath = "/WEB-INF/components/tableau-views/ttitulacion-docente.xhtml";
		this.useApiV1 = true;
	}

	public void setTableauViz(String title, String url) {
		setUrl(url);
		setTitle(title);
		this.useApiV1 = false;
	}

	public String setTableauVizApiV1(String title, String url) {
		this.useApiV1 = true;
		setComponentPath(url);
		setTitle(title);

		return "/analiticas/index?faces-redirect=true";
	}

	public String renderedVizComponentId(boolean useApiV1) {
		return useApiV1 ? "tableau-api-v1-viz" : "tableau-api-v3-viz";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isUseApiV1() {
		return useApiV1;
	}

	public void setUseApiV1(boolean useApiV1) {
		this.useApiV1 = useApiV1;
	}

	public String getComponentPath() {
		return componentPath;
	}

	public void setComponentPath(String componentPath) {
		this.componentPath = componentPath;
	}

}
