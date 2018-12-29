package Controller.BuildListeners.GlobalModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;

public class WindListener extends AGlobalListener {
    private final double baseWind = 25;

    public WindListener(IGizmoModel model) {
        super(model, 20, -20, 0, 10);
    }

    @Override
    Instruction getInstruction(double value) {
        return new DefaultInstructions.SetWind((value)*baseWind);
    }


}
