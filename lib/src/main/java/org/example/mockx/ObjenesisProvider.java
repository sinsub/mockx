package org.example.mockx;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

public class ObjenesisProvider {
    private static Objenesis objenesisStd;

    // TODO make this a proper singleton with Thread safety
    public static Objenesis getObjenesisStd() {
        if (objenesisStd == null) {
            objenesisStd = new ObjenesisStd();
        }
        return objenesisStd;
    }

    // Prevent object construction
    private ObjenesisProvider() {
    }
}
