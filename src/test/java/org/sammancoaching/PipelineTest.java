package org.sammancoaching;

import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.*;
import static org.mockito.Mockito.*;

class PipelineTest {

    @Test
    void sucessoNoDeploy() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);
        Logger log = mock(Logger.class);

        Project projeto = Project.builder()
                .setTestStatus(TestStatus.PASSING_TESTS)
                .setDeploysSuccessfully(true)
                .build();

        when(config.sendEmailSummary()).thenReturn(true);

        Pipeline pipeline = new Pipeline(config, emailer, log);
        pipeline.run(projeto);

        verify(emailer).send("Deployment completed successfully");
    }

    @Test
    void erroNosTestes() {
        Config config = mock(Config.class);
        Emailer emailer = mock(Emailer.class);

        Project projeto = Project.builder()
                .setTestStatus(TestStatus.FAILING_TESTS)
                .build();

        when(config.sendEmailSummary()).thenReturn(true);

        Pipeline pipeline = new Pipeline(config, emailer, mock(Logger.class));
        pipeline.run(projeto);

        verify(emailer).send("Tests failed");
        verify(emailer, never()).send("Deployment completed successfully");
    }
}