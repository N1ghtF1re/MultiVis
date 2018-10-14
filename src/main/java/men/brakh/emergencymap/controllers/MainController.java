package men.brakh.emergencymap.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    return "index.html";
  }

  @RequestMapping(value = "/api", method = RequestMethod.GET)
  public String api(HttpServletRequest request, Model model) {

    String baseUrl = String.format("%s://%s:%d",request.getScheme(),  request.getServerName(), request.getServerPort());


    model.addAttribute("host", baseUrl);

    return "api";
  }

}
