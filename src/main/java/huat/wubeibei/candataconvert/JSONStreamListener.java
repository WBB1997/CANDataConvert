package huat.wubeibei.candataconvert;

public interface JSONStreamListener {
    void produce(String json);

    void onComplete();

    void onError(Throwable e);
}
