package men.brakh.emergencymap.controllers;

import com.google.gson.Gson;
import men.brakh.emergencymap.db.EmergenciesRepository;
import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.RegionsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class MainController {

  @Autowired
  private EmergenciesRepository emergenciesRepository;

  @RequestMapping("/")
  public String index() {
    return "hello.html";
  }

  @RequestMapping(value="/api", method=RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<String> add(String json) {

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date startDate = null;
    java.util.Date endDate = null;
    try {
      startDate = sdf1.parse("2016-01-01");
      endDate = sdf1.parse("2017-01-01");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
    java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

    RegionsList regionsList = new RegionsList(19, sqlStartDate, sqlEndDate, emergenciesRepository);


    Gson gson = new Gson();
    List<Region> cities = regionsList.getList();/*new ArrayList<>();
    cities.add(new Region("Брестская область", "#F0A", 19));
    cities.add(new Region("Витебская область", "#AAA", 19));*/
    String resultJson = gson.toJson(cities);

    return new ResponseEntity<>(resultJson, HttpStatus.OK);
  }

  @RequestMapping("/api")
  public ResponseEntity<String> greetingSubmitS() {
    return new ResponseEntity<>("EEEEEEEEE", HttpStatus.OK);
  }
}
