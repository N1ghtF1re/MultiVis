package men.brakh.emergencymap.models;

import men.brakh.emergencymap.db.Emergencies;
import men.brakh.emergencymap.db.EmergenciesRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Класс со списом Регионов с их количеством повторений ситуаций и цветом
 */
public class RegionsList {
    private int n;
    private Date startDate;
    private Date endDate;

    ArrayList<Region> regionsList = new ArrayList<>();

    private EmergenciesRepository emergenciesRepository;

    /**
     * Создание списка регионов
     * @param n Количество ситуаций
     * @param startDate Дата, с которой начинается поиск (в формате yyyy-MM-dd)
     * @param endDate Дата, на которой заканчивается поиск (в формате yyyy-MM-dd)
     * @param emergenciesRepository Репозиторий БД
     */
    public RegionsList(int n, Date startDate, Date endDate, EmergenciesRepository emergenciesRepository) {
        this.n = n;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emergenciesRepository = emergenciesRepository;
        // Заполняем список
        init();
        // Получаем цвета каждого региона
        initialiseColors();
    }

    /**
     * Возвращаем результат запроса в БД, предварительно превратив из итератора в список
     * @return
     */
    List<Emergencies> getEmergenciesFromDB() {
        Iterable<Emergencies> emergencies = emergenciesRepository.findByDateRange(startDate, endDate);
        List<Emergencies> target = new ArrayList<>();
        emergencies.forEach(target::add);

        return target;
    }

    /**
     * Инициализация списка из базы данных по параметрам из конструктора
     */
    public void init() {
        // Получаем итерируемый объект со списком строк базы данных по запросу
        List<Emergencies> emergencies = getEmergenciesFromDB();

        // Получаем первую вернувшуюся строку
        Emergencies firstEmergency = emergencies.get(0);

        // Получаем первый регион из запроса и создаем объект
        Region currRegion = new Region(firstEmergency.getRegion(), n);

        /* Запрос в БД делается с сортировкой по названию региона =>
            они идут по порядку. Поэтому идем по списку и для регионов с
            одним названием изменяем количество повторений той или инной ситуации
            (берется из БД). Изменение происходит путем инкремента
         */

        for(Emergencies emergency : emergencies) {
            if(currRegion.getName().equals(emergency.getRegion())) {
                currRegion.incSituation(emergency.getSituation() - 1);
            } else {
                regionsList.add(currRegion); // Регионы с таким названием закончились => пушим в список
                currRegion = new Region(emergency.getRegion(), n);
                currRegion.incSituation(emergency.getSituation() - 1);
            }
        }
    }

    /**
     * Получаем массив из максимальных повторений i-ой ситуации
     * (i-1)-ый элемент - максимальное число повторений ситуации
     * @return Массив из максимальных повторений i-ой ситуации
     */
    private int[] getMaxArr() {
        if (regionsList.size() == 0) {
            return new int[n]; // Если список пустой, то число повторений каждой ситуации == 0
        }

        int[] maxArr = new int[n];
        for (int i = 0; i < n; i++) { // Изначально инициализируем массив значениями первого региона
            maxArr[i] = regionsList.get(0).getSitRepeats(i);
        }

        for (int i = 1; i < regionsList.size(); i++) {
            for (int j = 0; j < n; j++) {
                if (maxArr[j] < regionsList.get(i).getSitRepeats(j)) {
                    maxArr[j] = regionsList.get(i).getSitRepeats(j);
                }
            }
        }
        return maxArr;
    }

    /**
     * Получаем цвет определенного региона (Цвет i-го региона получается путем осветление его базового цвета на процент,
     * на который он отличается от максимального показателя для этого региона)
     * @param regionSits Массив повторений ситуаций региона ((i-1)-ый элемент - количество повторений i-ой ситуации)
     * @param basicColors Базовые цвета ситуаций (при максимальном числе повторений ситуации)
     * @see Color#getBasicColors(int)
     * @param maxArr - Массив максимальных значений повторений ситуаций
     * @see RegionsList#getMaxArr()
     * @return Объект результирующего цвета
     */
    public Color getRegionColor(int[] regionSits, Color[] basicColors, int[] maxArr) {
        LinkedList<Color> colors= new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (regionSits[i] != 0) {
                // Получаем коэффициент "осветления"
                int coef = Math.round( 100 - ( (float) regionSits[i]  / (float) maxArr[i] ) * 100 );
                // Осветляем коэффициент на coef% и заносим в список
                colors.add(new Color(basicColors[i]).ligherColor(coef));
            }
        }

        // Переносим значения из списка в массив
        Color[] colorMap = new Color[colors.size()];
        for(int i = 0; i < colorMap.length; i++) {
            colorMap[i] = colors.get(i);
        }
        colors.remove();

        return Color.mixColors(colorMap); // Смешиваем цвета из массива и возвращаем результирующий цвет
    }

    /**
     * Получаем цвета для каждого региона
     */
    public void initialiseColors() {
        int[] maxArr = getMaxArr(); // Получаем массив максимальных повторений ситуаций
        Color[] basicColors = Color.getBasicColors(n); // Получаем базовый цвета ситуаций

        for(int i = 0; i < regionsList.size(); i++) {
            Region currRegion = regionsList.get(i);
            int[] regionSits = currRegion.getSits();
            // Получаем цвет конкретного региона:
            String regionColor = getRegionColor(regionSits, basicColors, maxArr).getHex();
            // Устанавливаем цвет для этого региона
            currRegion.setColor(regionColor);
            regionsList.set(i, currRegion);
        }
    }

    /**
     * Возвращает список регионов
     * @return Список регионов
     */
    public ArrayList<Region> getList() {
        return regionsList;
    }

}

