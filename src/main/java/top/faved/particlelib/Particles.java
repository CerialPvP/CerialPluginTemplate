package top.faved.particlelib;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import static top.faved.particlelib.LocationUtils.*;

public class Particles {
    /**
     * Draws a line from point A to point B.
     * @param particle The particle you want to use.
     * @param l1 The starting position of the particle.
     * @param l2 The finishing position of the particle.
     * @param div The divisor of the particle. For a consistent line, use 12.
     */
    public static void drawLine(ParticleBuilder particle, Location l1, Location l2, double div) {
        Vector vec = VectorUtils.createBetweenLocs(l1, l2);
        double length = l1.distance(l2);
        for (int i = 0; i < div * length; i++) {
            particle.clone().location(
                    VectorUtils.offsetLoc(l1, VectorUtils.setLength(vec, i / div))
            ).spawn();
        }
    }

    /**
     * Draws a cube.
     * @param particle The particle you want to use.
     * @param loc The location of the cube.
     * @param size The size of the cube.
     * @param div The divisor of the lines in the cube. For a consistent line, use 12.
     */
    public static void drawCube(ParticleBuilder particle, Location loc, double size, double div) {
        Thread t = new Thread(() -> {
            Location c1 = loc.clone();
            c1.setPitch(0);
            Location c2 = forward(c1, size);
            Location c3 = below(c1, size);
            Location c4 = below(c2, size);
            Location c5 = right(c1, size);
            Location c6 = right(c2, size);
            Location c7 = right(c3, size);
            Location c8 = right(c4, size);

            drawLine(particle, c1, c2, div);
            drawLine(particle, c1, c3, div);
            drawLine(particle, c2, c4, div);
            drawLine(particle, c3, c4, div);

            drawLine(particle, c3, c7, div);
            drawLine(particle, c4, c8, div);
            drawLine(particle, c7, c8, div);

            drawLine(particle, c5, c7, div);
            drawLine(particle, c6, c8, div);
            drawLine(particle, c5, c6, div);

            drawLine(particle, c1, c5, div);
            drawLine(particle, c2, c6, div);
        });

        t.setName(String.format("Cube Particle - %.2f, %.2f, %.2f | size=%.2f, div=%.2f",
                    loc.getX(), loc.getY(), loc.getZ(), size, div));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a sphere.
     * @param particle The particle you want to use.
     * @param loc The location of the sphere.
     * @param size The size of the sphere.
     * @param multi The particle multiplier. A value less than 0.5 will generate an incomplete sphere.
     */
    public static void drawSphere(ParticleBuilder particle, Location loc, double size, double multi) {
        Thread t = new Thread(() -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (int i = 0; i < (360 * size * multi); i++) {
                int randnumsphere = random.nextInt(1, 360);
                Vector vSphere = VectorUtils.createSpherical(size, loc.getYaw() - randnumsphere, i);
                particle.clone().location(VectorUtils.offsetLoc(loc, vSphere)).spawn();
            }
        });
        t.setName(String.format("Sphere Particle - %.2f, %.2f, %.2f | size=%.2f, multi=%.2f",
                loc.getX(), loc.getY(), loc.getZ(), size, multi));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a vertical circle.
     * @param particle The particle you want to use.
     * @param loc The location of the circle.
     * @param size The size of the circle.
     * @param multi The particle multiplier. A value less than 0.5 will generate an incomplete circle.
     * @param halfParticleAmount Whether the particle amount should be reduced by half.
     */
    public static void drawVerticalCircle(ParticleBuilder particle, Location loc, double size, double multi, boolean halfParticleAmount) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < (360 * size * multi); i++) {
                Utils.broadcast("<aqua>i={0}</aqua>", i);
                if (halfParticleAmount && i % 2 == 0) {
                    Utils.broadcast("<green>halfParticleAmount is true and i % 2 = 0.</green>");
                    continue;
                }

                Vector vSphere = VectorUtils.createSpherical(size, loc.getYaw()-90, i);
                Utils.broadcast("<yellow>vSphere={0}</yellow>", VectorUtils.vecString(vSphere));
                Location vloc = VectorUtils.offsetLoc(loc, vSphere);
                Utils.broadcast("<yellow>loc: {0} | vloc: {1}</yellow>", VectorUtils.locString(loc), VectorUtils.locString(vloc));
                particle.clone().location(vloc).spawn();
            }
        });
        t.setName(String.format("Vertical Circle Particle - %.2f, %.2f, %.2f | size=%.2f, multi=%.2f, halfParticleAmount=%s",
                loc.getX(), loc.getY(), loc.getZ(), size, multi, halfParticleAmount));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a 3D triangle (pyramid).
     * @param particle The particle you want to use.
     * @param loc The location of the pyramid.
     * @param size The size of the pyramid.
     */
    public static void drawPyramid(ParticleBuilder particle, Location loc, double size) {
        Thread t = new Thread(() -> {
            Location c1 = VectorUtils.offsetLoc(loc, new Vector(0, -size, 0));
            Location c2 = VectorUtils.offsetLoc(loc, new Vector(size, -size, 0));
            Location c3 = VectorUtils.offsetLoc(loc, new Vector(0, -size, size));
            Location c4 = VectorUtils.offsetLoc(loc, new Vector(size, -size, size));
            Location top = VectorUtils.offsetLoc(loc, new Vector(size/2, 0, size/2));

            drawLine(particle, c1, top, 12);
            drawLine(particle, c2, top, 12);
            drawLine(particle, c3, top, 12);
            drawLine(particle, c4, top, 12);

            drawLine(particle, c1, c2, 12);
            drawLine(particle, c2, c4, 12);
            drawLine(particle, c3, c1, 12);
            drawLine(particle, c4, c3, 12);
        });
        t.setName(String.format("Pyramid Particle - %.2f, %.2f, %.2f | size=%.2f",
                loc.getX(), loc.getY(), loc.getZ(), size));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a plus.
     * @param particle The particle you want to use.
     * @param loc The location of the plus.
     * @param size The size of the plus.
     */
    public static void drawPlus(ParticleBuilder particle, Location loc, double size) {
        Thread t = new Thread(() -> {
            Location top = VectorUtils.offsetLoc(loc, new Vector(0, size/2, 0));
            Location bottom = VectorUtils.offsetLoc(loc, new Vector(0, -(size/2), 0));
            Location left = left(loc, size/2);
            Location right = right(loc, size/2);

            drawLine(particle, top, bottom, 12);
            drawLine(particle, left, right, 12);
        });
        t.setName(String.format("Plus Particle - %.2f, %.2f, %.2f | size=%.2f",
                loc.getX(), loc.getY(), loc.getZ(), size));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws an emerald (diamond) shape.
     * @param particle The particle you want to use.
     * @param loc The location of the emerald.
     * @param size The size of the emerald.
     */
    public static void drawEmerald(ParticleBuilder particle, Location loc, double size) {
        Thread t = new Thread(() -> {
            Location c1 = VectorUtils.offsetLoc(loc, new Vector(0, -size, 0));
            Location c2 = VectorUtils.offsetLoc(loc, new Vector(size, -size, 0));
            Location c3 = VectorUtils.offsetLoc(loc, new Vector(0, -size, size));
            Location c4 = VectorUtils.offsetLoc(loc, new Vector(size, -size, size));
            Location top = VectorUtils.offsetLoc(loc, new Vector(size/2, 0, size/2));
            Location bottom = VectorUtils.offsetLoc(loc, new Vector(size/2, size * -2, size/2));

            drawLine(particle, c1, top, 12);
            drawLine(particle, c2, top, 12);
            drawLine(particle, c3, top, 12);
            drawLine(particle, c4, top, 12);

            drawLine(particle, c1, bottom, 12);
            drawLine(particle, c2, bottom, 12);
            drawLine(particle, c3, bottom, 12);
            drawLine(particle, c4, bottom, 12);

            drawLine(particle, c1, c2, 12);
            drawLine(particle, c2, c4, 12);
            drawLine(particle, c3, c1, 12);
            drawLine(particle, c4, c3, 12);
        });
        t.setName(String.format("Emerald Particle - %.2f, %.2f, %.2f | size=%.2f",
                loc.getX(), loc.getY(), loc.getZ(), size));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a flat (horizontal) circle.
     * @param particle The particle you want to use.
     * @param loc The location of the circle.
     * @param size The size of the circle.
     * @param multi The particle multiplier. A value less than 0.5 will generate an incomplete circle.
     * @param halfParticleAmount Whether the particle amount should be reduced by half.
     */
    public static void drawFlatCircle(ParticleBuilder particle, Location loc, double size, double multi, boolean halfParticleAmount) {
//        loop (360 * {_size} * {_a}) times:
//        if {_particleDivider} is set:
//        mod(loop-value, {_particleDivider}) != 0
//        continue
//                set {_randnumsphere} to 90 / this isn't used anywhere so didn't bother to add /yeah ik
//        set {_vSphere} to spherical vector with radius {_size}, yaw loop-value, pitch 0
//        if {_dustColor} is set:
//        drawParticle({_dustColor}, ({_loc} ~ {_vSphere}), {_dustSize})
//        else:
//        draw 1 witch at {_loc} ~ {_vSphere} with extra 0
// yeah bro theres literally nothing fucking wrong about it
// its the same fucking shit
        Thread t = new Thread(() -> {
            for (int i = 0; i < (360 * size * multi); i++) {
//                Utils.broadcast("<aqua>i={0}</aqua>", i);
                if (halfParticleAmount && i % 2 == 0) {
//                    Utils.broadcast("<green>halfParticleAmount is true and i % 2 = 0.</green>");
                    continue;
                }

                Vector vSphere = VectorUtils.createSpherical(size, i, 90);
//                Utils.broadcast("<yellow>vSphere={0}</yellow>", VectorUtils.vecString(vSphere));
                Location vloc = VectorUtils.offsetLoc(loc, vSphere);
//                Utils.broadcast("<yellow>loc: {0} | vloc: {1}</yellow>", VectorUtils.locString(loc), VectorUtils.locString(vloc));
                particle.clone().location(vloc).spawn();
            }
        });
        t.setName(String.format("Flat Circle Particle - %.2f, %.2f, %.2f | size=%.2f, multi=%.2f",
                loc.getX(), loc.getY(), loc.getZ(), size, multi));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a horizontal spiral.
     * @param particle The particle you want to use.
     * @param loc The location of the spiral.
     * @param size The size of the spiral circle.
     * @param distance The distance of the spiral.
     * @param spacing The spacing of the spiral. Use very small numbers (like 0.001)
     * @param wait Amount of ticks to wait per 60 particles drawn.
     */
    public static void drawSpiral(ParticleBuilder particle, Location loc, double size, double distance, double spacing, long wait) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < (360 * size * distance / 6); i++) {
                Vector vSphere = VectorUtils.createSpherical(size, loc.getYaw() - 90, i * 6);
                particle.clone().location(VectorUtils.offsetLoc(LocationUtils.forward(loc, spacing * i * 6), vSphere)).spawn();
                if (i % 60 == 0) {
                    try {
                        Thread.sleep(wait * 50L); // * 50 cuz wait is in ticks and thread.sleep wants millis
                    } catch (InterruptedException ignored) {}
                }
            }
        });
        t.setName(String.format("Plus Particle - %.2f, %.2f, %.2f | size=%.2f",
                loc.getX(), loc.getY(), loc.getZ(), size));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }

    /**
     * Draws a vertical spiral.
     * @param particle The particle you want to use.
     * @param loc The location of the spiral.
     * @param size The size of the spiral circle.
     * @param distance The distance of the spiral.
     * @param spacing The spacing of the spiral. Use very small numbers (like 0.001)
     * @param wait Amount of ticks to wait per 60 particles drawn.
     */
    public static void drawSpiralUp(ParticleBuilder particle, Location loc, double size, double distance, double spacing, long wait) {
        loc.setPitch(-90);
        Thread t = new Thread(() -> {
            for (int i = 0; i < (360 * size * distance); i++) {
                Vector vSphere = VectorUtils.createSpherical(size, i * 6, 0);
                particle.clone().location(VectorUtils.offsetLoc(LocationUtils.forward(loc, spacing * i * 6), vSphere)).spawn();
                if (i % 60 == 0) {
                    try {
                        Thread.sleep(wait * 50L); // * 50 cuz wait is in ticks and thread.sleep wants millis
                    } catch (InterruptedException ignored) {}
                }
            }
        });
        t.setName(String.format("Vertical Spiral Particle - %.2f, %.2f, %.2f | size=%.2f, distance=%.2f, spacing=%.2f, wait=%sms",
                    loc.getX(), loc.getY(), loc.getZ(), size, distance, spacing, wait));
        t.setUncaughtExceptionHandler(new ThreadErrorHandler());
        t.start();
    }
    // rebooting server btw
}
