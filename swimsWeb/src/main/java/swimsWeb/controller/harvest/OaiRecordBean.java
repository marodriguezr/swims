/**
 * 
 */
package swimsWeb.controller.harvest;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import swimsEJB.model.harvest.managers.OaiRecordManager;


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
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
