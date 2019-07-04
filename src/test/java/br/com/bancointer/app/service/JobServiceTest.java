package br.com.bancointer.app.service;

import br.com.bancointer.app.exception.CustomException;
import br.com.bancointer.app.model.Job;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Elvis Fernandes on 27/06/19
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class JobServiceTest {

    public static final String ROOT_JOB = "root job";
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Autowired
    JobService underTest;
    private Job rootJob;

    @Before
    public void setup() {
        rootJob = Job.builder()
                .active(true)
                .name(ROOT_JOB)
                .build();
        underTest.save(rootJob);
    }

    @Test
    public void shouldNotCreateOrphanJobOtherThanRoot() {

        expectedEx.expect(CustomException.class);
        expectedEx.expectMessage("error.job.parentrequired");

        Job subJob = Job.builder()
                .name("Job 1")
                .active(true)
                .build();
        underTest.save(subJob);
    }

    @Test
    public void shouldNotCreateCycleReferencedJob() {

        expectedEx.expect(CustomException.class);
        expectedEx.expectMessage("error.job.cyclereference");

        Job subJob = Job.builder()
                .name("Job 1")
                .active(true)
                .build();
        subJob.setParentJob(subJob);
        underTest.save(subJob);
    }
}
