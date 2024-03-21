package cn.itscloudy.infocommit;

import java.util.ResourceBundle;

public interface IcConst {

    String ID = "InfoCommit";
    String NAME = ID;

    static String get(String key) {
        return IcResourceBundle.instance.get(key);
    }

    class IcResourceBundle {
        private static final IcResourceBundle instance = new IcResourceBundle();

        private final ResourceBundle bundle = ResourceBundle.getBundle("locales.ic");

        public String get(String key) {
            return bundle.getString(key);
        }
    }
}
