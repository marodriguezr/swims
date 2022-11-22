package swimsWeb.converters;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import swimsEJB.model.auth.dtos.UserDto;
import swimsEJB.model.auth.managers.UserManager;

@Named("userDtoConverter")
@FacesConverter(forClass = UserDto.class)
@RequestScoped
public class UserDtoConverter implements Converter<UserDto> {
	@EJB
	private UserManager userManager;

	@Override
	public UserDto getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && value.trim().length() > 0 && userManager != null) {
			try {
				UserDto foundUserDto = userManager.findOneUserById(Integer.parseInt(value));
				return foundUserDto;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, UserDto value) {
		try {
			if (value != null) {
				return String.valueOf(value.getId());
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}
}
