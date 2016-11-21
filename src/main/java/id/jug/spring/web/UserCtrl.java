package id.jug.spring.web;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import id.jug.spring.domain.Users;
import id.jug.spring.repositories.user.UserService;
import id.jug.spring.util.PageWrapper;
import id.jug.spring.web.dto.Changepass;

/**
 * Created by galih.lasahido@gmail.com
 */
@Controller
public class UserCtrl {
    @Autowired
    private UserService service;

    @RequestMapping(value = {"/user/list","/user"}, method = RequestMethod.GET)
    public String index(Model model, Pageable pageable, Authentication authentication) {
        String template = "user/list";
        PageWrapper<Users> page = new PageWrapper<>(service.findAll(pageable), "/user/list");

        if(page==null)
            return template;

        model.addAttribute("page", page);
        model.addAttribute("data", service.findAll(pageable).getContent());

        return template;
    }


    @RequestMapping(value = "/user/insert", method = RequestMethod.GET)
    public String getInsertPage(Users data, Model model) {
        return "user/insert";
    }

    @RequestMapping(value = "/user/action.insert", method = RequestMethod.POST)
    public String actionInsert(@Valid @ModelAttribute Users data, BindingResult bindingResult, Model model, Principal principal) {
        String url = "user/insert";

        try {
            this.validate(data, bindingResult);
            if(bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                List<FieldError> aList = bindingResult.getFieldErrors();
                for(FieldError bList : aList) {
                    errorMessage.append("<div>"+bList.getDefaultMessage()+"</div>");
                    url = "user/insert";
                }
                model.addAttribute("message", errorMessage.toString());
            } else {
                try {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String hashedPass = encoder.encode(data.getPassword());
                    data.setPassword(hashedPass);
                    data.setCreatedby(principal.getName());
                	
                    service.save(data);
                    url = "redirect:/user/insert?status=true";
                } catch (Exception e) {
                    StringBuilder ste = new StringBuilder();
                    ste.append(e.getMessage());
                    for(StackTraceElement element : e.getStackTrace()) {
                        ste.append(element.toString()+"\n");
                    }
                    model.addAttribute("message", ste.toString());
                }
            }
        } catch (Exception e) {
            StringBuilder ste = new StringBuilder();
            ste.append(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                ste.append(element.toString()+"\n");
            }
            model.addAttribute("message", ste.toString());
        }

        return url;
    }

    private void validate(Users account, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "usernameEmpty", "username is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "passwordEmpty", "password is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roles", "rolesEmpty", "roles is required");
        
        Users users = service.findByUsername(account.getUsername().trim());
        if(users!=null) {
        	errors.rejectValue("username",null, "username already exist");
        }
    }

    private void validateEdit(Users account, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "idEmpty", "id is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "usernameEmpty", "username is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roles", "rolesEmpty", "roles is required");
        
        Users users = service.findByUsernameAndNotId(account.getUsername().trim(), account.getId());
        if(users!=null) {
        	errors.rejectValue("username",null, "username already exist");
        }
    }


    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
    public String getEditPage(@PathVariable("id") Integer id, Agent data, Model model) {
        model.addAttribute("data", service.findById(id));
        return "user/update";
    }

    @RequestMapping(value = "/user/action.edit/{id}", method = RequestMethod.POST)
    public String actionEdit(@PathVariable("id") Integer id, @Valid @ModelAttribute(value="data")  Users data, BindingResult bindingResult, Model model, Principal principal) {
        String url = "user/update";

        try {
            this.validateEdit(data, bindingResult);
            if(bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                List<FieldError> aList = bindingResult.getFieldErrors();
                for(FieldError bList : aList) {
                    errorMessage.append("<div>"+bList.getDefaultMessage()+"</div>");
                }
                model.addAttribute("message", errorMessage.toString());
            } else {
                try {
                    service.updateProfile(data);
                    url = "redirect:/user/edit/"+id+"?status=true";
                } catch (Exception e) {
                    StringBuilder ste = new StringBuilder();
                    ste.append(e.getMessage());
                    for(StackTraceElement element : e.getStackTrace()) {
                        ste.append(element.toString()+"\n");
                    }
                    model.addAttribute("message", ste.toString());
                }
            }
        } catch (Exception e) {
            StringBuilder ste = new StringBuilder();
            ste.append(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                ste.append(element.toString()+"\n");
            }
            model.addAttribute("message", ste.toString());
        }

        return url;
    }
    
    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.GET)
    public String actionDelete(@PathVariable("id") Integer id, Principal principal) {
        String url = "redirect:/user/list";
        Users users = new Users();
        users.setId(id);
        service.delete(users);        
        return url;
    }
    
    @RequestMapping(value = "/user/search", method = RequestMethod.GET)
    public String getSearchPage(Model model, Pageable pageable, HttpServletRequest request, Principal pricipal) {
        String template = "user/index";
        PageWrapper<Users> page = new PageWrapper<>(service.searchUsers(pageable, request.getParameter("mode"), request.getParameter("value")), "/user/search?mode="+request.getParameter("mode")+"&value="+request.getParameter("value"));

        if(page==null)
            return template;

        model.addAttribute("page", page);
        model.addAttribute("data", service.searchUsers(pageable, request.getParameter("mode"), request.getParameter("value")).getContent());

        return template;
    }
    
    @RequestMapping(value = "/user/changepassword", method = RequestMethod.GET)
    public String getChangepassPage(Model model, Principal pricipal) {
    	return "user/changepassword";
    }
    

    @RequestMapping(value = "/user/action.changepassword", method = RequestMethod.POST)
    public String actionChange(@Valid @ModelAttribute(value="data")  Changepass data, BindingResult bindingResult, Model model, Principal principal) {
        String url = "user/changepassword";

        try {
            this.validateChangepassword(data, bindingResult, principal);
            if(bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                List<FieldError> aList = bindingResult.getFieldErrors();
                for(FieldError bList : aList) {
                    errorMessage.append("<div>"+bList.getDefaultMessage()+"</div>");
                }
                model.addAttribute("message", errorMessage.toString());
            } else {
                try {
                	Users users = new Users();
                	users.setUsername(principal.getName());
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String hashedPass = encoder.encode(data.getNewpassword());
                    users.setPassword(hashedPass);                    
                    service.changepassword(users);
                    url = "redirect:/user/changepassword?status=true";
                } catch (Exception e) {
                    StringBuilder ste = new StringBuilder();
                    ste.append(e.getMessage());
                    for(StackTraceElement element : e.getStackTrace()) {
                        ste.append(element.toString()+"\n");
                    }
                    model.addAttribute("message", ste.toString());
                }
            }
        } catch (Exception e) {
            StringBuilder ste = new StringBuilder();
            ste.append(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                ste.append(element.toString()+"\n");
            }
            model.addAttribute("message", ste.toString());
        }

        return url;
    }
      

    private void validateChangepassword(Changepass account, Errors errors, Principal principal) {
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "idpassword", "password is required");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newpassword", "newpasswordEmpty", "new password is required");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmpassword", "confirmpasswordEmpty", "confirm password is required");
    	
    	Users users = service.findByUsername(principal.getName());        

        if(users!=null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!encoder.matches(account.getPassword(), users.getPassword())) {
            	errors.rejectValue("password",null, "invalid old password");
            }
            
            if(!account.getNewpassword().equalsIgnoreCase(account.getConfirmpassword())) {
            	errors.rejectValue("newpassword",null, "invalid new and confirm password");
            }
        }
    }

    @RequestMapping(value = "/user/changepassworduser/{username}", method = RequestMethod.GET)
    public String getchangepassworduserpage(@PathVariable("username") String username, Model model, Principal pricipal) {
    	model.addAttribute("username", username);
    	return "user/changepassworduser";
    }
    

    @RequestMapping(value = "/user/action.changepassworduser/{username}", method = RequestMethod.POST)
    public String actionchangepassworduser(@PathVariable("username") String username, @Valid @ModelAttribute(value="data")  Changepass data, BindingResult bindingResult, Model model, Principal principal) {
        String url = "user/changepassworduser";
    	model.addAttribute("username", username);

        try {
            this.validateChangepassworduser(data, bindingResult, username);
            if(bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                List<FieldError> aList = bindingResult.getFieldErrors();
                for(FieldError bList : aList) {
                    errorMessage.append("<div>"+bList.getDefaultMessage()+"</div>");
                }
                model.addAttribute("message", errorMessage.toString());
            } else {
                try {
                	Users users = new Users();
                	users.setUsername(username);
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String hashedPass = encoder.encode(data.getNewpassword());
                    users.setPassword(hashedPass);
                    
                    service.changepassword(users);
                    url = "redirect:/user/changepassworduser/"+username;
                } catch (Exception e) {
                    StringBuilder ste = new StringBuilder();
                    ste.append(e.getMessage());
                    for(StackTraceElement element : e.getStackTrace()) {
                        ste.append(element.toString()+"\n");
                    }
                    model.addAttribute("message", ste.toString());
                }
            }
        } catch (Exception e) {
            StringBuilder ste = new StringBuilder();
            ste.append(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                ste.append(element.toString()+"\n");
            }
            model.addAttribute("message", ste.toString());
        }

        return url;
    }
      

    private void validateChangepassworduser(Changepass account, Errors errors, String username) {
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newpassword", "newpasswordEmpty", "new password is required");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmpassword", "confirmpasswordEmpty", "confirm password is required");
    	
        if(!account.getNewpassword().equalsIgnoreCase(account.getConfirmpassword())) {
        	errors.rejectValue("newpassword",null, "invalid new and confirm password");
        }
    }
}