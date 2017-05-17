package com.akhrszk.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.akhrszk.entities.User;
import com.akhrszk.exceptions.FileTypeException;

@Service
public class UserService extends MyService {
	
	@Value("${path.uploads.images}")
	private String uploadImagesPath;
	
	@Value("${path.documentroot}")
	private String documentroot;

	@PersistenceContext
	private EntityManager entityManager;
	
	private String encodePassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
	@Transactional
	public void create(String username, String password, String name) {
		String encodedPassword = encodePassword(password);
		User user = new User();
		user.setUsername(username);
		user.setPassword(encodedPassword);
		user.setName(name);
		user.setCreatedAt(new Date());
		entityManager.persist(user);
		entityManager.flush();
	}
	
	@Transactional
	public void updateProfile(int userId, String username, String name, MultipartFile icon) throws IOException, FileTypeException {
		String iconUri = null;
		if(!icon.isEmpty()) {
			FileUploader fileUploader = new FileUploader(icon);
			if(fileUploader.isImage()) {
				fileUploader.upload(documentroot + uploadImagesPath);
				fileUploader.cropSquare();
				File uploadedFile = fileUploader.getUploadedFile();
				iconUri = uploadImagesPath + '/' + uploadedFile.getName();
			} else {
				throw new FileTypeException();
			}
		}
		User user = findById(userId);
		user.setUsername(username);
		user.setName(name);
		user.setUpdatedAt(new Date());
		if(iconUri != null) {
			user.setIconUri(iconUri);
		}
		entityManager.persist(user);
		entityManager.flush();
	}
	
	@Transactional
	public void updatePassword(int userId, String password) {
		User user = findById(userId);
		user.setPassword(encodePassword(password));
		entityManager.persist(user);
		entityManager.flush();
	}
	
	public List<User> findAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		query.select(root).orderBy(builder.desc(root.get("id")));
		List<User> users = entityManager.createQuery(query).getResultList();
		return users;
	}
	
	public User findById(int id) throws NoResultException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		query.select(root).where(builder.equal(root.get("id"), id));
		return entityManager.createQuery(query).getSingleResult();
	}
	
	public User findByUsername(String username) throws NoResultException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		query.select(root).where(builder.equal(root.get("username"), username));
		return entityManager.createQuery(query).getSingleResult();
	}
	
	private boolean authenticate(User user, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (encoder.matches(password, user.getPassword())) {
			return true;
		} else {
			return false;
		}
	}
	
	public User authUserIdPassword(int userId, String password) {
		User user = null;
		try {
			user = this.findById(userId);
		} catch(NoResultException e) {
			return null;
		}
		if(this.authenticate(user, password)) {
			return user;
		} else {
			return null;
		}
	}
	
	public User authUsernamePassword(String username, String password) {
		User user = null;
		try {
			user = this.findByUsername(username);
		} catch(NoResultException e) {
			return null;
		}
		if(this.authenticate(user, password)) {
			return user;
		} else {
			return null;
		}
	}
	
	public boolean isExistsUsername(String username) {
		try {
			findByUsername(username);
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

}
