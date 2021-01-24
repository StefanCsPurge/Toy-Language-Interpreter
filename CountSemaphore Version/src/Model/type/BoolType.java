package Model.type;

import Model.value.BoolValue;

public class BoolType implements Type{
    @Override
    public boolean equals(Object other) {
        return other instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public BoolValue defaultValue() {
        return new BoolValue(false);
    }
}
