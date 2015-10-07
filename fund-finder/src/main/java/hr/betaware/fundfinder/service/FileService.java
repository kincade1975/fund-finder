package hr.betaware.fundfinder.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hr.betaware.fundfinder.resource.FileMetadata;

@Service
public class FileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

	@Value(value = "${file.repository}")
	private String fileRepository;

	public FileMetadata upload(MultipartFile multipartFile) {
		File file = null;
		BufferedOutputStream stream = null;

		try {
			File repository = new File(fileRepository);
			if (!repository.exists()) {
				repository.mkdirs();
			}

			String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			file = new File(repository, fileName.toLowerCase());

			byte[] bytes = multipartFile.getBytes();
			stream = new BufferedOutputStream(new FileOutputStream(file));
			stream.write(bytes);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		return new FileMetadata(file.getName(), encode(file));
	}

	public FileMetadata getMetadata(String name) {
		FileMetadata result = new FileMetadata(name);
		File repository = new File(fileRepository);
		for (File file : repository.listFiles()) {
			if (file.getName().equals(name)) {
				result.setBase64(encode(file));
				break;
			}
		}
		return result;
	}

	private String encode(File file) {
		try {
			return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

}
