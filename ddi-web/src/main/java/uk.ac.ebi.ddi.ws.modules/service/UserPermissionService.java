package uk.ac.ebi.ddi.ws.modules.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ws.modules.error.exception.UnauthorizedException;
import uk.ac.ebi.ddi.ws.modules.model.Role;

import java.net.URI;

@Service
public class UserPermissionService {

    @Value("${ddi.common.profile-service.endpoint}")
    private String profileServiceEndpoint;

    private RestTemplate restTemplate = new RestTemplate();

    public void hasRole(Role requiredRole, String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(profileServiceEndpoint)
                .path("/api/user/current");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-auth-token", accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        URI uri = builder.build().toUri();
        ResponseEntity<JsonNode> response = restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class);
        JsonNode userInfo = response.getBody();
        if (userInfo.has("roles")) {
            String[] roles = userInfo.get("roles").asText().split(",");
            for (String role : roles) {
                if (role.equals(requiredRole.name())) {
                    return;
                }
            }
        }
        throw new UnauthorizedException();
    }
}
