package com.gourabix.fileStore.controllers;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gourabix.fileStore.services.FileService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/files")
public class FileController {

	private static final Logger LOGGER = LogManager.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<Void> uploadFile(@RequestParam("file") MultipartFile file) {
		LOGGER.info("Starting file upload for file: " + file.getOriginalFilename());

		try {
			fileService.initStorageDirectory();
			fileService.uploadFile(file);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (IOException | RuntimeException exception) {
			LOGGER.error(exception.getMessage());
			LOGGER.error("Something went wrong while uploading file: " + file.getOriginalFilename());
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
		LOGGER.info("Starting file download: " + fileName + "...");
		Resource resourceFile = null;

		try {
			fileService.initStorageDirectory();
			resourceFile = fileService.loadFile(fileName);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.body(resourceFile);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
			LOGGER.error("Something went wrong while retrieving filename: " + fileName);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

}
