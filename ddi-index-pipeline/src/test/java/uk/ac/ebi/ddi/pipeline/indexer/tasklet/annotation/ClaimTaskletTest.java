package uk.ac.ebi.ddi.pipeline.indexer.tasklet.annotation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;
/**
 * Created by gaur on 08/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jobs/claimdataset/ddi-indexer-test-claimdataset.xml"})
public class ClaimTaskletTest {

    public static final String INDEXER_PARAMETER = "inderxer.param";
    public static final String TEST_MODE = "test.mode";


    @Autowired
    private JobLauncher jobLauncher;


    @Autowired
    @Qualifier("ddiClaimDatasetJob")
    private Job job;


    private JobParameters jobParameters;

    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepository jobRepository;

    @Before
    public void setUp() throws Exception {
        initJobParameters();
    }


    private void initJobParameters() {
        this.jobParameters =  new JobParametersBuilder().addString(INDEXER_PARAMETER,INDEXER_PARAMETER)
                .addString(TEST_MODE, "true")
                .toJobParameters();
        this.jobLauncherTestUtils = new JobLauncherTestUtils();
        this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
        this.jobLauncherTestUtils.setJobRepository(jobRepository);
        this.jobLauncherTestUtils.setJob(job);
    }

    @Test
    public void testLaunchJobWithJobLauncher() throws Exception {

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

}