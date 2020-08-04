package com.photoapp.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.photoapp.users.data.AlbumsServiceFeignClient;
import com.photoapp.users.data.UserEntity;
import com.photoapp.users.repository.UserRepository;
import com.photoapp.users.shared.UserDto;
import com.photoapp.users.ui.model.AlbumResponseModel;

import feign.FeignException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private RestTemplate restTemplate;

	@Autowired
	private Environment environment;

	@Autowired
	private AlbumsServiceFeignClient albumsServiceFeignClient;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {

		// set unique Id
		userDetails.setUserId(UUID.randomUUID().toString());

		// encrypt Password
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

		// Map fields from UserDto to UserEntity
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		// set password manually
		// userEntity.setEncryptedPassword("test");

		userRepository.save(userEntity);

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException(username);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException(userId);

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//  	Using RestTemplate
//
//		String albumsUrlOriginal = "http://albums-service/users/987654321/albums";
//		String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
//
//		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET,
//				null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//				});
//		List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
//
//		userDto.setAlbums(albumsList);
//
//		return userDto;

//		1. Using Feign try Catch
//		logger.info("Before calling albums Microservice");
//		List<AlbumResponseModel> albumsList = null;
//		try {
//			albumsList = albumsServiceFeignClient.getAlbums(userId);
//		} catch (FeignException e) {
//			logger.error(e.getLocalizedMessage());
//		}
//		logger.info("After calling albums Microservice");
//		userDto.setAlbums(albumsList);
//
//		return userDto;

//		2. Using Feign Error Decoder
		logger.info("Before calling albums Microservice");
		List<AlbumResponseModel> albumsList = albumsServiceFeignClient.getAlbums(userId);
		logger.info("After calling albums Microservice");
		userDto.setAlbums(albumsList);

		return userDto;
	}

}
