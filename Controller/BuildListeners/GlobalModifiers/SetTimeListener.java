package Controller.BuildListeners.GlobalModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;

public class SetTimeListener  extends AGlobalListener{


    public SetTimeListener(IGizmoModel model) {
        super(model, 20, 0, 1, 10);
    }

    @Override
    Instruction getInstruction(double value) {
        return new DefaultInstructions.SetTimeMultipler(10-value);
    }

}
