package men.brakh.emergencymap.controllers;

import men.brakh.emergencymap.db.Emergencies;
import men.brakh.emergencymap.db.EmergenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Наброски API сервиса
 */

@Controller
@RequestMapping(path="/api")
public class DBAPIController {
    @Autowired
    private EmergenciesRepository emergenciesRepository;

    final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Добавление элемента в базу данных по запросу на /api/add&region=REGION&situation=SITUATION&strDate=STRDATE
     * @param region Название региона
     * @param situation Номер ситуации
     * @param strDate Дата в формате yyyy-MM-dd
     * @return "Saved" если добавление выполнено успешно
     */
    @RequestMapping("/add")
    public @ResponseBody String addNewUser (@RequestParam String region
            , @RequestParam int situation, @RequestParam String strDate) {

        Emergencies n = new Emergencies();
        n.setRegion(region);
        n.setSituation(situation);


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

    /**
     * Возвращает JSON со всеми записями из базы данных по запросу на /api/all
     * @return JSON со всеми записями из базы данных
     */
    @RequestMapping("/all")
    public @ResponseBody Iterable<Emergencies> getAllUsers() {
        return emergenciesRepository.findAll();
    }

    /**
     * Возвращает все записи из базы данных для нужного региона по запросу на /api/region?name=REGIONNAME
     * @param name Название региона
     * @return Все записи из базы данных для нужного региона
     */
    @RequestMapping("/region")
    public @ResponseBody Iterable<Emergencies> getByRegion(@RequestParam String name) {
        Iterable<Emergencies> emergencies = emergenciesRepository.findByRegion(name);
        for(Emergencies emergency : emergencies) {
            System.out.println(emergency.getRegion());
        }

        return emergenciesRepository.findByRegion(name);
    }

    /**
     * Возвращает все записи из базы данных за нужный диапазон дат по запросу на /api/date/start=DATE&end=&DATE
     * @param start Начальная дата диапазона в формате yyyy-MM-dd
     * @param end Конечная дата диапазона в формате yyyy-MM-dd
     * @return Все записи из базы данных за нужный диапазон дат
     */
    @RequestMapping("/date")
    public @ResponseBody Iterable<Emergencies> getByDateRange(@RequestParam String start, @RequestParam String end) {
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        try {
            startDate = sdf1.parse(start);
            endDate = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());

        return emergenciesRepository.findByDateRange(sqlStartDate, sqlEndDate);
    }
}