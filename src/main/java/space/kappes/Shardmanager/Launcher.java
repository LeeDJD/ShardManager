package space.kappes.Shardmanager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Launcher {

    public static void main(String[] args) {
        createDirs();
        initLogger();
        launch();
    }

    private static void launch() {
        new ShardManager();
    }

    private static void createDirs() {
        new File("log/").mkdirs();
    }

    private static void initLogger() {
        final ConsoleAppender consoleAppender = new ConsoleAppender();
        final PatternLayout patternLayout = new PatternLayout("(%d{HH:mm:ss}) [Shardmanager] [%p] | %m%n");
        consoleAppender.setLayout(patternLayout);
        consoleAppender.activateOptions();
        Logger.getRootLogger().addAppender(consoleAppender);

        final PatternLayout filePatternLayout = new PatternLayout("(%d{dd.MM.yyyy HH:mm:ss}) [Shardmanager] [%p] | %m%n\"");

        final FileAppender fileAppender = new FileAppender();
        fileAppender.setName("FileLog");
        fileAppender.setFile("latest.log");
        fileAppender.setLayout(filePatternLayout);
        fileAppender.activateOptions();
        Logger.getRootLogger().addAppender(fileAppender);

        final FileAppender dateFileLog = new FileAppender();
        dateFileLog.setName("FileLogger");
        dateFileLog.setFile(String.format("log/%s.log", new SimpleDateFormat("dd_MM_yyyy-HH_mm").format(new Date())));
        dateFileLog.setLayout(filePatternLayout);
        dateFileLog.activateOptions();
        Logger.getRootLogger().addAppender(dateFileLog);

    }

}
