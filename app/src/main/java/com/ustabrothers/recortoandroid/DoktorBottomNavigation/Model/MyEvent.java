package com.ustabrothers.recortoandroid.DoktorBottomNavigation.Model;

public class MyEvent {
    private String eventId;
    private String eventName;
    private int eventDay;
    private int eventMonth;
    private int eventYear;

    public MyEvent() {
        // Boş yapıcı metot gereklidir.
    }

    public MyEvent(String eventId, String eventName, int eventDay, int eventMonth, int eventYear) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDay = eventDay;
        this.eventMonth = eventMonth;
        this.eventYear = eventYear;
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

    public int getEventDay() {
        return eventDay;
    }

    public void setEventDay(int eventDay) {
        this.eventDay = eventDay;
    }

    public int getEventMonth() {
        return eventMonth;
    }

    public void setEventMonth(int eventMonth) {
        this.eventMonth = eventMonth;
    }

    public int getEventYear() {
        return eventYear;
    }

    public void setEventYear(int eventYear) {
        this.eventYear = eventYear;
    }

    // toString metodu, etkinliği listelediğinizde görüntülenecek metni tanımlar.
    @Override
    public String toString() {
        return eventName + " - " + eventDay + "/" + eventMonth + "/" + eventYear;
    }
}


