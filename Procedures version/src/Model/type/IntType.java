package Model.type;

import Model.value.IntValue;

public class IntType implements Type{
    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IntValue defaultValue() {
        return new IntValue(0);
    }
}
