package Model.Board;

import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import physics.Vect;

import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

    public final String
            name = "([0-9A-Za-z_]+)",
            intMatch = "(\\d+)",
            floatMatch = "(\\d*\\.\\d+)",
            intPairMatch = intMatch + " " + intMatch,
            floatPairMatch = floatMatch + " " + floatMatch,
            colourSingleMatch = intMatch + " " + intPairMatch, //Red, Green, Blue representation
            colourMatch = colourSingleMatch + " " + colourSingleMatch;
    public final Pattern
            giz = Pattern.compile("(Square|Circle|Triangle|RightFlipper|LeftFlipper) " + name + " " + intPairMatch),
            gizCol = Pattern.compile(giz.pattern() + " " + colourMatch),
            abs = Pattern.compile("Absorber " + name + " " + intPairMatch + " " + intPairMatch),
            absCol = Pattern.compile(abs.pattern() + " " + colourMatch),
            ball = Pattern.compile("Ball " + name + " " + floatPairMatch + " " + floatPairMatch),
            ballCol = Pattern.compile(ball.pattern() + " " + colourMatch),
            rot = Pattern.compile("Rotate " + name),
            del = Pattern.compile("Delete " + name),
            moveFloat = Pattern.compile("Move " + name + " " + floatPairMatch),
            moveInt = Pattern.compile("Move " + name + " " + intPairMatch),
            connect = Pattern.compile("Connect " + name + " " + name),
            keyConnect = Pattern.compile("KeyConnect key (\\d+) (up|down) " + name),
            gravSet = Pattern.compile("Gravity " + floatMatch),
            fricSet = Pattern.compile("Friction " + floatPairMatch),
            windSet = Pattern.compile("Wind " + floatMatch),
            absorpSet = Pattern.compile("Absorption " +  floatMatch);

    Set<String> names = new HashSet<>(Collections.singletonList("Outer Walls"));

    public Instruction parseInstruction(String fileLine) {
        Matcher m = gizCol.matcher(fileLine);
        fileLine = fileLine.trim();

        if (m.matches()) {
            String opCode = m.group(1);
            String name = m.group(2);
            Color o, a;

            if (names.contains(name))
                return new DefaultInstructions.DoNothing();

            Vect v = new Vect(1, 1);
            Location2D l = new Location2D(Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)), 1, 1);
            o = new Color(Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)));
            a = new Color(Integer.parseInt(m.group(8)), Integer.parseInt(m.group(9)), Integer.parseInt(m.group(10)));

            names.add(name);

            return new DefaultInstructions.AddGizmo(GizmoType.valueOf(opCode), name, l, v, o, a);

        }

        m = giz.matcher(fileLine);
        if (m.matches()) {
            String opCode = m.group(1);
            String name = m.group(2);

            if (names.contains(name))
                return new DefaultInstructions.DoNothing();

            Vect v = new Vect(1, 1);
            Location2D l = new Location2D(Double.parseDouble(m.group(3)), Double.parseDouble(m.group(4)), 1, 1);

            names.add(name);

            return new DefaultInstructions.AddGizmo(GizmoType.valueOf(opCode), name, l, v);

        }

        m = ballCol.matcher(fileLine);

        if (m.matches()) {

            String name = m.group(1);
            if (names.contains(name))
                return new DefaultInstructions.DoNothing();
            Double x = Double.parseDouble(m.group(2));
            Double y = Double.parseDouble(m.group(3));
            Double r = 0.25;
            if (!(x - r < 0))
                x = x - r;
            if (!(y - r < 0))
                y = y - r;

            Location2D l = new Location2D(x, y, r, r);

            Vect v = new Vect(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));
            Color o = new Color(Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8))),
                    a = new Color(Integer.parseInt(m.group(9)), Integer.parseInt(m.group(10)), Integer.parseInt(m.group(11)));

            names.add(name);
            return new DefaultInstructions.AddGizmo(GizmoType.Ball, name, l, v,o,a);
        }

        m = ball.matcher(fileLine);

        if (m.matches()) {

            String name = m.group(1);
            if (names.contains(name))
                return new DefaultInstructions.DoNothing();
            Double x = Double.parseDouble(m.group(2));
            Double y = Double.parseDouble(m.group(3));
            Double r = 0.25;
            if (!(x - r < 0))
                x = x - r;
            if (!(y - r < 0))
                y = y - r;

            Location2D l = new Location2D(x, y, r, r);

            Vect v = new Vect(Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)));

            names.add(name);
            return new DefaultInstructions.AddGizmo(GizmoType.Ball, name, l, v);
        }


        m = absCol.matcher(fileLine);
        if (m.matches()) {
            String name = m.group(1);
            if (names.contains(name))
                return new DefaultInstructions.DoNothing();

            double width = Double.parseDouble(m.group(4)) - Double.parseDouble(m.group(2));
            double height = Double.parseDouble(m.group(5)) - Double.parseDouble(m.group(3));
            Location2D l = new Location2D(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(3)), width, height);
            Vect v = new Vect(0, 0);

            Color o = new Color(Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8))),
                    a = new Color(Integer.parseInt(m.group(9)), Integer.parseInt(m.group(10)), Integer.parseInt(m.group(11)));
            names.add(name);
            return new DefaultInstructions.AddGizmo(GizmoType.Absorber, name, l, v,o,a);
        }

        m = abs.matcher(fileLine);
        if (m.matches()) {
            String name = m.group(1);
            if (names.contains(name))
                return new DefaultInstructions.DoNothing();

            double width = Double.parseDouble(m.group(4)) - Double.parseDouble(m.group(2));
            double height = Double.parseDouble(m.group(5)) - Double.parseDouble(m.group(3));
            Location2D l = new Location2D(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(3)), width, height);
            Vect v = new Vect(0, 0);

            names.add(name);
            return new DefaultInstructions.AddGizmo(GizmoType.Absorber, name, l, v);
        }

        m = rot.matcher(fileLine);
        if (m.matches()) {
            if (!names.contains(m.group(1))) {
                return new DefaultInstructions.DoNothing();
            }
            return new DefaultInstructions.Rotate(m.group(1));
        }

        m = del.matcher(fileLine);
        if (m.matches()) {
            if (!names.contains(m.group(1)))
                return new DefaultInstructions.DoNothing();
            return new DefaultInstructions.RemoveGizmo(m.group(1));
        }
        m = moveFloat.matcher(fileLine);


        if (m.matches()) {
            if (!names.contains(m.group(1))) {
                return new DefaultInstructions.DoNothing();
            }
            return new DefaultInstructions.Shift(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(3)), m.group(1));

        }
        m = moveInt.matcher(fileLine);


        if (m.matches()) {
            if (!names.contains(m.group(1))) {
                return new DefaultInstructions.DoNothing();
            }
            return new DefaultInstructions.Shift(Double.parseDouble(m.group(2)), Double.parseDouble(m.group(3)), m.group(1));


        }
        m = connect.matcher(fileLine);


        if (m.matches()) {
            if (!names.contains(m.group(1)) || !names.contains(m.group(2))) {
                return new DefaultInstructions.DoNothing();
            }
            return new DefaultInstructions.Connect(m.group(1), m.group(2));
        }
        m = keyConnect.matcher(fileLine);

        if (m.matches()) {

            if (!names.contains(m.group(3))) {
                return new DefaultInstructions.DoNothing();
            }

            return new DefaultInstructions.KeyConnect(Integer.parseInt(m.group(1)), m.group(2), m.group(3));
        }

        m = gravSet.matcher(fileLine);
        if (m.matches()) return new DefaultInstructions.SetGravity(Double.parseDouble(m.group(1)));

        m = fricSet.matcher(fileLine);
        if (m.matches()) return new DefaultInstructions.SetFriction(Double.parseDouble(m.group(1)), Double.parseDouble(m.group(2)));

        m= windSet.matcher(fileLine);
        if(m.matches()) return new DefaultInstructions.SetWind(Double.parseDouble(m.group(1)));

        m= absorpSet.matcher(fileLine);
        if(m.matches()) return new DefaultInstructions.SetAbsorptionRate(Double.parseDouble(m.group(1)));

        return new DefaultInstructions.DoNothing();
    }

    void readFile(String filename, IGizmoModel model) throws FileNotFoundException {

        try {
            BufferedReader in = new BufferedReader(new FileReader(new File((filename))));
            String line = "";

            while ((line = in.readLine()) != null) {
                if (!line.isEmpty())
                    model.executeInstruction(parseInstruction(line));
//                    parseInstruction(line).process((ConcreteGizmoModel) model);

            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

