package Model;

import Model.Board.ConcreteGizmoModel;
import Model.util.DefaultInstructions;

public interface Instruction {
    void process(ConcreteGizmoModel concreteModel) throws DefaultInstructions.BadInstructionException;
}
