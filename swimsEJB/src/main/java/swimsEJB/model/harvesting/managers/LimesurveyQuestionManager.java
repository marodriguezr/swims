package swimsEJB.model.harvesting.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import swimsEJB.model.core.managers.DaoManager;
import swimsEJB.model.harvesting.dtos.LimesurveyQuestionDto;
import swimsEJB.model.harvesting.dtos.LinkableLimesurveyQuestionDto;
import swimsEJB.model.harvesting.entities.LimesurveyQuestion;
import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.services.LimesurveyService;

/**
 * Session Bean implementation class QuestionManager
 */
@Stateless
@LocalBean
public class LimesurveyQuestionManager {

	@EJB
	private DaoManager daoManager;
	@EJB
	private StudyVariableManager studyVariableManager;

	/**
	 * Default constructor.
	 */
	public LimesurveyQuestionManager() {
		// TODO Auto-generated constructor stub
	}

	public LimesurveyQuestion createOneQuestion(String limesurveyQuestionTitle, int limesurveySurveyId,
			int limesurveyQuestionId, StudyVariable studyVariable) throws Exception {
		LimesurveyQuestion question = new LimesurveyQuestion();
		question.setLimesurveyQuestionTitle(limesurveyQuestionTitle);
		question.setLimesurveySurveyId(limesurveySurveyId);
		question.setLimesurveyQuestionId(limesurveyQuestionId);
		question.setStudyVariable(studyVariable);

		question.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		question.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return (LimesurveyQuestion) daoManager.createOne(question);
	}

	public LimesurveyQuestion findOneQuestionById(int id) throws Exception {
		return (LimesurveyQuestion) daoManager.findOneById(LimesurveyQuestion.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<LimesurveyQuestion> findAllQuestionsByLimesurveySurveyId(int limesurveySurveyId) {
		return daoManager.findManyWhere(LimesurveyQuestion.class, "o.limesurveySurveyId = " + limesurveySurveyId, null);
	}

	public List<LinkableLimesurveyQuestionDto> findAllLinkableLimesurveyQuestionDtos(Integer limesurveySurveyId)
			throws Exception {
		if (limesurveySurveyId == null) {
			throw new Exception("Survey id cannot be null.");
		}
		HashMap<Integer, LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtosMap = new HashMap<>();
		HashMap<Integer, LimesurveyQuestion> alreadyRegisteredQuestionsMap = new HashMap<>();
		for (LimesurveyQuestion limesurveyQuestion : findAllQuestionsByLimesurveySurveyId(limesurveySurveyId)) {
			alreadyRegisteredQuestionsMap.put(limesurveyQuestion.getLimesurveyQuestionId(), limesurveyQuestion);
		}
		List<LimesurveyQuestionDto> linkableChildLimesurveyQuestionDtos = new ArrayList<>();

		for (LimesurveyQuestionDto limesurveyQuestionDto : LimesurveyService.listQuestions(limesurveySurveyId)
				.values()) {
			if (alreadyRegisteredQuestionsMap.containsKey(limesurveyQuestionDto.getId()))
				continue;
			if (limesurveyQuestionDto.getParentQid() != 0) {
				linkableChildLimesurveyQuestionDtos.add(limesurveyQuestionDto);
				continue;
			}
			LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto = new LinkableLimesurveyQuestionDto();
			linkableLimesurveyQuestionDto.setLinkableParentLimesurveyQuestionDto(limesurveyQuestionDto);
			linkableLimesurveyQuestionDtosMap.put(limesurveyQuestionDto.getId(), linkableLimesurveyQuestionDto);
		}

		for (LimesurveyQuestionDto limesurveyQuestionDto : linkableChildLimesurveyQuestionDtos) {
			LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto = linkableLimesurveyQuestionDtosMap
					.get(limesurveyQuestionDto.getParentQid());
			if (linkableLimesurveyQuestionDto == null)
				continue;
			List<LimesurveyQuestionDto> limesurveyQuestionDtos = linkableLimesurveyQuestionDto
					.getLinkableChildLimesurveyQuestionDtos() == null ? new ArrayList<>()
							: new ArrayList<>(linkableLimesurveyQuestionDto.getLinkableChildLimesurveyQuestionDtos());
			limesurveyQuestionDtos.add(limesurveyQuestionDto);
			linkableLimesurveyQuestionDto.setLinkableChildLimesurveyQuestionDtos(limesurveyQuestionDtos);
		}

		return new ArrayList<>(linkableLimesurveyQuestionDtosMap.values());
	}

	public List<LimesurveyQuestion> createManyLimesurveyQuestions(StudyVariable studyVariable,
			List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos) throws Exception {
		List<LimesurveyQuestion> createdLimesurveyQuestions = new ArrayList<>();
		for (LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto : linkableLimesurveyQuestionDtos) {
//			createdLimesurveyQuestions.add(createOneQuestion(
//					linkableLimesurveyQuestionDto.getLinkableParentLimesurveyQuestionDto().getTitle(),
//					linkableLimesurveyQuestionDto.getLinkableParentLimesurveyQuestionDto().getSid(),
//					linkableLimesurveyQuestionDto.getLinkableParentLimesurveyQuestionDto().getId(), studyVariable));
			
//			for (LimesurveyQuestionDto limesurveyQuestionDto : linkableLimesurveyQuestionDto
//					.getLinkableChildLimesurveyQuestionDtos()) {
//				createdLimesurveyQuestions.add(createOneQuestion(limesurveyQuestionDto.getTitle(),
//						limesurveyQuestionDto.getSid(), limesurveyQuestionDto.getId(), studyVariable));
//			}
		}

		return createdLimesurveyQuestions;
	}

	public List<LimesurveyQuestion> createManyLimesurveyQuestions(String studyVariableId,
			List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos) throws Exception {
		StudyVariable studyVariable = studyVariableManager.findOneStudyVariableById(studyVariableId);
		if (studyVariable == null)
			throw new Exception("La variable de estudio no está registrada.");
		return createManyLimesurveyQuestions(studyVariableId, linkableLimesurveyQuestionDtos);
	}
}
