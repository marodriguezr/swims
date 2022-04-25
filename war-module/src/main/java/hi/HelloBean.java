package hi;

import java.io.Serializable;

import javax.inject.Named;

import ejb2.HiManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;

@Named("helloworld")
@ViewScoped
public class HelloBean implements Serializable {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private HiManager hm;
	
	private int id;
	private String name;

public String getMessage() {
      return "Hello World!";
   }

	@PostConstruct
	public void onInit() {
		id = -1;
		name = "";
	}
	
	public void actionListener() {
		hm.add(id, name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}