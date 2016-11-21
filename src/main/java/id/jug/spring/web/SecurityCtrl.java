package id.jug.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by galih.lasahido@gmail.com
 */
@Controller
public class SecurityCtrl {

	@RequestMapping("/login")
	private String getLoginPage() {
		return "security/index";
	}
	
	@RequestMapping("/error-login")
	private String getErrorLoginPage(Model modal) {
		modal.addAttribute("error", "error");
		return "security/index";
	}
}