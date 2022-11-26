package swimsEJB.model.harvesting.managers;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Question;

/**
 * Session Bean implementation class QuestionManager
 */
@Stateless
@LocalBean
public class QuestionManager {
	
	@EJB
	private DaoManager daoManager;

    /**
     * Default constructor. 
     */
    public QuestionManager() {
        // TODO Auto-generated constructor stub
    }

}
