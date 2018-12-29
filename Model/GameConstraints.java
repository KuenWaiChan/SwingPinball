package Model;

public class GameConstraints {

  public  GameConstraints(GameConstraints c){
      this.TICKS=c.TICKS;
      this.TICK_SPEED=c.TICK_SPEED;
      this.g=c.g;
        this.mu1=c.mu1;
        this.mu2=c.mu2;
        this.absorption=c.absorption;
        this.wind=c.wind;

    }
    public static final double
            BOARD_WIDTH = 20.0;
    public static final double BOARD_HEIGHT = 20.0;
    public  double TICKS = 60.0;
    public  double TICK_SPEED = 1.0 / TICKS;


    private double
            g = 25.0,           //  25L/sec
            mu1 = 0.025,              //0.025 / SEC
            mu2 = 0.025;                  //0.025 / L
    private double absorption = 1;
    private double wind = 0;

    public GameConstraints() {

    }

    public void setTimeMultiplier(double x){
        TICKS = 60 * x;
        TICK_SPEED = 1.0/TICKS;

    }
    public double getGravity() {
        return g;     //25L per second.
    }

    public void setGravity(double g) {
        this.g = g;
    }

    public double getMu1() {
        return mu1;       // mu1/ticks = per tick
    }

    public double getMu2() {
        return mu2;
    }

    public void setFriction(double mu1, double mu2) {
        this.mu1 = mu1;
        this.mu2 = mu2;
    }

    public double getAbsorption() {
        return absorption;
    }

    public void setAbsorption(double absorption) {
        this.absorption = absorption;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double w){wind=w;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameConstraints that = (GameConstraints) o;

        if (Double.compare(that.g, g) != 0) return false;
        if (Double.compare(that.mu1, mu1) != 0) return false;
        if (Double.compare(that.mu2, mu2) != 0) return false;
        if (Double.compare(that.absorption, absorption) != 0) return false;
        return Double.compare(that.wind, wind) == 0;
    }
}
