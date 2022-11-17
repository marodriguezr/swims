package swimsEJB.model.harvesting.managers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.ThesisAssignment;

/**
 * Session Bean implementation class ThesisAsignmentManager
 */
@Stateless
@LocalBean
public class ThesisAssignmentManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public ThesisAssignmentManager() {
		// TODO Auto-generated constructor stub
	}

	public List<ThesisAssignment> findThesisAssignmentsByUserId(int userId) {
		@SuppressWarnings("unchecked")
		List<ThesisAssignment> foundThesisAssignments = daoManager.findManyWhere(ThesisAssignment.class,
				"o.userId = " + userId, null);
		return foundThesisAssignments;
	}

	public List<ThesisAssignment> findUndispatchedThesisAssignmentsByUserId(int userId) {
		@SuppressWarnings("unchecked")
		List<ThesisAssignment> foundThesisAssignments = daoManager.findManyWhere(ThesisAssignment.class,
				"o.userId = " + userId + "AND o.isDispatched = false", null);
		return foundThesisAssignments;
	}

	public int countThesisAssignmentsByUserId(int userId) {
		return findThesisAssignmentsByUserId(userId).size();
	}

	public int countUndispatchedThesisAssignmentsByUserId(int userId) {
		return findUndispatchedThesisAssignmentsByUserId(userId).size();
	}

}
