package linebot.linelogin.controller;

import linebot.linelogin.model.AccessToken;
import linebot.linelogin.model.IdToken;
import linebot.linelogin.model.LineResponse;
import linebot.linelogin.service.LineAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class ApiController {

    @Autowired
    private LineAPIService lineAPIService;

    @GetMapping("/test")
    public @ResponseBody ResponseEntity<String> recieveToken(){
        return new ResponseEntity<String>("Get Response", HttpStatus.OK);
    }

    @GetMapping("/auth")
    public @ResponseBody ResponseEntity<LineResponse> auth(
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "url", required = true) String url) {
//        AccessToken access_token = lineAPIService.gameAccessToken(code);
        AccessToken access_token = lineAPIService.accessToken(code, url);
        IdToken id_token = lineAPIService.idToken(access_token.id_token);
            if(nonce.equals(id_token.nonce)){
                LineResponse lineRes = new LineResponse(access_token.scope, access_token.access_token, access_token.token_type, access_token.expires_in,
                        access_token.id_token,id_token.sub, id_token.name, id_token.picture);
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

//    @GetMapping("/createToken")
//    public @ResponseBody ResponseEntity<String> createToken(
//            @RequestParam(value = "scope", required =false) String scope,
//            @RequestParam(value = "access_token", required =false) String access_token,
//            @RequestParam(value = "token_type", required =false) String token_type,
//            @RequestParam(value = "expires_in", required = false) int expires_in,
//            @RequestParam(value = "id_token", required =false) String id_token,
//            @RequestParam(value = "userId") String userId
//    ){
//        LineResponse line = new LineResponse(scope,access_token,token_type,expires_in,id_token,userId);
//        String token = lineAPIService.createToken(line);
//        return new ResponseEntity<String>((String) token, HttpStatus.OK);
//    }

    @GetMapping("/getToken")
    public @ResponseBody ResponseEntity<LineResponse> getToken(
            @RequestParam(value = "token") String token){
        LineResponse line = lineAPIService.decodeToken(token);
        return new ResponseEntity<LineResponse>((LineResponse) line, HttpStatus.OK);
    }

}
