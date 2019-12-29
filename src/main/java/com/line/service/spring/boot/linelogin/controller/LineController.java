package com.line.service.spring.boot.linelogin.controller;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

import com.line.service.spring.boot.linelogin.api.LineAPIService;
import com.line.service.spring.boot.linelogin.api.response.AccessToken;
import com.line.service.spring.boot.linelogin.api.response.IdToken;
import com.line.service.spring.boot.linelogin.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LineController {

    private static final String LINE_WEB_LOGIN_STATE = "lineWebLoginState";
    static final String ACCESS_TOKEN = "accessToken";
    private static final Logger logger = Logger.getLogger(LineController.class);
    private static final String NONCE = "nonce";

    @Autowired
    private LineAPIService lineAPIService;

    @RequestMapping("/")
    public String login() { return "user/login"; }

//    >
    @RequestMapping(value = "/gotoauthpage")
    public String goToAuthPage(HttpSession session) {
        final String state = Utils.getToken();
        final String nonce = Utils.getToken();
        session.setAttribute(LINE_WEB_LOGIN_STATE, state);
        session.setAttribute(NONCE, nonce);
        final  String url = lineAPIService.getLineLoginUrl(state, nonce, Arrays.asList("openid", "profile", "email"));
        return "redirect:" + url;
    }

    @RequestMapping("/auth")
    public String auth(
            HttpSession session,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "errorCode", required = false) String errorCode,
            @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        if(logger.isDebugEnabled()) {
            logger.debug("parameter code : " + code);
            logger.debug("parameter state : " + state);
            logger.debug("parameter scope : " + scope);
            logger.debug("parameter error : " + error);
            logger.debug("parameter errorCode : " + errorCode);
            logger.debug("parameter errorMessage : " + errorMessage);
        }

        if(error != null || errorCode != null || errorMessage != null) {
            return "redirect:/loginCancel";
        }

        if(!state.equals(session.getAttribute(LINE_WEB_LOGIN_STATE))) {
            return "redirect:/sessionError";
        }

        session.removeAttribute(LINE_WEB_LOGIN_STATE);
        AccessToken token = lineAPIService.accessToken(code);
        if(logger.isDebugEnabled()) {
            logger.debug("scope : " + token.scope);
            logger.debug("access_token : " + token.access_token);
            logger.debug("token_type : " + token.token_type);
            logger.debug("expires_in : " + token.expires_in);
            logger.debug("refresh_token : " + token.refresh_token);
            logger.debug("id_token : " + token.id_token);
        }

        session.setAttribute(ACCESS_TOKEN, token);
        return "redirect:/success";

    }

    @RequestMapping("/success")
    public String success(HttpSession session, Model model) {

        AccessToken token = (AccessToken)session.getAttribute(ACCESS_TOKEN);
        if(token == null) {
            return "redirect:/";
        }

        if(!lineAPIService.verifyIdToken(token.id_token, (String) session.getAttribute(NONCE))) {
            return "redirect:/";
        }

        session.removeAttribute(NONCE);
        IdToken idToken = lineAPIService.idToken(token.id_token);
        if(logger.isDebugEnabled()) {
            logger.debug("UserId : " + idToken.sub);
            logger.debug("displayName : " + idToken.name);
            logger.debug("pictureUrl : " + idToken.picture);
            logger.debug("email : " + idToken.email);
        }

        model.addAttribute("idToken", idToken);
        return "user/success";

    }

    @RequestMapping("/loginCancel")
    public String loginCancel() { return "user/login_cancel"; }

    @RequestMapping("/sessionError")
    public String sessionError() { return "user/session_error"; }

}
