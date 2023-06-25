package telran.java47.securety.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


import telran.java47.securety.model.User;
import telran.java47.securety.model.UserRoles;

@Component
@Order(40)
public class DeleteUserFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		if (checkEndPoint(HttpMethod.resolve(request.getMethod()), path)) {
			User user = (User) request.getUserPrincipal();
			String[] arr = path.split("/");
			String userName = arr[arr.length - 1];
			if (!(user.getName().equalsIgnoreCase(userName)
					|| user.getRoles().contains(UserRoles.ADMINISTRATOR.name()))) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);

	}

	private boolean checkEndPoint(HttpMethod method, String path) {
		return method == HttpMethod.DELETE && path.matches("/account/user/\\w+/?");
	}

}
