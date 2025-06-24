// AdminController.java
package com.recipeapp.recipe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/secret")
    public String adminSecret() {
        return "Nur für Admins sichtbar!";
    }
}
