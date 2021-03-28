package com.rincentral.test.services;

import com.rincentral.test.exceptions.CarException;
import com.rincentral.test.models.BodyCharacteristics;
import com.rincentral.test.models.CarFullInfo;
import com.rincentral.test.models.EngineCharacteristics;
import com.rincentral.test.models.external.ExternalBrand;
import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.models.external.ExternalCarInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.rincentral.test.exceptions.ExceptionData.NO_SUCH_BRAND;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class DataContainer {
    private final ExternalCarsApiService extCarService;

    @Getter
    private final Map<Integer, ExternalBrand> externalBrands = new HashMap<>();
    @Getter
    private final Map<Integer, CarFullInfo> cars = new HashMap<>();
    @Getter
    private final Map<Integer, BodyCharacteristics> bodies = new HashMap<>();
    @Getter
    private final Map<Integer, EngineCharacteristics> engines = new HashMap<>();
    @Getter
    private final Map<Integer, String> bodyStyles = new HashMap<>();
    @Getter
    private final Set<String> models = new HashSet<>();

    @PostConstruct
    private void fillData(){
        externalBrands.putAll(extCarService.loadAllBrands().stream().collect(Collectors.toMap(ExternalBrand::getId, brand -> brand)));
        cars.putAll(extCarService.loadAllCars().stream().collect(Collectors.toMap(ExternalCar::getId, this::getCarInfo)));
    }

    private CarFullInfo getCarInfo(ExternalCar car){
        ExternalBrand brand = Optional.ofNullable(externalBrands.get(car.getBrandId())).orElseThrow(() -> new CarException(format(NO_SUCH_BRAND.getMessageTemplate(), car.getBrandId()), NO_SUCH_BRAND));
        ExternalCarInfo info = extCarService.loadCarInformationById(car.getId());
        models.add(car.getModel());
        return CarFullInfo.builder()
                          .id(car.getId())
                          .segment(car.getSegment())
                          .brand(brand.getTitle())
                          .model(car.getModel())
                          .country(brand.getCountry())
                          .generation(car.getGeneration())
                          .modification(car.getModification())
                          .body(addBody(info))
                          .engine(addEngine(info))
                          .masSpeed(info.getMaxSpeed())
                          .acceleration(info.getAcceleration())
                          .gearboxType(info.getGearboxType())
                          .wheelDrive(info.getWheelDriveType())
                          .yearPresented(Integer.parseInt(info.getYearsRange().split("-")[0]))
                          .yearExpired(Integer.getInteger(info.getYearsRange().split("-")[1], null))
                          .build();
    }

    private int addBody(ExternalCarInfo info){
        List<String> styleNames = Arrays.asList(info.getBodyStyle().split("\\s*,\\s*"));
        bodyStyles.putAll(styleNames.stream().collect(Collectors.toMap(String::hashCode, name->name)));
        BodyCharacteristics body = BodyCharacteristics.builder()
                                                      .bodyStyles(styleNames.stream().map(String::hashCode).collect(Collectors.toList()))
                                                      .length(info.getBodyLength())
                                                      .height(info.getBodyHeight())
                                                      .width(info.getBodyWidth())
                                                      .build();
        int bodyId = body.hashCode();
        bodies.put(bodyId, body);
        return bodyId;
    }

    private int addEngine(ExternalCarInfo info){
        EngineCharacteristics engine = EngineCharacteristics.builder()
                                                            .displacement(info.getEngineDisplacement())
                                                            .engineType(info.getEngineType())
                                                            .fuel(info.getFuelType())
                                                            .hpw(info.getHp())
                                                            .build();
        int engineID = engine.hashCode();
        engines.put(engineID, engine);
        return engineID;
    }
}
