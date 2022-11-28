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
import swimsEJB.model.harvesting.entities.Response;
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
	@EJB
	private ResponseManager responseManager;

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
				"select lsa.limesurvey_survey_id from harvesting.thesis_assignments ta, harvesting.survey_assignments lsa "
						+ "where ta.id = lsa.thesis_assignment_id and ta.oai_record_id = '" + oaiRecordId + "'");
		List<Object> objects = query.getResultList();
		List<Integer> alreadyPresentlimesurveySurveyIds = new ArrayList<>();
		for (Object object : objects) {
			alreadyPresentlimesurveySurveyIds.add(Integer.parseInt(object.toString()));
		}
		return alreadyPresentlimesurveySurveyIds;
	}

	public SurveyAssignment createOneSurveyAssignment(int limesurveySurveyId, String limesurveySurveyAccessToken,
			ThesisAssignment thesisAssignment) throws Exception {
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
	public List<SurveyAssignment> findAllSurveyAssignmentsByThesisAssignementId(int thesisAssignementId) {
		List<SurveyAssignment> SurveyAssignments = daoManager.findManyWhere(SurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId, null);
		return SurveyAssignments;
	}

	@SuppressWarnings("unchecked")
	public List<SurveyAssignment> findAllUndispatchedSurveyAssignmentsByThesisAssignementId(int thesisAssignementId) {
		List<SurveyAssignment> SurveyAssignments = daoManager.findManyWhere(SurveyAssignment.class,
				"o.thesisAssignment.id = " + thesisAssignementId + " and o.isDispatched = false", null);
		return SurveyAssignments;
	}

	public SurveyAssignment findOneSurveyAssignmentById(int id) throws Exception {
		return (SurveyAssignment) daoManager.findOneById(SurveyAssignment.class, id);
	}

	public SurveyAssignment updateOneSurveyAssignment(SurveyAssignment surveyAssignment,
			ThesisAssignment thesisAssignment, Integer limesurveySurveyId, String limesurveySurveyToken,
			Boolean isDispatched) throws Exception {
		if (thesisAssignment != null)
			surveyAssignment.setThesisAssignment(thesisAssignment);
		if (limesurveySurveyId != null)
			surveyAssignment.setLimesurveySurveyId(limesurveySurveyId);
		if (limesurveySurveyToken != null)
			surveyAssignment.setLimesurveySurveyToken(limesurveySurveyToken);
		if (isDispatched != null)
			surveyAssignment.setIsDispatched(isDispatched);
		return (SurveyAssignment) daoManager.updateOne(surveyAssignment);
	}

	public SurveyAssignment updateOneSurveyAssignmentById(int id, ThesisAssignment thesisAssignment,
			Integer limesurveySurveyId, String limesurveySurveyToken, Boolean isDispatched) throws Exception {
		SurveyAssignment surveyAssignment = findOneSurveyAssignmentById(id);
		if (surveyAssignment == null)
			throw new Exception("La Asignación de Encuesta especificada no se encuentra registrada.");
		return updateOneSurveyAssignment(surveyAssignment, thesisAssignment, limesurveySurveyId, limesurveySurveyToken,
				isDispatched);
	}

	public SurveyAssignment updateOneSurveyAssignmentAsDispatched(SurveyAssignment surveyAssignment) throws Exception {
		return updateOneSurveyAssignment(surveyAssignment, null, null, null, true);
	}

	public SurveyAssignment updateOneSurveyAssignmentAsDispatchedById(int id) throws Exception {
		return updateOneSurveyAssignmentById(id, null, null, null, true);
	}

	public SurveyAssignment dispatchSurvey(SurveyAssignment surveyAssignment) throws Exception {
		List<Response> responses = responseManager
				.createManyResponsesByLimesurveySurveyIdAndLimesurveySurveyToken(surveyAssignment);
		if (responses.isEmpty())
			throw new Exception("Ha ocurrido un error en el proceso de despacho.");
		return updateOneSurveyAssignmentAsDispatched(surveyAssignment);
	}
}
