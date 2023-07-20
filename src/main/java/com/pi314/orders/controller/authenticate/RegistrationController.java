package com.pi314.orders.controller.authenticate;

import com.pi314.orders.model.dto.UserDTO;
import com.pi314.orders.model.entity.User;
import com.pi314.orders.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
   private final UserService userService;

    @ModelAttribute("user")
    public UserDTO userRegistrationDto() {
        return new UserDTO();
    }

    /**
     * If the user is not logged in, show the registration form. Otherwise, redirect to the home page
     *
     * @param authentication The Authentication object that represents the user that is currently logged in.
     * @return A string that is the name of the view to be rendered.
     */
    @GetMapping
    public String showRegistrationForm(Authentication authentication) {
        if (authentication == null) {
            return "registration";
        } else {
            return "redirect:/";
        }
    }

    // A method that is called when the user submits the registration form. It checks if the user already exists in the
    // database and if not, it saves the user.
    @PostMapping
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDTO userDto, BindingResult result) {

        User existingEmail = userService.findByEmail(userDto.getEmail());
        if (existingEmail != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }
        userService.register(userDto);
        return "redirect:/registration?success";
    }
}
