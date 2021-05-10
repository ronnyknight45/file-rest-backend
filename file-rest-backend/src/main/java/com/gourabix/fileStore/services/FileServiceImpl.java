package com.gourabix.fileStore.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger LOGGER = LogManager.getLogger(FileServiceImpl.class);
	private final Path ROOT_FILE_PATH = Paths.get("fileUploads");

	@Override
	public boolean uploadFile(MultipartFile file) throws IOException, RuntimeException {
		LOGGER.info("Adding file to storage...");
		Files.copy(file.getInputStream(), this.ROOT_FILE_PATH.resolve(file.getOriginalFilename()),
				StandardCopyOption.REPLACE_EXISTING);
		LOGGER.info("File uploaded successfully!");
		return true;
	}

	@Override
	public void initStorageDirectory() throws IOException {
		LOGGER.info("Initializing storage directory: /fileUploads...");
		if (!Files.isDirectory(ROOT_FILE_PATH))
			Files.createDirectory(ROOT_FILE_PATH);
	}
	
	@Override
	public Resource loadFile(String fileName) throws MalformedURLException, RuntimeException {
		Path file = ROOT_FILE_PATH.resolve(fileName);
		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() || resource.isReadable())
			return resource;
		else
			throw new RuntimeException("Could not read filename: " + fileName + " !");
	}


}
