package co.com.nequi.model.franchise;

import co.com.nequi.model.exceptions.NequiErrorType;
import lombok.Getter;

@Getter
public enum FranchiseErrorType implements NequiErrorType {

    FRANCHISE_NOT_FOUND(404, "FRANCHISE_001", "Franchise not found"),
    FRANCHISE_ERROR_SERVER(500, "FRANCHISE_002", "Error retrieving franchise data: "),
    PROPERTY_DUPLICATE(400, "23505", "Franchise properties must be unique: duplicate value found [name]"),
    ;

    private final Integer status;
    private final String code;
    private final String description;
    FranchiseErrorType(Integer status, String code, String description) {
        this.code = code;
        this.description = description;
        this.status = status;
    }
}
