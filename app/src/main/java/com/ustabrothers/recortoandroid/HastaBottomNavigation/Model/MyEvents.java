package com.ustabrothers.recortoandroid.HastaBottomNavigation.Model;

public class MyEvents {
    private String eventId;
    private String eventName;
    private String eventDate;

    public MyEvents() {
        // Boş yapıcı metot gereklidir.
    }

    public MyEvents(String eventId, String eventName, String eventDate) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public String toString() {
        return "Etkinlik İsmi: " + eventName + ", Tarih: " + eventDate;
    }
}
