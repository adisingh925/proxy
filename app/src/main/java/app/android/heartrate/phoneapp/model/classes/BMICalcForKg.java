package app.android.heartrate.phoneapp.model.classes;

public class BMICalcForKg implements IBMICalc {
    public static float maxHeight = 2.5f;
    public static float maxMass = 200.0f;
    public static float minHeight = 0.5f;
    public static float minMass = 10.0f;

    @Override
    public float countBMI(float f, float f2) {
        if (isValidMass(f) && isValidHeight(f2)) {
            return f / (f2 * f2);
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
