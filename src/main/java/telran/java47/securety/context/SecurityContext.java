package telran.java47.securety.context;

import telran.java47.securety.model.User;

public interface SecurityContext {
	User addUserSession(String sessionId, User user);

	User removeUserSession(String sessionId);

	User getUserBySessionId(String sessionId);
}
