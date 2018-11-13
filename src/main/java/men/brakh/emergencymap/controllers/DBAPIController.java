package men.brakh.emergencymap.controllers;

import men.brakh.emergencymap.BeansConfiguration;
import men.brakh.emergencymap.db.*;
import men.brakh.emergencymap.dto.ColorDTO;
import men.brakh.emergencymap.http.APICommunication;
import men.brakh.emergencymap.models.Color;
import men.brakh.emergencymap.models.Population;
import men.brakh.emergencymap.models.Region;
import men.brakh.emergencymap.models.RegionsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.*;

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

    private static final Logger logger = LoggerFactory.getLogger(DBAPIController.class);

    final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * Вовзращает JSON уже сформированного списка регионов с их результирующим цветом
     * @param startDate Начальная дата диапазона в формате yyyy-MM-dd
     * @param endDate Конечная дата диапазона в формате yyyy-MM-dd
     * @param mode Режим отображения (базовый/на основе демографии. По умолчанию: basic)
     * @return JSON уже сформированного списка регионов с их результирующим цветом
     */
    @GetMapping(value="/regionsList")
    public List<Region> getRegionsList(@RequestParam String startDate,
                                       @RequestParam String endDate,
                                       @RequestParam(defaultValue = "basic") String mode) {
        logger.info(String.format("Request to /regionList with params: %s, %s, %s", startDate, endDate, mode));
        java.util.Date start;
        java.util.Date end;
        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            logger.warn("Invalid date entered " + e.getMessage());
            return new ArrayList<>();
        }
        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        RegionsList regionsList = new RegionsList(situationsRepository.getSitCount(), sqlStartDate, sqlEndDate, mode);

        return regionsList.getList();
    }

    /**
     * Получение числа жителей в регионе
     * @param region Регион
     * @return число жителей в регионе
     */
    @GetMapping(value="/population")
    public long getRegionPopulation(@RequestParam String region) {
        logger.info(String.format("Request to /population with params:%s", region));
        return Population.get(region);
    }

    /**
     * Добавление элемента в базу данных по запросу на /api/add&region=REGION&situation=SITUATION&strDate=STRDATE
     * @param region Название региона
     * @param situation Номер ситуации
     * @param strDate Дата в формате yyyy-MM-dd
     * @return "Saved" если добавление выполнено успешно
     */
    @GetMapping("/add")
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
    @GetMapping(value="/accidents", params={})
    public Iterable<Emergencies> getAllAccidents() {
        logger.info(String.format("Request to /accidents with params: - "));
        return emergenciesRepository.findAll();
    }

    /**
     * Возвращает все записи из базы данных для нужного региона по запросу
     * @param region Название региона
     * @return Все записи из базы данных для нужного региона
     */
    @GetMapping(value="/accidents", params={"region"})
    public Iterable<Emergencies> getByRegion(@RequestParam String region) {
        logger.info(String.format("Request to /accidents with params: %s ", region));
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
    @GetMapping(value="/accidents", params={"startDate", "endDate"})
    public Iterable<Emergencies> getByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        logger.info(String.format("Request to /accidents with params: %s, %s", startDate, endDate));
        java.util.Date start = null;
        java.util.Date end = null;
        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            logger.warn("Invalid date entered " + e.getMessage());
            return null;
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
    @GetMapping(value="/accidents", params={"startDate", "endDate", "region"})
    public Iterable<Emergencies> getByDateRangeAndRegion(@RequestParam String startDate, @RequestParam String endDate, String region) {
        java.util.Date start = null;
        java.util.Date end = null;

        logger.info(String.format("Request to /accidents with params: %s, %s %s", startDate, endDate, region));

        try {
            start = sdf1.parse(startDate);
            end = sdf1.parse(endDate);
        } catch (ParseException e) {
            logger.warn("Invalid  date entered " + e.getMessage());
            return null;
        }
        java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

        return emergenciesRepository.findByDateRangeAndRegion(sqlStartDate, sqlEndDate, region);
    }

    /**
     * Возвращает список ситуаций (id, название)
     * @return JSON списска ситуаций [{"id": 1", "name": "..."}, ...]
     */
    @GetMapping("/sitsList")
    public Iterable<Situations> getSitsList() {
        logger.info(String.format("Request to /sitsList"));

        return situationsRepository.findAll();
    }




    /**
     * Возвращает базовые цвета
     * @return JSON с базовыми цветами
     */
    @GetMapping("/basicColors")
    public ColorDTO[] getBasicColors() {

        Color[] colors = Color.getBasicColors(situationsRepository.getSitCount());
        ColorDTO[] colorWidthIds = new ColorDTO[colors.length];
        for(int i = 0; i < colors.length; i++) {
            colorWidthIds[i] = new ColorDTO(colors[i], i+1);
        }

        return colorWidthIds;
    }

    /**
     * Получение полигона для региона
     * @param region Название региона
     * @return полигон региона
     */
    @GetMapping("/polygons/get")
    public @ResponseBody String getPolygon(@RequestParam  String region) {
        region = region.replaceAll(" ", "%20"); // Заменяем пробелы на %20 ()
        Polygons polygons = polygonsRepository.findFirstByRegion(region);
        if(polygons != null) {
            return polygons.getPolygon();
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(BeansConfiguration.class);
        APICommunication apiCommunication = (APICommunication) context.getBean("apiBean");
        String coords = apiCommunication.getCoords(region);

        Polygons n = new Polygons();
        n.setRegion(region);
        n.setPolygon(coords);

        polygonsRepository.save(n);
        logger.info(String.format("Added new row in table 'polygons'. Region: %s", region));

        return coords;
    }

    /**
     * Возвращаем количество уникальный ситуаций в БД
     * @return количество уникальных ситуаций в БД
     */
    @GetMapping("/sitsList/count")
    public int getSitsCount(){
        return situationsRepository.getSitCount();
    }
}