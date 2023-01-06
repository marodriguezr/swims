package swimsEJB.model.harvesting.managers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.Beneficiary;

/**
 * Session Bean implementation class BeneficiaryManager
 */
@Stateless
@LocalBean
public class BeneficiaryManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public BeneficiaryManager() {
		// TODO Auto-generated constructor stub
	}

	public Beneficiary createOneBeneficiary(String name) throws Exception {
		if (name.isBlank())
			throw new Exception("Debe ingresar el Nombre del Beneficario.");
		Beneficiary newBeneficiary = new Beneficiary();
		newBeneficiary.setName(name);
		return (Beneficiary) daoManager.createOne(newBeneficiary);
	}

	public Beneficiary findOneBeneficiaryById(int id) throws Exception {
		return (Beneficiary) daoManager.findOneById(Beneficiary.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Beneficiary> findAllBeneficiaries() {
		return daoManager.findAll(Beneficiary.class);
	}
}
