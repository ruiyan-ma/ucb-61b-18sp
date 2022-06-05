public class NBody {

    /**
     * Read the radius of the universe in the given file.
     */
    public static double readRadius(String file) {
        In in = new In(file);
        int planetNum = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    /**
     * Given a file name, returns an array of Planets
     * corresponding to the planets in the file.
     */
    public static Planet[] readPlanets(String file) {
        In in = new In(file);
        int planetNum = in.readInt();
        double radius = in.readDouble();

        Planet[] planets = new Planet[planetNum];
        for (int i = 0; i < planetNum; i++) {
            /* Each line has the x and y coordinates of the initial position,
             * x and y components of the inital velocity, the mass, the name
             * of an image file used to display the planet.  */
            double xPos = in.readDouble(), yPos = in.readDouble();
            double xVel = in.readDouble(), yVel = in.readDouble();
            double mass = in.readDouble();
            String imgFile = in.readString();
            planets[i] = new Planet(xPos, yPos, xVel, yVel, mass, imgFile);
        }

        return planets;
    }

    public static void main(String[] args) {
        // Collecting all needed input
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double r = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        // Drawing the background
        StdDraw.setScale(-r, r);
        StdDraw.picture(0, 0, "images/starfield.jpg");

        // Drawing planets
        for (Planet body : planets) {
            body.draw();
        }

        // Creating an animation
        StdDraw.enableDoubleBuffering();
        double time = 0;

        // For each time through the loop
        while (time < T) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];

            // Calculate the nex x and y forces for each Planet
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            // Call update method on each of the Planets.
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            // Draw the background image
            StdDraw.picture(0, 0, "images/starfield.jpg");

            // Draw all Planets
            for (Planet body : planets) {
                body.draw();
            }

            // Show the offscreen buffer and pause the animation for 10 milliseconds
            StdDraw.show();
            StdDraw.pause(10);

            // Increase time by dt
            time += dt;
        }

        // Print out the final state of the universe
        StdOut.println(planets.length);
        StdOut.println(r);
        for (Planet planet : planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planet.xxPos, planet.yyPos, planet.xxVel, planet.yyVel,
                    planet.mass, planet.imgFileName);
        }
    }
}
