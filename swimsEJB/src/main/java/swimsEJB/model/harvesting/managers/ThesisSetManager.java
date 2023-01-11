package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.ThesisSet;

/**
 * Session Bean implementation class OaiSetManager
 */
@Stateless
@LocalBean
public class ThesisSetManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public ThesisSetManager() {
		// TODO Auto-generated constructor stub
	}
	

	public ThesisSet createOneOaiSet(String identifier, String name) throws Exception {
		ThesisSet oaiSet;
		oaiSet = new ThesisSet();

		oaiSet.setId(identifier);
		oaiSet.setName(name);
		oaiSet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		oaiSet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		oaiSet.setIsActive(true);

		try {
			oaiSet = (ThesisSet) daoManager.createOne(oaiSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del Set OAI.");
		}

		return oaiSet;
	}

	@SuppressWarnings("unchecked")
	public List<ThesisSet> findAllOaiSets() {
		return daoManager.findAll(ThesisSet.class);
	}

	public ThesisSet findOneOaiSetById(String identifier) throws Exception {
		return (ThesisSet) daoManager.findOneById(ThesisSet.class, identifier);
	}

	public ThesisSet updateOneOaiSetById(String identifier, String name) throws Exception {
		ThesisSet oaiSet = findOneOaiSetById(identifier);
		if (oaiSet == null)
			throw new Exception("El Set OAI que ha especificado no existe.");
		oaiSet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (ThesisSet) daoManager.updateOne(oaiSet);
	}

	public ThesisSet deleteOneOaiSetById(String identifier) throws Exception {
		return (ThesisSet) daoManager.deleteOneById(ThesisSet.class, identifier);
	}
}
