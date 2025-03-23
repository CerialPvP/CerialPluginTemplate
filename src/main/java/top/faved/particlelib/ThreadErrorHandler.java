package top.faved.particlelib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadErrorHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("ParticleLib");

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error("Caught error in thread {}:", t.getName(), e);
    }
}
