package swimsEJB.model.harvesting.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	@SuppressWarnings("unchecked")
	public int countUndispatchedThesisAssignmentsByUserId(int userId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery(
				"select count (distinct ta.thesis_record_id) from harvesting.thesis_assignments ta where ta.is_dispatched = false and ta.user_id = "
						+ userId);
		List<Object> objects = query.getResultList();
		return Integer.parseInt(objects.get(0).toString());
	}

	@SuppressWarnings("unchecked")
	public int countUndispatchedSurveysByUserId(int userId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery(
				"select count(ta) from harvesting.thesis_assignments ta where ta.is_dispatched = false and ta.user_id = "
						+ userId);
		List<Object> objects = query.getResultList();
		return Integer.parseInt(objects.get(0).toString());
	}

	@SuppressWarnings("unchecked")
	public List<Integer> filterLimesurveySurveysByAssignedLimesurveySurveyIds(List<Integer> availableLimesurveySurveys,
			String oaiRecordId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery(
				"select ta.limesurvey_survey_id from harvesting.thesis_assignments ta where ta.thesis_record_id = '"
						+ oaiRecordId + "'");
		List<Object> objects = query.getResultList();
		List<Integer> alreadyPresentlimesurveySurveyIds = new ArrayList<>();
		for (Object object : objects) {
			alreadyPresentlimesurveySurveyIds.add(Integer.parseInt(object.toString()));
		}
		return availableLimesurveySurveys.stream().filter(arg0 -> !alreadyPresentlimesurveySurveyIds.contains(arg0))
				.collect(Collectors.toList());
	}
}
