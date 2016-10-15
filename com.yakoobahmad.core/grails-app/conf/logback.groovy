import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "[%level] %d{HH:mm:ss.SSS} [%thread] %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])
logger("com.yakoobahmad", INFO)
logger("com.yakoobahmad", DEBUG)