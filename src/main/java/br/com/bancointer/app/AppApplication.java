package br.com.bancointer.app;

import br.com.bancointer.app.config.CoreConfig;
import br.com.bancointer.app.model.Job;
import br.com.bancointer.app.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CoreConfig.class)
public class AppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Autowired
	JobService service;

	@Override
	public void run(String... args) throws Exception {
		// caseErro();
		// caseSucess();
	}

	public void caseErro(){
		Job job1 = Job.builder().name("Job1").active(true).build();
		this.service.save(job1);
		Job job2 = Job.builder().name("Job2").active(true).parentJob(job1).build();
		this.service.save(job2);
		Job job3 = Job.builder().name("Job3").active(true).parentJob(job1).build();
		this.service.save(job3);
		Job job2_ = Job.builder().name("Job2").active(true).parentJob(job2).build();
		this.service.save(job2_);
		Job job1__ = Job.builder().name("Job1").active(true).parentJob(job3).build();
		this.service.save(job1__);
	}

	public void caseSucess() {
		Job job1 = Job.builder().name("Job1").active(true).build();
		job1 = this.service.save(job1);
		Job job2 = Job.builder().name("Job2").active(true).parentJob(job1).build();
		job2 = this.service.save(job2);
		Job job3 = Job.builder().name("Job3").active(true).parentJob(job1).build();
		job3 = this.service.save(job3);
		Job job3_ = Job.builder().name("Job3").active(true).parentJob(job1).build();
	}
}
