package men.brakh.emergencymap.http;

public interface APICommunication {
    public long getPopulation(String region);
    public String getCoords(String region);
}
