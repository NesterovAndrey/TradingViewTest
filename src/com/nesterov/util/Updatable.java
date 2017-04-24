package com.nesterov.util;

import java.util.function.Consumer;

public interface Updatable<P> {
    void updateProgress(P args);
}
