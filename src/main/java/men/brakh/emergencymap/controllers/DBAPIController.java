package men.brakh.emergencymap.controllers;

import com.google.gson.*;
import men.brakh.emergencymap.HttpInteraction;
import men.brakh.emergencymap.db.*;
import men.brakh.emergencymap.models.Color;
import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.RegionsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Наброски API сервиса
 */

@Controller
@RequestMapping(path="/api")
public class DBAPIController {
    @Autowired
    private EmergenciesRepository emergenciesRepository;

    @Autowired
    private SituationsRepository situationsRepository;


    final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Вовзращает JSON уже сформированного списка регионов с их результирующим цветом
     * @param startDate Начальная дата диапазона в формате yyyy-MM-dd
     * @param endDate Конечная дата диапазона в формате yyyy-MM-dd
     * @return JSON уже сформированного списка регионов с их результирующим цветом
     */
    @RequestMapping("regionsList")
    public @ResponseBody
    ResponseEntity<String> getRegionsList(@RequestParam String startDate, @RequestParam String endDate) {

        java.util.Date start;
        java.util.Date end;
        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            return new ResponseEntity<>("Error: Incorrect date", HttpStatus.BAD_REQUEST);
        }
        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        RegionsList regionsList = new RegionsList(19, sqlStartDate, sqlEndDate, emergenciesRepository);


        Gson gson = new Gson();
        List<Region> cities = regionsList.getList();
        String resultJson = gson.toJson(cities);

        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

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
     * Отличается от getRegionsList тем, что getRegionsList возвращает уже подготовленный список регионов с их
     * цветом и тд, а getByDateRange - возвращает просто строки базы данных
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


    /**
     * Возвращает список ситуаций (id, название)
     * @return JSON списска ситуаций [{"id": 1", "name": "..."}, ...]
     */
    @RequestMapping("/sitsList")
    public @ResponseBody Iterable<Situations> getSitsList() {
        return situationsRepository.findAll();
    }

    /**
     * Класс для возврата ID вместе с цветом при запросе
     */
    private class ColorWidthId extends Color{
        private int id;
        public ColorWidthId(Color color, int id) {
            super(color);
            this.id = id;
        }
        public int getId() {
            return id;
        }
    }

    /**
     * Возвращает базовые цвета
     * @return JSON с базовыми цветами
     */
    @RequestMapping("/basicColors")
    public @ResponseBody ColorWidthId[] getBasicColors() {

        Color[] colors = Color.getBasicColors(19);
        ColorWidthId[] colorWidthIds = new ColorWidthId[colors.length];
        for(int i = 0; i < colors.length; i++) {
            colorWidthIds[i] = new ColorWidthId(colors[i], i+1);
        }

        return colorWidthIds;
    }

    @RequestMapping("/polygons/get")
    public @ResponseBody String getPolygon(@RequestParam  String region) {
        region = region.replaceAll(" ", "%20");
        HttpInteraction http = new HttpInteraction();
        return http.getCoordsFromNominatim(region);
    }
}