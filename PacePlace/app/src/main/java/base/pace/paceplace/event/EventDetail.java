package base.pace.paceplace.event;

public class EventDetail {
    private String mEventName, mEventDay, mEventDateTime, mEventAddress, mEventRoom,
            mEventGradType, mEventSubjectType,mEventCreatedBy,mEventDescription;

    private int mLocationId, mEventCreatedByUserId;

    public EventDetail(String eventName, String eventDay, String eventDateTime, String eventAddress, String eventRoom, String eventGradType,
                String eventSubjectType, String eventCreatedBy, String eventDescription){
        mEventName = eventName;
        mEventDay = eventDay;
        mEventDateTime=eventDateTime;

        mEventAddress = eventAddress;
        mEventRoom = eventRoom;
        mEventGradType = eventGradType;
        mEventSubjectType = eventSubjectType;
        mEventCreatedBy = eventCreatedBy;
        mEventDescription = eventDescription;

    }

    public EventDetail(String eventName, String eventDateTime, String eventDescription,
                       String eventGradType,String eventSubjectType, int eventCreatedByUserId, String eventLocation){
        mEventName = eventName;
        mEventDateTime=eventDateTime;
        mEventDescription = eventDescription;
        mEventGradType = eventGradType;
        mEventSubjectType = eventSubjectType;
        mEventCreatedByUserId = eventCreatedByUserId;
        mEventAddress = eventLocation;
    }

    public int getmLocationId() {
        return mLocationId;
    }

    public void setmLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public int getmEventCreatedByUserId() {
        return mEventCreatedByUserId;
    }

    public void setmEventCreatedByUserId(int mEventCreatedByUserId) {
        this.mEventCreatedByUserId = mEventCreatedByUserId;
    }

    public String getmEventName() {
        return mEventName;
    }

    public String getmEventDay() {
        return mEventDay;
    }

    public String getmEventDateTime() {
        return mEventDateTime;
    }

    public String getmEventAddress() {
        return mEventAddress;
    }

    public String getmEventRoom() {
        return mEventRoom;
    }

    public String getmEventGradType() {
        return mEventGradType;
    }

    public String getmEventSubjectType() {
        return mEventSubjectType;
    }

    public String getmEventCreatedBy() {
        return mEventCreatedBy;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public void setmEventDay(String mEventDay) {
        this.mEventDay = mEventDay;
    }

    public void setmEventDateTime(String mEventDateTime) {
        this.mEventDateTime = mEventDateTime;
    }

    public void setmEventAddress(String mEventAddress) {
        this.mEventAddress = mEventAddress;
    }

    public void setmEventRoom(String mEventRoom) {
        this.mEventRoom = mEventRoom;
    }

    public void setmEventGradType(String mEventGradType) {
        this.mEventGradType = mEventGradType;
    }

    public void setmEventSubjectType(String mEventSubjectType) {
        this.mEventSubjectType = mEventSubjectType;
    }

    public void setmEventCreatedBy(String mEventCreatedBy) {
        this.mEventCreatedBy = mEventCreatedBy;
    }

    public String getmEventDescription() {
        return mEventDescription;
    }

    public void setmEventDescription(String mEventDescription) {
        this.mEventDescription = mEventDescription;
    }
}
