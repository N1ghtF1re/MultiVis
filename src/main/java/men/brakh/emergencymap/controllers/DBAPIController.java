package men.brakh.emergencymap.controllers;

import men.brakh.emergencymap.db.*;
import men.brakh.emergencymap.http.NominatimCommunication;
import men.brakh.emergencymap.models.Color;
import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.RegionsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Наброски API сервиса
 */

@RestController
@RequestMapping(path="/api")
public class DBAPIController {
    @Autowired
    private EmergenciesRepository emergenciesRepository;

    @Autowired
    private SituationsRepository situationsRepository;

    @Autowired
    private PolygonsRepository polygonsRepository;

    @Autowired
    private PopulationsRepository populationsRepository;


    final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Вовзращает JSON уже сформированного списка регионов с их результирующим цветом
     * @param startDate Начальная дата диапазона в формате yyyy-MM-dd
     * @param endDate Конечная дата диапазона в формате yyyy-MM-dd
     * @param mode Режим отображения (базовый/на основе демографии. По умолчанию: basic)
     * @return JSON уже сформированного списка регионов с их результирующим цветом
     */
    @RequestMapping(value="/regionsList")
    public List<Region> getRegionsList(@RequestParam String startDate,
                                       @RequestParam String endDate,
                                       @RequestParam(defaultValue = "basic") String mode) {
        java.util.Date start;
        java.util.Date end;
        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            return new ArrayList<Region>();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        RegionsList regionsList = new RegionsList(situationsRepository.getSitCount(), sqlStartDate, sqlEndDate, mode);

        return regionsList.getList();
    }


    /**
     * Добавление элемента в базу данных по запросу на /api/add&region=REGION&situation=SITUATION&strDate=STRDATE
     * @param region Название региона
     * @param situation Номер ситуации
     * @param strDate Дата в формате yyyy-MM-dd
     * @return "Saved" если добавление выполнено успешно
     */
    @RequestMapping("/add")
    public String addNewAccident (@RequestParam String region,
                                  @RequestParam int situation,
                                  @RequestParam String strDate) {

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
     * Возвращает JSON со всеми записями из базы данных по запросу
     * @return JSON со всеми записями из базы данных
     */
    @RequestMapping(value="/accidents", params={})
    public Iterable<Emergencies> getAllAccidents() {
        return emergenciesRepository.findAll();
    }

    /**
     * Возвращает все записи из базы данных для нужного региона по запросу
     * @param region Название региона
     * @return Все записи из базы данных для нужного региона
     */
    @RequestMapping(value="/accidents", params={"region"})
    public Iterable<Emergencies> getByRegion(@RequestParam String region) {
        Iterable<Emergencies> emergencies = emergenciesRepository.findByRegion(region);
        for(Emergencies emergency : emergencies) {
            System.out.println(emergency.getRegion());
        }

        return emergenciesRepository.findByRegion(region);
    }


    /**
     * Возвращает все записи из базы данных за нужный диапазон дат по запросу
     * Отличается от getRegionsList тем, что getRegionsList возвращает уже подготовленный список регионов с их
     * цветом и тд, а getByDateRange - возвращает просто строки базы данных
     * @param startDate Начальная дата диапазона в формате yyyy-MM-dd
     * @param endDate Конечная дата диапазона в формате yyyy-MM-dd
     * @return Все записи из базы данных за нужный диапазон дат
     */
    @RequestMapping(value="/accidents", params={"startDate", "endDate"})
    public Iterable<Emergencies> getByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        java.util.Date start = null;
        java.util.Date end = null;
        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        return emergenciesRepository.findByDateRange(sqlStartDate, sqlEndDate);
    }

    /**
     * Возвращает все записи из базы данных за нужный диапазон дат для нужного региона
     * @param startDate Начальная дата диапазона в формате yyyy-MM-dd
     * @param endDate Конечная дата диапазона в формате yyyy-MM-dd
     * @param region Навание региона
     * @return Все записи из базы данных за нужный диапазон дат
     */
    @RequestMapping(value="/accidents", params={"startDate", "endDate", "region"})
    public Iterable<Emergencies> getByDateRangeAndRegion(@RequestParam String startDate, @RequestParam String endDate, String region) {
        java.util.Date start = null;
        java.util.Date end = null;

        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        return emergenciesRepository.findByDateRangeAndRegion(sqlStartDate, sqlEndDate, region);
    }

    /**
     * Возвращает список ситуаций (id, название)
     * @return JSON списска ситуаций [{"id": 1", "name": "..."}, ...]
     */
    @RequestMapping("/sitsList")
    public Iterable<Situations> getSitsList() {
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
    public ColorWidthId[] getBasicColors() {

        Color[] colors = Color.getBasicColors(situationsRepository.getSitCount());
        ColorWidthId[] colorWidthIds = new ColorWidthId[colors.length];
        for(int i = 0; i < colors.length; i++) {
            colorWidthIds[i] = new ColorWidthId(colors[i], i+1);
        }

        return colorWidthIds;
    }

    /**
     * Получение полигона для региона
     * @param region Название региона
     * @return полигон региона
     */
    @RequestMapping("/polygons/get")
    public @ResponseBody String getPolygon(@RequestParam  String region) {
        region = region.replaceAll(" ", "%20"); // Заменяем пробелы на %20 ()
        Polygons polygons = polygonsRepository.findFirstByRegion(region);
        if(polygons != null) {
            return polygons.getPolygon();
        }
        NominatimCommunication nominatimCommunication = new NominatimCommunication();
        String coords = nominatimCommunication.getCoordsFromNominatim(region);

        Polygons n = new Polygons();
        n.setRegion(region);
        n.setPolygon(coords);

        polygonsRepository.save(n);

        return coords;
    }

    /**
     * Возвращаем количество уникальный ситуаций в БД
     * @return количество уникальных ситуаций в БД
     */
    @RequestMapping("/sitsList/count")
    public int getSitsCount(){
        return situationsRepository.getSitCount();
    }
}