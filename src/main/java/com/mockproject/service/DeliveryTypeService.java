package com.mockproject.service;

import com.mockproject.dto.DeliveryTypeDTO;
import com.mockproject.entity.DeliveryType;
import com.mockproject.entity.Unit;
import com.mockproject.entity.UnitDetail;
import com.mockproject.mapper.DeliveryTypeMapper;
import com.mockproject.repository.DeliveryTypeRepository;
import com.mockproject.repository.UnitDetailRepository;
import com.mockproject.service.interfaces.IDeliveryTypeService;
import com.mockproject.service.interfaces.IUnitService;
import com.mockproject.utils.ListUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryTypeService implements IDeliveryTypeService {

    private final UnitDetailRepository unitDetailRepository;
    private final IUnitService service;
    private final DeliveryTypeRepository deliveryTypeRepository;

    @Override
    public List<DeliveryTypeDTO> getDeliveryTypes(boolean status) {
        Optional<List<DeliveryType>> deliveryTypeList = deliveryTypeRepository.findByStatus(status);
        ListUtils.checkList(deliveryTypeList);
        List<DeliveryTypeDTO> deliveryTypeDTOList = new ArrayList<>();
        for (DeliveryType d: deliveryTypeList.get()){
            deliveryTypeDTOList.add(DeliveryTypeMapper.INSTANCE.toDTO(d));
        }
        return deliveryTypeDTOList;
    }

    @Override
    public DeliveryTypeDTO getDeliveryType(Long deliveryId, boolean status){
        Optional<DeliveryType> deliveryType = deliveryTypeRepository.findByIdAndStatus(deliveryId,status);
        deliveryType.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Delivery type not found"));
        DeliveryTypeDTO deliveryTypeDTO = DeliveryTypeMapper.INSTANCE.toDTO(deliveryType.get());
        return deliveryTypeDTO;
    }

    @Override
    public DeliveryTypeDTO getByIdTrue(Long id) {
        UnitDetail unitDetail = new UnitDetail();
        unitDetail.setId(id);
        return DeliveryTypeMapper.INSTANCE.toDTO(deliveryTypeRepository.findByIdAndStatus(id,true).orElseThrow());
    }

    @Override
    public List<DeliveryTypeDTO> getAllDeliveryTypesByTrainingClassId(Long id) {
        List<Unit> units = service.getListUnitsByTrainingClassId(id);
        List<UnitDetail> list = unitDetailRepository.findByUnitInAndStatus(units, true).orElseThrow();
        List<DeliveryType> deliveryTypes = list.stream().map(p-> deliveryTypeRepository.findByIdAndStatus(p.getDeliveryType().getId(), true).orElseThrow()).distinct().toList();
        return deliveryTypes.stream().map(DeliveryTypeMapper.INSTANCE::toDTO).toList();
    }
}
