package linebot.linelogin.controller;

import linebot.linelogin.service.LineAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    private LineAPIService lineAPIService;

    @GetMapping("/gameauth")
    public @ResponseBody ResponseEntity<String> gameauth(){

    }

    @GetMapping("/recieveToken")
    public @ResponseBody ResponseEntity<String> recieveToken(){
        return new ResponseEntity<String>("Get Response", HttpStatus.OK);
    }


}
