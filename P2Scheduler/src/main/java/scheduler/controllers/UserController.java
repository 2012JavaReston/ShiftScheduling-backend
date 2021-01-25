package scheduler.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import scheduler.models.User;
import scheduler.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class UserController {

	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping(value = "/user", params = {"id"})
	public User getUserById(HttpServletRequest req, HttpServletResponse resp, int id) {
		resp.setStatus(200);
		return userService.getUserById(id);
	}
	
	@GetMapping(value = "/all-users")
	public List<User> getAllUsers(HttpServletRequest req, HttpServletResponse resp) {
		resp.setStatus(200);
		return userService.getAllUsers();
	}
	
	@PostMapping(value = "/signup")
	public String signup(HttpServletRequest req, HttpServletResponse resp, @RequestBody() User user) {
		if(userService.checkEmail(user.getEmail())) {
			if(userService.insertUser(user)) {
				resp.setStatus(200);
				return "Signup successful!";
			} else {
				resp.setStatus(500);
				return "Failed to create account";
			}
		} else {
			resp.setStatus(400);
			return "Email taken";
		}
	}
	
	@PostMapping(value = "/login")
	public String login(HttpServletRequest req, HttpServletResponse resp, @RequestBody() User user) {
		user = userService.getByCredentials(user.getEmail(), user.getPassword());
		
		if(user != null) {
			HttpSession session = req.getSession();
			session.setAttribute("user", user);
			
			resp.setStatus(200);
			
			return "Successfully logged in!";
		}
		
		resp.setStatus(400);
		
		return "Incorrect email or password";
	}
	
	@PostMapping(value = "/logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		session.invalidate();
		resp.setStatus(200);
		return "Successfully log out!";
	}
	
	@PutMapping(value = "update-user")
	public String update(HttpServletRequest req, HttpServletResponse resp, @RequestBody() User user) {
		if(userService.updateUser(user)) {
			resp.setStatus(200);
			return "Update successful!";
		} else {
			resp.setStatus(500);
			return "Failed to update account";
		}
	}
	
	@DeleteMapping(value = "delete-user")
	public String delete(HttpServletRequest req, HttpServletResponse resp, @RequestBody() User user) {
		if(userService.deleteUser(user)) {
			resp.setStatus(200);
			return "Delete successful!";
		} else {
			resp.setStatus(500);
			return "Failed to delete account";
		}
	}
}