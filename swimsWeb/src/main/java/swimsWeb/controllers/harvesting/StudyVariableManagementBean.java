package swimsWeb.controllers.harvesting;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import swimsEJB.model.harvesting.entities.StudyVariable;
import swimsEJB.model.harvesting.entities.StudyVariableClass;
import swimsEJB.model.harvesting.managers.StudyVariableClassManager;
import swimsEJB.model.harvesting.managers.StudyVariableManager;
import swimsWeb.utilities.JSFMessages;

import static swimsWeb.constants.WebappPaths.HARVESTING_STUDY_VARIABLE_MANAGEMENT_WEBAPP_PATH;
import static swimsWeb.constants.Constants.*;

@Named
@SessionScoped
public class StudyVariableManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<StudyVariable> studyVariables;
	@EJB
	private StudyVariableManager studyVariableManager;
	private StudyVariable selectedStudyVariable;
	@EJB
	private StudyVariableClassManager studyVariableClassManager;
	private List<StudyVariableClass> studyVariableClasses;
	private String selectedVariableClassId;
	private boolean isCreateNewStudyVariableDialog;
	private Integer selectedStudyVariableTypeId;

	public StudyVariableManagementBean() {
		// TODO Auto-generated constructor stub
	}

	public String loadPage() {
		return HARVESTING_STUDY_VARIABLE_MANAGEMENT_WEBAPP_PATH + "?faces-redirect=true";
	}

	public void onPageLoad() {
		this.studyVariables = studyVariableManager.findAllStudyVariables();
		this.studyVariableClasses = studyVariableClassManager.findAllStudyVariableClasses();
	}

	public void openNew() {
		this.selectedStudyVariable = new StudyVariable();
		this.selectedStudyVariable.setId("");
		this.isCreateNewStudyVariableDialog = true;
		this.selectedStudyVariableTypeId = null;
	}

	public String renderStudyVariableType(StudyVariable studyVariable) {
		if (studyVariable.getIsCategoricalNominal())
			return "Categórica Nominal";
		if (studyVariable.getIsCategoricalOrdinal())
			return "Categórica Ordinal";
		if (studyVariable.getIsNumericContinuous())
			return "Numérica Contínua";
		return "Numérica Discreta";
	}

	public void saveStudyVariable() {
		System.out.println(-1);
		if (isCreateNewStudyVariableDialog) {
			System.out.println(0);
			if (selectedStudyVariable.getId() == null) {
				JSFMessages.WARN("Por favor ingrese un Identificador apropiado");
				return;
			}
			if (selectedStudyVariableTypeId == null) {
				JSFMessages.WARN("Por favor seleccione un Tipo");
				return;
			}
			System.out.println("1");
			if (selectedVariableClassId == null) {
				JSFMessages.WARN("Por favor seleccione una Clase");
				return;
			}
			try {
				StudyVariable createdStudyVariable = studyVariableManager.createOneStudyVariable(
						selectedStudyVariable.getId(), selectedStudyVariable.getName(),
						selectedStudyVariableTypeId == IS_NUMERIC_CONTINUOUS_ID,
						selectedStudyVariableTypeId == IS_NUMERIC_DISCRETE_ID,
						selectedStudyVariableTypeId == IS_CATEGORICAL_NOMINAL_ID,
						selectedStudyVariableTypeId == IS_CATEGORICAL_ORDINAL_ID, selectedVariableClassId);
				this.studyVariables.add(0, createdStudyVariable);
				JSFMessages.INFO("Variable de Estudio creada de forma exitosa.");
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSFMessages.ERROR(e.getMessage());
				return;
			}
		}
		try {
			StudyVariable updatedStudyVariable = studyVariableManager.updateOneStudyVariableById(
					selectedStudyVariable.getId(), selectedStudyVariable.getName(),
					selectedStudyVariableTypeId == IS_NUMERIC_CONTINUOUS_ID,
					selectedStudyVariableTypeId == IS_NUMERIC_DISCRETE_ID,
					selectedStudyVariableTypeId == IS_CATEGORICAL_NOMINAL_ID,
					selectedStudyVariableTypeId == IS_CATEGORICAL_ORDINAL_ID, selectedVariableClassId);

			this.studyVariables.removeIf(arg0 -> arg0.getId() == updatedStudyVariable.getId());
			this.studyVariables.add(0, updatedStudyVariable);
			JSFMessages.INFO("Variable de estudio actualizada de forma exitosa.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFMessages.ERROR(e.getMessage());
		}
	}

	public void setSelectedStudyVariableWithNewStudyVariableFlag(StudyVariable studyVariable) {
		this.selectedStudyVariable = studyVariable;
		this.isCreateNewStudyVariableDialog = false;
		if (studyVariable.getIsNumericContinuous())
			this.selectedStudyVariableTypeId = IS_NUMERIC_CONTINUOUS_ID;
		if (studyVariable.getIsNumericDiscrete())
			this.selectedStudyVariableTypeId = IS_NUMERIC_DISCRETE_ID;
		if (studyVariable.getIsCategoricalOrdinal())
			this.selectedStudyVariableTypeId = IS_CATEGORICAL_ORDINAL_ID;
		if (studyVariable.getIsCategoricalNominal())
			this.selectedStudyVariableTypeId = IS_CATEGORICAL_NOMINAL_ID;
		this.selectedVariableClassId = studyVariable.getStudyVariableClass().getId();

	}

	public List<StudyVariable> getStudyVariables() {
		return studyVariables;
	}

	public void setStudyVariables(List<StudyVariable> studyVariables) {
		this.studyVariables = studyVariables;
	}

	public StudyVariable getSelectedStudyVariable() {
		return selectedStudyVariable;
	}

	public void setSelectedStudyVariable(StudyVariable selectedStudyVariable) {
		this.selectedStudyVariable = selectedStudyVariable;
	}

	public List<StudyVariableClass> getStudyVariableClasses() {
		return studyVariableClasses;
	}

	public void setStudyVariableClasses(List<StudyVariableClass> studyVariableClasses) {
		this.studyVariableClasses = studyVariableClasses;
	}

	public String getSelectedVariableClassId() {
		return selectedVariableClassId;
	}

	public void setSelectedVariableClassId(String selectedVariableClassId) {
		this.selectedVariableClassId = selectedVariableClassId;
	}

	public boolean isCreateNewStudyVariableDialog() {
		return isCreateNewStudyVariableDialog;
	}

	public void setCreateNewStudyVariableDialog(boolean isCreateNewStudyVariableDialog) {
		this.isCreateNewStudyVariableDialog = isCreateNewStudyVariableDialog;
	}

	public Integer getSelectedStudyVariableTypeId() {
		return selectedStudyVariableTypeId;
	}

	public void setSelectedStudyVariableTypeId(Integer selectedStudyVariableTypeId) {
		this.selectedStudyVariableTypeId = selectedStudyVariableTypeId;
	}

}
