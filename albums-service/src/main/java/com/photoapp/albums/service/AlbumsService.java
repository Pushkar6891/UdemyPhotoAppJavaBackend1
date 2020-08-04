package com.photoapp.albums.service;

import java.util.List;

import com.photoapp.albums.entities.AlbumEntity;

public interface AlbumsService {
	List<AlbumEntity> getAlbums(String userId);
}