package com.rincentral.test.services;

import com.rincentral.test.exceptions.CarException;
import com.rincentral.test.models.CarFullInfo;
import com.rincentral.test.models.CarInfo;
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

import static com.rincentral.test.exceptions.ExceptionData.EMPTY_DATA;
import static com.rincentral.test.exceptions.ExceptionData.NO_SUCH_BRAND;
import static com.rincentral.test.exceptions.ExceptionData.NO_SUCH_MODEL;
import static com.rincentral.test.exceptions.ExceptionData.TO_MUCH_PARAMS_PRESENTED;
import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class CarService {
    private final DataContainer dataContainer;

    public List<CarInfo> getCars(CarRequestParameters params){
        return new ArrayList<>();
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
                                .mapToInt(CarFullInfo::getMasSpeed)
                                .average()
                                .orElseThrow(() -> new CarException(format(NO_SUCH_MODEL.getMessageTemplate(), params.getModel()), NO_SUCH_MODEL));
        }
        if(params.getBrand() != null){
            return dataContainer.getCars().values().stream()
                                .filter(car -> params.getBrand().equals(car.getBrand()))
                                .mapToInt(CarFullInfo::getMasSpeed)
                                .average()
                                .orElseThrow(() -> new CarException(format(NO_SUCH_BRAND.getMessageTemplate(), params.getBrand()), NO_SUCH_BRAND));
        }
        return dataContainer.getCars().values().stream()
                            .mapToInt(CarFullInfo::getMasSpeed)
                            .average()
                            .orElseThrow(() -> new CarException(NO_SUCH_MODEL));
    }
}
