package swimsEJB.model.core.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import static swimsEJB.constants.SystemEnvironmentVariables.TABLEAU_CLIENT_ID;
import static swimsEJB.constants.SystemEnvironmentVariables.TABLEAU_USER;
import static swimsEJB.constants.SystemEnvironmentVariables.TABLEAU_SECRET;
import static swimsEJB.constants.SystemEnvironmentVariables.TABLEAU_SECRET_ID;

@Stateless
@LocalBean
public class TableauJWTManager {

	public TableauJWTManager() {
	}

	public String getTableauJWT() throws Exception {
		if (TABLEAU_CLIENT_ID == null || TABLEAU_USER == null || TABLEAU_SECRET_ID == null || TABLEAU_SECRET == null)
			throw new Exception("Variables de Tableau no registradas de forma adecuada.");
		if (TABLEAU_CLIENT_ID.isBlank() || TABLEAU_USER.isBlank() || TABLEAU_SECRET_ID.isBlank()
				|| TABLEAU_SECRET.isBlank())
			throw new Exception("Variables de Tableau no registradas de forma adecuada.");

		List<String> scopes = new ArrayList<>(Arrays.asList("tableau:views:embed"));
		JWSSigner signer = new MACSigner(TABLEAU_SECRET);
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).keyID(TABLEAU_SECRET_ID)
				.customParam("iss", TABLEAU_CLIENT_ID).build();
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().issuer(TABLEAU_CLIENT_ID)
				.expirationTime(new Date(new Date().getTime() + 60 * 1000)) // expires in 1 minute
				.jwtID(UUID.randomUUID().toString()).audience("tableau").subject(TABLEAU_USER).claim("scp", scopes)
				.build();
		SignedJWT signedJWT = new SignedJWT(header, claimsSet);
		signedJWT.sign(signer);

		return signedJWT.serialize();
	}
}
