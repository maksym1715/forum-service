package telran.java47.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.accounting.dao.UserAccountRepository;
import telran.java47.accounting.dto.RolesDto;
import telran.java47.accounting.dto.UserDto;
import telran.java47.accounting.dto.UserEditDto;
import telran.java47.accounting.dto.UserRegisterDto;
import telran.java47.accounting.dto.exceptions.UserExistsException;
import telran.java47.accounting.dto.exceptions.UserNotFoundException;
import telran.java47.accounting.model.UserAccount;




@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {
	
	final UserAccountRepository userAccountRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
	    if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
	        throw new UserExistsException("User already exists");
	    }
	    UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
	    String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
	    userAccount.setPassword(password);
	    userAccount.addRole("USER");
	    userAccountRepository.save(userAccount);
	    return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		userAccountRepository.deleteById(login);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        userAccount.setFirstName(userEditDto.getFirstName());
        userAccount.setLastName(userEditDto.getLastName());
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
		
	}

	@Override
	public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        if (isAddRole) {
            userAccount.addRole(role);
        } else {
            userAccount.removeRole(role);
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, RolesDto.class);
	}

	@Override
	public void changePassword(String loginString, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(loginString)
                .orElseThrow(UserNotFoundException::new);
		String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
    }

	@Override
	public void run(String... args) throws Exception {
		if(!userAccountRepository.existsById("admin")) {
			String password = BCrypt.hashpw("admin", BCrypt.gensalt());
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole("USER");
			userAccount.addRole("MODERATOR");
			userAccount.addRole("ADMINISTRATOR");
			userAccountRepository.save(userAccount);
		}
		
	}

}
