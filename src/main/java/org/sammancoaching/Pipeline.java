package org.sammancoaching;

import org.sammancoaching.dependencies.*;

public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void run(Project project) {
        final boolean testsPassed = executarFaseDeTestes(project);

        boolean deploySuccessful = false;
        if (testsPassed) {
            deploySuccessful = executarFaseDeDeploy(project);
        }

        notificarResultadoFinal(testsPassed, deploySuccessful);
    }

    private boolean executarFaseDeTestes(Project project) {
        if (!project.hasTests()) {
            log.info("No tests");
            return true;
        }

        boolean isTestSuccess = "success".equals(project.runTests());

        if (isTestSuccess) {
            log.info("Tests passed");
            return true;
        }

        log.error("Tests failed");
        return false;
    }

    private boolean executarFaseDeDeploy(Project project) {
        boolean isDeploySuccess = "success".equals(project.deploy());

        if (isDeploySuccess) {
            log.info("Deployment successful");
            return true;
        }

        log.error("Deployment failed");
        return false;
    }

    private void notificarResultadoFinal(boolean testsPassed, boolean deploySuccessful) {
        if (config.sendEmailSummary()) {
            log.info("Sending email");
            if (!testsPassed) {
                emailer.send("Tests failed");
            } else if (!deploySuccessful) {
                emailer.send("Deployment failed");
            } else {
                emailer.send("Deployment completed successfully");
            }
        } else {
            log.info("Email disabled");
        }
    }
}