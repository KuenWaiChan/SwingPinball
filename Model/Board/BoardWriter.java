package Model.Board;

import Model.GameConstraints;
import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.ProtoGizmo;
import Model.util.GizmoType;
import javafx.util.Pair;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BoardWriter {

	public void writeFile(String file, HashMap<String, ProtoGizmo> gizmos, GameConstraints constraints, HashMap<Integer, HashSet<ProtoGizmo>> keyDown, HashMap<Integer, HashSet<ProtoGizmo>> keyUp) {
		try {
			String out = "";
			for (ProtoGizmo ps : gizmos.values()) {
				ProtoGizmo g = ps;
				GizmoType type = g.getType();
				String name = ps.getName();
				IBoardPlacement shape = g.getDrawingLocation();
				double dx = shape.x();
				double dy = shape.y();

				int x = (int) dx;
				int y = (int) dy;
				switch (type) {
					case Absorber:
						int h = (int) shape.h();
						int w = (int) shape.w();
						out = out + generateAbs(name, x, y, x + w, y + h, ps.hasDefaultColourScheme(), ps.getOriginalColor(), ps.getAlternateColor());

						break;
					case Ball:
						float vx = (float) g.getVelocity().x();
						float vy = (float) g.getVelocity().y();
						out = out + generateBall(name, (float) (g.getPhysicsLocation().getCenter().x()), (float) (g.getPhysicsLocation().getCenter().y()), vx, vy,ps.hasDefaultColourScheme(), ps.getOriginalColor(), ps.getAlternateColor());
						break;
					default:
						out = out + generateGiz(type, name, x, y,ps.hasDefaultColourScheme(), ps.getOriginalColor(), ps.getAlternateColor());
						break;
				}


				for (int i = 0; i < g.getDrawingLocation().r() / 90; i++)
					out += generateRot(name);
			}
			for (ProtoGizmo pp : gizmos.values()) {
				for (ProtoGizmo c :  pp .getConnectedGizmos())
					out += generateConnect(pp.getName(), c.getName());
			}
			out = out + "Gravity " + constraints.getGravity() + "\r\n";
			out = out + "Friction " + (float) constraints.getMu1() + " " + (float) constraints.getMu2() + "\r\n";
			out = out + "Wind " + constraints.getWind() + "\r\n";
			out = out + "Absorption " + constraints.getAbsorption() + "\r\n";
				
			for (Integer c : keyUp.keySet())
				for (ProtoGizmo p : keyUp.get(c))
					out += generateKeyConnect(c, true, p.getName());


			for (Integer c : keyDown.keySet())
				for (ProtoGizmo p : keyDown.get(c))
					out += generateKeyConnect(c, false, p.getName());


			File f = new File(file);
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(out);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private String generateGiz(GizmoType type, String name, int x, int y, boolean hasDefault, Color orig, Color alt) {
		String ret = type + " " + name + " " + x + " " + y ;
		if(!hasDefault) ret = ret + " " + orig.getRed() +" " +orig.getGreen()+ " " +orig.getBlue() + " " + alt.getRed()+ " " +alt.getGreen()+ " "+ alt.getBlue();


		return (ret + "\r\n");
	}

	private String generateAbs(String name, int x, int y, int l, int h, boolean hasDefault, Color orig, Color alt) {
		String ret= "Absorber " + name + " " + x + " " + y + " " + l + " " + h  ;
		if(!hasDefault) ret = ret + " " + orig.getRed() +" " +orig.getGreen()+ " " +orig.getBlue() + " " + alt.getRed()+ " " +alt.getGreen()+ " "+ alt.getBlue();


		return (ret+"\r\n");
	}

	private String generateBall(String name, float x, float y, float vx, float vy, boolean hasDefault, Color orig, Color alt) {
		String ret = "Ball " + name + " " + x+" " +  y  + " " + vx + " " + vy;
		if(!hasDefault) ret = ret + " " + orig.getRed() +" " +orig.getGreen()+ " " +orig.getBlue() + " " + alt.getRed()+ " " +alt.getGreen()+ " "+ alt.getBlue();


		return ret+"\r\n";
	}

	private String generateRot(String name) {
		return "Rotate " + name + "\r\n";
	}

	private String generateConnect(String nameA, String nameB) {
		return "Connect " + nameA + " " + nameB + "\r\n";
	}

	private String generateKeyConnect(Integer key, boolean up, String name) {
		String s = "KeyConnect key " + key + " ";
		if (up)
			s += "up ";
		else
			s += "down ";
		s += name;
		return s + "\r\n";
	}
}
