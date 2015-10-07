package hr.betaware.fundfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hr.betaware.fundfinder.resource.FileMetadata;
import hr.betaware.fundfinder.service.FileService;

@RestController
@RequestMapping(value = { "/api/v1/file" })
public class FileController {

	@Autowired
	private FileService service;

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public FileMetadata upload(@RequestParam MultipartFile file) {
		return service.upload(file);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{name}")
	public FileMetadata getMetadata(@PathVariable String name) {
		return service.getMetadata(name);
	}

}
