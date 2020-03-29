package huat.wubeibei.candataconvert.modal;



public class Head {
    private String MsgName;
    private String MsgID;
    private String MsgSendType;
    private int MsgCycleTime;
    private int MsgLength;
    private int Times;

    public String getMsgName() {
        return MsgName;
    }

    public void setMsgName(String msgName) {
        MsgName = msgName;
    }

    public String getMsgID() {
        return MsgID;
    }

    public void setMsgID(String msgID) {
        MsgID = msgID;
    }

    public String getMsgSendType() {
        return MsgSendType;
    }

    public void setMsgSendType(String msgSendType) {
        MsgSendType = msgSendType;
    }

    public int getMsgCycleTime() {
        return MsgCycleTime;
    }

    public void setMsgCycleTime(int msgCycleTime) {
        MsgCycleTime = msgCycleTime;
    }

    public int getMsgLength() {
        return MsgLength;
    }

    public void setMsgLength(int msgLength) {
        MsgLength = msgLength;
    }

    public int getTimes() {
        return Times;
    }

    public void setTimes(int times) {
        Times = times;
    }

    @Override
    public String toString() {
        return "{MsgName=" + getMsgName() + "," +
                "MsgID=" + getMsgID() + "," +
                "MsgSendType=" + getMsgSendType() + "," +
                "MsgCycleTime=" + getMsgCycleTime() + "," +
                "MsgLength=" + getMsgLength() + "," +
                "Times=" + getTimes() + "," +
                "}";
    }
}
