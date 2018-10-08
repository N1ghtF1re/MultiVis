package men.brakh.emergencymap.controllers;

import men.brakh.emergencymap.db.Emergencies;
import men.brakh.emergencymap.db.EmergenciesRepository;
import men.brakh.emergencymap.models.RegionsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class DBController {
    @Autowired
    private EmergenciesRepository emergenciesRepository;

    @RequestMapping("/add")
    public @ResponseBody String addNewUser (@RequestParam String region
            , @RequestParam int situation, @RequestParam String strDate) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Emergencies n = new Emergencies();
        n.setRegion(region);
        n.setSituation(situation);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf1.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        n.setDate(sqlDate);

        emergenciesRepository.save(n);
        return "Saved";
    }

    @RequestMapping("/all")
    public @ResponseBody Iterable<Emergencies> getAllUsers() {
        // This returns a JSON or XML with the users
        return emergenciesRepository.findAll();
    }

    @RequestMapping("/region")
    public @ResponseBody Iterable<Emergencies> getByRegion(@RequestParam String region) {
        Iterable<Emergencies> emergencies = emergenciesRepository.findByRegion(region);
        for(Emergencies emergency : emergencies) {
            System.out.println(emergency.getRegion());
        }

        return emergenciesRepository.findByRegion(region);
    }

    @RequestMapping("/date")
    public @ResponseBody Iterable<Emergencies> getByDateRange(@RequestParam String startDateStr, @RequestParam String endDateStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        try {
            startDate = sdf1.parse(startDateStr);
            endDate = sdf1.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        RegionsList regionsList = new RegionsList(19, sqlStartDate, sqlEndDate, emergenciesRepository);


        return emergenciesRepository.findByDateRange(sqlStartDate, sqlEndDate);
    }
}