package swimsEJB.model.harvest.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvest.entities.OaiSet;

/**
 * Session Bean implementation class OaiSetManager
 */
@Stateless
@LocalBean
public class OaiSetManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public OaiSetManager() {
		// TODO Auto-generated constructor stub
	}
	

	public OaiSet createOneOaiSet(String identifier, String name, int createdBy) throws Exception {
		OaiSet oaiSet;
		oaiSet = new OaiSet();

		oaiSet.setId(identifier);
		oaiSet.setName(name);
		oaiSet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		oaiSet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		oaiSet.setIsActive(true);

		try {
			oaiSet = (OaiSet) daoManager.createOne(oaiSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del Set OAI.");
		}

		return oaiSet;
	}

	@SuppressWarnings("unchecked")
	public List<OaiSet> findAllOaiSets() {
		return daoManager.findAll(OaiSet.class);
	}

	public OaiSet findOneOaiSetById(String identifier) throws Exception {
		return (OaiSet) daoManager.findOneById(OaiSet.class, identifier);
	}

	public OaiSet updateOneOaiSetById(String identifier, String name) throws Exception {
		OaiSet oaiSet = findOneOaiSetById(identifier);
		if (oaiSet == null)
			throw new Exception("El Set OAI que ha especificado no existe.");
		oaiSet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (OaiSet) daoManager.updateOne(oaiSet);
	}

	public OaiSet deleteOneOaiSetById(String identifier) throws Exception {
		return (OaiSet) daoManager.deleteOneById(OaiSet.class, identifier);
	}
}
