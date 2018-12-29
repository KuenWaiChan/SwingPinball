package Controller.BuildListeners.BoardModifiers;

public interface IKeyConnectListener {
    void cancel();
    void submitData(Integer keyStroke, String actionCommand);
}
