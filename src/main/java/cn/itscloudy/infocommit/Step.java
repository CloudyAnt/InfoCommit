package cn.itscloudy.infocommit;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Step extends Seg {

    private final String key;

    protected Step(String key) {
        this.key = key;
    }

    public static Step resolve(String key, String name, String[] options, String defaultValue) {
        if (options == null || options.length == 1) {
            return new InputStep(key, name, defaultValue);
        } else {
            return new RadioStep(key, name, options, defaultValue);
        }
    }
    @NotNull
    abstract FormStep getFormStep();
}
