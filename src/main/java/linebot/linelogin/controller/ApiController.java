package linebot.linelogin.controller;

import linebot.linelogin.entity.AccessToken;
import linebot.linelogin.entity.IdToken;
import linebot.linelogin.entity.LineResponse;
import linebot.linelogin.entity.UserProfile;
import linebot.linelogin.service.LineAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin("*")
@RestController
public class ApiController {

    @Autowired
    private LineAPIService lineAPIService;

    @GetMapping("/recieveToken")
    public @ResponseBody ResponseEntity<String> recieveToken(){
        return new ResponseEntity<String>("Get Response", HttpStatus.OK);
    }

    @GetMapping("/auth")
    public @ResponseBody ResponseEntity<LineResponse> authForGame(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "nonce", required = false) String nonce,
            HttpServletResponse response) {
        AccessToken access_token = lineAPIService.gameAccessToken(code);
        IdToken id_token = lineAPIService.idToken(access_token.id_token);
        Cookie c = new Cookie("name", "test");
        response.addCookie(c);
        if(nonce.equals(id_token.nonce)){
            LineResponse lineRes = new LineResponse(access_token.scope, access_token.access_token, access_token.token_type, access_token.expires_in,
                    access_token.id_token,id_token.sub);
            return new ResponseEntity<LineResponse>(lineRes, HttpStatus.OK);
        }
        return new ResponseEntity<LineResponse>((LineResponse) null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getGenerateCode")
    public @ResponseBody ResponseEntity<String> getGenerateCode(){
        String sth = lineAPIService.getGenerateCode();
        System.out.println(sth);
        return new ResponseEntity<String>(sth, HttpStatus.OK);
    }

}
