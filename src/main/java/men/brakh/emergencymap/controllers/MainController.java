package men.brakh.emergencymap.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
  private static final Logger logger = LoggerFactory.getLogger(MainController.class);

  @RequestMapping(value = "/", method = {RequestMethod.GET} )
  public ModelAndView index(){
    logger.info("Request to /");
    ModelAndView modelAndView = new ModelAndView("index");

    return modelAndView;
  }


  @RequestMapping(value = "/api", method = RequestMethod.GET)
  public String api(HttpServletRequest request, Model model) {

    String baseUrl = String.format("%s://%s:%d",request.getScheme(),  request.getServerName(), request.getServerPort());


    model.addAttribute("host", baseUrl);

    return "api";
  }

}
