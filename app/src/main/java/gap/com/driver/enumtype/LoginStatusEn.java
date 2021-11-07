package gap.com.driver.enumtype;

/**
 * Created by root on 9/28/15.
 */
public enum LoginStatusEn {
    Init(0),PasswordCreation(1),Registered(2);

    private int code;

    LoginStatusEn(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
