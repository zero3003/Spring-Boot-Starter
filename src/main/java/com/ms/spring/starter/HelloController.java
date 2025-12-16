package com.ms.spring.starter;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ms.spring.starter.service.FileStorageService;

@RestController
public class HelloController {

	private final FileStorageService fileStorageService;

	public HelloController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	@GetMapping("/")
	public String index() {
		return "Wellcome !!";
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(
			@PathVariable("filename") String filename) throws IOException {

		Resource resource = fileStorageService.loadAsResource(filename);

		String contentType = Files.probeContentType(resource.getFile().toPath());

		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"inline; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
