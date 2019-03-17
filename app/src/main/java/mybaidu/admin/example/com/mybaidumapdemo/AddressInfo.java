package mybaidu.admin.example.com.mybaidumapdemo;

import java.io.Serializable;

public class AddressInfo implements Serializable {
    private double latitude;
    private double longitude;
    private int imageId;
    private String name;
    private String distance;

    public AddressInfo() {

    }

    public AddressInfo(double latitude, double longitude, int imageId, String name, String distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageId = imageId;
        this.name = name;
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
