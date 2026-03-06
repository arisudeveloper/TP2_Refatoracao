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
        boolean testsPassed = executarFaseDeTestes(project);

        boolean deploySuccessful = false;
        if (testsPassed) {
            deploySuccessful = executarFaseDeDeploy(project);
        }

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

    private boolean executarFaseDeTestes(Project project) {
        if (project.hasTests()) {
            if ("success".equals(project.runTests())) {
                log.info("Tests passed");
                return true;
            } else {
                log.error("Tests failed");
                return false;
            }
        } else {
            log.info("No tests");
            return true;
        }
    }

    private boolean executarFaseDeDeploy(Project project) {
        if ("success".equals(project.deploy())) {
            log.info("Deployment successful");
            return true;
        } else {
            log.error("Deployment failed");
            return false;
        }
    }
}