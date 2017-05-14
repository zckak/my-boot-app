package com.akhrszk.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
public class User extends MyEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@Setter
	@Getter
	private Integer id;
	
	@Column(name="username", unique=true, nullable=false, length=15)
	@Setter
	@Getter
	private String username;
	
	@Column(name="password", nullable=false)
	@Setter
	@Getter
	private String password;
	
	@Column(name="name", nullable=false)
	@Setter
	@Getter
	private String name;
	
	@Column(name="icon_uri", nullable=true)
	@Setter
	@Getter
	private String iconUri;
	
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
	
	public boolean hasIcon() {
		if (iconUri == null) {
			return false;
		}
		return !iconUri.isEmpty();
	}
	
	public String getFormattedCreatedAt() {
		return formatDate(this.createdAt);
	}
	
	public String getFormattedUpdatedAt() {
		return formatDate(this.updatedAt);
	}
}
