package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.auth.entities.User;
import swimsEJB.model.auth.managers.UserManager;
import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.ThesisRecordAssignedLimesurveySurveyIdsDto;
import swimsEJB.model.harvesting.entities.ThesisRecord;
import swimsEJB.model.harvesting.entities.views.UndispatchedSurveyAssignment;
import swimsEJB.model.harvesting.entities.views.UndispatchedThesisAssignment;
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
	private ThesisRecordManager thesisRecordManager;
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

	public int countUndispatchedThesisAssignmentsByUserId(int userId) {
		@SuppressWarnings("unchecked")
		List<UndispatchedThesisAssignment> undispatchedThesisAssignments = daoManager
				.findManyWhere(UndispatchedThesisAssignment.class, "o.userId = " + userId, null);
		return undispatchedThesisAssignments.size();
	}

	@SuppressWarnings("unchecked")
	public int countUndispatchedSurveysByUserId(int userId) {
		List<UndispatchedSurveyAssignment> undispatchedSurveyAssignments = daoManager
				.findManyWhere(UndispatchedSurveyAssignment.class, "o.userId = " + userId, null);
		return undispatchedSurveyAssignments.size();

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
				.findLimesurveySurveyIdsByThesisRecordId(oaiRecordId);
		return availableLimesurveySurveys.stream().filter(arg0 -> !alreadyPresentlimesurveySurveyIds.contains(arg0))
				.collect(Collectors.toList());
	}

	public ThesisAssignment createOneThesisAssignment(int userId, String oaiRecordId) throws Exception {
		User user = userManager.findOneUserById2(userId);
		if (user == null)
			throw new Exception("El usuario especificado no está registrado.");

		ThesisRecord thesisRecord = thesisRecordManager.findOneOaiRecordById(oaiRecordId);
		if (thesisRecord == null)
			throw new Exception("El registro especificado no está registrado. " + oaiRecordId);

		ThesisAssignment thesisAssignment = new ThesisAssignment();
		thesisAssignment.setUserId(user.getId());
		thesisAssignment.setThesisRecord(thesisRecord);
		thesisAssignment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		thesisAssignment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		return (ThesisAssignment) daoManager.createOne(thesisAssignment);
	}

	public ThesisAssignment findOneThesisAssignmentByOaiRecordIdAndUserId(String oaiRecordId, int userId) {
		return (ThesisAssignment) daoManager.findOneWhere(ThesisAssignment.class,
				"o.thesisRecord.id = '" + oaiRecordId + "' AND userId = " + userId);
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
			List<ThesisRecordAssignedLimesurveySurveyIdsDto> assignedLimesurveySurveyIds, int userId) throws Exception {
		User user = userManager.findOneUserById2(userId);
		if (user == null)
			throw new Exception("El usuario especificado no está registrado.");

		List<ThesisAssignment> thesisAssignments = new ArrayList<>();
		for (ThesisRecordAssignedLimesurveySurveyIdsDto oaiRecordAssignedLimesurveySurveyIds : assignedLimesurveySurveyIds) {
			for (int limesurveySurveyId : oaiRecordAssignedLimesurveySurveyIds.getAssignedLimesurveySurveyIds()) {

				ThesisAssignment thesisAssignment = findOneThesisAssignmentByOaiRecordIdAndUserId(
						oaiRecordAssignedLimesurveySurveyIds.getThesisRecord().getId(), userId);
				thesisAssignment = thesisAssignment == null
						? createOneThesisAssignment(user.getId(),
								oaiRecordAssignedLimesurveySurveyIds.getThesisRecord().getId())
						: thesisAssignment;
				limesurveySurveyAssignmentManager.createOneSurveyAssignment(limesurveySurveyId,
						LimesurveyService.addParticipant(limesurveySurveyId, user.getEmail()), thesisAssignment);
				thesisAssignments.add(thesisAssignment);
			}
		}
		return thesisAssignments;
	}

	public ThesisAssignment findOneThesisAssignemntById(int thesisAssignementId) throws Exception {
		return (ThesisAssignment) daoManager.findOneById(ThesisAssignment.class, thesisAssignementId);
	}

	@SuppressWarnings("unchecked")
	public List<ThesisAssignment> findAllUndispatchedThesisAssignments() throws Exception {
		List<ThesisAssignment> thesisAssignments = new ArrayList<>();
		List<UndispatchedThesisAssignment> undispatchedThesisAssignments = daoManager
				.findAll(UndispatchedThesisAssignment.class);
		for (UndispatchedThesisAssignment undispatchedThesisAssignment : undispatchedThesisAssignments) {
			ThesisAssignment foundThesisAssignment = this
					.findOneThesisAssignemntById(undispatchedThesisAssignment.getId());
			if (foundThesisAssignment != null)
				thesisAssignments.add(foundThesisAssignment);
		}

		return thesisAssignments;
	}

	@SuppressWarnings("unchecked")
	public List<ThesisAssignment> findAllUndispatchedThesisAssignmentsByUserId(int userId) throws Exception {
		List<ThesisAssignment> thesisAssignments = new ArrayList<>();
		List<UndispatchedThesisAssignment> undispatchedThesisAssignments = daoManager
				.findManyWhere(UndispatchedThesisAssignment.class, "o.userId = " + userId, null);
		for (UndispatchedThesisAssignment undispatchedThesisAssignment : undispatchedThesisAssignments) {
			ThesisAssignment foundThesisAssignment = this
					.findOneThesisAssignemntById(undispatchedThesisAssignment.getId());
			if (foundThesisAssignment != null)
				thesisAssignments.add(foundThesisAssignment);
		}

		return thesisAssignments;
	}
}
