package swimsWeb.controller.harvest;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class StepsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int activeIndex = 0;

	public StepsBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void onLoad() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String viewId = ctx.getViewRoot().getViewId().split("/")[ctx.getViewRoot().getViewId().split("/").length - 1];
		switch (viewId) {
		case "origen.xhmtl":
			this.activeIndex = 0;
			break;
		case "fecha.xhtml":
			this.activeIndex = 1;
			break;
		case "filtrado.xhtml":
			this.activeIndex = 2;
			break;
		case "confirmacion.xhtml":
			this.activeIndex = 3;
			break;
		default:
			break;
		}
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

}
