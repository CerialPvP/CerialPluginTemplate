package top.faved.particlelib;

import org.bukkit.Location;

import java.text.DecimalFormat;

import static top.faved.particlelib.MathUtils.getCos;
import static top.faved.particlelib.MathUtils.getSin;

public class LocationUtils {
    public static Location forward(Location loc, double amount) {
        return loc.clone().add(loc.getDirection().multiply(amount));
    }

    public static Location backward(Location loc, double amount) {
        return forward(loc, -amount);
    }

    private static double roundDecimal(double number, int decimals) {
        return Double.parseDouble(new DecimalFormat("#."+"#".repeat(decimals)).format(number));
    }

    public static Location right(Location loc, double amount) {
        float angle = loc.getYaw();
        if (angle < 0)
            angle = 180 + (180 - Math.abs(angle));

        angle = (float) roundDecimal(angle, MathUtils.accuracy);

        double cosValue = getCos(angle);
        double sinValue = getSin(angle);

        return loc.clone().add(cosValue * amount, 0, sinValue * amount);
    }

    public static Location left(Location loc, double amount) {
        return right(loc, -amount);
    }

    public static Location below(Location loc, double amount) {
        Location location = loc.clone();
        location.setY(location.getY() - amount);
        return location;
    }

    public static Location above(Location loc, double amount) {
        Location location = loc.clone();
        location.setY(location.getY() + amount);
        return location;
    }
}
