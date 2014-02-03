package com.ais.equinox.tuner.web.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(value = "index")
	public String getIndexPage(ModelMap model) {

		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String name = user.getUsername(); // get logged in username
		System.out.println(user.getAuthorities());
		model.addAttribute("username", name);
		return "index";
	}

	protected boolean hasRole(String[] roles) {
		boolean result = false;
		for (GrantedAuthority authority : SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities()) {
			String userRole = authority.getAuthority();
			for (String role : roles) {
				if (role.equals(userRole)) {
					result = true;
					break;
				}
			}

			if (result) {
				break;
			}
		}

		return result;
	}
}
