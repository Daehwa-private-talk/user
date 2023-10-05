package com.daehwa.user.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class SamlController {
    @RequestMapping("/")
    fun home(@AuthenticationPrincipal principal: Saml2AuthenticatedPrincipal, model: Model): String {
        model.addAttribute("name", principal.name)
        model.addAttribute("emailAddress", principal.getFirstAttribute("email"))
        model.addAttribute("userAttributes", principal.attributes)

        return "home"
    }
}