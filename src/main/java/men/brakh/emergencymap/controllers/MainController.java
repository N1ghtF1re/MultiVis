package men.brakh.emergencymap.controllers;

import com.google.gson.Gson;
import men.brakh.emergencymap.models.Region;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

  @RequestMapping("/")
  public String index() {
    return "hello.html";
  }

  @RequestMapping(value="/api", method=RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<String> add(String json) {
    Gson gson = new Gson();
    List<Region> cities = new ArrayList<>();
    //cities.add(new Region("Брестская область", "#F0A"));
    //cities.add(new Region("Витебская область", "#AAA"));
    String resultJson = gson.toJson(cities);

    return new ResponseEntity<>(resultJson, HttpStatus.OK);
  }

  @RequestMapping("/api")
  public ResponseEntity<String> greetingSubmitS() {
    return new ResponseEntity<>("EEEEEEEEE", HttpStatus.OK);
  }
}
