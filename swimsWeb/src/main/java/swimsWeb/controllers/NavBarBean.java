package swimsWeb.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import swimsWeb.interfaces.OnRefreshEventListener;

@Named
@RequestScoped
public class NavBarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private OnRefreshEventListener onRefreshEventListener;
	private String toUpdateFormId;

	public NavBarBean() {
		// TODO Auto-generated constructor stub
	}

	public OnRefreshEventListener getOnRefreshEventListener() {
		return onRefreshEventListener;
	}

	public void setOnRefreshEventListener(OnRefreshEventListener onRefreshEventListener) {
		this.onRefreshEventListener = onRefreshEventListener;
	}

	public String getToUpdateFormId() {
		return toUpdateFormId;
	}

	public void setToUpdateFormId(String toUpdateFormId) {
		this.toUpdateFormId = toUpdateFormId;
	}

}
