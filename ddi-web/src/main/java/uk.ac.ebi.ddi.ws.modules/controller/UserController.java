package uk.ac.ebi.ddi.ws.modules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ddi.security.model.*;
import uk.ac.ebi.ddi.service.db.model.dataset.MongoUser;
import uk.ac.ebi.ddi.service.db.repo.database.MongoUserDetailsRepository;

@RestController
public class UserController {

	@Autowired
	MongoUserDetailsRepository mongoUserDetailsRepository;

	@RequestMapping(value = "/api/user/current", method = RequestMethod.GET)
	@CrossOrigin
	public MongoUser getCurrent() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		/*if (authentication instanceof UserAuthentication) {
			MongoUser user = ((UserAuthentication) authentication).getDetails();
			return mongoUserDetailsRepository.findByUserId(user.getUserId());
		}*/
		return new MongoUser(); //anonymous user support
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.GET)
	@CrossOrigin
	public MongoUser getUser(@RequestParam(value="username", required=true) String username) {
		return mongoUserDetailsRepository.findPublicById(username);
	}

	//@RequestMapping(value = "/api/mongo", method = Re
	// questMethod.GET)
	//@CrossOrigin
	//public MongoUser getMongoUser() {
	//	return mongoUserDetailsRepository.findByUserId("0");
	//}

	@RequestMapping(value = "/api/users/count", method = RequestMethod.GET)
	@CrossOrigin
	public long getUsersCount() {
		return mongoUserDetailsRepository.count();
	}

}
