package com.akhrszk.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.akhrszk.entities.Item;
import com.akhrszk.entities.User;
import com.akhrszk.exceptions.FileTypeException;

@Service
public class ItemService extends MyService {
	
	@Value("${path.uploads.images}")
	private String uploadImagesPath;
	
	@Value("${path.documentroot}")
	private String documentroot;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void create(String comment, MultipartFile file, User user) throws IOException, FileTypeException {
		String imageUri = null;
		if(!file.isEmpty()) {
			FileUploader fileUploader = new FileUploader(file);
			if(fileUploader.isImage()) {
				fileUploader.upload(documentroot + uploadImagesPath);
				File uploadedFile = fileUploader.getUploadedFile();
				imageUri = uploadImagesPath + '/' + uploadedFile.getName();
			} else {
				throw new FileTypeException();
			}
		}
		Item item = new Item();
		item.setComment(comment);
		item.setCreatedAt(new Date());
		item.setUser(user);
		if(imageUri != null) {
			item.setImageUri(imageUri);
		}
		entityManager.persist(item);
		entityManager.flush();
	}

	public List<Item> findAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Item> query = builder.createQuery(Item.class);
		Root<Item> root = query.from(Item.class);
		query.select(root).orderBy(builder.desc(root.get("id")));
		List<Item> items = entityManager.createQuery(query).getResultList();
		return items;
	}
	
	public List<Item> findByUser(User user) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Item> query = builder.createQuery(Item.class);
		Root<Item> root = query.from(Item.class);
		query.select(root).where(builder.equal(root.get("user"), user));
		List<Item> items = entityManager.createQuery(query).getResultList();
		return items;
	}
}
