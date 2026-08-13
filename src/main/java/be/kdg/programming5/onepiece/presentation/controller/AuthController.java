package be.kdg.programming5.onepiece.presentation.controller;

import be.kdg.programming5.onepiece.business.exception.UsernameTakenException;
import be.kdg.programming5.onepiece.business.service.UserService;
import be.kdg.programming5.onepiece.presentation.viewmodel.RegisterViewModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerViewModel", new RegisterViewModel());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerViewModel") RegisterViewModel viewModel,
                           BindingResult bindingResult) {

        if (!viewModel.getPassword().equals(viewModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "register.password.mismatch");
        }

        if (bindingResult.hasErrors()) {
            logger.debug("Register form has {} error(s)", bindingResult.getErrorCount());
            return "register";
        }

        try {
            userService.register(viewModel.getUsername(), viewModel.getEmail(), viewModel.getPassword());
        } catch (UsernameTakenException ex) {
            bindingResult.rejectValue("username", "register.username.taken");
            return "register";
        }

        return "redirect:/login?registered";
    }
}