package com.akhrszk.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.akhrszk.entities.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="items")
public class Item extends MyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@Setter
	@Getter
	private Integer id;
	
	@Column(name="comment", length=500)
	@Setter
	@Getter
	private String comment;
	
	@Column(name="image_uri")
	@Setter
	@Getter
	private String imageUri;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	@Setter
	@Getter
	private User user;
	
	@Column(name="created_at")
	@Temporal(TemporalType.TIMESTAMP)
	@Setter
	@Getter
	private Date createdAt;

	@Column(name="updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	@Setter
	@Getter
	private Date updatedAt;
	
	public boolean hasComment() {
		if(comment == null) {
			return false;
		}
		return !comment.isEmpty();
	}
	
	public boolean hasImage() {
		if(imageUri == null) {
			return false;
		}
		return !imageUri.isEmpty();
	}

	public String getFormattedCreatedAt() {
		return formatDate(this.createdAt);
	}
	
	public String getFormattedUpdatedAt() {
		return formatDate(this.updatedAt);
	}
}
