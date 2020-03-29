package huat.wubeibei.candataconvert.modal;

public class Signal {
    private String SignalName;
    private String LayoutFormat;
    private int SignalLength;
    private int StartBitPosition;
    private double Resolution;
    private int Offset;
    private int MinValue;
    private int MaxValue;
    private int DefaultValue;
    private int InvalidValue;

    public String getKeyword(){
        return SignalName;
    }

    public String getSignalName() {
        return SignalName;
    }

    public void setSignalName(String signalName) {
        SignalName = signalName;
    }

    public String getLayoutFormat() {
        return LayoutFormat;
    }

    public void setLayoutFormat(String layoutFormat) {
        LayoutFormat = layoutFormat;
    }

    public int getSignalLength() {
        return SignalLength;
    }

    public void setSignalLength(int signalLength) {
        SignalLength = signalLength;
    }

    public int getStartBitPosition() {
        return StartBitPosition;
    }

    public void setStartBitPosition(int startBitPosition) {
        StartBitPosition = startBitPosition;
    }

    public double getResolution() {
        return Resolution;
    }

    public void setResolution(double resolution) {
        Resolution = resolution;
    }

    public int getOffset() {
        return Offset;
    }

    public void setOffset(int offset) {
        Offset = offset;
    }

    public int getMinValue() {
        return MinValue;
    }

    public void setMinValue(int minValue) {
        MinValue = minValue;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }

    public int getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        DefaultValue = defaultValue;
    }

    public int getInvalidValue() {
        return InvalidValue;
    }

    public void setInvalidValue(int invalidValue) {
        InvalidValue = invalidValue;
    }
}
