/**
 * 
 */
package swimsWeb.controller.harvesting;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.managers.OaiRecordManager;


@Named("oaiRecordBean")
@SessionScoped
/**
 * @author miguel
 *
 */
public class OaiRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private OaiRecordManager oaiRecordManager;

	/**
	 * 
	 */
	public OaiRecordBean() {
		// TODO Auto-generated constructor stub
	}

	public String getMessage() {
		return "HiMom";
	}
	
	public void actionListener() {
		try {
			this.oaiRecordManager.findAllCISICOaiRecords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
