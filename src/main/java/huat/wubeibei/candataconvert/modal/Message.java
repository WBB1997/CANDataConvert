package huat.wubeibei.candataconvert.modal;

import java.util.HashMap;

public class Message {
    /**Message*/
    private Head head;

    /**
     * key = signalName
     * value = SignalDefine
     * */
    private HashMap<String, Signal> signalMap;
    /**
     * key = signalName
     * value = SignalValue
     * */
    private HashMap<String, Integer> valueMap;

    public Message() {
        head = new Head();
        signalMap = new HashMap<>();
        valueMap = new HashMap<>();
    }

    public void setSignalValue(String signalName, int signalValue){
        valueMap.put(signalName, signalValue);
    }

    public int getSignalValue(String signalName){
        return valueMap.get(signalName);
    }

    public String getKeyword(){
        return head.getMsgName();
    }

    public Head getHead() {
        return head;
    }

    public HashMap<String, Integer> getValueMap() {
        return valueMap;
    }

    public void setValueMap(HashMap<String, Integer> valueMap) {
        this.valueMap = valueMap;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public HashMap<String, Signal> getSignalMap() {
        return signalMap;
    }

    public void setSignalMap(HashMap<String, Signal> signalMap) {
        this.signalMap = signalMap;
    }

    @Override
    public String toString() {
        return "head: " + getHead().toString();
    }
}
