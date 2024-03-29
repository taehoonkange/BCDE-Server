package com.bcdeproject;

import com.bcdeproject.global.profile.controller.ProfileController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.io.File;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class BcdeprojectApplication {

	@Value("${spring.environment}")
	private String environment;

	@Value("${spring.file-dir}")
	private String fileDir;

	public static final String APPLICATION_LOCATIONS =
			"spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(BcdeprojectApplication.class)
				.properties(APPLICATION_LOCATIONS)
						.run(args);
		log.info("배포 Test 6");
	}

	/**
	 * @description 이미지, 영상 업로드할 폴더를 프로파일 별로 다른 경로에 생성한다.
	 */
	@PostConstruct
	private void init() {

		if (environment.equals("local")) {
			String staticFolder = System.getProperty("user.dir") + "/src/main/resources/static";
			mkdirResource(staticFolder);

			String files = System.getProperty("user.dir") + fileDir;
			mkdirResource(files);
		} else if (environment.equals("development")) {
			String filesFolder = "/home/ec2-user/files";
			mkdirResource(filesFolder);
		}
	}

	/**
	 * @param fileDir 생성을 위한 폴더명
	 * @description 주어진 경로에 폴더를 생성함
	 */
	private static void mkdirResource(String fileDir) {
		File Folder = new File(fileDir);

		// 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
		if (!Folder.exists()) {
			try {
				Folder.mkdir(); //폴더 생성합니다.
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}

}
