package net.javaguides.springboot.helloworldapp.controller;

import org.springframework.web.bind.annotation.*;

import net.javaguides.springboot.helloworldapp.bean.AuthenticationBean;
import java.util.Base64;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class BasicAuthController {

	private static String generateSecurePassword(String password, String salt) {
		String returnValue = null;
		byte[] securePassword = GreetingController.hash(password.toCharArray(), salt.getBytes());

		returnValue = Base64.getEncoder().encodeToString(securePassword);

		return returnValue;
	}

	@GetMapping(path = "/basicauth")
	public AuthenticationBean helloWorldBean(@RequestHeader("captchaHash") String captchaHash, @RequestHeader("captchaText") String captchaText) {
		String newSecurePassword = generateSecurePassword(captchaText, GreetingController.getSalt());

		if (newSecurePassword.equalsIgnoreCase(captchaHash)) {
			return new AuthenticationBean("You are authenticated");
		} else {
			return new AuthenticationBean("Wrong captcha!");
		}
	}
}
