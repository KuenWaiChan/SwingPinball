package Controller.BuildListeners.GlobalModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;

import javax.swing.*;

public class AbsorptionListener extends  AGlobalListener{
    private double base = 0.1;

    public AbsorptionListener(IGizmoModel model) {
        super(model, 10,1, 10, 1   );
    }

    @Override
    Instruction getInstruction(double value) {
        return new DefaultInstructions.SetAbsorptionRate((value+1/SCALE)*base);
    }
}
