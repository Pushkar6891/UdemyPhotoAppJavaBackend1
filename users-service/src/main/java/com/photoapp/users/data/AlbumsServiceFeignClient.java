package com.photoapp.users.data;
// Without Fallback Factory

/*
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photoapp.users.ui.model.AlbumResponseModel;

@FeignClient(name = "albums-service", fallback = AlbumsFallback.class)
public interface AlbumsServiceFeignClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

// Fallback Class If Albums Service is down
@Component
class AlbumsFallback implements AlbumsServiceFeignClient{

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		return new ArrayList<>();
	}
	
}
*/

// Using Fallback Factory
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photoapp.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "albums-service", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceFeignClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceFeignClient> {

	@Override
	public AlbumsServiceFeignClient create(Throwable cause) {
		return new AlbumsServiceClientFallback(cause);
	}

}

class AlbumsServiceClientFallback implements AlbumsServiceFeignClient {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Throwable cause;

	public AlbumsServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {

		if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			logger.error("404 error took place when getAlbums was called with userId: " + id + ". Error message: "
					+ cause.getLocalizedMessage());
		} else {
			logger.error("Other error took place: " + cause.getLocalizedMessage());
		}

		return new ArrayList<>();
	}

}