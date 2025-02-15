package frc.robot.utils;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.TimeUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.AngularVelocityUnit;

/**
 * Data class holding custom-defined units for the WPILib units library, to make working with
 * Phoenix v5 motor controllers easier.
 */
public class TalonUnits {

    /**
     * Base Talon unit of angle. 2048 encoder ticks in a rotation.
     */
    public static final AngleUnit EncoderTicks = Units.derive(Units.Rotations)
                                                      .splitInto(2048) //TODO this might be 4096. or 1024. idk
                                                      .named("SRX Encoder Ticks")
                                                      .symbol("tcs")
                                                      .make();
    public static final AngleUnit EncoderTick = EncoderTicks; //"singularized alias"
    
    /**
     * 100ms, one-tenth of a Second.
     */
    public static final TimeUnit Deciseconds = Units.derive(Units.Seconds)
                                                    .splitInto(10)
                                                    .named("Deciseconds")
                                                    .symbol("ds")
                                                    .make();
    public static final TimeUnit Decisecond = Deciseconds;

    /**
     * Base Talon unit of angular velocity. Encoder ticks per 100ms.
     */
    public static final AngularVelocityUnit EncoderTicksPerDecisecond = EncoderTicks.per(Decisecond);
}
