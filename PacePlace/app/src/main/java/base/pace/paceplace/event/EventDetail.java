package base.pace.paceplace.event;

public class EventDetail {
    private String mEventName, mEventDay, mEventDateTime, mEventAddress, mEventRoom, mEventGradType,mEventStudentType,mEventCreatedBy;

    public EventDetail(String eventName, String eventDay, String eventDateTime, String eventAddress, String eventRoom, String eventGradType,
                String eventStudentType, String eventCreatedBy){
        mEventName = eventName;
        mEventDay = eventDay;
        mEventDateTime=eventDateTime;

        mEventAddress = eventAddress;
        mEventRoom = eventRoom;
        mEventGradType = eventGradType;
        mEventStudentType = eventStudentType;
        mEventCreatedBy = eventCreatedBy;

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

    public String getmEventStudentType() {
        return mEventStudentType;
    }

    public String getmEventCreatedBy() {
        return mEventCreatedBy;
    }

}
