package ru.fondorg.mytracksrv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fondorg.mytracksrv.repo.ProjectRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MytrackSrvApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MytrackSrvApplication.class, args);
	}


	@Autowired
	public ProjectRepository projectRepository;

	@Override
	public void run(String... args) throws Exception {
		/*Project project = new Project();
		project.setTitle("Test project");
		project.setDescription("test description");
		project = projectRepository.save(project);*/
	}
}
