package huat.wubeibei.candataconvert;

import com.alibaba.fastjson.JSONObject;
import huat.wubeibei.candataconvert.modal.Message;
import huat.wubeibei.candataconvert.modal.Signal;
import huat.wubeibei.candataconvert.util.ByteUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static huat.wubeibei.candataconvert.util.ByteUtil.Motorola;
import static java.util.Arrays.copyOfRange;

public class DataConvert {

    private HashMap<String, Message> MsgNameMap = new HashMap<>();
    private HashMap<String, Message> MsgIDMap = new HashMap<>();
    private InputStream config;
    private final static int headLength = 2;

    // 类初始化
    public DataConvert(InputStream inputStream) {
        this.config = inputStream;
        initMessageMap();
        System.out.println("DataConvert Resource load success!");
    }

    // 获得报文段的字节流，并重置报文段的值(恢复为默认值，如默认值小于零则不恢复)
    // msgID 为报文段的ID
    public byte[] getByte(String msgName) {
        Message msg = MsgNameMap.get(msgName);
        if (msg == null)
            return null;
        else {
            // 构建一个Byte数组
            int length = msg.getHead().getMsgLength() + headLength;
            byte[] bytes = new byte[length];
            // 获取信号列表
            Collection<Signal> signalCollection = msg.getSignalMap().values();
            // 遍历每一个信号
            for (Signal sig : signalCollection) {
                int SrcNum = msg.getSignalValue(sig.getSignalName());
                int Byte_offset = sig.getOffset();
                int start_bit_index = sig.getStartBitPosition();
                int bitLength = sig.getSignalLength();
                String state = sig.getLayoutFormat();
                ByteUtil.setBits(bytes, SrcNum, Byte_offset, start_bit_index, bitLength, state);
                // 顺便恢复默认值，如果值小于零则无效，不恢复
                if (sig.getDefaultValue() >= 0) {
                    msg.setSignalValue(sig.getSignalName(), sig.getDefaultValue());
                }
            }
            // 添加报文ID
            ByteUtil.setBits(bytes, Integer.parseInt(msg.getHead().getMsgID(), 16),0, (length - 1) * 8, 16, Motorola);
            return bytes;
        }
    }

    // 从字节流中获得报文的JSON串
    public void getJSONString(byte[] bytes, JSONStreamListener jsonStreamListener) {
        try {
            String key = ByteUtil.bytesToHex(copyOfRange(bytes, 8, 10));
            // 从第一个前导0开始一直到结尾截取字符串
            String sampleKey = key.substring(key.indexOf('0'));
            // 报文段
            Message message = MsgIDMap.get(sampleKey);
            Collection<Signal> signalCollection = message.getSignalMap().values();
            for (Signal sig : signalCollection) {
                double value = sig.getResolution() * ByteUtil.countBit(bytes, 0, sig.getStartBitPosition(), sig.getSignalLength(), sig.getLayoutFormat());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg_id", message.getHead().getMsgID());
                jsonObject.put("signal_name", sig.getSignalName());
                jsonObject.put("value", value);
                jsonStreamListener.produce(jsonObject.toJSONString());
            }
            jsonStreamListener.onComplete();
        } catch (Throwable e) {
            jsonStreamListener.onError(e);
        }
    }

    // 设置报文的值
    public void setSignalValue(String msgName, String signalName, double value) {
        try {
            Signal sig = MsgNameMap.get(msgName).getSignalMap().get(signalName);
            int maxValue = sig.getMaxValue();
            int minValue = sig.getMinValue();
            if (value < minValue || value > maxValue)
                return;
            int realValue = (int) (value / sig.getResolution());
            MsgNameMap.get(msgName).setSignalValue(signalName, realValue);
        } catch (Throwable e) {
            System.out.println("huat.wubeibei.candataconvert.DataConvert: No signal found");
        }
    }

    // 测试
    public static void main(String[] args) {
//        File file = new File("D:\\AndroidStudioProject\\SmartScreen\\app\\src\\main\\assets\\messageLayout.xml");
//        huat.wubeibei.huat.wubeibei.candataconvert.DataConvert dataConvert = new huat.wubeibei.huat.wubeibei.candataconvert.DataConvert(file);
//        dataConvert.initMessageMap();
    }

    // 初始化报文
    private void initMessageMap() {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(config);
            Element rootElement = document.getRootElement();
            List<Element> messageList_element = rootElement.elements("Message");
            // 设置报文
            for (Element msg_element : messageList_element) {
                Message msg = new Message();
                // 初始化报文头
                Element head_element = msg_element.element("Head");
                fillAttribute(head_element, msg.getHead());
                // 初始化每一个信号与其值
                List<Element> signalList_element = msg_element.element("SignalList").elements();
                HashMap<String, Signal> signalHashMap = new HashMap<>();
                HashMap<String, Integer> valueHashMap = new HashMap<>();
                for (Element signal_element : signalList_element) {
                    Signal sig = new Signal();
                    fillAttribute(signal_element, sig);
                    ///////////////////////////////////////
                    signalHashMap.put(sig.getKeyword(), sig);
                    int value = sig.getDefaultValue() != -1 ? sig.getDefaultValue() : 0;
                    valueHashMap.put(sig.getKeyword(), value);
                }
                // 设置信号与其值
                msg.setSignalMap(signalHashMap);
                msg.setValueMap(valueHashMap);
                //
                System.out.println("huat.wubeibei.candataconvert.DataConvert->initMessageMap: " + msg.toString());
                // 存储报文
                MsgNameMap.put(msg.getKeyword(), msg);
                MsgIDMap.put(msg.getHead().getMsgID(), msg);
            }
        } catch (DocumentException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 初始化报文的子函数
    private void fillAttribute(Element element, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (object != null) {
            // 拿到该类
            Class<?> clz = object.getClass();
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields) {
                // 如果类型是String
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    Method m = object.getClass().getMethod("set" + field.getName(), String.class);
                    m.invoke(object, element.element(field.getName()).getData());
                }
                // 如果类型是int
                if (field.getGenericType().toString().equals("int")) {
                    Method m = object.getClass().getMethod("set" + field.getName(), int.class);
                    String num = (String) element.element(field.getName()).getData();
                    m.invoke(object, Integer.parseInt(num));
                }
                // 如果类型是double
                if (field.getGenericType().toString().equals("double")) {
                    Method m = object.getClass().getMethod("set" + field.getName(), double.class);
                    String num = (String) element.element(field.getName()).getData();
                    m.invoke(object, Double.parseDouble(num));
                }
            }
        }
    }
}
