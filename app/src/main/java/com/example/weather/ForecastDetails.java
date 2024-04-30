package com.example.weather;

import java.util.List;

/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class ForecastDetails {
    private String forecastLocationName;
    private String forecastDateToday;
    private String forecastWindToday;
    private String forecastPressureToday;
    private String forecastHumidityToday;
    private String temperatureMinToday;
    private String uvRiskToday;
    private String pollutionToday;
    private String sunsetToday;
    private String sunriseToday;
    private String visibilityToday;
    private String windDirectionToday;

    private String airQualityIndexToday;

    private List<String[]> forecastList;


    // Getters and setters for all fields

    public String getForecastLocationName() {
        return forecastLocationName;
    }

    public void setForecastLocationName(String forecastLocationName) {
        this.forecastLocationName = forecastLocationName;
    }


    public String getForecastDate() {
        return forecastDateToday;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDateToday = forecastDate;
    }

    public String getForecastWind() {
        return forecastWindToday;
    }

    public void setForecastWind(String forecastWind) {
        this.forecastWindToday = forecastWind;
    }

    public String getForecastPressure() {
        return forecastPressureToday;
    }

    public void setForecastPressure(String forecastPressure) {
        this.forecastPressureToday = forecastPressure;
    }

    public String getForecastHumidity() {
        return forecastHumidityToday;
    }

    public void setForecastHumidity(String forecastHumidity) {
        this.forecastHumidityToday = forecastHumidity;
    }

    public String getTemperatureMinToday() {
        return temperatureMinToday;
    }

    public void setTemperatureMinToday(String temperatureMin) {
        this.temperatureMinToday = temperatureMin;
    }


    public String getAirQualityIndex() {
        return airQualityIndexToday;
    }

    public void setAirQualityIndex(String airQualityIndex) {
        this.airQualityIndexToday = airQualityIndex;
    }

    public String getUvRiskToday() {
        return uvRiskToday;
    }

    public void setUvRiskToday(String uvRiskToday) {
        this.uvRiskToday = uvRiskToday;
    }

    public String getPollutionToday() {
        return pollutionToday;
    }

    public void setPollutionToday(String pollutionToday) {
        this.pollutionToday = pollutionToday;
    }

    public String getSunsetToday() {
        return sunsetToday;
    }

    public void setSunsetToday(String sunsetToday) {
        this.sunsetToday = sunsetToday;
    }

    public String getSunriseToday() {
        return sunriseToday;
    }

    public void setSunriseToday(String sunriseToday) {
        this.sunriseToday = sunriseToday;
    }

    public String getVisibilityToday() {
        return visibilityToday;
    }

    public void setVisibilityToday(String visibilityToday) {
        this.visibilityToday = visibilityToday;
    }

    public String getWindDirectionToday() {
        return windDirectionToday;
    }

    public void setWindDirectionToday(String windDirectionToday) {
        this.windDirectionToday = windDirectionToday;
    }

    public List<String[]> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<String[]> forecastList) {
        this.forecastList = forecastList;
    }
}
