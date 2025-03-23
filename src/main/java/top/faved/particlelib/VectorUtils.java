package top.faved.particlelib;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

import static top.faved.particlelib.MathUtils.getCos;
import static top.faved.particlelib.MathUtils.getSin;

public class VectorUtils {
    private static final double DEG_TO_RAD = Math.PI / 180;

    /**
     * Creates a new {@link Vector} between 2 locations.
     * @param from The starting location.
     * @param to The finishing location.
     * @return A new vector between 2 locations.
     */
    public static Vector createBetweenLocs(Location from, Location to) {
        return new Vector(to.getX()-from.getX(), to.getY()-from.getY(), to.getZ()-from.getZ());
    }

    /**
     * Creates a new spherical vector.
     * @param radius The radius of the sphere.
     * @param yaw The theta value.
     * @param pitch The phi value.
     * @return A new spherical vector.
     */
    public static Vector createSpherical(double radius, double yaw, double pitch) {
        double r = Math.abs(radius);
        double t = yaw * DEG_TO_RAD;
        double p = pitch * DEG_TO_RAD;
        double sinp = Math.sin(p);
        double x = r * sinp * Math.cos(t);
        double y = r * Math.cos(p);
        double z = r * sinp * Math.sin(t);
//        double sinp = getSin(pitch);
//        double x = r * sinp * getCos(yaw);
//        double y = r * getCos(pitch);
//        double z = r * sinp * getSin(yaw);
        return new Vector(x, y, z);
    }

    /**
     * Sets the length of the vector.
     * @param vec The vector you want to modify.
     * @param length The new length of the vector.
     */
    public static Vector setLength(Vector vec, double length) {
        vec = vec.clone();
        if (!vec.isNormalized()) vec.normalize();
        vec.multiply(length);
        return vec;
    }

    public static Location offsetLoc(Location loc, Vector vec) {
        return loc.clone().add(vec);
    }

    private static final DecimalFormat f = new DecimalFormat("#.##");

    public static String locString(Location loc) {
        return Utils.stringFormat("Loc -> X={0}, Y={1}, Z={2} | PITCH={3}, YAW={4}",
                f.format(loc.getX()), f.format(loc.getY()), f.format(loc.getZ()), f.format(loc.getPitch()), f.format(loc.getYaw()));
    }

    public static String vecString(Vector vec) {
        return Utils.stringFormat("Vec -> X={0}, Y={1}, Z={2}",
                f.format(vec.getX()), f.format(vec.getY()), f.format(vec.getZ()));
    }
}
