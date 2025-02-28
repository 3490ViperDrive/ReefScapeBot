package frc.robot.utils;

import java.util.function.DoubleSupplier;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * Filters all three controller axes necessary for driving. Avoids redundant calculations for filtering x and y
 * and avoids allocating a bunch of short-lived double arrays or Translation2ds.
 */
public class GamepadFilter {
    //If REFRESH_THRESHOLD seconds have elapsed between calls to the getters,
    //the filtered axis values will be re-computed.
    public static final double REFRESH_THRESHOLD = 0.008;

    private final DoubleSupplier xSup;
    private final DoubleSupplier ySup;
    private final DoubleSupplier thetaSup;
    private final double deadband;

    private double lastFilteredX;
    private double lastFilteredY;
    private double lastFilteredTheta;
    private double lastRefreshedTimestamp;

    /**
     * Creates a new GamepadFilter.
     * @param xSup the controller axis for the forwards/backwards axis of the robot. Positive is forwards.
     * @param ySup the controller axis for the left/right axis of the robot. Positive is left.
     * @param thetaSup the controller axis for the robot's rotation. Positive is counterclockwise.
     * @param deadband the deadband to use for deadband calculations. [0, 1]
     */
    public GamepadFilter(DoubleSupplier xSup, DoubleSupplier ySup, DoubleSupplier thetaSup, double deadband) {
        this.xSup = xSup;
        this.ySup = ySup;
        this.thetaSup = thetaSup;
        this.deadband = deadband;
        lastRefreshedTimestamp = -1; //force a refresh
        refreshAxes();
    }

    /**
     * Creates a new GamepadFilter, pulling the appropriate axes from the given gamepad.
     * @param gamepad the xbox controller to get axes from.
     */
    public GamepadFilter(XboxController gamepad, double deadband) {
        this(() -> -gamepad.getLeftY(),
             () -> -gamepad.getLeftX(),
             () -> -gamepad.getRightX(),
             deadband);
    }

    /**
     * Creates a new GamepadFilter, pulling the appropriate axes from the given gamepad.
     * @param gamepad the xbox controller to get axes from.
     */
    public GamepadFilter(CommandXboxController gamepad, double deadband) {
        this(() -> -gamepad.getLeftY(),
             () -> -gamepad.getLeftX(),
             () -> -gamepad.getRightX(),
             deadband);
    }

    @Logged
    public double getX() {
        refreshAxes();
        return xSup.getAsDouble();
    }

    @Logged
    public double getY() {
        refreshAxes();
        return ySup.getAsDouble();
    }

    @Logged
    public double getTheta() {
        refreshAxes();
        return thetaSup.getAsDouble();
    }

    @Logged
    public double getTimeSinceLastRefreshed() {
        return Timer.getFPGATimestamp() - lastRefreshedTimestamp;
    }

    private void refreshAxes() {
        double currentTimestamp = Timer.getFPGATimestamp();
        if (currentTimestamp - lastRefreshedTimestamp > REFRESH_THRESHOLD) {
            double x = xSup.getAsDouble();
            double y = ySup.getAsDouble();
            double theta = thetaSup.getAsDouble();
            double translationMagnitude = InputFilteringUtil.squareInput(
                                            InputFilteringUtil.applyDeadbandSpecial(
                                                MathUtil.clamp(Math.hypot(x, y), 0, 1),
                                                deadband));
            double translationAngle = Math.atan2(y, x);
            lastFilteredX = translationMagnitude * Math.cos(translationAngle);
            lastFilteredY = translationMagnitude * Math.sin(translationAngle);
            lastFilteredTheta = InputFilteringUtil.squareInput(
                                    InputFilteringUtil.applyDeadbandSpecial(theta, deadband));
            lastRefreshedTimestamp = currentTimestamp;
        }
    }
}
