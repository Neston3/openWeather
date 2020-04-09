package com.maricajr.openweather;

public class CardItem {
    private String city;
    private String wind;
    private String humidity;
    private String pressure;
    private String clouds;
    private String temperature;


    CardItem(String title, String wind, String humidity, String pressure, String clouds, String temperature) {
        this.city = title;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
        this.clouds = clouds;
        this.temperature = temperature;
    }

    String getWind() {
        return wind + " km/hr";
    }

    String getHumidity() {
        return humidity + "%";
    }

    String getPressure() {
        return pressure + " hpa";
    }

    String getClouds() {
        return clouds;
    }

    String getTemperature() {
        return temperature + " \u2103";
    }

    String getCity() {
        return city;
    }

}
