package linebot.linelogin.controller;

import linebot.linelogin.entity.AccessToken;
import linebot.linelogin.service.LineAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    private LineAPIService lineAPIService;

    @GetMapping("/recieveToken")
    public @ResponseBody ResponseEntity<String> recieveToken(){
        return new ResponseEntity<String>("Get Response", HttpStatus.OK);
    }

    @GetMapping("/authForGame")
    public AccessToken authForGame(@RequestParam(value = "code", required = false) String code,
                                   @RequestParam(value = "state", required = false) String state,
                                   @RequestParam(value = "scope", required = false) String scope) {
        AccessToken token = lineAPIService.accessToken(code);
        return token;
    }

}
