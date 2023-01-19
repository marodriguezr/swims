package swimsWeb.controllers;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import swimsWeb.interfaces.OnRefreshEventListener;

@Named
@ViewScoped
public class NavBarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private OnRefreshEventListener onRefreshEventListener;
	private String updatableFormString;

	public NavBarBean() {
		// TODO Auto-generated constructor stub
	}

	public OnRefreshEventListener getOnRefreshEventListener() {
		return onRefreshEventListener;
	}

	public void setOnRefreshEventListener(OnRefreshEventListener onRefreshEventListener) {
		this.onRefreshEventListener = onRefreshEventListener;
	}

	public String getUpdatableFormString() {
		return updatableFormString;
	}

	public void setUpdatableFormString(String updatableFormString) {
		this.updatableFormString = updatableFormString;
	}

}
