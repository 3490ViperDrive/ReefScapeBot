package frc.robot.utils;

import edu.wpi.first.math.MathUtil;

/**
 * Utility class for certain controller input filtering methods.
 */
public class InputFilteringUtil {
    private InputFilteringUtil() {}

    public static double applyDeadbandSpecial(double value, double deadband) {
        return MathUtil.inverseInterpolate(deadband, 1, MathUtil.applyDeadband(Math.abs(value), deadband)) * Math.signum(value);
    }

    public static double squareInput(double value) {
        return Math.pow(Math.abs(value), 2) * Math.signum(value);
    }

    public static double applyMultiplier(double value, double multiplier) {
        return 1 - (value * multiplier);
    }

    public static double triggerToAxis(double leftTrigger, double rightTrigger) {
        return rightTrigger - leftTrigger;
    }

    //"random magic code that makes the joystick actually circular" - ohyes10fps
    //newer controllers may not need this
    public static double solveJoystickDiagonalDelta(double x, double y) {
        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double diffPercent = 1.0 - (Math.abs(absX - absY) / Math.max(absX, absY));
        return Math.max(Math.hypot(x, y) - (0.12 * diffPercent), 0);
    }
}
