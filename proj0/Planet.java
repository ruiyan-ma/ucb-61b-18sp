import java.lang.Math;

public class Planet {

    /** Gravitational constant. */
    public static final double G = 6.67e-11;

    /**
     * First constructor.
     */
    public Planet(double xP, double yP, double xV, double yV,
                  double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    /**
     * Second constructor: take in a Body object and initialize
     * an identical Body object.
     */
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    /**
     * Calculates the distance between two bodys.
     */
    public double calcDistance(Planet planet) {
        double dx = planet.xxPos - xxPos;
        double dy = planet.yyPos - yyPos;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * Calculates the force exerted on this body by the given body.
     */
    public double calcForceExertedBy(Planet planet) {
        return G * mass * planet.mass / Math.pow(calcDistance(planet), 2);
    }

    /**
     * Calculate the force exerted in the X direction.
     */
    public double calcForceExertedByX(Planet planet) {
        double dx = planet.xxPos - xxPos;
        return calcForceExertedBy(planet) * dx / calcDistance(planet);
    }

    /**
    * Calculate the force exerted in the Y direction.
    */
    public double calcForceExertedByY(Planet planet) {
        double dy = planet.yyPos - yyPos;
        return calcForceExertedBy(planet) * dy / calcDistance(planet);
    }

    /**
     * Calculate the net X force exerted by all bodies in an array.
     */
    public double calcNetForceExertedByX(Planet[] planets) {
        double xNetForce = 0;
        for (Planet body : planets) {
            if (!this.equals(body)) {
                xNetForce += calcForceExertedByX(body);
            }
        }
        return xNetForce;
    }

    /**
     * Calculate the net X force exerted by all bodies in an array.
     */
    public double calcNetForceExertedByY(Planet[] planets) {
        double yNetForce = 0;
        for (Planet body : planets) {
            if (!this.equals(body)) {
                yNetForce += calcForceExertedByY(body);
            }
        }
        return yNetForce;
    }

    /**
     *
     * */
    public void update(double dt, double fX, double fY) {
        // 1. Calculate the acceleration using the provided x and y force.
        double xAcc = fX / mass;
        double yAcc = fY / mass;

        // 2. Calculate the new velocity by using the acceleration and current velocity.
        xxVel += xAcc * dt;
        yyVel += yAcc * dt;

        // 3. Calculate the new position by using the new velocity.
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }


    /**
     * Draw the Body's image at the Body's position.
     * */
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

    /** Its current x position. */
    public double xxPos;

    /** Its current y position. */
    public double yyPos;

    /** Its current velocity in the x direction. */
    public double xxVel;

    /** Its current velocity in the y direction. */
    public double yyVel;

    /**  Its mass. */
    public double mass;

    /** The name of the file that corresponds to the image that depicts the body. */
    public String imgFileName;
}
