package org.example.mockx;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class ObjenesisProvider {

    private ObjenesisProvider() {}

    private static final class ObjenesisStdHolder {
        static final Objenesis objenesisStd = new ObjenesisStd();
    }

    public static Objenesis getObjenesisStd() {
        return ObjenesisStdHolder.objenesisStd;
    }

}
