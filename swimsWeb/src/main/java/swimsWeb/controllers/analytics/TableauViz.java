package swimsWeb.controllers.analytics;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class TableauViz implements Serializable {

	private static final long serialVersionUID = 7421349093593404325L;
	private String url;
	private String title;

	public TableauViz() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		this.url = "https://prod-useast-a.online.tableau.com/t/miguelrodriguez/views/social-impact/Dashboard1";
		this.title = "Dashboard General";
	}

	public void setTableauViz(String title, String url) {
		setUrl(url);
		setTitle(title);
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

}
