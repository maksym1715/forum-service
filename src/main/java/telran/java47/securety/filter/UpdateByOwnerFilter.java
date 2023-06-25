package telran.java47.securety.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import telran.java47.securety.model.HttpMethod;

@Component
@Order(30)
public class UpdateByOwnerFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		if (checkEndPoint(HttpMethod.valueOf(request.getMethod()), path)) {
			Principal principal = request.getUserPrincipal();
			String[] arr = path.split("/");
			String user = arr[arr.length - 1];
			if (!principal.getName().equalsIgnoreCase(user)) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);

	}

	private boolean checkEndPoint(HttpMethod method, String path) {
		return method == HttpMethod.PUT && path.matches("/account/user/\\w+/?")
				|| method == HttpMethod.POST && path.matches("/forum/post/\\w+/?")
				|| method == HttpMethod.PUT && path.matches("/forum/post/\\w+/comment/\\w+/?");
	}

}
