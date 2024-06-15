package com.example.paybizsdk.Logger;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class FileLogger {
    private static final String TAG = "FileLogger";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final String LOG_FILE_NAME = "app_log-" + dateFormat.format(new Date()) + ".txt";
    private static File logFile;
    public static void initialize(Context context) {
        File logDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (logDir != null) {
            logFile = new File(logDir, LOG_FILE_NAME);
        }
    }

    public static void log(String level, String tag, String message) {
        String logMessage = formatLogMessage(level, tag, message);
        writeToFile(logMessage);
        Log.println(getLogLevel(level), tag, message);
    }

    private static String formatLogMessage(String level, String tag, String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        return String.format("%s %s/%s: %s\n", timestamp, level, tag, message);
    }

    private static int getLogLevel(String level) {
        switch (level) {
            case "VERBOSE":
                return Log.VERBOSE;
            case "DEBUG":
                return Log.DEBUG;
            case "INFO":
                return Log.INFO;
            case "WARN":
                return Log.WARN;
            case "ERROR":
                return Log.ERROR;
            default:
                return Log.ASSERT;
        }
    }

    private static void writeToFile(String logMessage) {
        if (logFile != null) {
            try (FileOutputStream fos = new FileOutputStream(logFile, true);
                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                osw.write(logMessage);
            } catch (IOException e) {
                Log.e(TAG, "Error writing log to file", e);
            }
        } else {
            Log.e(TAG, "Log file not initialized");
        }
    }

}
