package View;

import Model.Board.ConcreteGizmoModel;
import Model.GameConstraints;
import View.MainView.GBallView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class TempDriver {
    public static void main(String[] args) throws FileNotFoundException {
        ConcreteGizmoModel model = new ConcreteGizmoModel(new GameConstraints(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        GBallView view = new GBallView(model);

//        String filen = "gizmoball\\gizE1.txt";
//
////        ////System.out.println.out.println(file.getAbsolutePath());
//        model.loadFile(filen);

        view.update();
    }

}
