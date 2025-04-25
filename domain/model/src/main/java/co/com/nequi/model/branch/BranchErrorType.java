package co.com.nequi.model.branch;

import co.com.nequi.model.exceptions.NequiErrorType;
import lombok.Getter;

@Getter
public enum BranchErrorType implements NequiErrorType {

    BRANCH_NOT_FOUND(404, "BRANCH_001", "BRANCH not found"),
    PROPERTY_DUPLICATE(400, "23505", "Branch properties must be unique: duplicate value found [name]")
    ;

    private final Integer status;
    private final String code;
    private final String description;
    BranchErrorType(Integer status, String code, String description) {
        this.code = code;
        this.description = description;
        this.status = status;
    }
}
