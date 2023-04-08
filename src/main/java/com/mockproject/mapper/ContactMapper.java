package com.mockproject.mapper;

import com.mockproject.dto.ContactDTO;
import com.mockproject.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDTO toDTO(Contact contact);

    Contact toEntity(ContactDTO dto);
}
