/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.apiController;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.example.project.model.Activity;
import com.example.project.model.ActivityCandidate;
import com.example.project.service.ActivityService;
import com.example.project.utils.Rest;

@RestController
@RequestMapping("/api/")
public class ActivityController {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ActivityService activityService;

	@RequestMapping(path = "activity", method = RequestMethod.POST)
	public Rest publishActivity(@RequestBody Activity activity) {

		return activityService.publishActivity(activity);
	}

	@RequestMapping(path = "activity/{activityId}", method = RequestMethod.GET)
	public Rest publishActivity(@PathVariable Integer activityId) {

		return activityService.getActivityDetail(activityId);
	}

	@CrossOrigin("http://localhost:8082")
	@RequestMapping(path = "activityCandidate/{activityId}", method = RequestMethod.POST)
	public Rest registCandidate(@PathVariable Integer activityId, @RequestPart(required = false) MultipartFile file,
			@RequestParam String json) throws Exception {

		ActivityCandidate info = JSON.parseObject(json, ActivityCandidate.class);
		if (file !=null && !file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String newFileName = UUID.randomUUID().toString().replaceAll("-", "")
					+ originalFilename.substring(originalFilename.lastIndexOf("."));

			File dest = new File("/Users/alexwang/upload/" + newFileName);
			dest.getParentFile().mkdirs();
			file.transferTo(dest);
			System.out.println(dest.getAbsolutePath());
			info.setPic(newFileName);
		}

		return activityService.registCandidate(activityId, info);
	}

	@RequestMapping(path = "activityUnRegCandidate/{activityId}", method = RequestMethod.POST)
	public Rest unRegistCandidate(@PathVariable Integer activityId) {

		return activityService.unRegistCandidate(activityId);
	}

	@RequestMapping(path = "activity/{activityId}/voteCandidate", method = RequestMethod.POST)
	public Rest voteCandidate(@PathVariable Integer activityId, @RequestBody List<ActivityCandidate> cans) throws Exception {

		return activityService.voteCandidate(activityId, cans);
	}

	@RequestMapping(value = "pic/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity showPhotos(@PathVariable String fileName) {

		try {
			return ResponseEntity

					.ok(resourceLoader.getResource("file:/Users/alexwang/upload/" + fileName.replaceAll("[.]+", ".")))

			;
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

}
