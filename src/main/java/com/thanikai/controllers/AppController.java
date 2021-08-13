package com.thanikai.controllers;

import com.thanikai.databaseconfig.User;
import com.thanikai.databaseconfig.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    @Autowired
    private UserService userService;

    @RequestMapping("/welcome/{id}")
    public String viewWelcomePage(@PathVariable(name = "id") Long id, Model model) {
        User user = userService.get(id);
        model.addAttribute("users", user);
        return "welcome_page";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("users") User user) {
        userService.save(user);
        return "redirect:/welcome/" + user.getId();
    }

    @RequestMapping("/editUser/{id}")
    public ModelAndView showEditUserForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_role");

        User user = userService.get(id);
        mav.addObject("users", user);

        return mav;
    }
}
