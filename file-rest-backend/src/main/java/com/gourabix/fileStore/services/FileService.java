package com.gourabix.fileStore.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	void initStorageDirectory() throws IOException;

	boolean uploadFile(MultipartFile file) throws IOException, RuntimeException;

	Resource loadFile(String fileName) throws MalformedURLException, RuntimeException;

}
