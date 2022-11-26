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
import swimsEJB.model.harvesting.entities.SurveyAssignment;
import swimsEJB.model.harvesting.entities.ThesisAssignment;

/**
 * Session Bean implementation class SurveyAssignmentManager
 */
@Stateless
@LocalBean
public class SurveyAssignmentManager {

	@EJB
	private DaoManager daoManager;

	/**
	 * Default constructor.
	 */
	public SurveyAssignmentManager() {
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

	public SurveyAssignment createOneSurveyAssignment(int limesurveySurveyId,
			String limesurveySurveyAccessToken, ThesisAssignment thesisAssignment) throws Exception {
		SurveyAssignment SurveyAssignment = new SurveyAssignment();
		SurveyAssignment.setLimesurveySurveyId(limesurveySurveyId);
		SurveyAssignment.setLimesurveySurveyToken(limesurveySurveyAccessToken);
		SurveyAssignment.setThesisAssignment(thesisAssignment);
		SurveyAssignment.setIsDispatched(false);
		SurveyAssignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		SurveyAssignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (SurveyAssignment) daoManager.createOne(SurveyAssignment);
	}

	@SuppressWarnings("unchecked")
	public List<SurveyAssignment> findAllSurveyAssignmentsByThesisAssignementId(
			int thesisAssignementId) {
		List<SurveyAssignment> SurveyAssignments = daoManager.findManyWhere(
				SurveyAssignment.class, "o.thesisAssignment.id = " + thesisAssignementId, null);
		return SurveyAssignments;
	}

	@SuppressWarnings("unchecked")
	public List<SurveyAssignment> findAllUndispatchedSurveyAssignmentsByThesisAssignementId(
			int thesisAssignementId) {
		List<SurveyAssignment> SurveyAssignments = daoManager.findManyWhere(
				SurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId + " and o.isDispatched = false", null);
		return SurveyAssignments;
	}
}
