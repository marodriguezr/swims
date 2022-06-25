package swimsEJB.model.core.managers;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.entities.Sysparam;

/**
 * Session Bean implementation class SysparamManager
 */
@Stateless
@LocalBean
public class SysparamManager {

	@EJB
	private DaoManager daoManager;
	
    /**
     * Default constructor. 
     */
    public SysparamManager() {
        // TODO Auto-generated constructor stub
    }

    public Sysparam createOneSysparam(String key, String value) throws Exception {
    	Sysparam sysparam = new Sysparam();
    	sysparam.setKey(key);
    	sysparam.setValue(value);
    	try {
			return (Sysparam) daoManager.createOne(sysparam);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del parámetro del sistema.");
		}
    }
    
    public Sysparam findOneByKey(String key) throws Exception {
    	return (Sysparam) daoManager.findOneById(Sysparam.class, key);
    }
}
