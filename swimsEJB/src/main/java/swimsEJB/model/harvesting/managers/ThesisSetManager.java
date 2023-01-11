package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.ThesisSet;

/**
 * Session Bean implementation class ThesisSetManager
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
	

	public ThesisSet createOneThesisSet(String identifier, String name) throws Exception {
		ThesisSet thesisSet;
		thesisSet = new ThesisSet();

		thesisSet.setId(identifier);
		thesisSet.setName(name);
		thesisSet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		thesisSet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		thesisSet.setIsActive(true);

		try {
			thesisSet = (ThesisSet) daoManager.createOne(thesisSet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Ha ocurrido un error en la creación del Set de Tesis.");
		}

		return thesisSet;
	}

	@SuppressWarnings("unchecked")
	public List<ThesisSet> findAllThesisSets() {
		return daoManager.findAll(ThesisSet.class);
	}

	public ThesisSet findOneThesisSetById(String identifier) throws Exception {
		return (ThesisSet) daoManager.findOneById(ThesisSet.class, identifier);
	}

	public ThesisSet updateOneThesisSetById(String identifier, String name) throws Exception {
		ThesisSet thesisSet = findOneThesisSetById(identifier);
		if (thesisSet == null)
			throw new Exception("El Set de Tesis que ha especificado no existe.");
		thesisSet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (ThesisSet) daoManager.updateOne(thesisSet);
	}

	public ThesisSet deleteOneThesisSetById(String identifier) throws Exception {
		return (ThesisSet) daoManager.deleteOneById(ThesisSet.class, identifier);
	}
}
