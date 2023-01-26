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
		HashMap<Integer, LimesurveyQuestionDto> limesurveyQuestionDtosMap = new HashMap<>();
		for (LimesurveyQuestionDto limesurveyQuestionDto : LimesurveyService.listQuestions(limesurveySurveyId)) {
			limesurveyQuestionDtosMap.put(limesurveyQuestionDto.getId(), limesurveyQuestionDto);
		}

		for (LimesurveyQuestionDto limesurveyQuestionDto : limesurveyQuestionDtosMap.values()) {
			if (alreadyRegisteredQuestionsMap.containsKey(limesurveyQuestionDto.getId())) {
				continue;
			}

			if (limesurveyQuestionDto.getParentQid() != 0) {
				LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto = linkableLimesurveyQuestionDtosMap
						.containsKey(limesurveyQuestionDto.getParentQid())
								? linkableLimesurveyQuestionDtosMap.get(limesurveyQuestionDto.getParentQid())
								: new LinkableLimesurveyQuestionDto();
				if (linkableLimesurveyQuestionDto.getLinkableParentLimesurveyQuestionDto() == null)
					linkableLimesurveyQuestionDto.setLinkableParentLimesurveyQuestionDto(
							limesurveyQuestionDtosMap.get(limesurveyQuestionDto.getParentQid()));
				List<LimesurveyQuestionDto> limesurveyQuestionDtos = linkableLimesurveyQuestionDto
						.getLinkableChildLimesurveyQuestionDtos() == null ? new ArrayList<>()
								: new ArrayList<>(
										linkableLimesurveyQuestionDto.getLinkableChildLimesurveyQuestionDtos());
				limesurveyQuestionDtos.add(limesurveyQuestionDto);
				linkableLimesurveyQuestionDto.setLinkableChildLimesurveyQuestionDtos(limesurveyQuestionDtos);
				linkableLimesurveyQuestionDto.setParentQuestionAlreadyRegistered(true);
				linkableLimesurveyQuestionDtosMap.put(limesurveyQuestionDto.getParentQid(),
						linkableLimesurveyQuestionDto);
				continue;
			}

			if (linkableLimesurveyQuestionDtosMap.containsKey(limesurveyQuestionDto.getId()))
				continue;

			LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto = new LinkableLimesurveyQuestionDto();
			linkableLimesurveyQuestionDto.setLinkableParentLimesurveyQuestionDto(limesurveyQuestionDto);
			linkableLimesurveyQuestionDtosMap.put(limesurveyQuestionDto.getId(), linkableLimesurveyQuestionDto);

		}

		return new ArrayList<>(linkableLimesurveyQuestionDtosMap.values());
	}

	public List<LimesurveyQuestionDto> linkableLimesurveyQuestionDtoToLimesurveyQuestionDtos(
			LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto) {
		List<LimesurveyQuestionDto> limesurveyQuestionDtos = new ArrayList<>();

//		if (linkableLimesurveyQuestionDto.getLinkableParentLimesurveyQuestionDto() != null)
//			limesurveyQuestionDtos.add(linkableLimesurveyQuestionDto.getLinkableParentLimesurveyQuestionDto());

		if (linkableLimesurveyQuestionDto.getLinkableChildLimesurveyQuestionDtos() == null)
			return limesurveyQuestionDtos;

		if (linkableLimesurveyQuestionDto.getLinkableChildLimesurveyQuestionDtos().isEmpty())
			return limesurveyQuestionDtos;

		limesurveyQuestionDtos.addAll(linkableLimesurveyQuestionDto.getLinkableChildLimesurveyQuestionDtos());
		return limesurveyQuestionDtos;
	}

	public List<LimesurveyQuestionDto> linkableLimesurveyQuestionDtosToLimesurveyQuestionDtos(
			List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos) {

		List<LimesurveyQuestionDto> limesurveyQuestionDtos = new ArrayList<>();

		for (LinkableLimesurveyQuestionDto linkableLimesurveyQuestionDto : linkableLimesurveyQuestionDtos) {
			limesurveyQuestionDtos
					.addAll(linkableLimesurveyQuestionDtoToLimesurveyQuestionDtos(linkableLimesurveyQuestionDto));
		}

		return limesurveyQuestionDtos;
	}

	public List<LimesurveyQuestion> createManyLimesurveyQuestions(StudyVariable studyVariable,
			List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos) throws Exception {
		List<LimesurveyQuestion> createdLimesurveyQuestions = new ArrayList<>();
		if (linkableLimesurveyQuestionDtos == null) {
			throw new Exception("Linkable limesurvey question dtos cannot be null.");
		}

		List<LimesurveyQuestionDto> toLinkLimesurveyQuestionDtos = linkableLimesurveyQuestionDtosToLimesurveyQuestionDtos(
				linkableLimesurveyQuestionDtos);
		for (LimesurveyQuestionDto toLinkLimesurveyQuestionDto : toLinkLimesurveyQuestionDtos) {
			createdLimesurveyQuestions.add(createOneQuestion(toLinkLimesurveyQuestionDto.getTitle(),
					toLinkLimesurveyQuestionDto.getSid(), toLinkLimesurveyQuestionDto.getId(), studyVariable));
		}
		return createdLimesurveyQuestions;
	}

	public List<LimesurveyQuestion> createManyLimesurveyQuestions(String studyVariableId,
			List<LinkableLimesurveyQuestionDto> linkableLimesurveyQuestionDtos) throws Exception {
		StudyVariable studyVariable = studyVariableManager.findOneStudyVariableById(studyVariableId);
		if (studyVariable == null)
			throw new Exception("La variable de estudio no está registrada.");
		return createManyLimesurveyQuestions(studyVariable, linkableLimesurveyQuestionDtos);
	}
}
