package id.jug.spring.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardCtrl {
	
	@GetMapping(value={"/","/dashboard"})
	private String getHome(Principal username, Model modal) {
		return "dashboard/index";
	}
	
}