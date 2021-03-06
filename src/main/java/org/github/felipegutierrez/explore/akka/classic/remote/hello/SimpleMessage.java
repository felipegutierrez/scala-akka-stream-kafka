package org.github.felipegutierrez.explore.akka.classic.remote.hello;

import java.io.Serializable;

public class SimpleMessage implements Serializable {
    public final String msg;

    public SimpleMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
