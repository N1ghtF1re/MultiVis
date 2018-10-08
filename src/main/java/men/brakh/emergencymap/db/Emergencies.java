package men.brakh.emergencymap.db;



import javax.persistence.*;
import java.sql.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Emergencies {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String region;

    private Date date;

    private int situation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }
}
