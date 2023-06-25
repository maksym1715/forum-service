package telran.java47.securety.model;

public enum UserRoles {
	ADMINISTRATOR, USER, MODERATOR;
	
	public String getRoleName() {
        return name().toUpperCase();
    }
}
