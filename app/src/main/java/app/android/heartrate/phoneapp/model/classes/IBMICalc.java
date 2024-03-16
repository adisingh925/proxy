package app.android.heartrate.phoneapp.model.classes;

public interface IBMICalc {
    float countBMI(float f, float f2);

    boolean isValidHeight(float f);

    boolean isValidMass(float f);
}
