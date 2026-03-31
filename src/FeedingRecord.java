public class FeedingRecord {
    private String babyName;
    private String feedingType;
    private double amountOz;
    private String feedingTime;
    private int durationMinutes;
    private String notes;

    public FeedingRecord(String babyName, String feedingType, double amountOz, String feedingTime, int durationMinutes, String notes) {
        this.babyName = babyName;
        this.feedingType = feedingType;
        this.amountOz = amountOz;
        this.feedingTime = feedingTime;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

    public String getBabyName() {
        return this.babyName;
    }

    public String getFeedingType() {
        return this.feedingType;
    }

    public double getAmountOz() {
        return this.amountOz;
    }

    public String getFeedingTime() {
        return this.feedingTime;
    }

    public int getDurationMinutes() {
        return this.durationMinutes;
    }

    public String getNotes() {
        return this.notes;
    }


    public void setBabyName(String babyName) { this.babyName = babyName; }
    public void setFeedingType(String feedingType) { this.feedingType = feedingType; }
    public void setAmountOz(double amountOz) { this.amountOz = amountOz; }
    public void setFeedingTime(String feedingTime) { this.feedingTime = feedingTime; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setNotes(String notes) { this.notes = notes; }
}
