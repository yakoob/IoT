import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import grails.util.BuildSettings
import grails.util.Environment

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "[%level] %d{HH:mm:ss.SSS} [%thread] %logger - %msg%n"
    }
}

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "[%level] %d{HH:mm:ss.SSS} [%thread] %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    root(ERROR, ['STDOUT', 'FULL_STACKTRACE'])

    logger("com.yakoobahmad", INFO)
    logger("com.yakoobahmad", DEBUG)

}
else {
    root(ERROR, ['STDOUT'])
}






