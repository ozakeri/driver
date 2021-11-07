package gap.com.driver.util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Created by mahdi on 4/28/15.
 */
public class MyPasswordTransformationMethod extends PasswordTransformationMethod {

    private static MyPasswordTransformationMethod instance = null;

    private MyPasswordTransformationMethod()
    {

    }

    public static MyPasswordTransformationMethod getInstance()
    {
        if (instance == null)
            instance = new MyPasswordTransformationMethod();

        return instance;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*'; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}