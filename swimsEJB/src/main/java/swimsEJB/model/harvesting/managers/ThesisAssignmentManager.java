package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import swimsEJB.model.auth.entities.User;
import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.OaiRecordAssignedLimesurveySurveyIdsDto;
import swimsEJB.model.harvesting.entities.OaiRecord;
import swimsEJB.model.harvesting.entities.ThesisAssignment;
import swimsEJB.model.harvesting.services.LimesurveyService;

/**
 * Session Bean implementation class ThesisAsignmentManager
 */
@Stateless
@LocalBean
public class ThesisAssignmentManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private UserManager userManager;
	@EJB
	private OaiRecordManager oaiRecordManager;
	@EJB
	private LimesurveySurveyAssignmentManager limesurveySurveyAssignmentManager;

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

	public int countThesisAssignmentsByUserId(int userId) {
		return findThesisAssignmentsByUserId(userId).size();
	}

	@SuppressWarnings("unchecked")
	public int countUndispatchedThesisAssignmentsByUserId(int userId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery("select count (distinct ta.oai_record_id) "
				+ "from harvesting.thesis_assignments ta, harvesting.limesurvey_survey_assignments lsa "
				+ "where lsa.thesis_assignment_id = ta.id and lsa.is_dispatched = false and ta.user_id = " + userId);
		List<Object> objects = query.getResultList();
		return Integer.parseInt(objects.get(0).toString());
	}

	@SuppressWarnings("unchecked")
	public int countUndispatchedSurveysByUserId(int userId) {
		EntityManager entityManager = daoManager.getEntityManager();
		Query query = entityManager.createNativeQuery("select count(ta) "
				+ "from harvesting.thesis_assignments ta, harvesting.limesurvey_survey_assignments lsa "
				+ "where ta.id = lsa.thesis_assignment_id and lsa.is_dispatched = false and ta.user_id = " + userId);
		List<Object> objects = query.getResultList();
		return Integer.parseInt(objects.get(0).toString());
	}

	/**
	 * This method finds all the assigned survey ids for a given oaiRecordId and the
	 * compares the available survey ids to it in order to filter the survey ids
	 * that are already included or for instance that have already been assigned.
	 * 
	 * @param availableLimesurveySurveys
	 * @param oaiRecordId
	 * @return
	 */
	public List<Integer> filterLimesurveySurveysByAssignedLimesurveySurveyIds(List<Integer> availableLimesurveySurveys,
			String oaiRecordId) {
		List<Integer> alreadyPresentlimesurveySurveyIds = limesurveySurveyAssignmentManager
				.findLimesurveySurveyIdsByOaiRecordId(oaiRecordId);
		return availableLimesurveySurveys.stream().filter(arg0 -> !alreadyPresentlimesurveySurveyIds.contains(arg0))
				.collect(Collectors.toList());
	}

	public ThesisAssignment createOneThesisAssignment(int userId, String oaiRecordId) throws Exception {
		User user = userManager.findOneUserById2(userId);
		if (user == null)
			throw new Exception("El usuario especificado no está registrado.");

		OaiRecord oaiRecord = oaiRecordManager.findOneOaiRecordById(oaiRecordId);
		if (oaiRecord == null)
			throw new Exception("El registro especificado no está registrado. " + oaiRecordId);

		ThesisAssignment thesisAssignment = new ThesisAssignment();
		thesisAssignment.setUserId(user.getId());
		thesisAssignment.setOaiRecord(oaiRecord);
		thesisAssignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		thesisAssignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		return (ThesisAssignment) daoManager.createOne(thesisAssignment);
	}

	public ThesisAssignment findOneThesisAssignmentByOaiRecordIdAndUserId(String oaiRecordId, int userId) {
		return (ThesisAssignment) daoManager.findOneWhere(ThesisAssignment.class,
				"o.oaiRecord.id = '" + oaiRecordId + "' AND userId = " + userId);
	}

	/**
	 * This method creates many limesurvey assignemts.
	 * 
	 * @param assignedLimesurveySurveyIds An object with an OaiRecordDto and a list
	 *                                    of the limesurvey survey ids
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ThesisAssignment> createManyThesisAssignemnts(
			List<OaiRecordAssignedLimesurveySurveyIdsDto> assignedLimesurveySurveyIds, int userId) throws Exception {
		User user = userManager.findOneUserById2(userId);
		if (user == null)
			throw new Exception("El usuario especificado no está registrado.");

		List<ThesisAssignment> thesisAssignments = new ArrayList<>();
		for (OaiRecordAssignedLimesurveySurveyIdsDto oaiRecordAssignedLimesurveySurveyIds : assignedLimesurveySurveyIds) {
			for (int limesurveySurveyId : oaiRecordAssignedLimesurveySurveyIds.getAssignedLimesurveySurveyIds()) {

				ThesisAssignment thesisAssignment = findOneThesisAssignmentByOaiRecordIdAndUserId(
						oaiRecordAssignedLimesurveySurveyIds.getOaiRecord().getId(), userId);
				thesisAssignment = thesisAssignment == null
						? createOneThesisAssignment(user.getId(),
								oaiRecordAssignedLimesurveySurveyIds.getOaiRecord().getId())
						: thesisAssignment;
				limesurveySurveyAssignmentManager.createOneLimesurveySurveyAssignment(limesurveySurveyId,
						LimesurveyService.addParticipant(limesurveySurveyId, user.getEmail()), thesisAssignment);
				thesisAssignments.add(thesisAssignment);
			}
		}
		return thesisAssignments;
	}
}
