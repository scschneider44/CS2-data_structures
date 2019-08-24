package edu.caltech.cs2.project01;

// import edu.caltech.cs2.project01.Body;
import edu.caltech.cs2.libraries.IBody;
import edu.caltech.cs2.libraries.StdDraw;
import edu.caltech.cs2.libraries.Vector2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBodySimulation {
    public static void main(String[] args) throws FileNotFoundException {
        // store the command line arguments
        double endTime = Double.parseDouble(args[0]);
        double timeIncrement = Double.parseDouble(args[1]);
        String filename = args[2];
        // Initialize the scanner to read the file in
        Scanner in = new Scanner(new File(filename));
        // Get the info from the file
        int numBodies = Integer.parseInt(in.nextLine().strip());
        double radius = Double.parseDouble(in.nextLine());
        // get the array of bodies from the file
        IBody[] bodies = readBodies(in, numBodies);
        setupDrawing(radius);
        // while the time is before the designated end time update the bodies
        while (endTime > 0) {
            drawStep(bodies);
            calculateStep(bodies, timeIncrement);
            StdDraw.pause(7);
            endTime -= timeIncrement;
        }
        printState(radius, bodies);
    }

    public static IBody[] readBodies(Scanner in, int numBodies) {
        // create array to hold bodies
        IBody[] bodies = new Body[numBodies];
        for (int i = 0; i < numBodies; i++) {
            // find the values to pass into the constructor
            IBody body = new Body(in.nextDouble(),
                    in.nextDouble(),
                    in.nextDouble(),
                    in.nextDouble(),
                    in.nextDouble(),
                    in.next());
            bodies[i] = body;
        }
        return bodies;
    }

    public static void calculateStep(IBody[] bodies, double dt) {
        // don't interleave steps or else calcs will be flawed
        for (IBody body : bodies) {
            body.calculateNewForceFrom(bodies);
        }
        for (IBody body : bodies) {
            body.updatePosition(dt);
        }
    }

    public static void setupDrawing(double radius) {
        StdDraw.setScale(-radius, radius);
        StdDraw.enableDoubleBuffering();
    }

    public static void drawStep(IBody[] bodies) {
        StdDraw.picture(0, 0, "data/images/starfield.jpg");
        for (IBody body : bodies) {
            Vector2D pos = body.getCurrentPosition();
            String bodyFile = "data/images/" + body.getFileName();
            StdDraw.picture(pos.getX(), pos.getY(), bodyFile);
        }
        StdDraw.show();
    }

    public static void printState(double radius, IBody[] bodies) {
        System.out.printf("%d\n", bodies.length);
        System.out.printf("%8.2e\n", radius);
        for (IBody body : bodies) {
            System.out.printf("%s\n", body.toString());
        }
    }
}