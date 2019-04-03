package datacontainers.staticcontainers;

import java.util.List;

public class Termination {
    private List<Condition> conditions;

    public Termination(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

}
