package com.ehub.service.impl;

import com.ehub.common.UserStatus;
import com.ehub.dto.request.AddressRequest;
import com.ehub.dto.request.UserCreationRequest;
import com.ehub.dto.request.UserPasswordRequest;
import com.ehub.dto.request.UserUpdateRequest;
import com.ehub.dto.response.UserPageResponse;
import com.ehub.dto.response.UserResponse;
import com.ehub.exception.InvalidDataException;
import com.ehub.exception.ResourceNotFoundException;
import com.ehub.model.AddressEntity;
import com.ehub.model.UserEntity;
import com.ehub.repository.AddressRepository;
import com.ehub.repository.UserRepository;
import com.ehub.service.EmailService;
import com.ehub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        log.info("findAll start");

        // Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // tenCot:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        // Xu ly truong hop FE muon bat dau voi page = 1
        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }

        // Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<UserEntity> entityPage;

        if (StringUtils.hasLength(keyword)) {
            keyword = "%" + keyword.toLowerCase() + "%";
            entityPage = userRepository.searchByKeyword(keyword, pageable);
        } else {
            entityPage = userRepository.findAll(pageable);
        }
        log.info("findAll end");

        return getUserPageResponse(page, size, entityPage);
    }

    @Override
    public UserResponse findById(Long id) {
        log.info("Find user by id: {}", id);
        UserEntity userEntity = getUserEntity(id);

        return UserResponse.builder()
                .id(id)
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .username(userEntity.getUsername())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserCreationRequest req) {
        log.info("Saving userEntity: {}", req);
        UserEntity userByEmail = userRepository.findByEmail(req.getEmail());
        if (userByEmail != null) {
            throw new InvalidDataException("Email already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(req.getFirstName());
        userEntity.setLastName(req.getLastName());
        userEntity.setGender(req.getGender());
        userEntity.setBirthday(req.getBirthday());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhone(req.getPhone());
        userEntity.setUsername(req.getUsername());
        userEntity.setPassword(passwordEncoder.encode(req.getPassword()));
        userEntity.setType(req.getType());
        userEntity.setStatus(req.getStatus());
        userRepository.save(userEntity);
        log.info("Saved userEntity: {}", userEntity);

        if (userEntity.getId() != null) {
            log.info("User id: {}", userEntity.getId());
            List<AddressEntity> addresses = new ArrayList<>();
            req.getAddresses().forEach(addressRequest -> {
                AddressEntity addressEntity = new AddressEntity();
                this.setAddressValues(addressEntity, addressRequest, userEntity);
                addresses.add(addressEntity);
            });
            addressRepository.saveAll(addresses);
            log.info("Saved addresses: {}", addresses);
        }

        // Send email verification: Fake data so no need to send verification email
//        try {
//            emailService.sendVerificationEmail(req.getEmail(), req.getUsername());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return userEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest req) {
        log.info("Updating userEntity: {}", req);

        UserEntity userEntity = getUserEntity(req.getId());
        userEntity.setFirstName(req.getFirstName());
        userEntity.setLastName(req.getLastName());
        userEntity.setGender(req.getGender());
        userEntity.setBirthday(req.getBirthday());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhone(req.getPhone());
        userEntity.setUsername(req.getUsername());

        userRepository.save(userEntity);
        log.info("Updated userEntity: {}", userEntity);

        // save address
        List<AddressEntity> addresses = new ArrayList<>();

        req.getAddresses().forEach(addressRequest -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(userEntity.getId(), addressRequest.getAddressType());
            if (addressEntity == null) {
                addressEntity = new AddressEntity();
            }
            this.setAddressValues(addressEntity, addressRequest, userEntity);
            addresses.add(addressEntity);
        });

        // save addresses
        addressRepository.saveAll(addresses);
        log.info("Updated addresses: {}", addresses);
    }

    private void setAddressValues(AddressEntity addressEntity, AddressRequest addressRequest, UserEntity userEntity) {
        addressEntity.setApartmentNumber(addressRequest.getApartmentNumber());
        addressEntity.setFloor(addressRequest.getFloor());
        addressEntity.setBuilding(addressRequest.getBuilding());
        addressEntity.setStreetNumber(addressRequest.getStreetNumber());
        addressEntity.setStreet(addressRequest.getStreet());
        addressEntity.setCity(addressRequest.getCity());
        addressEntity.setCountry(addressRequest.getCountry());
        addressEntity.setAddressType(addressRequest.getAddressType());
        addressEntity.setUserId(userEntity.getId());
    }

    @Override
    public void changePassword(UserPasswordRequest req) {
        log.info("Changing password for user: {}", req);

        UserEntity user = getUserEntity(req.getId());
        if (req.getPassword().equals(req.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        userRepository.save(user);
        log.info("Changed password for user: {}", user);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user: {}", id);

        UserEntity user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
        log.info("Deleted user id: {}", id);
    }

    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private static UserPageResponse getUserPageResponse(int page, int size, Page<UserEntity> userEntities) {
        log.info("Convert User Entity Page");

        List<UserResponse> userList = userEntities.stream()
                .map(entity -> UserResponse.builder()
                        .id(entity.getId())
                        .firstName(entity.getFirstName())
                        .lastName(entity.getLastName())
                        .gender(entity.getGender())
                        .birthday(entity.getBirthday())
                        .username(entity.getUsername())
                        .phone(entity.getPhone())
                        .email(entity.getEmail())
                        .build()
                ).toList();

        UserPageResponse response = new UserPageResponse();
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(userEntities.getTotalElements());
        response.setTotalPages(userEntities.getTotalPages());
        response.setUsers(userList);

        return response;
    }
}
