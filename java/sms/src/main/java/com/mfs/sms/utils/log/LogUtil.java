package com.mfs.sms.utils.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

public class LogUtil {
    private boolean enabled = false;
    private String location = "console";
    private PrintStream printStream;

    public LogUtil(boolean enabled,String location) {
        this.enabled = enabled;
        this.location = location;
    }

    private PrintStream getPrintStream() {
        if (printStream == null) {
            synchronized (this) {
                if (printStream == null) {
                    switch (location) {
                        case "console" : printStream = System.out;
                        break;
                        default:
                            try {
                                printStream = new PrintStream(new FileOutputStream(location));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
                return printStream;
            }
        }
        return printStream;
    }

    public void log(Object msg, Class clazz) {
        if (enabled) {
            String log = new Date().toLocaleString() + " " + "CUSTOM" + "    " + "mfs" + "   --- " + clazz.getName() + "   :   " + msg;
            getPrintStream().println(log);
        }
    }
}
