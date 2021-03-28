package com.rincentral.test.services;

import com.rincentral.test.exceptions.CarException;
import com.rincentral.test.models.*;
import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import com.rincentral.test.models.params.CarRequestParameters;
import com.rincentral.test.models.params.MaxSpeedRequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rincentral.test.exceptions.ExceptionConstants.EMPTY_PARAMETER;
import static com.rincentral.test.exceptions.ExceptionConstants.NO_SUCH_BRAND;
import static com.rincentral.test.exceptions.ExceptionConstants.NO_SUCH_MODEL;
import static com.rincentral.test.exceptions.ExceptionConstants.TO_MUCH_PARAMS_PRESENTED;
import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class CarService {
    private final DataContainer dataContainer;

    public List<? extends CarInfoData> getCars(CarRequestParameters params){
        Stream<CarFullInfo> carsStream = dataContainer.getCars().values().stream();
        if(params.getCountry() != null)
            carsStream = carsStream.filter(car -> params.getCountry().equals(car.getCountry()));
        if(params.getSegment() != null)
            carsStream = carsStream.filter(car -> params.getSegment().equals(car.getSegment()));
        if(params.getMinEngineDisplacement() != null){
            carsStream = carsStream
                    .filter(car -> (params.getMinEngineDisplacement() * 1000) <= car.getEngine().getDisplacement());
        }
        if(params.getMinEngineHorsepower() != null){
            carsStream = carsStream
                    .filter(car -> params.getMinEngineHorsepower() <= car.getEngine().getHpw());
        }
        if(params.getMinMaxSpeed() != null){
            carsStream = carsStream
                    .filter(car -> params.getMinMaxSpeed() <= car.getMaxSpeed());
        }
        if(params.getSearch() != null){
            carsStream = carsStream
                    .filter(car -> car.getModel().contains(params.getSearch())
                            || car.getGeneration().contains(params.getSearch())
                            || car.getModification().contains(params.getSearch()));
        }

        return carsStream.map(car -> params.getIsFull() != null && params.getIsFull() ?
                                     CarInfoExtended.builder()
                                                    .id(car.getId())
                                                    .segment(car.getSegment())
                                                    .brand(car.getBrand())
                                                    .model(car.getModel())
                                                    .country(car.getCountry())
                                                    .generation(car.getGeneration())
                                                    .modification(car.getModification())
                                                    .body(car.getBody())
                                                    .engine(car.getEngine())
                                                    .build() :
                                     CarInfo.builder()
                                            .id(car.getId())
                                            .segment(car.getSegment())
                                            .brand(car.getBrand())
                                            .model(car.getModel())
                                            .country(car.getCountry())
                                            .generation(car.getGeneration())
                                            .modification(car.getModification())
                                            .build())

                         .collect(Collectors.toList());
    }

    public List<String> getAllFuelTypes(){
        return Stream.of(FuelType.values()).map(Enum::name).collect(Collectors.toList());
    }

    public List<String> getAllEngineTypes(){
        return Stream.of(EngineType.values()).map(Enum::name).collect(Collectors.toList());
    }

    public List<String> getAllWheelDriveTypes(){
        return Stream.of(WheelDriveType.values()).map(Enum::name).collect(Collectors.toList());
    }

    public List<String> getAllGearboxTypes(){
        return Stream.of(GearboxType.values()).map(Enum::name).collect(Collectors.toList());
    }

    public List<String> getBodiesStyles(){
        return new ArrayList<>(dataContainer.getBodyStyles().values());
    }

    public double getMaxSpeed(MaxSpeedRequestParameters params){
        if(params.getBrand() != null && params.getModel() != null)
            throw new CarException(TO_MUCH_PARAMS_PRESENTED.getMessageTemplate(), TO_MUCH_PARAMS_PRESENTED);
        if(params.getModel() != null) {
            return dataContainer.getCars().values().stream()
                                .filter(car -> params.getModel().equals(car.getModel()))
                                .mapToInt(CarFullInfo::getMaxSpeed)
                                .average()
                                .orElseThrow(() -> new CarException(format(NO_SUCH_MODEL.getMessageTemplate(), params.getModel()), NO_SUCH_MODEL));
        }
        if(params.getBrand() != null){
            return dataContainer.getCars().values().stream()
                                .filter(car -> params.getBrand().equals(car.getBrand()))
                                .mapToInt(CarFullInfo::getMaxSpeed)
                                .average()
                                .orElseThrow(() -> new CarException(format(NO_SUCH_BRAND.getMessageTemplate(), params.getBrand()), NO_SUCH_BRAND));
        }
        throw new CarException(EMPTY_PARAMETER);
    }
}
