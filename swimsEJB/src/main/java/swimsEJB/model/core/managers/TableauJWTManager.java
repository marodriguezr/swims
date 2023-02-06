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

@Stateless
@LocalBean
public class TableauJWTManager {

    public TableauJWTManager() {
    }

    public String getTableauJWT() throws Exception {
        String secret = "L5lr/0Z1UQQRSDro4IVNdMdNdFGqT5JaLHJPTzqsm+Y=";
        String kid = "ae15681e-b733-4253-b58a-8510255d3a42";
        String clientId = "bd21eb52-2a10-4043-8425-a1abe52602c8";
        List<String> scopes = new ArrayList<>(Arrays.asList("tableau:views:embed"));
        String username = "miguelrodrii@outlook.com";
        JWSSigner signer = new MACSigner(secret);
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).keyID(kid).customParam("iss", clientId).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(clientId).expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .jwtID(UUID.randomUUID().toString())
                .audience("tableau")
                .subject(username)
                .claim("scp", scopes)
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
        // model.addAttribute("token", signedJWT.serialize());
    }
}
