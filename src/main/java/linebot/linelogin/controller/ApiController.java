package linebot.linelogin.controller;

import linebot.linelogin.entity.AccessToken;
import linebot.linelogin.service.LineAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@RestController
public class ApiController {

    @Autowired
    private LineAPIService lineAPIService;

    @GetMapping("/recieveToken")
    public @ResponseBody ResponseEntity<String> recieveToken(){
        return new ResponseEntity<String>("Get Response", HttpStatus.OK);
    }

    @PostMapping("/authForGame")
    public @ResponseBody ResponseEntity<AccessToken> authForGame(
            @RequestBody AccessToken code,
            HttpServletResponse response) {
        AccessToken token = lineAPIService.gameAccessToken(code.code);
        Cookie cookie = new Cookie("token", token.access_token);
        response.addCookie(cookie);
        return new ResponseEntity<AccessToken> (token, HttpStatus.OK);
    }




}
