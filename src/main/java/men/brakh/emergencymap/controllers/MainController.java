package men.brakh.emergencymap.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

  @RequestMapping("/")
  public String index() {
    return "hello.html";
  }

  @RequestMapping(value="/api", method=RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<String> add(String json) {

    System.out.println("SS");

    return new ResponseEntity<>("EEEEEEEEE", HttpStatus.OK);
  }

  @RequestMapping("/api")
  public ResponseEntity<String> greetingSubmitS() {
    return new ResponseEntity<>("EEEEEEEEE", HttpStatus.OK);
  }
}
