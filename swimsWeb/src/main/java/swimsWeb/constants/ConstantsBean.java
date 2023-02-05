package swimsWeb.constants;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import static swimsWeb.constants.Constants.*;

@Named
@RequestScoped
public class ConstantsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public ConstantsBean() {
		// TODO Auto-generated constructor stub
	}

	public int getIS_NUMERIC_CONTINUOUS_ID() {
		return IS_NUMERIC_CONTINUOUS_ID;
	}

	public int getIS_NUMERIC_DISCRETE_ID() {
		return IS_NUMERIC_DISCRETE_ID;
	}

	public int getIS_CATEGORICAL_NOMINAL_ID() {
		return IS_CATEGORICAL_NOMINAL_ID;
	}

	public int getIS_CATEGORICAL_ORDINAL_ID() {
		return IS_CATEGORICAL_ORDINAL_ID;
	}
}
