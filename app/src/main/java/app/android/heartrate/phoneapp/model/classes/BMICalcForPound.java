package app.android.heartrate.phoneapp.model.classes;

public class BMICalcForPound implements IBMICalc {
    public static float maxHeight = 98.42f;
    public static float maxMass = 440.0f;
    public static float minHeight = 19.68f;
    public static float minMass = 22.0f;

    @Override
    public float countBMI(float f, float f2) {
        if (isValidMass(f) && isValidHeight(f2)) {
            return (f / (f2 * f2)) * 703.0f;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean isValidMass(float f) {
        return minMass <= f && f <= maxMass;
    }

    @Override
    public boolean isValidHeight(float f) {
        return minHeight <= f && f <= maxHeight;
    }
}
