package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.entities.LimesurveySurveyAssignment;
import swimsEJB.model.harvesting.entities.ThesisAssignment;

/**
 * Session Bean implementation class SurveyAssignmentManager
 */
@Stateless
@LocalBean
public class LimesurveySurveyAssignmentManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public LimesurveySurveyAssignmentManager() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findLimesurveySurveyIdsByOaiRecordId(String oaiRecordId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery(
				"select lsa.limesurvey_survey_id from harvesting.thesis_assignments ta, harvesting.limesurvey_survey_assignments lsa "
						+ "where ta.id = lsa.thesis_assignment_id and ta.oai_record_id = '" + oaiRecordId + "'");
		List<Object> objects = query.getResultList();
		List<Integer> alreadyPresentlimesurveySurveyIds = new ArrayList<>();
		for (Object object : objects) {
			alreadyPresentlimesurveySurveyIds.add(Integer.parseInt(object.toString()));
		}
		return alreadyPresentlimesurveySurveyIds;
	}

	public LimesurveySurveyAssignment createOneLimesurveySurveyAssignment(int limesurveySurveyId,
			String limesurveySurveyAccessToken, ThesisAssignment thesisAssignment) throws Exception {
		LimesurveySurveyAssignment limesurveySurveyAssignment = new LimesurveySurveyAssignment();
		limesurveySurveyAssignment.setLimesurveySurveyId(limesurveySurveyId);
		limesurveySurveyAssignment.setLimesurveySurveyToken(limesurveySurveyAccessToken);
		limesurveySurveyAssignment.setThesisAssignment(thesisAssignment);
		limesurveySurveyAssignment.setIsDispatched(false);
		limesurveySurveyAssignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		limesurveySurveyAssignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (LimesurveySurveyAssignment) daoManager.createOne(limesurveySurveyAssignment);
	}

	@SuppressWarnings("unchecked")
	public List<LimesurveySurveyAssignment> findAllLimesurveySurveyAssignmentsByThesisAssignementId(
			int thesisAssignementId) {
		List<LimesurveySurveyAssignment> limesurveySurveyAssignments = daoManager.findManyWhere(
				LimesurveySurveyAssignment.class, "o.thesisAssignment.id = " + thesisAssignementId, null);
		return limesurveySurveyAssignments;
	}

	@SuppressWarnings("unchecked")
	public List<LimesurveySurveyAssignment> findAllUndispatchedLimesurveySurveyAssignmentsByThesisAssignementId(
			int thesisAssignementId) {
		List<LimesurveySurveyAssignment> limesurveySurveyAssignments = daoManager.findManyWhere(
				LimesurveySurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId + " and o.isDispatched = false", null);
		return limesurveySurveyAssignments;
	}
}
